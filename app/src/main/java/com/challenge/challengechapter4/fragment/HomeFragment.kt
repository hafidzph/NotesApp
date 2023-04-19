package com.challenge.challengechapter4.fragment

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.challenge.challengechapter4.R
import com.challenge.challengechapter4.data.local.database.AppDatabase
import com.challenge.challengechapter4.data.ui.adapter.NoteAdapter
import com.challenge.challengechapter4.data.ui.datastore.UserPreferences
import com.challenge.challengechapter4.data.ui.viewmodel.NoteViewModel
import com.challenge.challengechapter4.data.ui.viewmodel.UserViewModel
import com.challenge.challengechapter4.data.ui.viewmodel.factory.NoteViewModelFactory
import com.challenge.challengechapter4.data.ui.viewmodel.factory.UserViewModelFactory
import com.challenge.challengechapter4.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var userVM: UserViewModel
    private lateinit var adapter: NoteAdapter
    private lateinit var noteVM: NoteViewModel
    private var getUserId: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        val viewMF = UserViewModelFactory(AppDatabase.getInstance(requireNotNull(this.activity).application).userDao(),
            requireNotNull(this.activity).application, UserPreferences(requireContext()))
        userVM = ViewModelProvider(this, viewMF)[UserViewModel::class.java]
        val noteMF = NoteViewModelFactory(AppDatabase.getInstance(requireNotNull(this.activity).application).noteDao(),
            requireNotNull(this.activity).application)
        noteVM = ViewModelProvider(this, noteMF)[NoteViewModel::class.java]
        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val sharedPref = requireActivity().getPreferences(Context.MODE_PRIVATE)
        getUserId = sharedPref.getInt("userId", 0)
        val getUsername = sharedPref.getString("username", "")
        binding.showUsername.text = "Hello, $getUsername"
        (activity as AppCompatActivity).supportActionBar?.show()

        adapter = NoteAdapter(listOf(), findNavController())
        binding.rvNote.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.rvNote.adapter = adapter
        noteVM.getAllNotes(getUserId).observe(viewLifecycleOwner){ notes ->
            if (notes.isEmpty()) {
                binding.rvNote.visibility = View.GONE
                binding.emptyBox.visibility = View.VISIBLE
            } else {
                binding.rvNote.visibility = View.VISIBLE
                binding.emptyBox.visibility = View.GONE
                adapter.setNotes(notes)
            }
        }
        binding.btnLogout.setOnClickListener {
            userVM.clear()
            findNavController().navigate(R.id.action_fragmentHome_to_loginFragment)
        }

        binding.btnPlus.setOnClickListener {
            findNavController().navigate(R.id.action_fragmentHome_to_dialogFragmentCreate)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.filter -> true
            R.id.ascending -> {
                noteVM.getAllNotesAsc(getUserId).observe(viewLifecycleOwner){
                    adapter.setNotes(it)
                }
                true
            }
            R.id.descending -> {
                noteVM.getAllNotesDesc(getUserId).observe(viewLifecycleOwner){
                    adapter.setNotes(it)
                }
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}