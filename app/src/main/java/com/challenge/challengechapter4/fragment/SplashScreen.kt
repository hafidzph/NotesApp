package com.challenge.challengechapter4.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.challenge.challengechapter4.R
import com.challenge.challengechapter4.data.local.database.AppDatabase
import com.challenge.challengechapter4.data.ui.datastore.UserPreferences
import com.challenge.challengechapter4.data.ui.viewmodel.UserViewModel
import com.challenge.challengechapter4.data.ui.viewmodel.factory.UserViewModelFactory
import com.challenge.challengechapter4.databinding.FragmentSplashScreenBinding
import kotlinx.coroutines.launch

@SuppressLint("CustomSplashScreen")
class SplashScreen : Fragment() {
    private lateinit var binding:  FragmentSplashScreenBinding
    private lateinit var rotateAnimation: Animation
    private lateinit var userVM : UserViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSplashScreenBinding.inflate(inflater, container, false)
        val viewMF = UserViewModelFactory(
            AppDatabase.getInstance(requireNotNull(this.activity).application).userDao(),
            requireNotNull(this.activity).application,
            UserPreferences(requireContext())
        )
        userVM = ViewModelProvider(this, viewMF)[UserViewModel::class.java]
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rotateAnimation = AnimationUtils.loadAnimation(requireContext(), R.anim.anim)
        binding.img.startAnimation(rotateAnimation)
        (activity as AppCompatActivity).supportActionBar?.hide()

        Handler(Looper.getMainLooper()).postDelayed({
            lifecycleScope.launch {
                if(userVM.isUserAlreadyLoggedIn()){
                    findNavController().navigate(R.id.action_splashScreen_to_fragmentHome)
                }else{
                    findNavController().navigate(R.id.action_splashScreen_to_loginFragment)
                }
            }
        }, 2000)
    }
}