package com.example.woopapp.fragments.results

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.woopapp.MainViewModel
import com.example.woopapp.R
import com.example.woopapp.adapters.ResultAdapter
import com.example.woopapp.fragments.BaseFragment
import com.example.woopapp.models.Answer
import kotlinx.android.synthetic.main.fragment_results.*

class ResultsFragment : BaseFragment(R.layout.fragment_results) {

    private val viewModel: MainViewModel by activityViewModels()

    @SuppressLint("StringFormatMatches")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getResults().observe(viewLifecycleOwner, Observer { (answers, screenTitle) ->
            val amountOfCorrectAnswers = answers.filter { it.isCorrect }.size
            resultTextView.text = getString(R.string.result, amountOfCorrectAnswers, answers.size)
            setupRecyclerView(answers.toMutableList())
            title.text = screenTitle
        })

        viewModel.navigateToSelectionFragmentLiveData.observe(viewLifecycleOwner, Observer {
            findNavController().navigate(ResultsFragmentDirections.toSelection())
        })

        tryAgainButton.setOnClickListener {
            viewModel.resetDataAndNavigateToSelectionFragment()
        }
    }

    private fun setupRecyclerView(answers: MutableList<Answer>) {
        recyclerView.apply {
            hasFixedSize()
            adapter = ResultAdapter(answers)
        }
    }
}