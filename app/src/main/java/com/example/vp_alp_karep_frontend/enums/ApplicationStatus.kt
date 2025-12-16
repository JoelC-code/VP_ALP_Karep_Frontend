package com.example.vp_alp_karep_frontend.enums

import com.google.gson.annotations.SerializedName

enum class ApplicationStatus() {
    @SerializedName("rejected")
    Rejected,

    @SerializedName("accepted")
    Accepted,

    @SerializedName("pending")
    Pending,

    @SerializedName("cancelled")
    Cancelled,
}

fun ApplicationStatus.displayText(): String =
    when(this) {
        ApplicationStatus.Pending -> "Pending"
        ApplicationStatus.Cancelled -> "Cancelled"
        ApplicationStatus.Rejected -> "Rejected"
        ApplicationStatus.Accepted -> "Accepted"
    }