package com.zhari.bitaste.presentation.profile

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.zhari.bitaste.R
import com.zhari.bitaste.data.network.firebase.auth.FirebaseAuthDataSourceImpl
import com.zhari.bitaste.data.repository.UserRepositoryImpl
import com.zhari.bitaste.databinding.FragmentProfileBinding
import com.zhari.bitaste.presentation.login.LoginActivity
import com.zhari.bitaste.utils.proceedWhen
import org.koin.androidx.viewmodel.ext.android.viewModel

class FragmentProfile : Fragment() {

    private lateinit var binding: FragmentProfileBinding
    private val viewModel: ProfileViewModel by viewModel()

    private fun createViewModel(): ProfileViewModel {
        val firebaseAuth = FirebaseAuth.getInstance()
        val dataSource = FirebaseAuthDataSourceImpl(firebaseAuth)
        val repo = UserRepositoryImpl(dataSource)
        return ProfileViewModel(repo)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentProfileBinding.inflate(
            inflater,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        super.onViewCreated(
            view,
            savedInstanceState
        )
        setupForm()
        showUserData()
        setClickListeners()
        observeData()
    }

    private fun observeData() {
        viewModel.updateProfileResult.observe(viewLifecycleOwner) {
            it.proceedWhen(
                doOnSuccess = {
                    binding.pbChangeProfileLoading.isVisible = false
                    binding.btnChangeProfile.isVisible = true
                    binding.btnChangeProfile.isEnabled = true
                },
                doOnLoading = {
                    binding.pbChangeProfileLoading.isVisible = true
                    binding.btnChangeProfile.isVisible = false
                },
                doOnError = {
                    binding.pbChangeProfileLoading.isVisible = false
                    binding.btnChangeProfile.isVisible = true
                    binding.btnChangeProfile.isEnabled = true
                    Toast.makeText(
                        requireContext(),
                        "Change Profile Failed: ${it.exception?.message.orEmpty()}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            )
        }
    }

    private fun setClickListeners() {
        binding.btnChangeProfile.setOnClickListener {
            changeProfileData()
        }
        binding.btnChangePassword.setOnClickListener {
            requestChangePassword()
        }
        binding.btnLogout.setOnClickListener {
            doLogout()
        }
    }

    private fun doLogout() {
        AlertDialog.Builder(requireContext())
            .setMessage(
                "Apakah kamu ingin keluar? " +
                    "${viewModel.getCurrentUser()?.fullName}"
            )
            .setPositiveButton("Ya") { _, _ ->
                viewModel.doLogout()
                navigateToLogin()
            }.setNegativeButton("Tidak") { _, _ ->
            }.create().show()
    }

    private fun navigateToLogin() {
        requireActivity().run {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }

    private fun requestChangePassword() {
        viewModel.createChangePasswordReq()
        AlertDialog.Builder(requireContext())
            .setMessage(
                "A password change request will be sent to the email: " +
                    "${viewModel.getCurrentUser()?.email}"
            )
            .setPositiveButton("Ok") { _, _ ->
            }.create().show()
    }

    private fun changeProfileData() {
        val fullName = binding.layoutForm.etName.text.toString().trim()
        if (isFormValid()) {
            viewModel.updateProfile(fullName)
        }
    }

    private fun isFormValid(): Boolean {
        val currentName = viewModel.getCurrentUser()?.fullName
        val newName = binding.layoutForm.etName.text.toString().trim()
        return checkNameValidation(currentName, newName)
    }

    private fun checkNameValidation(
        currentName: String?,
        newName: String
    ): Boolean {
        return if (newName.isEmpty()) {
            binding.layoutForm.tilName.isErrorEnabled = true
            binding.layoutForm.tilName.error = getString(
                R.string.text_error_name_cannot_empty
            )
            false
        } else if (newName == currentName) {
            binding.layoutForm.tilName.isErrorEnabled = true
            binding.layoutForm.tilName.error = getString(R.string.text_error_new_name_must_be_different)
            false
        } else {
            binding.layoutForm.tilName.isErrorEnabled = false
            true
        }
    }

    private fun showUserData() {
        binding.layoutForm.etName.setText(viewModel.getCurrentUser()?.fullName)
        binding.layoutForm.etEmail.setText(viewModel.getCurrentUser()?.email)
    }

    private fun setupForm() {
        binding.layoutForm.tilName.isVisible = true
        binding.layoutForm.tilEmail.isVisible = true
        binding.layoutForm.etEmail.isEnabled = false
    }
}
