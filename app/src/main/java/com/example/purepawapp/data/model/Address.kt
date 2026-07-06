package com.example.purepawapp.data.model

data class Address(
    val fullName: String = "",
    val phone: String = "",
    val street: String = "",
    val ward: String = "",
    val district: String = "",
    val city: String = "",
    val isDefault: Boolean = false
)
