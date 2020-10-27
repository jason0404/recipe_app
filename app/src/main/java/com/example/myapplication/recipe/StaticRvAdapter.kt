package com.example.myapplication.recipe

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.myapplication.CellClickListener
import com.example.myapplication.database.Recipe
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.example.myapplication.R
import com.example.myapplication.RecipeDetails
import com.example.myapplication.common.StaticRvModel
import com.example.myapplication.database.FavList

class StaticRvAdapter(private val mData: ArrayList<StaticRvModel>, private val cellClickListener: CellClickListener) :
    RecyclerView.Adapter<StaticRvAdapter.StaticRVViewHolder>() {

    var row_index:Int = -1
    var onItemClick: (Int) -> Unit = {}

    override fun getItemCount(): Int {
        return mData!!.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StaticRVViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.static_rv_item, parent, false)
        return StaticRVViewHolder(view)
    }

    class StaticRVViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textView: TextView = itemView.findViewById(R.id.textStatic)
        val imageView: ImageView = itemView.findViewById(R.id.imageStatic)
        val linearLayout: LinearLayout = itemView.findViewById(R.id.linearLayout)
    }

    override fun onBindViewHolder(holder: StaticRvAdapter.StaticRVViewHolder, position: Int) {
        var currentItem:StaticRvModel = mData!!.get(position)
        holder.imageView.setImageResource(currentItem.image!!)
        holder.textView.setText(currentItem.text)

        holder.linearLayout.setOnClickListener {
            row_index = position
            notifyDataSetChanged()
            cellClickListener?.onCellClickListner(position)
        }
        if(row_index == position){
            holder.linearLayout.setBackgroundResource(R.drawable.satic_rv_selected_bg)
        }
        else{
            holder.linearLayout.setBackgroundResource(R.drawable.static_rv_bg)
        }
    }
}