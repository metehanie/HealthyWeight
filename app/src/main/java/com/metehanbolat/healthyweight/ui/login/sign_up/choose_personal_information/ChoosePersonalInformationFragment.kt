package com.metehanbolat.healthyweight.ui.login.sign_up.choose_personal_information

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.navGraphViewModels
import com.metehanbolat.healthyweight.R
import com.metehanbolat.healthyweight.databinding.FragmentChoosePersonalInformationBinding
import com.metehanbolat.healthyweight.model.auth.Member
import com.metehanbolat.healthyweight.ui.login.sign_up.SignUpNavGraphViewModel
import com.metehanbolat.healthyweight.ui.main.home.HomeActivity
import com.metehanbolat.healthyweight.util.UiState
import com.metehanbolat.healthyweight.util.gone
import com.metehanbolat.healthyweight.util.validateRule
import com.metehanbolat.healthyweight.util.visible
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ChoosePersonalInformationFragment : Fragment() {

    private var _binding: FragmentChoosePersonalInformationBinding? = null
    private val binding get() = _binding!!

    private val navGraphViewModel: SignUpNavGraphViewModel by navGraphViewModels(R.id.sign_up_graph)
    private val viewModel: ChoosePersonalInformationFragmentViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChoosePersonalInformationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.nextButton.setOnClickListener {
            if (emptyFieldControl()) {
                val member = Member(
                    name = binding.nameText.text.toString(),
                    surname = binding.surnameText.text.toString(),
                    email = binding.emailText.text.toString(),
                    password = binding.passwordText.text.toString(),
                    birthday = navGraphViewModel.chosenBirthday.value,
                    weight = navGraphViewModel.chosenWeight.value.toString(),
                    height = navGraphViewModel.chosenHeight.value.toString(),
                    gender = navGraphViewModel.chosenGender.value
                )
                viewModel.signUpMemberToAuth(member)
            }
        }

        viewModel.signUpMemberToAuth.observe(viewLifecycleOwner) { state ->
            when (state) {
                is UiState.Loading -> {
                    binding.loadingLottie.visible()
                    viewVisibilityState(false)
                }
                is UiState.Success -> {
                    binding.loadingLottie.gone()
                    viewModel.signUpMemberToFirestore(state.data)
                }
                is UiState.Failure -> {
                    binding.loadingLottie.gone()
                    viewVisibilityState(true)
                }
            }
        }

        viewModel.signUpMemberToFirestore.observe(viewLifecycleOwner) { state ->
            when (state) {
                is UiState.Loading -> {
                    binding.loadingLottie.visible()
                    viewVisibilityState(false)
                }
                is UiState.Success -> {
                    binding.loadingLottie.gone()
                    Intent(requireContext(), HomeActivity::class.java).apply {
                        startActivity(this)
                        requireActivity().finish()
                    }
                }
                is UiState.Failure -> {
                    binding.loadingLottie.gone()
                    viewVisibilityState(true)
                }
            }
        }
    }

    private fun emptyFieldControl(): Boolean {
        val nameTextEmptyRule =
            binding.nameText.validateRule(R.string.error_text) { it.isNullOrEmpty() }
        val surnameTextEmptyRule =
            binding.surnameText.validateRule(R.string.error_text) { it.isNullOrEmpty() }
        val emailTextEmptyRule =
            binding.emailText.validateRule(R.string.error_text) { it.isNullOrEmpty() }
        val passwordTextEmptyRule =
            binding.passwordText.validateRule(R.string.error_text) { it.isNullOrEmpty() }
        return nameTextEmptyRule && surnameTextEmptyRule && emailTextEmptyRule && passwordTextEmptyRule
    }

    private fun viewVisibilityState(isVisible: Boolean) {
        binding.apply {
            if (isVisible) {
                informationText.visible()
                nameTextInputLayout.visible()
                surnameTextInputLayout.visible()
                emailTextInputLayout.visible()
                passwordTextInputLayout.visible()
                nextButton.visible()
            } else {
                informationText.gone()
                nameTextInputLayout.gone()
                surnameTextInputLayout.gone()
                emailTextInputLayout.gone()
                passwordTextInputLayout.gone()
                nextButton.gone()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}