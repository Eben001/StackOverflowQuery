package com.ebenezer.gana.stackoverflowquery.ui.answersList

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ebenezer.gana.stackoverflowquery.data.model.response.Question
import com.ebenezer.gana.stackoverflowquery.data.model.response.convertDate
import com.ebenezer.gana.stackoverflowquery.data.model.response.convertTitle
import com.ebenezer.gana.stackoverflowquery.databinding.FragmentAnswersListBinding
import kotlinx.coroutines.launch

class AnswersListFragment : Fragment() {

    private var _binding: FragmentAnswersListBinding? = null
    private val binding get() = _binding!!

    private val viewModel: AnswersListViewModel by viewModels()
    private val navigationArgs: AnswersListFragmentArgs by navArgs()

    private lateinit var question: Question
    private lateinit var answersListAdapter: AnswersListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAnswersListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        question = navigationArgs.question


        updateUI(question)

        val linearLayoutManager = LinearLayoutManager(this.context)
        answersListAdapter = AnswersListAdapter(arrayListOf())

        binding.answersList.apply {
            layoutManager = linearLayoutManager
            adapter = answersListAdapter
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    if (dy > 0) {
                        val childCount = answersListAdapter.itemCount
                        val lastPosition =
                            linearLayoutManager.findLastCompletelyVisibleItemPosition()
                        if (childCount - 1 == lastPosition && binding.loadingView.visibility == View.GONE) {
                            binding.loadingView.visibility = View.VISIBLE

                            lifecycleScope.launch {
                                viewModel.getNextPage(question.questionId)

                            }
                        }
                    }
                }
            })
        }

        observeViewModel()

        lifecycleScope.launch {
            viewModel.getNextPage(question.questionId)

        }

    }



    private fun observeViewModel() {
        viewModel.answersList.observe(viewLifecycleOwner) { answersList ->
            answersList?.let {
                binding.answersList.visibility = View.VISIBLE
                answersListAdapter.addAnswers(it)
            }
        }

        viewModel.error.observe(viewLifecycleOwner) { errorMessage ->
            binding.listError.visibility = if (errorMessage == null) View.GONE else View.VISIBLE
            binding.listError.text = errorMessage
        }

        viewModel.loading.observe(viewLifecycleOwner) { isLoading ->
            isLoading?.let {
                binding.loadingView.visibility = if (it) View.VISIBLE else View.GONE
                if (it) {
                    binding.listError.visibility = View.GONE
                    binding.answersList.visibility = View.GONE

                }
            }
        }
    }

    private fun updateUI(question: Question) {
        binding.questionScore.text = question.score
        binding.questionDate.text = convertDate(question.date)
        binding.questionTitle.text = convertTitle(question.title)
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}