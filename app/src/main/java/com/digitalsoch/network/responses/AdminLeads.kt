package com.digitalsoch.network.responses

import java.io.Serializable

data class AdminLeads(
    val name: String,
    val date: String,
    val city:String,
    val address:String,
    val number: String,
    val type:String,
    val from:String,
    val email:String,
    val requirement:String,
    val pincode:String,
):Serializable
