package com.challenge.challengechapter4.data.ui.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.provider.ContactsContract.CommonDataKinds.Note
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.challenge.challengechapter4.fragment.DialogFragmentDelete
import com.challenge.challengechapter4.fragment.DialogFragmentUpdate
import com.challenge.challengechapter4.data.local.data.Notes
import com.challenge.challengechapter4.data.ui.viewmodel.NoteViewModel
import com.challenge.challengechapter4.databinding.NoteItemBinding

class NoteAdapter(private var listNote: List<Notes>, private val context: Context) : RecyclerView.Adapter<NoteAdapter.NoteViewHolder>() {
    inner class NoteViewHolder(val binding: NoteItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(note: Notes, noteId: Int) {
            binding.noteData = note
            binding.btnDelete.setOnClickListener {
                val dfDelete = DialogFragmentDelete.newInstance(noteId)
                dfDelete.show((context as AppCompatActivity).supportFragmentManager, "DialogFragmentDelete")
            }

            binding.btnEdit.setOnClickListener {
                val dfEdit = DialogFragmentUpdate.newInstance(noteId)
                dfEdit.show((context as AppCompatActivity).supportFragmentManager, "DialogFragmentUpdate")
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
