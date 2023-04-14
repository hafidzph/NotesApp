package com.challenge.challengechapter4.data.fragment

import android.os.Bundle
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
import com.challenge.challengechapter4.databinding.FragmentDialogCreateBinding
import com.challenge.challengechapter4.databinding.FragmentDialogDeleteBinding
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class DialogFragmentDelete : DialogFragment() {
    lateinit var noteVm: NoteViewModel
    lateinit var binding: FragmentDialogDeleteBinding

    companion object {
        fun newInstance(noteId: Int): DialogFragmentDelete {
            val fragment = DialogFragmentDelete()
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
        binding = FragmentDialogDeleteBinding.inflate(inflater, container, false)
        val app = requireNotNull(this.activity).application
        val dataSource = AppDatabase.getInstance(app).noteDao()
        val viewMF = NoteViewModelFactory(dataSource, app)
        noteVm = ViewModelProvider(this, viewMF)[NoteViewModel::class.java]
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val noteId = arguments?.getInt("noteId")
        binding.btnDelete.setOnClickListener {
            lifecycleScope.async {
                if(noteId != null){
                    noteVm.delete(noteId)
                    Toast.makeText(requireContext(), "Notes berhasil dihapus", Toast.LENGTH_LONG).show()
                }else{
                    Toast.makeText(requireContext(), "Notes gagal dihapus", Toast.LENGTH_LONG).show()
                }
                dismiss()
                findNavController().navigateUp()
            }
        }

        binding.btnCancel.setOnClickListener {
            dismiss()
            findNavController().navigateUp()
        }
    }
}