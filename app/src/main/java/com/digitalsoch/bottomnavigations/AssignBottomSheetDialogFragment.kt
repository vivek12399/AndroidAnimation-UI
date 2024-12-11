package com.digitalsoch.bottomnavigations

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.digitalsoch.adapter.AssignAdapter
import com.digitalsoch.databinding.FragmentAssignBottomSheetBinding

class AssignBottomSheetDialogFragment(
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