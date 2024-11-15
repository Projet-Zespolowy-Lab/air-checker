package com.example.air_checker.model

enum class IndexColors(val rgb: Long, val value: String) {
    Bardzo_Dobry(0xFF57b108, "100%"),
    Dobry(0xFFb0dd10, "75%"),
    Umiarkowany(0xFFffd911, "60%"),
    Dostateczny(0xFFe58100, "50%"),
    Zły(0xFFe50000, "30%"),
    Bardzo_Zły(0xFF990000, "15%"),
    Brak(0xFFbfbfbf, "-")
}