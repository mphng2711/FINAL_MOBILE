package com.example.purepawapp.util

import android.content.Intent
import android.net.Uri
import androidx.fragment.app.Fragment

fun Fragment.dialPhone(phoneNumber: String) {
    startActivity(Intent(Intent.ACTION_DIAL, Uri.parse("tel:$phoneNumber")))
}

fun Fragment.sendEmail(email: String, subject: String = "") {
    val intent = Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:$email")).apply {
        putExtra(Intent.EXTRA_SUBJECT, subject)
    }
    startActivity(intent)
}

fun Fragment.openMapDirections(address: String) {
    val uri = Uri.parse("geo:0,0?q=${Uri.encode(address)}")
    startActivity(Intent(Intent.ACTION_VIEW, uri))
}
