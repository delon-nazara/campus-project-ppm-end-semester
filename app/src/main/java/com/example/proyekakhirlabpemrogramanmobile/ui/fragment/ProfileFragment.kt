package com.example.proyekakhirlabpemrogramanmobile.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.proyekakhirlabpemrogramanmobile.databinding.FragmentProfileBinding
import com.example.proyekakhirlabpemrogramanmobile.ui.activity.HomeActivity
import com.example.proyekakhirlabpemrogramanmobile.ui.activity.MainActivity
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class ProfileFragment : Fragment() {
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        val view = binding.root

        val auth = Firebase.auth

        binding.userEmail.text = auth.currentUser?.email ?: "unknown_user"

        binding.logoutButton.setOnClickListener {
            auth.signOut()
            startMainActivity()
        }

        return view
    }

    private fun startMainActivity() {
        startActivity(Intent(requireContext(), MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}