package com.digitalsoch.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.RecyclerView
import com.digitalsoch.R
import com.digitalsoch.databinding.AdminLeadListItemBinding
import com.digitalsoch.databinding.LeadListItemBinding
import com.digitalsoch.network.responses.Lead


class LeadsViewAdapter(
    private val context:Context,
    private val leads: List<Lead>,
    private val onAccept: (Lead) -> Unit,
    private val onDecline: (Lead) -> Unit
) : RecyclerView.Adapter<LeadsViewAdapter.LeadViewHolder>() {

    class LeadViewHolder(val binding: LeadListItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LeadViewHolder {
        val binding = LeadListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return LeadViewHolder(binding)
    }

    override fun onBindViewHolder(holder: LeadViewHolder, position: Int) {
        val lead = leads[position]
        holder.binding.apply {
            animateViews(this)
            textViewName.text = lead.name
            textViewSlot.text = lead.timeSlot
            textViewNumber.text = lead.number
            textViewRequirementType.text = lead.requirementType
            textViewRequirementNote.text = lead.yourNoteDetail
            cardViewAccept.setOnClickListener { onAccept(lead) }
            cardViewDecline.setOnClickListener { onDecline(lead) }
        }
    }

    override fun getItemCount(): Int = leads.size
    private fun animateViews(binding: LeadListItemBinding) {
        binding.textViewName.animation = AnimationUtils.loadAnimation(context, R.anim.text_animation1)
        binding.textViewNumber.animation = AnimationUtils.loadAnimation(context, R.anim.text_animation1)
        binding.textViewSlot.animation = AnimationUtils.loadAnimation(context, R.anim.text_animation1)
        binding.textViewRequirementType.animation = AnimationUtils.loadAnimation(context, R.anim.text_animation1)
        binding.nameTag.animation = AnimationUtils.loadAnimation(context, R.anim.text_animation1)
        binding.numberTag.animation = AnimationUtils.loadAnimation(context, R.anim.text_animation1)
        binding.timeSlotTag.animation = AnimationUtils.loadAnimation(context, R.anim.text_animation1)
        binding.reqTag.animation = AnimationUtils.loadAnimation(context, R.anim.text_animation1)

        binding.textViewRequirementNote.animation = AnimationUtils.loadAnimation(context, R.anim.text_animation2)
        binding.reqNoteTag.animation = AnimationUtils.loadAnimation(context, R.anim.text_animation2)

        binding.cardViewAccept.animation = AnimationUtils.loadAnimation(context, R.anim.text_animation3)
        binding.cardViewDecline.animation = AnimationUtils.loadAnimation(context, R.anim.text_animation3)
    }
}
