package com.challenge.challengechapter4.data.fragment

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.*
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
    lateinit var binding: FragmentHomeBinding
    lateinit var userVM: UserViewModel
    lateinit var prefs: UserPreferences
    lateinit var adapter: NoteAdapter
    lateinit var noteVM: NoteViewModel
    var getUserId: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        val app = requireNotNull(this.activity).application
        val dataSource = AppDatabase.getInstance(app).userDao()
        prefs = UserPreferences(requireContext())
        val viewMF = UserViewModelFactory(dataSource, app, prefs)
        userVM = ViewModelProvider(this, viewMF)[UserViewModel::class.java]
        val noteDS = AppDatabase.getInstance(app).noteDao()
        val noteMF = NoteViewModelFactory(noteDS, app)
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

        adapter = NoteAdapter(listOf(), requireContext())
        binding.rvNote.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.rvNote.adapter = adapter
        noteVM.getAllNotes(getUserId).observe(viewLifecycleOwner){
            adapter.setNotes(it)
        }
        binding.btnLogout.setOnClickListener {
            userVM.clear()
            findNavController().navigate(R.id.action_fragmentHome_to_loginFragment)
        }

        binding.btnPlus.setOnClickListener {
            val createDialogFragment = DialogFragmentCreate.newInstance()

            createDialogFragment.show(requireActivity().supportFragmentManager, "DialogFragmentCreate")
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