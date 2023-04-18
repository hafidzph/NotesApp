package com.challenge.challengechapter4.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
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
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRegisterBinding.inflate(inflater, container, false)
        val viewMF = UserViewModelFactory(AppDatabase.getInstance(requireNotNull(this.activity).application).userDao(),
            requireNotNull(this.activity).application, UserPreferences(requireContext()))
        userVM = ViewModelProvider(this, viewMF)[UserViewModel::class.java]
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity).supportActionBar?.hide()

        binding.login.setOnClickListener {
            findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
        }

        binding.btnRegis.setOnClickListener {
            val username = binding.etUsername.text.toString()
            val email = binding.etEmail.text.toString()
            val password = binding.etPass.text.toString()
            val confirmPassword = binding.etPassConf.text.toString()

            if(password.isEmpty() || confirmPassword.isEmpty() || email.isEmpty() || username.isEmpty()){
                binding.tilUsername.error = if (username.isEmpty()) "Username tidak boleh kosong" else null
                binding.tilEmail.error = if (email.isEmpty()) "Email tidak boleh kosong" else null
                binding.tilPass.error = if (password.isEmpty()) "Password tidak boleh kosong" else null
                binding.tilPassConf.error = if (confirmPassword.isEmpty()) "Konfirmasi Password tidak boleh kosong" else null
            }else {
                binding.tilUsername.error = null
                binding.tilEmail.error = null
                binding.tilPass.error = null
                lifecycleScope.launch {
                    val isExist = userVM.checkIfUserExists(username, email)
                    if(password != confirmPassword){
                        binding.tilPassConf.error = "Password tidak sesuai"
                    }else{
                        binding.tilPassConf.error = null
                        if (isExist) {
                            Toast.makeText(requireContext(), "Username atau email sudah terdaftar", Toast.LENGTH_LONG).show()
                        } else {
                            userVM.insert(Users(username = username, email = email, password = password))
                            Toast.makeText(requireContext(), "Akun berhasil didaftarkan", Toast.LENGTH_LONG).show()
                            findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
                        }
                    }
                }
            }
        }
    }


}