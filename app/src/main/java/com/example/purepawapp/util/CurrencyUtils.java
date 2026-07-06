package com.example.purepawapp.util;

import java.text.DecimalFormat;

public final class CurrencyUtils {
    private static final DecimalFormat VND_FORMAT = new DecimalFormat("#,###");

    private CurrencyUtils() {
    }

    public static String toVndString(double amount) {
        return VND_FORMAT.format(amount).replace(',', '.') + "đ";
    }
}
