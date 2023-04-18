package com.challenge.challengechapter4.fragment

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.challenge.challengechapter4.R
import com.challenge.challengechapter4.data.local.data.Notes
import com.challenge.challengechapter4.data.local.database.AppDatabase
import com.challenge.challengechapter4.data.ui.viewmodel.NoteViewModel
import com.challenge.challengechapter4.data.ui.viewmodel.factory.NoteViewModelFactory
import com.challenge.challengechapter4.databinding.FragmentDialogUpdateBinding
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class DialogFragmentUpdate : DialogFragment() {
    lateinit var binding: FragmentDialogUpdateBinding
    lateinit var noteVm: NoteViewModel

    companion object {
        fun newInstance(noteId: Int): DialogFragmentUpdate {
            val fragment = DialogFragmentUpdate()
            val args = Bundle()
            args.putInt("noteId", noteId)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDialogUpdateBinding.inflate(inflater, container, false)
        val app = requireNotNull(this.activity).application
        val dataSource = AppDatabase.getInstance(app).noteDao()
        val viewMF = NoteViewModelFactory(dataSource, app)
        noteVm = ViewModelProvider(this, viewMF)[NoteViewModel::class.java]
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val sharedPref = requireActivity().getPreferences(Context.MODE_PRIVATE)
        val getUserId = sharedPref.getInt("userId", 0)
        binding.btnUpdate.setOnClickListener {
            lifecycleScope.async {
                val title = binding.etTitle.text.toString()
                val note = binding.etNote.text.toString()
                val noteId = arguments?.getInt("noteId")
                if(title.isEmpty() && noteId != null){
                    noteVm.updateNote(noteId, note)
                    Toast.makeText(requireContext(), "Notes content berhasil diupdate", Toast.LENGTH_LONG).show()
                }else if (note.isEmpty() && noteId != null){
                    noteVm.updateTitle(noteId, title)
                    Toast.makeText(requireContext(), "Title notes berhasil diupdate", Toast.LENGTH_LONG).show()

                }else if(note.isNotEmpty() && title.isNotEmpty() && noteId != null){
                    noteVm.updateAll(noteId, title, note)
                    Toast.makeText(requireContext(), "Notes berhasil diupdate", Toast.LENGTH_LONG).show()

                }else{
                    Toast.makeText(requireContext(), "Notes gagal untuk diupdate", Toast.LENGTH_LONG).show()
                }
                dismiss()
                findNavController().navigateUp()
            }
        }
    }
}