package com.example.mypokemon.ui.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.RadioButton
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.example.mypokemon.R
import com.example.mypokemon.baseclasses.BaseFragment
import com.example.mypokemon.databinding.ActivityQuizBinding
import com.example.mypokemon.data.QuizDataClass
import com.example.mypokemon.data.Result
import com.example.mypokemon.data.ResultDataClass
import com.example.mypokemon.enums.DataStates
import com.example.mypokemon.utils.setImage
import com.example.mypokemon.utils.showToast

import kotlinx.coroutines.flow.collectLatest


class QuizFragment : BaseFragment(R.layout.activity_quiz) {
    private lateinit var binding: ActivityQuizBinding
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = ActivityQuizBinding.bind(view)
        mViewModel.score.observe(viewLifecycleOwner) {
            binding.mHeaderLayout.tvScore.text = "Score : ${it.score}"
        }
        binding.mHeaderLayout.tvHeader.text = "Pokemon Quiz"
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            mViewModel.pokemones.collectLatest {
                when (it) {
                    is DataStates.Initial -> {}
                    is DataStates.Loading -> {

                    }
                    is DataStates.Success<*> -> {
                        if (savedInstanceState == null) {
                            //Create quiz on first run
                            createQuiz((it.value as ResultDataClass?)?.results ?: listOf())
                        }

                    }
                    is DataStates.Error -> {
                        requireContext().showToast(it.message)
                    }
                }
            }
        }

        mViewModel.quizData.observe(viewLifecycleOwner) {
            //show new quiz
            bindQuizData(it)
        }


        binding.btnSubmit.setOnClickListener {

            //click to submit the answer
            val radioButton =
                view.findViewById<RadioButton>(binding.radioOptions.checkedRadioButtonId)
            try {

                onOptionSelect(radioButton.text.toString())
            } catch (ex: NullPointerException) {
                
            }
        }


    }

    private fun bindQuizData(quizData: QuizDataClass?) {
        // setting data and showing quiz
        with(binding) {
            val kk = quizData?.result?.url?.split('/')
            val end = kk?.get(kk.lastIndex - 1)
            val uu = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/${
                end
            }.png"
            pokiImage.setImage(uu)
            if (radioOptions.childCount != 0) {
                radioOptions.removeAllViews()
            }
            quizData?.optionsList?.forEach {
                radioOptions.addView(getRadioButton(it))
            }
            radioOptions.invalidate()
        }
    }

    private fun getRadioButton(text: String): RadioButton {
        val rn = (1000..5000).random()
        val radioButton = RadioButton(requireContext())
        radioButton.id = rn
        radioButton.text = text
        return radioButton
    }

    private fun createQuiz(list: List<Result>) {
        //creating quiz
        val rand = (0..list.lastIndex).random()
        val optionsList: ArrayList<String> = ArrayList()
        optionsList.add(list[rand].name)
        for (i in 1 until 4) {
            val newRand = (0..list.lastIndex).random()
            optionsList.add(list[newRand].name)
        }
        optionsList.shuffle()
        val data = QuizDataClass(list[rand], optionsList)
        mViewModel.setQuiz(data)
    }

    @SuppressLint("ResourceType")
    private fun onOptionSelect(optionText: String) {
        //on quiz select matching the quiz name with answer and showing the result
        if (mViewModel.quizData.value?.result?.name == optionText) {
            Toast.makeText(requireContext(), "Correct", Toast.LENGTH_SHORT).show()

            mViewModel.addScore(mViewModel.score.value?.score?.plus(1))
            createQuiz(
                (((mViewModel.pokemones.value as DataStates.Success<*>).value) as ResultDataClass?)?.results
                    ?: listOf()
            )

        } else {
            Toast.makeText(requireContext(), "Incorrect", Toast.LENGTH_SHORT).show()


        }
    }


}