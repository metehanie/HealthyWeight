package com.metehanbolat.healthyweight.ui.login.sign_up.chooseheight

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import com.metehanbolat.healthyweight.R
import com.metehanbolat.healthyweight.databinding.FragmentChooseHeightBinding
import com.metehanbolat.healthyweight.ui.login.sign_up.SignUpNavGraphViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ChooseHeightFragment : Fragment() {

    private var _binding: FragmentChooseHeightBinding? = null
    private val binding get() = _binding!!

    private val navViewModel: SignUpNavGraphViewModel by navGraphViewModels(R.id.sign_up_graph)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChooseHeightBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setHeightPicker()
        buttonClick()
    }

    private fun buttonClick() {
        binding.nextButton.setOnClickListener {
            navViewModel.setChosenHeight(binding.heightPicker.value)
            val action = ChooseHeightFragmentDirections.actionChooseHeightFragmentToChooseWeightFragment()
            findNavController().navigate(action)
        }
    }

    private fun setHeightPicker() {
        binding.heightPicker.apply {
            minValue = 120
            maxValue = 230
            value = 170
        }

    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
