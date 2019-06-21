package com.goyourfly.view

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.View

class TestView(context: Context,attributeSet: AttributeSet):View(context,attributeSet){
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val widthMode = View.MeasureSpec.getMode(widthMeasureSpec)
        val heightMode = View.MeasureSpec.getMode(heightMeasureSpec)
        val widthSize = View.MeasureSpec.getSize(widthMeasureSpec)
        val heightSize = View.MeasureSpec.getSize(heightMeasureSpec)
        Log.d("TestView","Width:$widthSize,Height:$heightSize")
    }
}