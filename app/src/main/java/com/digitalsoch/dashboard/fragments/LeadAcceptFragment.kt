package com.digitalsoch.dashboard.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.digitalsoch.R
import com.digitalsoch.databinding.FragmentLeadAcceptBinding
import com.digitalsoch.databinding.FragmentLeadAddBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LeadAcceptFragment : Fragment() {

    private var _binding: FragmentLeadAcceptBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentLeadAcceptBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewLifecycleOwner.lifecycleScope.launch {
            startAnimations()
        }

        binding.back.setOnClickListener {
            findNavController().popBackStack()
        }
    }
    private suspend fun startAnimations() {
        withContext(Dispatchers.Main) {
            binding.back.animation = AnimationUtils.loadAnimation(requireContext(), R.anim.left_anim)
            binding.textTitle.animation = AnimationUtils.loadAnimation(requireContext(), R.anim.text_animation)
            binding.mainBg.animation = AnimationUtils.loadAnimation(requireContext(), R.anim.icon_anim)
            binding.cNameTag.animation = AnimationUtils.loadAnimation(requireContext(), R.anim.text_animation2)
            binding.cPhoneTag.animation = AnimationUtils.loadAnimation(requireContext(), R.anim.text_animation2)
            binding.cEmailTag.animation = AnimationUtils.loadAnimation(requireContext(), R.anim.text_animation2)
            binding.cAddressTag.animation = AnimationUtils.loadAnimation(requireContext(), R.anim.text_animation2)
            binding.cCitytag.animation = AnimationUtils.loadAnimation(requireContext(), R.anim.text_animation2)
            binding.cPintag.animation = AnimationUtils.loadAnimation(requireContext(), R.anim.text_animation2)
            binding.cReqTag.animation = AnimationUtils.loadAnimation(requireContext(), R.anim.text_animation2)
            binding.cReqTypetag.animation = AnimationUtils.loadAnimation(requireContext(), R.anim.text_animation2)
            binding.EmpNote.animation = AnimationUtils.loadAnimation(requireContext(), R.anim.text_animation2)

            binding.btnSubmit.animation = AnimationUtils.loadAnimation(requireContext(), R.anim.text_animation3)
        }

    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}