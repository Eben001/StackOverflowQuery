package com.ebenezer.gana.stackoverflowquery.ui.answersList

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ebenezer.gana.stackoverflowquery.data.model.response.Answer
import com.ebenezer.gana.stackoverflowquery.databinding.ListItemAnswerBinding

class AnswersListAdapter(private val answersList: ArrayList<Answer>) :
    RecyclerView.Adapter<AnswersListAdapter.AnswersViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AnswersViewHolder {
        return AnswersViewHolder(
            ListItemAnswerBinding.inflate(
                LayoutInflater.from(parent.context),
                parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: AnswersViewHolder, position: Int) {
        val current = answersList[position]
        holder.bind(current)
    }

    override fun getItemCount() = answersList.size
    fun addAnswers(newAnswers: List<Answer>) {
        val currentLength = answersList.size
        answersList.addAll(newAnswers)
        notifyItemInserted(currentLength)

    }

    class AnswersViewHolder(val binding: ListItemAnswerBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(answer: Answer) {
            binding.itemAnswer.text = answer.toString()
        }
    }
}