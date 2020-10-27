package com.example.myapplication

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.auth.AuthViewModel
import com.example.myapplication.auth.AuthViewModelFactory
import com.example.myapplication.common.StaticRvModel
import com.example.myapplication.database.AppDatabase
import com.example.myapplication.database.Recipe
import com.example.myapplication.recipe.RecipeListRecycleAdapter
import com.example.myapplication.recipe.StaticRvAdapter
import com.google.api.Distribution
import com.google.firebase.firestore.FirebaseFirestore
import com.squareup.okhttp.internal.Internal.instance
import kotlinx.android.synthetic.main.fragment_page1.*
import kotlinx.coroutines.InternalCoroutinesApi
import okhttp3.internal.Internal.instance
import org.kodein.di.generic.instance
import java.sql.Array
import java.util.*
import org.kodein.di.android.x.kodein
import org.kodein.di.KodeinAware
import org.kodein.di.generic.instance
import java.util.concurrent.Executors
import kotlin.collections.ArrayList

/**
 * A simple [Fragment] subclass.
 */
@Suppress("DEPRECATION")
@InternalCoroutinesApi
class page1 : Fragment(),  CellClickListener, KodeinAware{

    override val kodein by kodein()
    private var listRecipe: List<Recipe>? = null
    private var sortedList: List<Recipe>? = null
    private lateinit var viewModel: AuthViewModel
    private val factory: AuthViewModelFactory by instance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProviders.of(this, factory).get(AuthViewModel::class.java)
        return inflater.inflate(R.layout.fragment_page1, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        logout.setOnClickListener {
            Executors.newSingleThreadExecutor().execute {
                viewModel.deleteUSer()
            }
            val intent = Intent(this.context, SplashScreen::class.java)
            startActivity(intent)
        }
        add.setOnClickListener {
            val intent = Intent(this.context, Test::class.java)
            startActivity(intent)
        }

        val item: ArrayList<StaticRvModel>? = ArrayList()
        var type = resources.getStringArray(R.array.recipe_types)

        item!!.add(StaticRvModel(R.drawable.breakfast, type[0]))
        item!!.add(StaticRvModel(R.drawable.burger, type[1]))
        item!!.add(StaticRvModel(R.drawable.vegetarian, type[2]))
        item!!.add(StaticRvModel(R.drawable.icecream, type[3]))
        item!!.add(StaticRvModel(R.drawable.bread, type[4]))

        rv_1.apply {
            layoutManager = LinearLayoutManager(this.context, LinearLayoutManager.HORIZONTAL, false)
            adapter = StaticRvAdapter(item.orEmpty() as ArrayList<StaticRvModel>, this@page1)
        }


        setData()

        swipe_refresher.setOnRefreshListener {
            setData()
            editText.text = null
            editText.clearFocus()
            view.hideKeyboard()
        }
    }

    private fun View.hideKeyboard() {
        val inputManager = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputManager.hideSoftInputFromWindow(windowToken, 0)
    }

    @SuppressLint("DefaultLocale")
    private fun setData() {
        recycleView.visibility = View.GONE
        shimmer.visibility = View.VISIBLE
        shimmer.startShimmer()


        FirebaseFirestore.getInstance().collection("Recipe").whereEqualTo("status", true).get().addOnSuccessListener { documentSnapshot ->
            listRecipe = documentSnapshot.toObjects(Recipe::class.java)

            buttonSrc.setOnClickListener {
                if(editText.text != null) {
                    sortedList = listRecipe!!.filter {
                        it.name!!.toUpperCase().contains(editText.text.toString().toUpperCase()) || it.cat!!.toUpperCase().contains(editText.text.toString().toUpperCase())
                    }
                    recycleView?.apply {
                        layoutManager = LinearLayoutManager(activity)
                        adapter = RecipeListRecycleAdapter(this?.context, sortedList.orEmpty() as MutableList<Recipe>)
                    }
                }
                else{
                    recycleView?.apply {
                        layoutManager = LinearLayoutManager(activity)
                        adapter = RecipeListRecycleAdapter(this?.context, listRecipe.orEmpty() as MutableList<Recipe>)
                    }
                }
                view?.hideKeyboard()
            }

                recycleView?.apply {
                    layoutManager = LinearLayoutManager(activity)
                    adapter = RecipeListRecycleAdapter(this?.context, listRecipe.orEmpty() as MutableList<Recipe> )
                }

                shimmer?.stopShimmer()
                shimmer?.visibility = View.GONE
                recycleView?.visibility = View.VISIBLE
                swipe_refresher?.isRefreshing = false
            }
    }

    override fun onCellClickListner(position: Int) {
        recycleView.visibility = View.GONE
        shimmer.visibility = View.VISIBLE
        shimmer.startShimmer()
        var selected: String? = null
        when(position) {
            0 -> selected = "breakfast"
            1 -> selected = "fastfood"
            2 -> selected = "salad"
            3 -> selected = "dessert"
            4 -> selected = "bread"
            else -> {
                Toast.makeText(this.context,"Selection not found !",Toast.LENGTH_SHORT).show()
            }
        }
        sortedList = listRecipe?.filter {
            it?.cat == selected
        }
        recycleView?.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = RecipeListRecycleAdapter(this?.context, sortedList.orEmpty() as MutableList<Recipe>)
        }
        shimmer?.stopShimmer()
        shimmer?.visibility = View.GONE
        recycleView?.visibility = View.VISIBLE
        swipe_refresher?.isRefreshing = false

    }
}
