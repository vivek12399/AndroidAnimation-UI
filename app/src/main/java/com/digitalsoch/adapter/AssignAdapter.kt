package com.digitalsoch.adapter


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.digitalsoch.R

class AssignAdapter(
    private val items: List<String>,
    private val onItemClick: (String) -> Unit
) : RecyclerView.Adapter<AssignAdapter.AssignViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AssignViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_assign, parent, false)
        return AssignViewHolder(view)
    }

    override fun onBindViewHolder(holder: AssignViewHolder, position: Int) {
        val item = items[position]
        holder.textView.text = item
        holder.itemView.setOnClickListener {
            onItemClick(item)
        }
    }

    override fun getItemCount(): Int = items.size

    class AssignViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textView: TextView = itemView.findViewById(R.id.textViewItem)
    }
}