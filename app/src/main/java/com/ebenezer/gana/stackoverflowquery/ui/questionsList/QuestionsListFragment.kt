package com.ebenezer.gana.stackoverflowquery.ui.questionsList

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ebenezer.gana.stackoverflowquery.databinding.FragmentQuestionsListBinding

private const val TAG = "QuestionsListFragment"

class QuestionsListFragment : Fragment() {

    private var _binding: FragmentQuestionsListBinding? = null
    private val binding get() = _binding!!

    private val viewModel: QuestionsListViewModel by viewModels()

    private lateinit var questionsListAdapter: QuestionsListAdapter

    private var isDataLoaded = false


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d(TAG, "onCreateView: Called")

        // Inflate the layout for this fragment
        _binding = FragmentQuestionsListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart: Called")
        if (!isDataLoaded) {
            viewModel.getNextPage()

        }

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val linearLayoutManager = LinearLayoutManager(this.context)
        questionsListAdapter = QuestionsListAdapter(arrayListOf()) {
            val action =
                QuestionsListFragmentDirections.actionQuestionsListFragmentToAnswersListFragment(
                    it
                )

            this.findNavController().navigate(action)
        }


        /**
         * Add an onScroll listener to the recyclerView to check when it's the last item in the list.
         * Fetch new questions if it's the last item in the list
         */
        binding.questionsList.apply {
            layoutManager = linearLayoutManager
            adapter = questionsListAdapter
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    if (dy > 0) {
                        val childCount = questionsListAdapter.itemCount
                        val lastPosition =
                            linearLayoutManager.findLastCompletelyVisibleItemPosition()
                        if (childCount - 1 == lastPosition && binding.loadingView.visibility == View.GONE) {
                            binding.loadingView.visibility = View.VISIBLE
                            viewModel.getNextPage()
                        }
                    }
                }
            })
        }

        observeViewModel()
        setListeners()

    }

    //Sets listener for swipeRefreshLayout
    private fun setListeners() {
        binding.swipeLayout.setOnRefreshListener {
            viewModel.getFirstPage()
            binding.questionsList.visibility = View.GONE
        }

    }

    //Function to observe LiveData from the viewModel
    private fun observeViewModel() {

        viewModel.questionsList.observe(viewLifecycleOwner) { questions ->
            questions?.let {
                binding.questionsList.visibility = View.VISIBLE
                binding.swipeLayout.isRefreshing = false
                questionsListAdapter.addQuestions(it)
                isDataLoaded = true

            }
        }

        viewModel.error.observe(viewLifecycleOwner) { errorMsg ->
            binding.listError.visibility = if (errorMsg == null) View.GONE else View.VISIBLE
            binding.listError.text = "Error\n$errorMsg"
        }

        viewModel.loading.observe(viewLifecycleOwner) { isLoading ->
            isLoading?.let {
                binding.loadingView.visibility = if (it) View.VISIBLE else View.GONE
                if (it) {
                    binding.listError.visibility = View.GONE
                    binding.questionsList.visibility = View.GONE
                }
            }
        }


    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null

    }

}