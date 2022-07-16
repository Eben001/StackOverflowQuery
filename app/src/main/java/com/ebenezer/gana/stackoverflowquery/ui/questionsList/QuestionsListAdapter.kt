package com.ebenezer.gana.stackoverflowquery.ui.questionsList

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ebenezer.gana.stackoverflowquery.data.model.response.Question
import com.ebenezer.gana.stackoverflowquery.data.model.response.convertDate
import com.ebenezer.gana.stackoverflowquery.databinding.ListItemQuestionBinding

class QuestionsListAdapter(
    val questions: ArrayList<Question>,
    private val onItemClicked: (Question) -> Unit
) :
    RecyclerView.Adapter<QuestionsListAdapter.QuestionViewHolder>() {

    fun addQuestions(newQuestions: List<Question>) {
       val currentLength = questions.size
        questions.addAll(newQuestions)
        notifyItemInserted(currentLength)
    }

    class QuestionViewHolder(private var binding: ListItemQuestionBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(question: Question) {
            binding.apply {
                binding.itemTitle.text = question.title
                binding.itemScore.text = question.score
                binding.itemDate.text = convertDate(question.date)

            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuestionViewHolder {
        return QuestionViewHolder(
            ListItemQuestionBinding.inflate(
                LayoutInflater.from(parent.context),
                parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: QuestionViewHolder, position: Int) {
        val currentItem = questions[position]
        holder.bind(currentItem)
        holder.itemView.setOnClickListener {
            onItemClicked(currentItem)
        }
    }
    override fun getItemCount(): Int {
        return questions.size
    }
}