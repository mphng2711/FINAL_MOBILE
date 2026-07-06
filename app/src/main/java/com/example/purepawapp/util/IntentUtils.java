package com.example.purepawapp.util;

import android.content.Intent;
import android.net.Uri;

import androidx.fragment.app.Fragment;

public final class IntentUtils {

    private IntentUtils() {
    }

    public static void dialPhone(Fragment fragment, String phoneNumber) {
        fragment.startActivity(new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phoneNumber)));
    }

    public static void sendEmail(Fragment fragment, String email, String subject) {
        Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:" + email));
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        fragment.startActivity(intent);
    }

    public static void openMapDirections(Fragment fragment, String address) {
        Uri uri = Uri.parse("geo:0,0?q=" + Uri.encode(address));
        fragment.startActivity(new Intent(Intent.ACTION_VIEW, uri));
    }
}
