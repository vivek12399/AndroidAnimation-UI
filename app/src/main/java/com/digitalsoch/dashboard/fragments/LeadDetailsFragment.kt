package com.digitalsoch.dashboard.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import com.digitalsoch.R
import com.digitalsoch.network.responses.Lead
import com.digitalsoch.databinding.FragmentLeadDetailsBinding


class LeadDetailsFragment : Fragment() {

    private var _binding: FragmentLeadDetailsBinding? = null
    private val binding get() = _binding!!
    var lead: Lead? = null
    var status: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLeadDetailsBinding.inflate(inflater, container, false)
        lead = arguments?.getSerializable("lead") as? Lead
        status = arguments?.getString("status")
        if (status == "1"){
            binding.btnAssign.visibility = View.GONE
        }else{
            binding.btnAssign.visibility = View.VISIBLE
        }
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.textTitle.animation = AnimationUtils.loadAnimation(requireContext(),R.anim.text_animation)
        binding.back.animation = AnimationUtils.loadAnimation(requireContext(), R.anim.left_anim)
        binding.mainLayout.animation=AnimationUtils.loadAnimation(requireContext(), R.anim.text_animation1)
        binding.secondLayout.animation=AnimationUtils.loadAnimation(requireContext(), R.anim.text_animation2)
        binding.yourNoteCard.animation = AnimationUtils.loadAnimation(requireContext(),R.anim.left_anim)
        binding.empNoteCard.animation = AnimationUtils.loadAnimation(requireContext(), R.anim.right_anim)
        binding.btnAssign.animation = AnimationUtils.loadAnimation(requireContext(), R.anim.text_animation3)

        binding.btnAssign.setOnClickListener {
            findNavController().navigate(R.id.action_LeadsDetails_to_LeadsAddFragment)
        }
        if (lead == null){
            Toast.makeText(requireContext(), "Cant found lead please contact Tech Team.", Toast.LENGTH_SHORT).show()
            findNavController().navigate(R.id.action_LeadsListFragmen_to_LeadsAssingedListFragment)
            return
        }else{
            lead?.let {
                binding.apply {
                    textViewName.text=it.name
                    textViewNumber.text = it.number
                    timeSlot.text= it.timeSlot
                    city.text= it.city
                    textViewEmployeeName.text= it.employeeName
                    val status = it.status
                    textViewStatus.text = status
                    if (status.equals("Accepted", ignoreCase = true)) {
                        textViewStatus.setTextColor(ContextCompat.getColor(requireContext(), R.color.green))
                    } else if((status.equals("Rejected", ignoreCase = true))){
                        textViewStatus.setTextColor(ContextCompat.getColor(requireContext(), R.color.red))
                    } else{
                        textViewStatus.setTextColor(ContextCompat.getColor(requireContext(), R.color.orangeLight))
                    }
                    rType.text= it.requirementType
                    address.text= it.address
                    noteDetails.text= it.yourNoteDetail
                    empNoteDetails.text= it.employeeNoteDetail
                }
            }
        }


        binding.back.setOnClickListener{
            parentFragmentManager.popBackStack()
        }
    }

}