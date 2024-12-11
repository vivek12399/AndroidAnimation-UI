package com.digitalsoch.adapter

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.RecyclerView
import com.digitalsoch.R
import com.digitalsoch.databinding.AssignedByAdminListItemBinding
import com.digitalsoch.network.responses.AdminLeads

class LeadsAssignedByAdminAdapter(
    private val context: Context,
    private val leads: List<AdminLeads>,
    private val onItemClick: (AdminLeads) -> Unit
) : RecyclerView.Adapter<LeadsAssignedByAdminAdapter.LeadViewHolder>() {

    class LeadViewHolder(val binding: AssignedByAdminListItemBinding) : RecyclerView.ViewHolder(binding.root)
    fun callItem(position: Int) {
        var lead = leads[position]
        val intent = Intent(Intent.ACTION_DIAL).apply {
            data = Uri.parse("tel:${lead.number}")
        }
        context.startActivity(intent)
    }
    fun assignedItem(position: Int) {
        var lead = leads[position]
        onItemClick(lead)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LeadViewHolder {
        val binding = AssignedByAdminListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return LeadViewHolder(binding)
    }

    override fun onBindViewHolder(holder: LeadViewHolder, position: Int) {
        val lead = leads[position]

        holder.binding.apply {

            animateViews(this)
            type.text = lead.type
            city.text = lead.city
            date.text = lead.date
            requirement.text = lead.requirement
            from.text = lead.from

            // Set click listeners
            btnAssign.setOnClickListener {
                onItemClick(lead)
            }

            btnCall.setOnClickListener {
                val intent = Intent(Intent.ACTION_DIAL).apply {
                    data = Uri.parse("tel:${lead.number}")
                }
                it.context.startActivity(intent)
            }


        }
    }


    override fun getItemCount(): Int = leads.size
    private fun animateViews(binding: AssignedByAdminListItemBinding) {
        binding.type.animation = AnimationUtils.loadAnimation(context, R.anim.text_animation1)
        binding.city.animation = AnimationUtils.loadAnimation(context, R.anim.text_animation1)
        binding.normalTxt.animation = AnimationUtils.loadAnimation(context, R.anim.text_animation1)
        binding.date.animation = AnimationUtils.loadAnimation(context, R.anim.text_animation1)
        binding.requirement.animation = AnimationUtils.loadAnimation(context, R.anim.text_animation2)
        binding.from.animation = AnimationUtils.loadAnimation(context, R.anim.text_animation2)
        binding.leadTxt.animation = AnimationUtils.loadAnimation(context, R.anim.text_animation2)
        binding.btnAssign.animation = AnimationUtils.loadAnimation(context, R.anim.text_animation3)
        binding.btnCall.animation = AnimationUtils.loadAnimation(context, R.anim.text_animation3)
    }

}
