package com.example.myapplication.recipe

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.myapplication.Edit
import com.example.myapplication.database.Recipe
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.example.myapplication.R
import com.example.myapplication.RecipeDetails
import com.example.myapplication.database.FavList
import kotlinx.coroutines.InternalCoroutinesApi

@InternalCoroutinesApi
class RecipeListRecycleAdapter(private val mContext: Context, private val mData: MutableList<Recipe>) :
    RecyclerView.Adapter<RecipeListRecycleAdapter.MyViewHolder>() {
    private lateinit var fStore: FirebaseFirestore
    private var favouriteList: List<FavList>? = null
    private var lData: List<Recipe> = mData
    private var count: Int? = null

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        fStore = FirebaseFirestore.getInstance()

        holder.relativeLayout1.animation = AnimationUtils.loadAnimation(mContext, R.anim.fade_scale_animation)
        holder.img.animation = AnimationUtils.loadAnimation(mContext, R.anim.fade_transition_animation)
        holder.tvRecname.text = mData.elementAt(position).name
        holder.tvRecdesc.text = mData.elementAt(position).desc
        FirebaseStorage.getInstance().reference.child("pics/${mData.elementAt(position).id}" + ".jpg")
            .downloadUrl.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Glide.with(this.mContext).load(task.result).into(holder.img)
                }
            }

        val documentReference =
            fStore.collection("Recipe")
                .whereEqualTo("id", mData.elementAt(position).id)
                .whereEqualTo("fav", true)

        documentReference.get().addOnSuccessListener { documentSnapshot1 ->
            favouriteList = documentSnapshot1.toObjects(FavList::class.java)
            for (s in favouriteList!!) {
                if (s.fav == true) {
                    holder.sub.setImageResource(R.drawable.ic_favorite_red_24dp)
                    holder.sub.setTag(R.drawable.ic_favorite_red_24dp)
                }
                else {
                    holder.sub.setImageResource(R.drawable.ic_favorite_black_24dp)
                    holder.sub.setTag(R.drawable.ic_favorite_black_24dp)
                }
            }
        }

        holder.relativeLayout1.setOnClickListener {
            val intent = Intent(this.mContext, RecipeDetails::class.java)
            intent.putExtra("recipe_id", mData.elementAt(position).id)
            mContext.startActivity(intent)
        }

        holder.delete.setOnClickListener {
            val ref = fStore.collection("Recipe")
            val query = ref.whereEqualTo("id", mData.elementAt(position).id)
                query.get().addOnCompleteListener { task ->
                    if(task.isSuccessful) {
                        for(doc in task.result!!){
                            ref.document(doc.id).update("status", false)
                            mData.removeAt(position)
                            notifyItemRemoved(position)
                            notifyItemRangeChanged(position,itemCount)
//                            Toast.makeText(mContext, "delete", Toast.LENGTH_LONG).show()
                        }
                    }
                }.addOnFailureListener {
                    Toast.makeText(mContext, it.toString(), Toast.LENGTH_LONG).show()
                }
        }

        holder.edit.setOnClickListener {
            val intent = Intent(this.mContext, Edit::class.java)
            intent.putExtra("recipe_id", mData.elementAt(position).id)
            mContext.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return mData.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(mContext).inflate(R.layout.element_recipe, parent, false)
        return MyViewHolder(view)
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvRecname: TextView = itemView.findViewById(R.id.name)
        val tvRecdesc: TextView = itemView.findViewById(R.id.desc)
        val relativeLayout1: RelativeLayout = itemView.findViewById(R.id.relativeLayout)
        var img: ImageView = itemView.findViewById(R.id.profile_img)
        var sub: ImageView = itemView.findViewById(R.id.subButton)
        var delete: ImageView = itemView.findViewById(R.id.dltButton)
        var edit: ImageView = itemView.findViewById(R.id.editButton)
    }
}