package com.challenge.challengechapter4.data.ui.adapter

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.os.Bundle
import android.provider.ContactsContract.CommonDataKinds.Note
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.challenge.challengechapter4.R
import com.challenge.challengechapter4.fragment.DialogFragmentDelete
import com.challenge.challengechapter4.fragment.DialogFragmentUpdate
import com.challenge.challengechapter4.data.local.data.Notes
import com.challenge.challengechapter4.data.ui.viewmodel.NoteViewModel
import com.challenge.challengechapter4.databinding.NoteItemBinding
import com.challenge.challengechapter4.fragment.HomeFragment

class NoteAdapter(private var listNote: List<Notes>, private val navController: NavController) : RecyclerView.Adapter<NoteAdapter.NoteViewHolder>() {
    inner class NoteViewHolder(val binding: NoteItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(note: Notes, noteId: Int) {
            binding.noteData = note
            val bundle = Bundle()
            bundle.putInt("noteId", noteId)
            binding.btnDelete.setOnClickListener {
                navController.navigate(R.id.action_fragmentHome_to_dialogFragmentDelete, bundle)
            }

            binding.btnEdit.setOnClickListener {
                navController.navigate(R.id.action_fragmentHome_to_dialogFragmentUpdate, bundle)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val itemView = NoteItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NoteViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) = holder.bind(listNote[position], listNote[position].id)

    override fun getItemCount() = listNote.size

    @SuppressLint("NotifyDataSetChanged")
    fun setNotes(notes: List<Notes>) {
        this.listNote = notes
        notifyDataSetChanged()
    }
}
