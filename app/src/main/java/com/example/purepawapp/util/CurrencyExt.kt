package com.example.purepawapp.util

import java.text.DecimalFormat

private val vndFormat = DecimalFormat("#,###")

fun Double.toVndString(): String = "${vndFormat.format(this).replace(',', '.')}đ"
