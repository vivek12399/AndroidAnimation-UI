package com.digitalsoch.dashboard.fragments

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.digitalsoch.R
import com.digitalsoch.adapter.LeadsAdapter
import com.digitalsoch.adapter.LeadsViewAdapter
import com.digitalsoch.bottomnavigations.FilterBottomSheetDialogFragment
import com.digitalsoch.databinding.FragmentAssignedLeadListBinding
import com.digitalsoch.network.responses.Lead
import com.digitalsoch.utils.Constants.ROLE
import com.google.android.material.snackbar.Snackbar

class AssingnedLeadListFragment : Fragment() {

    private var _binding: FragmentAssignedLeadListBinding? = null
    private val binding get() = _binding!!
    private var leadsAdapter: LeadsAdapter? = null
    private var isAllFabsVisible: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAssignedLeadListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        setupUIAnimations()
        setupSwipeActions()
        setupAssignNameTextClickListener()
        setupBackButtonClickListener()
    }

    private fun setupRecyclerView() {
        binding.leadRView.layoutManager = LinearLayoutManager(context)

        if (ROLE == 1) {
            val leadsAssigned = getLeadsAssigned()
            leadsAdapter = LeadsAdapter(requireContext(), leadsAssigned, binding.leadRView,
                onAccept = {
                    navigateToLeadDetails(it, "1")
                },
                onDecline = {
                    navigateToLeadDetails(it, "0")
                },
                onPending = {
                    navigateToLeadDetails(it, "0")
                }
            )
            binding.leadRView.adapter = leadsAdapter
        } else {
            val leads = getLeads()
            binding.leadRView.adapter = LeadsViewAdapter(requireContext(), leads,
                onAccept = {
                    findNavController().navigate(R.id.action_LeadsListFragmen_to_LeadAccept)
                },
                onDecline = {
                    Toast.makeText(requireContext(), "Lead Declined", Toast.LENGTH_SHORT).show()
                }
            )
        }
    }

    private fun setupUIAnimations() {
        binding.textTitle.animation = AnimationUtils.loadAnimation(requireContext(), R.anim.text_animation)
        binding.back.animation = AnimationUtils.loadAnimation(requireContext(), R.anim.left_anim)
        binding.assignNameText.animation = AnimationUtils.loadAnimation(requireContext(), R.anim.right_anim)
    }

    private fun setupSwipeActions() {
        val deleteIcon = ContextCompat.getDrawable(requireContext(), R.drawable.baseline_delete_24)
        val archiveIcon = ContextCompat.getDrawable(requireContext(), R.drawable.baseline_remove_red_eye_24)
        val clearPaint = Paint().apply { xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR) }

        val itemTouchHelperCallback = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                when (direction) {
                    ItemTouchHelper.LEFT -> {
                        leadsAdapter?.deleteItem(position)
                        showUndoSnackbar()
                    }
                    ItemTouchHelper.RIGHT -> {
                        leadsAdapter?.archiveItem(position)
                    }
                }
            }

            override fun onChildDraw(
                c: Canvas, recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder,
                dX: Float, dY: Float, actionState: Int, isCurrentlyActive: Boolean
            ) {
                val itemView = viewHolder.itemView
                val itemHeight = itemView.bottom - itemView.top
                val isCanceled = dX == 0f && !isCurrentlyActive

                if (isCanceled) {
                    clearCanvas(c, itemView.right + dX, itemView.top.toFloat(), itemView.right.toFloat(), itemView.bottom.toFloat())
                    super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                    return
                }

                val isSwipingRight = dX > 0
                val icon = if (isSwipingRight) archiveIcon else deleteIcon

                val back= if  (isSwipingRight) getColorBasedOnStatus(leadsAdapter?.getItem(viewHolder.adapterPosition)?.status, isSwipingRight) else Color.parseColor("#FF1400")
                drawBackground(c, itemView, dX, back, isSwipingRight)
                drawIcon(c, itemView, icon, itemHeight, isSwipingRight)

                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
            }

            private fun clearCanvas(c: Canvas?, left: Float, top: Float, right: Float, bottom: Float) {
                c?.drawRect(left, top, right, bottom, clearPaint)
            }

            private fun drawBackground(c: Canvas, itemView: View, dX: Float, color: Int, isSwipingRight: Boolean) {
                val background = ColorDrawable(color)
                if (isSwipingRight) {
                    background.setBounds(itemView.left, itemView.top, itemView.left + dX.toInt(), itemView.bottom)
                } else {
                    background.setBounds(itemView.right + dX.toInt(), itemView.top, itemView.right, itemView.bottom)
                }
                background.draw(c)
            }

            private fun drawIcon(c: Canvas, itemView: View, icon: Drawable?, itemHeight: Int, isSwipingRight: Boolean) {
                val intrinsicWidth = icon?.intrinsicWidth ?: 0
                val intrinsicHeight = icon?.intrinsicHeight ?: 0
                val scaleFactor = 1.5f
                val adjustedWidth = (intrinsicWidth * scaleFactor).toInt()
                val adjustedHeight = (intrinsicHeight * scaleFactor).toInt()
                val iconTop = itemView.top + (itemHeight - adjustedHeight) / 2
                val iconMargin = 16
                val iconLeft = if (isSwipingRight) itemView.left + iconMargin else itemView.right - iconMargin - adjustedWidth
                val iconRight = if (isSwipingRight) itemView.left + iconMargin + adjustedWidth else itemView.right - iconMargin
                val iconBottom = iconTop + adjustedHeight

                icon?.setBounds(iconLeft, iconTop, iconRight, iconBottom)
                icon?.draw(c)
            }

            private fun getColorBasedOnStatus(status: String?, isSwipingRight: Boolean): Int {
                return if (isSwipingRight) {
                    when (status) {
                        "Accepted" -> Color.parseColor("#128807")
                        "Rejected" -> Color.parseColor("#FF1400")
                        "Pending" -> Color.parseColor("#FFC107")
                        else -> Color.GRAY
                    }
                } else {
                    Color.parseColor("#FF1400")
                }
            }
        }

        val itemTouchHelper = ItemTouchHelper(itemTouchHelperCallback)
        itemTouchHelper.attachToRecyclerView(binding.leadRView)
    }


    private fun setupAssignNameTextClickListener() {
        binding.assignNameText.setOnClickListener {
            val bottomSheet = FilterBottomSheetDialogFragment { selectedItem ->
                binding.assignNameText.text = selectedItem
                binding.assignNameText.visibility = View.VISIBLE
            }
            bottomSheet.show(parentFragmentManager, "AssignBottomSheet")
        }
    }

    private fun setupBackButtonClickListener() {
        binding.back.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun navigateToLeadDetails(lead: Lead, status: String) {
        val bundle = Bundle().apply {
            putString("status", status)
            putSerializable("lead", lead)
        }
        findNavController().navigate(R.id.action_LeadsAssignedList_to_LeadsDetailsFragment, bundle)
    }

    private fun showUndoSnackbar() {
        val view = requireView()
        val snackbar = Snackbar.make(view, "Lead deleted", 3000)
        snackbar.setAction("UNDO") {
            leadsAdapter?.undoDelete()
        }
        snackbar.show()
    }

    private fun getLeadsAssigned(): ArrayList<Lead> {
        return arrayListOf(
            Lead(
                name = "Sophia Davis",
                number = "5552345678",
                timeSlot = "18 Jun 2024, 04.15 pm",
                assignedTo = "Alex Smith",
                status = "Accepted",
                requirementType = "UI/UX Design",
                address = "123 Maple Street",
                yourNoteDetail = "Create wireframes and prototypes for a new mobile application, focusing on user experience and interface design.",
                employeeNoteDetail = "Review and provide feedback on the wireframes and prototypes created for the new mobile application.",
                city = "Phoenix",
                email = "sophia.davis@example.com",
                pincode = "85001",
                employeeName = "Alex Smith"
            ),
            Lead(
                name = "Sarah Lee",
                number = "5557890123",
                timeSlot = "30 Jun 2024, 11.15 am",
                assignedTo = "Sophia Davis",
                status = "Pending",
                requirementType = "Cloud Computing",
                address = "303 Spruce Street",
                yourNoteDetail = "Design and implement cloud infrastructure for the application.",
                employeeNoteDetail = "",
                city = "San Francisco",
                email = "sarah.lee@example.com",
                pincode = "94101",
                employeeName = "Sophia Davis"
            ),


            Lead(
                name = "Sarah Lee",
                number = "5557890123",
                timeSlot = "30 Jun 2024, 11.15 am",
                assignedTo = "Sophia Davis",
                status = "Rejected",
                requirementType = "Cloud Computing",
                address = "303 Spruce Street",
                yourNoteDetail = "Design and implement cloud infrastructure for the application.",
                employeeNoteDetail = "Ensure cloud infrastructure is secure and scalable.",
                city = "San Francisco",
                email = "sarah.lee@example.com",
                pincode = "94101",
                employeeName = "Sophia Davis"
            ),
            Lead(
                name = "John Doe",
                number = "5553456789",
                timeSlot = "20 Jun 2024, 10.00 am",
                assignedTo = "Emma Brown",
                status = "Accepted",
                requirementType = "Backend Development",
                address = "456 Oak Street",
                yourNoteDetail = "Develop backend APIs for a web application, ensuring security and scalability.",
                employeeNoteDetail = "Integrate the backend APIs with the frontend and perform unit testing.",
                city = "Los Angeles",
                email = "john.doe@example.com",
                pincode = "90001",
                employeeName = "Emma Brown"
            ),



            Lead(
                name = "David Brown",
                number = "5558901234",
                timeSlot = "02 Jul 2024, 09.30 am",
                assignedTo = "Isabella Garcia",
                status = "Rejected",
                requirementType = "Cybersecurity",
                address = "404 Elm Street",
                yourNoteDetail = "Implement security measures to protect the application from cyber threats.",
                employeeNoteDetail = "Conduct regular security audits and vulnerability assessments.",
                city = "Seattle",
                email = "david.brown@example.com",
                pincode = "98101",
                employeeName = "Isabella Garcia"
            ),
            Lead(
                name = "Michael Johnson",
                number = "5556789012",
                timeSlot = "28 Jun 2024, 02.00 pm",
                assignedTo = "James Wilson",
                status = "Accepted",
                requirementType = "DevOps",
                address = "202 Cedar Street",
                yourNoteDetail = "Set up CI/CD pipelines for the application deployment.",
                employeeNoteDetail = "Monitor the CI/CD pipelines and troubleshoot any issues.",
                city = "Houston",
                email = "michael.johnson@example.com",
                pincode = "77001",
                employeeName = "James Wilson"
            ),
            Lead(
                name = "Lucas Martinez",
                number = "5559012345",
                timeSlot = "04 Jul 2024, 01.00 pm",
                assignedTo = "Liam Johnson",
                status = "Pending",
                requirementType = "Data Science",
                address = "505 Pine Street",
                yourNoteDetail = "Analyze large datasets to extract meaningful insights and trends.",
                employeeNoteDetail = "",
                city = "Denver",
                email = "lucas.martinez@example.com",
                pincode = "80201",
                employeeName = "Liam Johnson"
            ),
            Lead(
                name = "Emily Wilson",
                number = "5555678901",
                timeSlot = "25 Jun 2024, 03.45 pm",
                assignedTo = "Olivia Davis",
                status = "Accepted",
                requirementType = "Mobile App Development",
                address = "101 Birch Street",
                yourNoteDetail = "Develop a mobile application for both Android and iOS platforms.",
                employeeNoteDetail = "Test the mobile application on different devices and platforms.",
                city = "Chicago",
                email = "emily.wilson@example.com",
                pincode = "60601",
                employeeName = "Olivia Davis"
            ),

            Lead(
                name = "Lucas Martinez",
                number = "5559012345",
                timeSlot = "04 Jul 2024, 01.00 pm",
                assignedTo = "Liam Johnson",
                status = "Rejected",
                requirementType = "Data Science",
                address = "505 Pine Street",
                yourNoteDetail = "Analyze large datasets to extract meaningful insights and trends.",
                employeeNoteDetail = "Create data visualization dashboards to present findings.",
                city = "Denver",
                email = "lucas.martinez@example.com",
                pincode = "80201",
                employeeName = "Liam Johnson"
            ),
            Lead(
                name = "Jane Smith",
                number = "5554567890",
                timeSlot = "22 Jun 2024, 01.30 pm",
                assignedTo = "Liam Johnson",
                status = "Accepted",
                requirementType = "Frontend Development",
                address = "789 Pine Street",
                yourNoteDetail = "Create responsive web pages using React and ensure cross-browser compatibility.",
                employeeNoteDetail = "Perform code reviews and ensure adherence to coding standards.",
                city = "New York",
                email = "jane.smith@example.com",
                pincode = "10001",
                employeeName = "Liam Johnson"
            ),
            Lead(
                name = "David Brown",
                number = "5558901234",
                timeSlot = "02 Jul 2024, 09.30 am",
                assignedTo = "Isabella Garcia",
                status = "Pending",
                requirementType = "Cyber security",
                address = "404 Elm Street",
                yourNoteDetail = "Implement security measures to protect the application from cyber threats.",
                employeeNoteDetail = "",
                city = "Seattle",
                email = "david.brown@example.com",
                pincode = "98101",
                employeeName = "Isabella Garcia"
            ),
        )
    }

    private fun getLeads(): List<Lead> {
        return listOf(
            Lead(
                name = "Sophia Davis",
                number = "5552345678",
                timeSlot = "18 Jun 2024, 04.15 pm",
                assignedTo = "Alex Smith",
                status = "Accepted",
                requirementType = "UI/UX Design",
                address = "123 Maple Street",
                yourNoteDetail = "Create wireframes and prototypes for a new mobile application, focusing on user experience and interface design.",
                employeeNoteDetail = "Review and provide feedback on the wireframes and prototypes created for the new mobile application.",
                city = "Phoenix",
                email = "sophia.davis@example.com",
                pincode = "85001",
                employeeName = "Alex Smith"
            ),
            Lead(
                name = "Sarah Lee",
                number = "5557890123",
                timeSlot = "30 Jun 2024, 11.15 am",
                assignedTo = "Sophia Davis",
                status = "Rejected",
                requirementType = "Cloud Computing",
                address = "303 Spruce Street",
                yourNoteDetail = "Design and implement cloud infrastructure for the application.",
                employeeNoteDetail = "Ensure cloud infrastructure is secure and scalable.",
                city = "San Francisco",
                email = "sarah.lee@example.com",
                pincode = "94101",
                employeeName = "Sophia Davis"
            ),
            Lead(
                name = "John Doe",
                number = "5553456789",
                timeSlot = "20 Jun 2024, 10.00 am",
                assignedTo = "Emma Brown",
                status = "Accepted",
                requirementType = "Backend Development",
                address = "456 Oak Street",
                yourNoteDetail = "Develop backend APIs for a web application, ensuring security and scalability.",
                employeeNoteDetail = "Integrate the backend APIs with the frontend and perform unit testing.",
                city = "Los Angeles",
                email = "john.doe@example.com",
                pincode = "90001",
                employeeName = "Emma Brown"
            ),



            Lead(
                name = "David Brown",
                number = "5558901234",
                timeSlot = "02 Jul 2024, 09.30 am",
                assignedTo = "Isabella Garcia",
                status = "Rejected",
                requirementType = "Cybersecurity",
                address = "404 Elm Street",
                yourNoteDetail = "Implement security measures to protect the application from cyber threats.",
                employeeNoteDetail = "Conduct regular security audits and vulnerability assessments.",
                city = "Seattle",
                email = "david.brown@example.com",
                pincode = "98101",
                employeeName = "Isabella Garcia"
            ),
            Lead(
                name = "Michael Johnson",
                number = "5556789012",
                timeSlot = "28 Jun 2024, 02.00 pm",
                assignedTo = "James Wilson",
                status = "Accepted",
                requirementType = "DevOps",
                address = "202 Cedar Street",
                yourNoteDetail = "Set up CI/CD pipelines for the application deployment.",
                employeeNoteDetail = "Monitor the CI/CD pipelines and troubleshoot any issues.",
                city = "Houston",
                email = "michael.johnson@example.com",
                pincode = "77001",
                employeeName = "James Wilson"
            ),
            Lead(
                name = "Emily Wilson",
                number = "5555678901",
                timeSlot = "25 Jun 2024, 03.45 pm",
                assignedTo = "Olivia Davis",
                status = "Accepted",
                requirementType = "Mobile App Development",
                address = "101 Birch Street",
                yourNoteDetail = "Develop a mobile application for both Android and iOS platforms.",
                employeeNoteDetail = "Test the mobile application on different devices and platforms.",
                city = "Chicago",
                email = "emily.wilson@example.com",
                pincode = "60601",
                employeeName = "Olivia Davis"
            ),

            Lead(
                name = "Lucas Martinez",
                number = "5559012345",
                timeSlot = "04 Jul 2024, 01.00 pm",
                assignedTo = "Liam Johnson",
                status = "Rejected",
                requirementType = "Data Science",
                address = "505 Pine Street",
                yourNoteDetail = "Analyze large datasets to extract meaningful insights and trends.",
                employeeNoteDetail = "Create data visualization dashboards to present findings.",
                city = "Denver",
                email = "lucas.martinez@example.com",
                pincode = "80201",
                employeeName = "Liam Johnson"
            ),
            Lead(
                name = "Jane Smith",
                number = "5554567890",
                timeSlot = "22 Jun 2024, 01.30 pm",
                assignedTo = "Liam Johnson",
                status = "Accepted",
                requirementType = "Frontend Development",
                address = "789 Pine Street",
                yourNoteDetail = "Create responsive web pages using React and ensure cross-browser compatibility.",
                employeeNoteDetail = "Perform code reviews and ensure adherence to coding standards.",
                city = "New York",
                email = "jane.smith@example.com",
                pincode = "10001",
                employeeName = "Liam Johnson"
            )
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
