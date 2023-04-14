package com.challenge.challengechapter4.data.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.challenge.challengechapter4.R
import com.challenge.challengechapter4.data.local.data.Users
import com.challenge.challengechapter4.data.local.database.AppDatabase
import com.challenge.challengechapter4.data.ui.datastore.UserPreferences
import com.challenge.challengechapter4.data.ui.viewmodel.UserViewModel
import com.challenge.challengechapter4.data.ui.viewmodel.factory.UserViewModelFactory
import com.challenge.challengechapter4.databinding.FragmentRegisterBinding
import kotlinx.coroutines.launch

class RegisterFragment : Fragment() {
    lateinit var binding : FragmentRegisterBinding
    lateinit var userVM : UserViewModel
    lateinit var prefs : UserPreferences
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val app = requireNotNull(this.activity).application
        val dataSource = AppDatabase.getInstance(app).userDao()
        prefs = UserPreferences(requireContext())
        val viewMF = UserViewModelFactory(dataSource, app, prefs)
        userVM = ViewModelProvider(this, viewMF)[UserViewModel::class.java]

        binding.btnRegis.setOnClickListener {
            val username = binding.etUsername.text.toString()
            val email = binding.etEmail.text.toString()
            val password = binding.etPass.text.toString()
            val confirmPassword = binding.etPassConf.text.toString()

            if (password != confirmPassword) {
                Toast.makeText(requireContext(), "Password tidak sesuai", Toast.LENGTH_LONG).show()
            } else {
                lifecycleScope.launch {
                    val isExist = userVM.checkIfUserExists(username, email)
                    if (isExist) {
                        Toast.makeText(requireContext(), "Username atau email sudah terdaftar", Toast.LENGTH_LONG).show()
                    } else {
                        userVM.insert(Users(username = username, email = email, password = password))
                        findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
                    }
                }
            }
        }
    }


}