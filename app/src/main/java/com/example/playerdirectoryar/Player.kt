package com.example.playerdirectoryar

import android.graphics.Bitmap

class Player(
    val playerName: String, val playerPron: String, val playerEng: String, val squadNumber: Int, val position: String,
    val height: Float, val weight: Float, val dominantFoot: String?, val birthYear: Int) {
    var bitmapPlayer: Bitmap? = null
    var clubName: String? = null
}