package com.bryanadams.fetchnames.ui

import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bryanadams.fetchnames.R
import com.bryanadams.fetchnames.model.LineItemObject
import kotlinx.android.synthetic.main.name_item.view.*

class NamesAdapter(private val nameResponse: List<LineItemObject>) :
    RecyclerView.Adapter<NamesAdapter.ViewHolder>() {
    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(name: LineItemObject) {
            itemView.name.text = name.getText()
            if (name.isTitle() == true) {
                itemView.name.textSize = 30.0F
                itemView.name.typeface = Typeface.DEFAULT_BOLD

            } else {
                itemView.name.textSize = 20.0F
                itemView.name.typeface = Typeface.DEFAULT
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.name_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.bind(nameResponse[position])
    }

    override fun getItemCount(): Int = nameResponse.size
}