package com.example.myapplication

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.method.ScrollingMovementMethod
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.myapplication.database.Recipe
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_edit.*
import kotlinx.coroutines.InternalCoroutinesApi
import java.io.ByteArrayOutputStream


@InternalCoroutinesApi
class Edit : AppCompatActivity(){

    private val NUMBER = arrayOf("One", "Two", "Three", "Four", "Five",
        "Six", "Seven", "Eight", "Nine", "Ten")

    private val recipetypes = arrayOf("breakfast", "fastfood", "salad", "dessert", "bread")
    var adapter: ArrayAdapter<String>? = null
    private var ingredArray =  ArrayList<String>()
    private var directArray =  ArrayList<String>()
    private var selectedRecipe: String? = null
    private val REQUEST_IMAGE_CAPTURE = 100
    private var imageUri: Uri? = null
    private var rid: String? = null
    private var listRecipe: List<Recipe>? = null
    private var fStore = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit)

        rid = intent.getStringExtra("recipe_id").orEmpty()

        desc.setScroller(Scroller(this))
        desc.setMaxLines(1)
        desc.setVerticalScrollBarEnabled(true)
        desc.setMovementMethod(ScrollingMovementMethod())

        name.setScroller(Scroller(this))
        name.setMaxLines(1)
        name.setVerticalScrollBarEnabled(true)
        name.setMovementMethod(ScrollingMovementMethod())

        setData(rid)

        val spinner: Spinner = findViewById(R.id.spinner)
        ArrayAdapter.createFromResource(
            this,
            R.array.recipe_types,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = adapter
        }


        adapter = ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item)
        textin.setAdapter(adapter)
        textin1.setAdapter(adapter)

        live.setOnClickListener {
            takePictureIntent()
        }

        submit.setOnClickListener {
            val ref = fStore.collection("Recipe")
            val query = ref.whereEqualTo("id", rid)
            query.get().addOnCompleteListener { task ->
                if(task.isSuccessful) {
                    for(doc in task.result!!){
                        ref.document(doc.id).set(
                            Recipe(
                                rid,
                                name.text.toString(),
                                desc.text.toString(),
                                ingredArray,
                                (listRecipe as MutableList<Recipe>).elementAt(0).fav,
                                (listRecipe as MutableList<Recipe>).elementAt(0).cat,
                                directArray,
                                true
                            )
                        )
                    }
                    Toast.makeText(this, "Record edit successfully !", Toast.LENGTH_LONG).show()
                    var intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                }
            }.addOnFailureListener {
                Toast.makeText(this, it.toString(), Toast.LENGTH_LONG).show()
            }
        }

        add.setOnClickListener {
            val layoutInflater = baseContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            val addView: View = layoutInflater.inflate(R.layout.row, null)
            val textout: AutoCompleteTextView = addView.findViewById(R.id.textout)
            textout.setAdapter(adapter)
            textout.setText(textin.text.toString())
            val remove: Button = addView.findViewById(R.id.remove)

            val thisListener = View.OnClickListener {
                var index = (addView.parent as? LinearLayout)?.indexOfChild(addView)
                removeIngredList(index!!)
                (addView.parent as LinearLayout).removeView(addView)
            }

            remove.setOnClickListener(thisListener)
            container.addView(addView)
            ingredArray.clear()

            //List Child Value
            var childCount: Int? = container?.childCount
            for(child in 0 until childCount!!) {
                var thisChild: View = container.getChildAt(child)
                var childTextView: AutoCompleteTextView = thisChild.findViewById(R.id.textout)
                var childTextValue: String = childTextView.text.toString()
                addIngredList(childTextValue)
            }
        }

        add1.setOnClickListener {
            val layoutInflater = baseContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            val addView: View = layoutInflater.inflate(R.layout.row, null)
            val textout: AutoCompleteTextView = addView.findViewById(R.id.textout)
            textout.setAdapter(adapter)
            textout.setText(textin1.text.toString())
            val remove: Button = addView.findViewById(R.id.remove)

            val thisListener = View.OnClickListener {
                var index1 = (addView.parent as? LinearLayout)?.indexOfChild(addView)
                removeDirectList(index1!!)

                (addView.parent as LinearLayout).removeView(addView)
            }

            remove.setOnClickListener(thisListener)
            container1.addView(addView)
            directArray.clear()

            //List Child Value
            var childCount: Int? = container1?.childCount
            for(child in 0 until childCount!!) {
                var thisChild: View = container1.getChildAt(child)
                var childTextView: AutoCompleteTextView = thisChild.findViewById(R.id.textout)
                var childTextValue: String = childTextView.text.toString()
                addDirectList(childTextValue)
            }
        }
    }
    fun addIngredList(value: String){
        ingredArray.add(value)
        if(ingredArray.size != 0) {
//            Toast.makeText(this, ingredArray?.size.toString(), Toast.LENGTH_LONG).show()
        }
        else{
            Toast.makeText(this, "Array is null", Toast.LENGTH_LONG).show()
        }
    }

    fun addDirectList(value: String){
        directArray.add(value)
        if(directArray.size != 0) {
            selectedRecipe = recipetypes[spinner.selectedItemPosition]
//            Toast.makeText(this, directArray?.size.toString() + " " + selectedRecipe, Toast.LENGTH_LONG).show()
        }
        else{
            Toast.makeText(this, "Array is null", Toast.LENGTH_LONG).show()
        }
    }

    fun removeIngredList(index: Int){
            ingredArray.removeAt(index)
    }

    fun removeDirectList(index: Int){
        directArray.removeAt(index)
    }

    fun setData(rid: String?){
        FirebaseFirestore.getInstance().collection("Recipe").whereEqualTo("id", rid)
            .get().addOnSuccessListener { documentSanpshot ->
                listRecipe = documentSanpshot.toObjects(Recipe::class.java)

                when((listRecipe as MutableList<Recipe>).elementAt(0).cat) {
                    "breakfast" -> spinner.setSelection(0)
                    "fastfood" -> spinner.setSelection(1)
                    "salad" -> spinner.setSelection(2)
                    "dessert" -> spinner.setSelection(3)
                    "bread" -> spinner.setSelection(4)
                }


                //Set Image Resource
                FirebaseStorage.getInstance().reference.child("pics/${rid}.jpg")
                    .downloadUrl.addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Glide.with(this).load(task.result).into(live)
                        }
                    }

                //Set Field Data
                name.setText((listRecipe as MutableList<Recipe>).elementAt(0).name)
                desc.setText((listRecipe as MutableList<Recipe>).elementAt(0).desc)

                //Set ingredients data
                for(x in (listRecipe as MutableList<Recipe>).elementAt(0).ingred!!) {
                    val layoutInflater = baseContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
                    val addView: View = layoutInflater.inflate(R.layout.row, null)
                    val textout: AutoCompleteTextView = addView.findViewById(R.id.textout)
                    textout.setAdapter(adapter)
                    textout.setText(x)
                    val remove: Button = addView.findViewById(R.id.remove)

                    val thisListener = View.OnClickListener {
                        var index = (addView.parent as? LinearLayout)?.indexOfChild(addView)
                        removeIngredList(index!!)
                        (addView.parent as LinearLayout).removeView(addView)
                    }

                    remove.setOnClickListener(thisListener)
                    container.addView(addView)
                    ingredArray.clear()

                    //List Child Value
                    var childCount: Int? = container?.childCount
                    for(child in 0 until childCount!!) {
                        var thisChild: View = container.getChildAt(child)
                        var childTextView: AutoCompleteTextView = thisChild.findViewById(R.id.textout)
                        var childTextValue: String = childTextView.text.toString()
                        addIngredList(childTextValue)
                    }
                }

                for(y in (listRecipe as MutableList<Recipe>).elementAt(0).direct!!) {
                    val layoutInflater = baseContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
                    val addView: View = layoutInflater.inflate(R.layout.row, null)
                    val textout: AutoCompleteTextView = addView.findViewById(R.id.textout)
                    textout.setAdapter(adapter)
                    textout.setText(y)
                    val remove: Button = addView.findViewById(R.id.remove)

                    val thisListener = View.OnClickListener {
                        var index1 = (addView.parent as? LinearLayout)?.indexOfChild(addView)
                        removeDirectList(index1!!)

                        (addView.parent as LinearLayout).removeView(addView)
                    }

                    remove.setOnClickListener(thisListener)
                    container1.addView(addView)
                    directArray.clear()

                    //List Child Value
                    var childCount: Int? = container1?.childCount
                    for(child in 0 until childCount!!) {
                        var thisChild: View = container1.getChildAt(child)
                        var childTextView: AutoCompleteTextView = thisChild.findViewById(R.id.textout)
                        var childTextValue: String = childTextView.text.toString()
                        addDirectList(childTextValue)
                    }
                }
            }
    }
    private fun takePictureIntent() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == AppCompatActivity.RESULT_OK) {
            Log.w("Image", "uploadImage: Success")
            val bitmap = data?.extras?.get("data") as Bitmap
            uploadImageAndSaveUri(bitmap)

        }
    }

    private fun uploadImageAndSaveUri(bitmap: Bitmap) {
        val baos = ByteArrayOutputStream()

        val documentReference = fStore.collection("Recipe")
        documentReference.get().addOnSuccessListener { querySnapshot ->
            var count = 0
            for (document in querySnapshot.documents) {
                val recipe = document.toObject(Recipe::class.java)
                if (recipe != null) {
                    count += 1
                    Log.i("count1", count.toString())
                }
            }

            val storageRef = FirebaseStorage.getInstance()
                .reference.child("pics/${rid}.jpg")

            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
            val image = baos.toByteArray()
            val upload = storageRef.putBytes(image)

            upload.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    storageRef.downloadUrl.addOnCompleteListener { downloadTask ->
                        downloadTask.result?.let {
                            imageUri = it
                            live.setImageBitmap(bitmap)
                            Toast.makeText(
                                this, "The image is successfully uploaded",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }

                } else {
                    Log.w("Image", "uploadImage: Failure", task.exception)
                }
            }
        }
    }


}
