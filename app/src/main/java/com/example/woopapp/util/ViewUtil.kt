package com.example.woopapp.util

import android.content.Context
import android.graphics.Paint
import android.net.Uri
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.woopapp.R
import com.example.woopapp.constants.Constants

object ViewUtil {

    fun getProgressDrawable(
        context: Context,
        strokeWidth: Float = 10f,
        radius: Float = 50f
    ): CircularProgressDrawable {
        return CircularProgressDrawable(context).apply {
            this.strokeWidth = strokeWidth
            centerRadius = radius
            start()
        }
    }

    fun ImageView.loadImageFromAsserts(
        imageName: String,
        progressDrawable: CircularProgressDrawable? = null
    ) {
        val options = RequestOptions()
            .placeholder(progressDrawable)
            .error(R.mipmap.ic_launcher)

        Glide.with(this.context)
            .setDefaultRequestOptions(options)
            .load(Uri.parse("${Constants.baseAssertUri}$imageName"))
            .into(this)
    }

    fun TextView.setAnswerText(isCorrectAnswer: Boolean, answer: String) {
        if (isCorrectAnswer) {
            visibility = View.GONE
        } else {
            visibility = View.VISIBLE
            paintFlags = paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            text = answer
            alpha = 0.7f
        }
    }

    fun TextView.setAnswerResultBackground(isCorrect: Boolean) {
        if (isCorrect) {
            background = ContextCompat.getDrawable(context, R.drawable.bg_right_answer)
            text = context.getText(R.string.correct)
        } else {
            background = ContextCompat.getDrawable(context, R.drawable.bg_wrong_answer)
            text = context.getText(R.string.wrong)
        }
    }

    fun TextView.setTextAlphaIf(correct: Boolean, serial: String) {
        if (correct) {
            alpha = 0.7f
        }
        text = serial
    }

    fun TextView.expand(ratio: Float, defaultScaleY: Float, defaultScaleX: Float) {
        val newYRatio = defaultScaleY - ratio
        if (newYRatio <= defaultScaleY + 0.3) {
            scaleY = defaultScaleY - ratio
        }
        val newXRatio = defaultScaleX - ratio
        if (newXRatio <= defaultScaleX + 0.3) {
            scaleX = defaultScaleX - ratio
        }
    }
}