package com.digitalsoch.bottomnavigations

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.digitalsoch.adapter.AssignAdapter
import com.digitalsoch.databinding.FragmentAssignBottomSheetBinding

class TimeSlotBottomSheetDialogFragment(
    private val onItemSelected: (String) -> Unit
) : BottomSheetDialogFragment() {

    private var _binding: FragmentAssignBottomSheetBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAssignBottomSheetBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val timeSlots = listOf(
            "9:00 AM - 10:00 AM",
            "10:00 AM - 11:00 AM",
            "11:00 AM - 12:00 PM",
            "12:00 PM - 1:00 PM",
            "1:00 PM - 2:00 PM",
            "2:00 PM - 3:00 PM",
            "3:00 PM - 4:00 PM",
            "4:00 PM - 5:00 PM",
            "5:00 PM - 6:00 PM",
            "6:00 PM - 7:00 PM"
        )

        val adapter = AssignAdapter(timeSlots) { selectedItem ->
            onItemSelected(selectedItem)
            dismiss()
        }

        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            this.adapter = adapter
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}