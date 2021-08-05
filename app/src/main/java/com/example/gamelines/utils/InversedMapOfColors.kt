package com.example.gamelines.utils

import android.content.Context
import android.graphics.drawable.Drawable
import androidx.appcompat.content.res.AppCompatResources.getDrawable
import com.example.gamelines.R

class InversedMapOfColors (context: Context) {
    val inversedMapOfColors = mapOf(
        getDrawable(context, R.drawable.redbubble)!!.constantState to 1,     getDrawable(context, R.drawable.redbubble_y)!!.constantState to 11,
        getDrawable(context, R.drawable.greenbubble)!!.constantState to 2,   getDrawable(context, R.drawable.greenbubble_y)!!.constantState to 12,
        getDrawable(context, R.drawable.bluebubble)!!.constantState to 3,    getDrawable(context, R.drawable.bluebubble_y)!!.constantState to 13,
        getDrawable(context, R.drawable.orangebubble)!!.constantState to 4,  getDrawable(context, R.drawable.orangebubble_y)!!.constantState to 14,
        getDrawable(context, R.drawable.purplebubble)!!.constantState to 5,  getDrawable(context, R.drawable.purplebubble_y)!!.constantState to 15,
        getDrawable(context, R.drawable.brownbubble)!!.constantState to 6,   getDrawable(context, R.drawable.brownbubble_y)!!.constantState to 16,
        getDrawable(context, R.drawable.skybubble)!!.constantState to 7,     getDrawable(context, R.drawable.skybubble_y)!!.constantState to 17
    )
    fun getCurrentInt(currentColor: Drawable.ConstantState): Int = inversedMapOfColors.get(currentColor) ?: 0
}