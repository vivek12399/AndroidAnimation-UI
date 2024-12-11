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
import com.digitalsoch.network.responses.Lead
import com.digitalsoch.adapter.LeadsAdapter
import com.digitalsoch.adapter.LeadsViewAdapter
import com.digitalsoch.bottomnavigations.FilterBottomSheetDialogFragment
import com.digitalsoch.databinding.FragmentRejectLeadListBinding
import com.digitalsoch.utils.Constants.ROLE
import com.google.android.material.snackbar.Snackbar

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class RejectedLeadListFragment : Fragment() {

    private var _binding: FragmentRejectLeadListBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private var leadsAdapter: LeadsAdapter? = null
    private var isAllFabsVisible: Boolean? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View{

        _binding = FragmentRejectLeadListBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val deleteIcon: Drawable? = ContextCompat.getDrawable(requireContext(), R.drawable.baseline_delete_24) // Replace with your delete icon
        val archiveIcon: Drawable? = ContextCompat.getDrawable(requireContext(), R.drawable.baseline_remove_red_eye_24) // Replace with your delete icon
        val intrinsicWidth: Int = deleteIcon?.intrinsicWidth ?: 0
        val intrinsicHeight: Int = deleteIcon?.intrinsicHeight ?: 0
        var background = ColorDrawable()
        val deleteBackgroundColor = Color.parseColor("#FF1400")
        val archiveBackgroundColor = Color.parseColor("#FF1400")
        val clearPaint = Paint().apply { xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR) }
        val itemTouchHelperCallback = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {

                val position = viewHolder.adapterPosition
                when (direction) {
                    ItemTouchHelper.LEFT -> {
                        leadsAdapter!!.deleteItem(position)
                        showUndoSnackbar()
                    }
                    ItemTouchHelper.RIGHT -> {
                        leadsAdapter!!.archiveItem(position)
                    }
                }
            }

            override fun onChildDraw(
                c: Canvas,
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                dX: Float,
                dY: Float,
                actionState: Int,
                isCurrentlyActive: Boolean
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

                background.color = if (isSwipingRight) archiveBackgroundColor else deleteBackgroundColor
                background.setBounds(
                    if (isSwipingRight) itemView.left else itemView.right + dX.toInt(),
                    itemView.top,
                    if (isSwipingRight) itemView.left + dX.toInt() else itemView.right,
                    itemView.bottom
                )
                background.draw(c)

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

                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
            }

            private fun clearCanvas(c: Canvas?, left: Float, top: Float, right: Float, bottom: Float) {
                c?.drawRect(left, top, right, bottom, clearPaint)
            }
            private fun showUndoSnackbar() {
                val view = requireView()
                val snackbar = Snackbar.make(view, "Lead deleted", 3000)
                snackbar.setAction("UNDO") {
                    leadsAdapter!!.undoDelete()
                }
                snackbar.show()
            }

        }
        binding.textTitle.animation = AnimationUtils.loadAnimation(requireContext(),R.anim.text_animation)
        binding.back.animation = AnimationUtils.loadAnimation(requireContext(), R.anim.left_anim)
        binding.assignNameText.animation = AnimationUtils.loadAnimation(requireContext(), R.anim.right_anim)

        val leads = arrayListOf(
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
            )
        )
        binding.assignNameText.setOnClickListener {
            val bottomSheet = FilterBottomSheetDialogFragment { selectedItem ->
                binding.assignNameText.text = selectedItem

            }
            bottomSheet.show(parentFragmentManager, "AssignBottomSheet")
        }



        binding.back.setOnClickListener {
            findNavController().popBackStack()
        }
        isAllFabsVisible = false
        if (ROLE==1){
            binding.leadRView.layoutManager = LinearLayoutManager(context)
            leadsAdapter = LeadsAdapter(requireContext(),leads, binding.leadRView,onAccept = {

            }, onDecline = {
                /*findNavController().navigate(R.id.action_LeadsAssignedList_to_LeadsAddFragment)*/
                val bundle = Bundle().apply {
                    putString("status", "0")
                    putSerializable("lead", it) }
                findNavController().navigate(R.id.action_LeadsRejedctListFragment_to_LeadsDetailFragment, bundle)
            }, onPending = {


            })
            binding.leadRView.adapter = leadsAdapter
            val itemTouchHelper = ItemTouchHelper(itemTouchHelperCallback)
            itemTouchHelper.attachToRecyclerView(binding.leadRView)

        }else{
            binding.leadRView.layoutManager = LinearLayoutManager(context)
            binding.leadRView.adapter = LeadsViewAdapter(requireContext(),leads, onAccept = {
                findNavController().navigate(R.id.action_LeadsListFragmen_to_LeadAccept)
            }, onDecline = {
                Toast.makeText(context,"Lead Declined",Toast.LENGTH_SHORT).show()
            })

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}