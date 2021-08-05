package com.example.gamelines.utils

import android.content.Context
import android.graphics.drawable.Drawable
import com.example.gamelines.R

class MapOfColors {
    val colors = mapOf(
        1 to R.drawable.redbubble, 11 to R.drawable.redbubble_y,
        2 to R.drawable.greenbubble, 12 to R.drawable.greenbubble_y,
        3 to R.drawable.bluebubble, 13 to R.drawable.bluebubble_y,
        4 to R.drawable.orangebubble, 14 to R.drawable.orangebubble_y,
        5 to R.drawable.purplebubble, 15 to R.drawable.purplebubble_y,
        6 to R.drawable.brownbubble, 16 to R.drawable.brownbubble_y,
        7 to R.drawable.skybubble, 17 to R.drawable.skybubble_y,
        0 to R.drawable.empty,
        50 to R.drawable.boom,
        100 to R.drawable.wrong
    )
    fun getColor(context: Context, ball: Int): Drawable = context.resources.getDrawable(colors.get(ball)?:R.drawable.empty)
    fun getInversedColorInt(context: Context, ball: Int): Int {
        val reversedBall: Int
        if (ball in 1..7) reversedBall = ball+10
        else if (ball in 11..17) reversedBall = ball-10
        else reversedBall = 0
        return reversedBall
    }
}