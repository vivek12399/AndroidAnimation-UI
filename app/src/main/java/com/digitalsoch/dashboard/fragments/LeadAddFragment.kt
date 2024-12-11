package com.digitalsoch.dashboard.fragments

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.DatePicker
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.digitalsoch.R
import com.digitalsoch.bottomnavigations.AssignBottomSheetDialogFragment
import com.digitalsoch.bottomnavigations.TimeSlotBottomSheetDialogFragment
import com.digitalsoch.databinding.FragmentLeadAddBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Calendar

class LeadAddFragment : Fragment() {

    private var _binding: FragmentLeadAddBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLeadAddBinding.inflate(inflater, container, false)

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

        binding.assignButton.setOnClickListener {
            val bottomSheet = AssignBottomSheetDialogFragment { selectedItem ->
                binding.assignNameText.text = selectedItem
                binding.assignName.visibility = View.VISIBLE
            }
            bottomSheet.show(parentFragmentManager, "AssignBottomSheet")
        }
        binding.selectTime.setOnClickListener {
            val bottomSheet = TimeSlotBottomSheetDialogFragment { selectedItem ->
                binding.selectedTimeText.text = selectedItem
                binding.selectedTime.visibility = View.VISIBLE
            }
            bottomSheet.show(parentFragmentManager, "AssignBottomSheet")
        }


        binding.selectDate.setOnClickListener {
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)
            DatePickerDialog(requireContext(), { _: DatePicker, selectedYear: Int, selectedMonth: Int, selectedDay: Int ->
                binding.selectedDateText.text = String.format("%02d/%02d/%04d", selectedDay, selectedMonth + 1, selectedYear)
                binding.selectedDate.visibility = View.VISIBLE
            }, year, month, day).show()
        }
    }

    private suspend fun startAnimations() {
        withContext(Dispatchers.Main) {
            binding.textTitle.animation = AnimationUtils.loadAnimation(requireContext(), R.anim.text_animation)
            binding.mainBg.animation = AnimationUtils.loadAnimation(requireContext(), R.anim.icon_anim)
            binding.etNameLO.animation = AnimationUtils.loadAnimation(requireContext(), R.anim.text_animation2)
            binding.etPhoneLO.animation = AnimationUtils.loadAnimation(requireContext(), R.anim.text_animation2)
            binding.etEmailLO.animation = AnimationUtils.loadAnimation(requireContext(), R.anim.text_animation2)
            binding.etAddressLO.animation = AnimationUtils.loadAnimation(requireContext(), R.anim.text_animation2)
            binding.etPinLO.animation = AnimationUtils.loadAnimation(requireContext(), R.anim.text_animation2)
            binding.etCityLO.animation = AnimationUtils.loadAnimation(requireContext(), R.anim.text_animation2)
            binding.etReqTypeLO.animation = AnimationUtils.loadAnimation(requireContext(), R.anim.text_animation2)
            binding.etReqLO.animation = AnimationUtils.loadAnimation(requireContext(), R.anim.text_animation2)
            binding.selectionLayout.animation = AnimationUtils.loadAnimation(requireContext(), R.anim.text_animation2)
            binding.btnSubmit.animation = AnimationUtils.loadAnimation(requireContext(), R.anim.text_animation3)
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}