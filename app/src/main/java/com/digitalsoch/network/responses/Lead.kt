package com.digitalsoch.network.responses

import java.io.Serializable

data class Lead(
    val name: String,
    val number: String,
    val timeSlot: String,
    val assignedTo: String,
    val status: String,
    val requirementType: String,
    val address: String,
    val yourNoteDetail: String,
    val employeeNoteDetail: String,
    val city: String,
    val email: String,
    val pincode: String,
    val employeeName: String
):Serializable
