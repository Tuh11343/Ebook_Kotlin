package com.example.connectnodejs.fragment

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.connectnodejs.R
import com.example.connectnodejs.databinding.FragmentUserBinding
import com.example.connectnodejs.databinding.FragmentUserLoginBinding
import com.example.connectnodejs.utils.AppInstance
import com.example.connectnodejs.viewmodels.MainViewModel

class FragmentUser: Fragment() {

    private lateinit var binding:FragmentUserBinding
    private lateinit var mainViewModel: MainViewModel
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        sharedPreferences = requireContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        mainViewModel= ViewModelProvider(requireActivity())[MainViewModel::class.java]
        binding = FragmentUserBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnSignIn.setOnClickListener{
            openFragmentSignIn()
        }

        binding.btnSignUp.setOnClickListener{
            openFragmentSignUp()
        }

        /*binding.btnBack.setOnClickListener{
            mainViewModel.updateCurrentState(mainViewModel.lastState.value!!)
        }*/

    }

    private fun openFragmentSignIn() {
        mainViewModel.updateState(MainViewModel.Companion.State.SignIn)

        /*val fragmentManager: FragmentManager = requireActivity().supportFragmentManager
        val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()

        val fragmentSignIn = FragmentSignIn()

        // Thay thế Fragment hiện tại bằng FragmentB và thêm vào BackStack
        fragmentTransaction.replace(binding.frameLayout.id, fragmentSignIn)
        fragmentTransaction.addToBackStack(null) // Thêm Fragment vào Stack Back để quay lại khi cần
        fragmentTransaction.commit()*/
    }

    private fun openFragmentSignUp() {
        mainViewModel.updateState(MainViewModel.Companion.State.SignUp)

        /*val fragmentManager: FragmentManager = requireActivity().supportFragmentManager
        val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()

        val fragmentSignUp = FragmentSignUp()

        fragmentTransaction.replace(binding.frameLayout.id, fragmentSignUp)
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()*/
    }


}