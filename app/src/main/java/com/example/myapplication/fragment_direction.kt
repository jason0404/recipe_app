package com.example.myapplication

import android.app.Activity
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.myapplication.database.Recipe
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fragment_direction.*
import kotlinx.android.synthetic.main.fragment_direction.meals_title
import kotlinx.android.synthetic.main.fragment_direction.text_ingred
import kotlinx.android.synthetic.main.fragment_ingredient_fragment.*

/**
 * A simple [Fragment] subclass.
 */
class fragment_direction : Fragment() {
    private var rid: String? = null
    private var activity: Activity? = null
    private var listRecipe: List<Recipe>? = null
    private var sortedList: List<Recipe>? = null
    private var directList: List<String>? = null
    private var foodName: String? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        rid = getActivity()?.intent?.getStringExtra("recipe_id")
//        Toast.makeText(this.context, rid, Toast.LENGTH_LONG).show()
        text_ingred?.text = rid

        FirebaseFirestore.getInstance().collection("Recipe").get().addOnSuccessListener { documentSnapshot ->
            listRecipe = documentSnapshot.toObjects(Recipe::class.java)

            sortedList = listRecipe?.filter {
                it?.id == rid
            }

            directList = sortedList?.get(0)?.direct?.toList()
            foodName = sortedList?.get(0)?.name

            meals_title.text = foodName


            for((index, recipe) in directList!!.withIndex()){
                text_ingred.append("${index + 1})  $recipe \n\n")
            }
        }
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_direction, container, false)
    }

}
