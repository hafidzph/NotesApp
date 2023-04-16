package com.challenge.challengechapter4.data.fragment

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.challenge.challengechapter4.R
import com.challenge.challengechapter4.data.local.data.Users
import com.challenge.challengechapter4.data.local.database.AppDatabase
import com.challenge.challengechapter4.data.ui.datastore.UserPreferences
import com.challenge.challengechapter4.data.ui.viewmodel.UserViewModel
import com.challenge.challengechapter4.data.ui.viewmodel.factory.UserViewModelFactory
import com.challenge.challengechapter4.databinding.FragmentLoginBinding
import kotlinx.coroutines.launch


class LoginFragment : Fragment() {
    lateinit var binding : FragmentLoginBinding
    lateinit var prefs : UserPreferences
    lateinit var userVM : UserViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        val app = requireNotNull(this.activity).application
        val dataSource = AppDatabase.getInstance(app).userDao()
        prefs = UserPreferences(requireContext())
        val viewMF = UserViewModelFactory(dataSource, app, prefs)
        userVM = ViewModelProvider(this, viewMF)[UserViewModel::class.java]
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val sharedPref = requireActivity().getPreferences(Context.MODE_PRIVATE)
        lifecycleScope.launch {
            if(userVM.isUserAlreadyLoggedIn()){
                findNavController().navigate(R.id.action_loginFragment_to_fragmentHome)
            }
        }

        binding.btnLogin.setOnClickListener {
            lifecycleScope.launch {
                val usernameOrEmail = binding.etUsernameOrEmail.text.toString()
                val password = binding.etPass.text.toString()
                val user = userVM.getUserByUsernameAndPassword(usernameOrEmail, usernameOrEmail, password)
                sharedPref.edit().apply{
                    putInt("userId", user!!.id)
                    putString("username", user.username)
                    apply()
                }
                if(user != null) {
                    userVM.login()
                    findNavController().navigate(R.id.action_loginFragment_to_fragmentHome)
                } else {
                    Toast.makeText(requireContext(), "Username/Email atau Password yang anda masukkan tidak valid", Toast.LENGTH_LONG).show()
                }
            }
        }

        binding.daftar.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }
    }
}