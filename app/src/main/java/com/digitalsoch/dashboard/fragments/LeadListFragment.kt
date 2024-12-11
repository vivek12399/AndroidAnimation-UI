package com.digitalsoch.dashboard.fragments

import android.content.Intent
import android.content.res.ColorStateList
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
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.digitalsoch.R
import com.digitalsoch.network.responses.Lead
import com.digitalsoch.adapter.LeadsAssignedByAdminAdapter
import com.digitalsoch.adapter.LeadsViewAdapter
import com.digitalsoch.auth.LoginActivity
import com.digitalsoch.databinding.FragmentLeadListBinding
import com.digitalsoch.network.responses.AdminLeads
import com.digitalsoch.utils.Constants.ROLE
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class LeadListFragment : Fragment() {

    private var _binding: FragmentLeadListBinding? = null


    private val binding get() = _binding!!
    private var isAllFabsVisible: Boolean? = null
    private var leadsAssignedByAdminAdapter: LeadsAssignedByAdminAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentLeadListBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val assignedIcon: Drawable? = ContextCompat.getDrawable(requireContext(), R.drawable.baseline_add_24)
        val callIcon: Drawable? = ContextCompat.getDrawable(requireContext(), R.drawable.baseline_phone_forwarded_24)
        val intrinsicWidth: Int = assignedIcon?.intrinsicWidth ?: 0
        val intrinsicHeight: Int = assignedIcon?.intrinsicHeight ?: 0
        var background = ColorDrawable()
        val assignedBackgroundColor = Color.parseColor("#3C74AF")
        val callBackgroundColor = Color.parseColor("#128807")
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
                        leadsAssignedByAdminAdapter!!.assignedItem(position)
                    }
                    ItemTouchHelper.RIGHT -> {
                        leadsAssignedByAdminAdapter!!.callItem(position)
                        leadsAssignedByAdminAdapter!!.notifyItemChanged(position)
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
                val icon = if (isSwipingRight) callIcon else assignedIcon

                background.color = if (isSwipingRight) callBackgroundColor else assignedBackgroundColor
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


        }
        binding.textTitle.animation =
            AnimationUtils.loadAnimation(requireContext(), R.anim.text_animation)
        binding.imageView.animation =
            AnimationUtils.loadAnimation(requireContext(), R.anim.icon_anim)
        binding.btnLogout.animation =
            AnimationUtils.loadAnimation(requireContext(), R.anim.icon_anim)
        val leads = listOf(
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
            )
        )
        val adminLeads = listOf(
            AdminLeads(
                name = "Alice Benson",
                date = "03 Aug 2024, 10.00 am",
                city = "New York",
                address = "",
                number = "5551234567",
                type = "Android Application Development",
                from = "LinkedIn",
                email = "alice.benson@example.com",
                requirement = "Develop a user-friendly Android application with integrated payment gateway and push notifications.",
                pincode = "10001"
            ),
            AdminLeads(
                name = "Michael Smith",
                date = "12 Jul 2024, 11.30 am",
                city = "Los Angeles",
                address = "",
                number = "5559876543",
                type = "iOS Application Development",
                from = "Job Portal",
                email = "michael.smith@example.com",
                requirement = "Create an iOS application with social media integration and real-time chat features.",
                pincode = "90001"
            ),
            AdminLeads(
                name = "Emma Johnson",
                date = "25 Jun 2024, 02.45 pm",
                city = "Chicago",
                address = "",
                number = "5556789012",
                type = "Web Application Development",
                from = "Website",
                email = "emma.johnson@example.com",
                requirement = "Design and implement a responsive web application with user authentication and data visualization.",
                pincode = "60601"
            ),
            AdminLeads(
                name = "Oliver Brown",
                date = "10 Jul 2024, 09.00 am",
                city = "Houston",
                address = "",
                number = "5553456789",
                type = "Backend API Development",
                from = "Referrals",
                email = "oliver.brown@example.com",
                requirement = "Develop a scalable backend API with RESTful endpoints for a mobile application.",
                pincode = "77001"
            ),
            AdminLeads(
                name = "Sophia Davis",
                date = "18 Jun 2024, 04.15 pm",
                city = "Phoenix",
                address = "",
                number = "5552345678",
                type = "UI/UX Design",
                from = "LinkedIn",
                email = "sophia.davis@example.com",
                requirement = "Create wireframes and prototypes for a new mobile application, focusing on user experience and interface design.",
                pincode = "85001"
            )
        )

        binding.rejectedTxt.visibility = View.GONE
        binding.acceptedTxt.visibility = View.GONE
        binding.addTxt.visibility = View.GONE
        binding.assignedTxt.visibility = View.GONE
        binding.pendingTxt.visibility = View.GONE
        binding.addRejectedFab.hide()
        binding.addAccptedFab.hide()
        binding.addLeadFab.hide()
        binding.addAssignedFab.hide()
        binding.addPendingFab.hide()
        binding.fbImg.visibility = View.GONE
        binding.apply {
            fab.backgroundTintList = ColorStateList.valueOf(
                ContextCompat.getColor(
                    requireContext(),
                    android.R.color.white
                )
            )
            addLeadFab.backgroundTintList = ColorStateList.valueOf(
                ContextCompat.getColor(
                    requireContext(),
                    android.R.color.white
                )
            )
            addAssignedFab.backgroundTintList = ColorStateList.valueOf(
                ContextCompat.getColor(
                    requireContext(),
                    android.R.color.white
                )
            )
            addRejectedFab.backgroundTintList = ColorStateList.valueOf(
                ContextCompat.getColor(
                    requireContext(),
                    android.R.color.white
                )
            )
        }

        isAllFabsVisible = false
        if (ROLE == 1) {

            binding.leadRView.layoutManager = LinearLayoutManager(context)
            leadsAssignedByAdminAdapter =
                LeadsAssignedByAdminAdapter(requireContext(), adminLeads, onItemClick = {
                    val bundle = Bundle().apply {
                        putSerializable("lead", it)
                    }
                    findNavController().navigate(
                        R.id.action_LeadsListFragmen_to_LeadsAddFragment,
                        bundle
                    )
                })
            binding.leadRView.adapter = leadsAssignedByAdminAdapter
            val itemTouchHelper = ItemTouchHelper(itemTouchHelperCallback)
            itemTouchHelper.attachToRecyclerView(binding.leadRView)

            binding.addLeadFab.setOnClickListener {
                findNavController().navigate(R.id.action_LeadsListFragmen_to_LeadsAddFragment)
            }
            binding.addAssignedFab.setOnClickListener {
                findNavController().navigate(R.id.action_LeadsListFragmen_to_LeadsAssingedListFragment)
            }
            binding.addRejectedFab.setOnClickListener {
                findNavController().navigate(R.id.action_LeadsListFragmen_to_LeadReject)
            }
            binding.addAccptedFab.setOnClickListener {
                findNavController().navigate(R.id.action_LeadsListFragmen_to_LeadsAcceptListFragment)
            }
            binding.addPendingFab.setOnClickListener {
                findNavController().navigate(R.id.action_LeadsListFragmen_to_LeadsPendingListFragment)
            }
            binding.fab.setOnClickListener(View.OnClickListener {
                (if (!isAllFabsVisible!!) {
                    GlobalScope.launch(Dispatchers.Main) {
                        viewShow()
                    }
                    true
                } else {
                    GlobalScope.launch(Dispatchers.Main) {
                        binding.fbImg.visibility = View.GONE
                        binding.rejectedTxt.visibility = View.GONE
                        binding.acceptedTxt.visibility = View.GONE
                        binding.addTxt.visibility = View.GONE
                        binding.assignedTxt.visibility = View.GONE
                        binding.pendingTxt.visibility = View.GONE
                        binding.fbImg.visibility = View.GONE
                        binding.addRejectedFab.visibility = View.GONE
                        binding.addAccptedFab.visibility = View.GONE
                        binding.addLeadFab.visibility = View.GONE
                        binding.addAssignedFab.visibility = View.GONE
                        binding.addPendingFab.visibility = View.GONE
                    }
                    false
                }).also { isAllFabsVisible = it }
            })
        } else {
            binding.leadRView.layoutManager = LinearLayoutManager(context)
            binding.leadRView.adapter = LeadsViewAdapter(requireContext(),leads, onAccept = {
                findNavController().navigate(R.id.action_LeadsListFragmen_to_LeadAccept)
            }, onDecline = {
                Toast.makeText(context, "Lead Declined", Toast.LENGTH_SHORT).show()
            })
            binding.fab.apply {
                visibility = View.GONE
            }
        }

        binding.btnLogout.setOnClickListener {
            val dialogView = layoutInflater.inflate(R.layout.dialog_logout, null)
            val btnCancel = dialogView.findViewById<Button>(R.id.btnCancel)
            val btnConfirm = dialogView.findViewById<Button>(R.id.btnConfirm)

            val dialog = AlertDialog.Builder(requireContext())
                .setView(dialogView)
                .create()

            btnCancel.setOnClickListener {
                dialog.dismiss()
            }

            btnConfirm.setOnClickListener {
                /*preferenceProvider.clear()
                preferenceProvider.putBoolean(Constants.KEY_IS_LOGIN, false)*/
                startActivity(Intent(context, LoginActivity::class.java))
                requireActivity().finish()
                dialog.dismiss()
            }

            dialog.show()

        }

        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    handleBackPressed()

                }
            })
    }


    private fun viewShow() {
        binding.fbImg.animation =
            AnimationUtils.loadAnimation(requireContext(), R.anim.icon_show)
        binding.rejectedTxt.animation =
            AnimationUtils.loadAnimation(requireContext(), R.anim.icon_show)
        binding.addTxt.animation =
            AnimationUtils.loadAnimation(requireContext(), R.anim.icon_show)
        binding.acceptedTxt.animation =
            AnimationUtils.loadAnimation(requireContext(), R.anim.icon_show)
        binding.assignedTxt.animation =
            AnimationUtils.loadAnimation(requireContext(), R.anim.icon_show)
        binding.pendingTxt.animation =
            AnimationUtils.loadAnimation(requireContext(), R.anim.icon_show)
        binding.addAccptedFab.animation =
            AnimationUtils.loadAnimation(requireContext(), R.anim.icon_show)
        binding.addLeadFab.animation =
            AnimationUtils.loadAnimation(requireContext(), R.anim.icon_show)
        binding.addRejectedFab.animation =
            AnimationUtils.loadAnimation(requireContext(), R.anim.icon_show)
        binding.addAssignedFab.animation =
            AnimationUtils.loadAnimation(requireContext(), R.anim.icon_show)
        binding.addPendingFab.animation =
            AnimationUtils.loadAnimation(requireContext(), R.anim.icon_show)
        binding.fbImg.visibility = View.VISIBLE
        binding.rejectedTxt.visibility = View.VISIBLE
        binding.addTxt.visibility = View.VISIBLE
        binding.acceptedTxt.visibility = View.VISIBLE
        binding.assignedTxt.visibility = View.VISIBLE
        binding.pendingTxt.visibility = View.VISIBLE
        binding.addAccptedFab.show()
        binding.addLeadFab.show()
        binding.addRejectedFab.show()
        binding.addAssignedFab.show()
        binding.addPendingFab.show()
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun handleBackPressed() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_logout, null)
        val btnCancel = dialogView.findViewById<Button>(R.id.btnCancel)
        val btnConfirm = dialogView.findViewById<Button>(R.id.btnConfirm)
        val titleTxt = dialogView.findViewById<TextView>(R.id.title)
        titleTxt.text = "Are you sure you want to exit?"

        btnConfirm.text = "Yes"
        btnCancel.text = "No"
        val dialog = AlertDialog.Builder(requireContext())
            .setView(dialogView)
            .create()

        btnCancel.setOnClickListener {
            dialog.dismiss()
        }

        btnConfirm.setOnClickListener {
            requireActivity().finish()
            dialog.dismiss()
        }

        dialog.show()
    }

}