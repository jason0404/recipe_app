package com.example.myapplication.database

data class FavList (
    var id: String? = null,
    var name: String? = null,
    var desc: String? = null,
    var ingred: ArrayList<String>? = null,
    var fav: Boolean? = null,
    var cat: String? = null
)