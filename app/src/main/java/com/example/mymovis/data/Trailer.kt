package com.example.mymovis.data

class Trailer(
    private val key:String,
    private val name: String
) {

    fun getKey(): String {
        return key
    }

    fun getName(): String {
        return name
    }

}