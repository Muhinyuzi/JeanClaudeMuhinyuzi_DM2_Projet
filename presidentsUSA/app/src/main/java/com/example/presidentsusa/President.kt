package com.example.presidentsusa

data class President(
    val id: Int,
    val name: String,
    val ordinal: Int,
    val photo: String,
    val vicePresidents: List<String>,
    val yearsInOffice: String
)