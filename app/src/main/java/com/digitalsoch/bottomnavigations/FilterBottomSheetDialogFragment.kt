package com.digitalsoch.bottomnavigations

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.digitalsoch.adapter.AssignAdapter
import com.digitalsoch.databinding.FilterAssignBottomSheetBinding
import com.digitalsoch.databinding.FragmentAssignBottomSheetBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


class FilterBottomSheetDialogFragment(
    private val onItemSelected: (String) -> Unit
) : BottomSheetDialogFragment() {

    private var _binding: FilterAssignBottomSheetBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FilterAssignBottomSheetBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val items = listOf(
            "Alice - Admin",
            "Bob - Employee",
            "Charlie - Manager",
            "David - HR",
            "Eve - Developer",
            "Frank - Designer",
            "Grace - Support",
            "Hank - Sales",
            "Ivy - Marketing",
            "Jack - Finance"
        )
        val adapter = AssignAdapter(items) { selectedItem ->
            onItemSelected(selectedItem)
            dismiss()
        }

        binding.resetData.setOnClickListener {
            onItemSelected("Select Employee For Filter")
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