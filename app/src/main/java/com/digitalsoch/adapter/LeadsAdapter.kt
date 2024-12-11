package com.digitalsoch.adapter
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.RecyclerView
import com.digitalsoch.R
import com.digitalsoch.databinding.AdminLeadListItemBinding
import com.digitalsoch.databinding.AssignedByAdminListItemBinding
import com.digitalsoch.databinding.LeadListItemBinding
import com.digitalsoch.network.responses.Lead


class LeadsAdapter(
    private val context: Context,
    private val leads: ArrayList<Lead>,
    private val recyclerView: RecyclerView,
    private val onAccept: (Lead) -> Unit,
    private val onDecline: (Lead) -> Unit,
    private val onPending: (Lead) -> Unit
) : RecyclerView.Adapter<LeadsAdapter.LeadViewHolder>() {
    private var recentlyDeletedItem: Lead? = null
    private var recentlyDeletedItemPosition: Int = -1
    class LeadViewHolder(val binding: AdminLeadListItemBinding) : RecyclerView.ViewHolder(binding.root)
    fun deleteItem(position: Int) {
            recentlyDeletedItem = leads[position]
            recentlyDeletedItemPosition = position
            leads.removeAt(position)
            notifyItemRemoved(position)
    }
    fun archiveItem(position: Int) {
        var lead = leads[position]
        if (lead.status=="Accepted"){
            onAccept(lead)
        }else if (lead.status=="Rejected"){
            onDecline(lead)
        }else{
            onPending(lead)
        }

    }
    fun getItem(position: Int): Lead {
        return leads[position]
    }
    fun undoDelete() {
        recentlyDeletedItem?.let {
            leads.add(recentlyDeletedItemPosition, it)
            notifyItemInserted(recentlyDeletedItemPosition)
            Handler(Looper.getMainLooper()).post {
                recyclerView?.findViewHolderForAdapterPosition(recentlyDeletedItemPosition)?.itemView?.startAnimation(AnimationUtils.loadAnimation(context, R.anim.text_animation))
            }
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LeadViewHolder {
        val binding = AdminLeadListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return LeadViewHolder(binding)
    }

    override fun onBindViewHolder(holder: LeadViewHolder, position: Int) {
        val lead = leads[position]

        holder.binding.apply {
            animateViews(this)
            textViewName.text = lead.name
            textViewSlot.text = "Time Slot : "+lead.timeSlot
            textViewNumber.text = lead.number
            textViewEmpNote.text = if (lead.employeeNoteDetail == "") "" else lead.employeeNoteDetail
            textViewRequirementType.text = lead.requirementType
            textViewEmpName.text = lead.employeeName
            if (lead.status=="Accepted"){
                cardViewAccept.visibility=View.VISIBLE
                cardViewPending.visibility=View.GONE
                cardViewDecline.visibility=View.GONE
            }else if(lead.status=="Rejected"){
                cardViewAccept.visibility=View.GONE
                cardViewDecline.visibility=View.VISIBLE
                cardViewPending.visibility=View.GONE
            }else{
                cardViewPending.visibility=View.VISIBLE
                cardViewAccept.visibility=View.GONE
                cardViewDecline.visibility=View.GONE
            }

            cardViewAccept.setOnClickListener { onAccept(lead) }
            cardViewDecline.setOnClickListener { onDecline(lead) }
            cardViewPending.setOnClickListener { onPending(lead) }
        }
    }

    override fun getItemCount(): Int = leads.size


    private fun animateViews(binding: AdminLeadListItemBinding) {
        binding.textViewName.animation = AnimationUtils.loadAnimation(context, R.anim.text_animation1)
        binding.textViewNumber.animation = AnimationUtils.loadAnimation(context, R.anim.text_animation1)
        binding.reqTag.animation = AnimationUtils.loadAnimation(context, R.anim.text_animation1)
        binding.textViewRequirementType.animation = AnimationUtils.loadAnimation(context, R.anim.text_animation1)

        binding.empTag.animation = AnimationUtils.loadAnimation(context, R.anim.text_animation2)
        binding.textViewEmpName.animation = AnimationUtils.loadAnimation(context, R.anim.text_animation2)
        binding.empNoteTag.animation = AnimationUtils.loadAnimation(context,R.anim.text_animation2)
        binding.textViewEmpNote.animation = AnimationUtils.loadAnimation(context,R.anim.text_animation2)

        binding.textViewSlot.animation = AnimationUtils.loadAnimation(context, R.anim.text_animation3)
        binding.cardViewAccept.animation = AnimationUtils.loadAnimation(context, R.anim.text_animation3)
        binding.cardViewPending.animation = AnimationUtils.loadAnimation(context, R.anim.text_animation3)
        binding.cardViewDecline.animation = AnimationUtils.loadAnimation(context, R.anim.text_animation3)
    }
}
