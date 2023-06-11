package com.example.mymovis.data

class Review(
    private val author: String,
    private val content: String
) {


    fun getAuthor(): String {
        return author
    }

    fun getContent(): String {
        return content
    }



}