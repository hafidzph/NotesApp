package com.challenge.challengechapter4.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.challenge.challengechapter4.data.local.data.Notes
import com.challenge.challengechapter4.data.local.database.AppDatabase
import com.challenge.challengechapter4.data.ui.viewmodel.NoteViewModel
import com.challenge.challengechapter4.data.ui.viewmodel.factory.NoteViewModelFactory
import com.challenge.challengechapter4.databinding.FragmentDialogCreateBinding
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class DialogFragmentCreate : DialogFragment() {
    lateinit var noteVm: NoteViewModel
    lateinit var binding: FragmentDialogCreateBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDialogCreateBinding.inflate(inflater, container, false)
        val viewMF = NoteViewModelFactory(AppDatabase.getInstance(requireNotNull(this.activity).application).noteDao(),
            requireNotNull(this.activity).application)
        noteVm = ViewModelProvider(this, viewMF)[NoteViewModel::class.java]
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val sharedPref = requireActivity().getPreferences(Context.MODE_PRIVATE)
        val getUserId = sharedPref.getInt("userId", 0)

        binding.btnInput.setOnClickListener {
            lifecycleScope.launch {
                val title = binding.etTitle.text.toString()
                val note = binding.etNote.text.toString()
                if(title.isNotEmpty() && note.isNotEmpty()){
                    noteVm.insert(Notes(title = title, note = note, userId = getUserId))
                    dismiss()
                    findNavController().navigateUp()
                    Toast.makeText(
                        requireContext(),
                        "Notes berhasil ditambahkan",
                        Toast.LENGTH_LONG
                    ).show()
                }else {
                    binding.tilTitleInput.error = if(title.isEmpty()) "Judul tidak boleh kosong" else null
                    binding.tilNoteInput.error = if(note.isEmpty()) "Note tidak boleh" else null
                }
            }
        }
    }
}