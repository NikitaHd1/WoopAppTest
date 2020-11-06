package com.example.woopapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.woopapp.R
import com.example.woopapp.models.Answer
import com.example.woopapp.util.ViewUtil.loadImageFromAsserts
import com.example.woopapp.util.ViewUtil.setAnswerResultBackground
import com.example.woopapp.util.ViewUtil.setAnswerText
import com.example.woopapp.util.ViewUtil.setTextAlphaIf
import kotlinx.android.synthetic.main.result_card_item.view.*

class ResultAdapter(private var answers: MutableList<Answer>) :
    RecyclerView.Adapter<ResultAdapter.ResultViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ResultViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.result_card_item, parent, false)
        return ResultViewHolder(view)
    }

    override fun getItemCount() = answers.size

    override fun onBindViewHolder(holder: ResultViewHolder, position: Int) {
        holder.bind(answers[position])
    }

    inner class ResultViewHolder(val view: View) :
        RecyclerView.ViewHolder(view) {

        fun bind(answer: Answer) {
            view.characterName.text = answer.characterName
            view.serialName.setTextAlphaIf(answer.isCorrect, answer.serial)
            view.characterImage.loadImageFromAsserts(answer.imageName)
            view.answerResult.setAnswerResultBackground(answer.isCorrect)
            view.wrongAnswer.setAnswerText(answer.isCorrect, answer.answerString)
        }
    }
}