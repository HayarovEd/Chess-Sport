package bk.stavki.na.sport.app.ui.dashboard

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import bk.stavki.na.sport.app.databinding.FragmentQuizBinding
import bk.stavki.na.sport.app.ui.dashboard.QuizState.Game
import bk.stavki.na.sport.app.ui.dashboard.QuizState.Result

class QuizFragment : Fragment() {

    private var _binding: FragmentQuizBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<QuizViewModel>()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentQuizBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.data.observe(viewLifecycleOwner) { state ->
            when (state) {
                is Game -> {
                    binding.questTv.isVisible = true
                    binding.var1Bt.isVisible = true
                    binding.var2Bt.isVisible = true
                    binding.var3Bt.isVisible = true
                    binding.var4Bt.isVisible = true
                    binding.resultTv.isVisible = false
                    binding.returnBt.isVisible = false
                    binding.questTv.text = state.quest.quest
                    binding.var1Bt.text = state.quest.answer1
                    binding.var2Bt.text = state.quest.answer2
                    binding.var3Bt.text = state.quest.answer3
                    binding.var4Bt.text = state.quest.answer4
                    binding.var1Bt.setOnClickListener {
                        viewModel.sendAnswer(state.quest.answer1)
                    }
                    binding.var2Bt.setOnClickListener {
                        viewModel.sendAnswer(state.quest.answer2)
                    }
                    binding.var3Bt.setOnClickListener {
                        viewModel.sendAnswer(state.quest.answer3)
                    }
                    binding.var4Bt.setOnClickListener {
                        viewModel.sendAnswer(state.quest.answer4)
                    }
                }
                is Result -> {
                    binding.questTv.isVisible = false
                    binding.var1Bt.isVisible = false
                    binding.var2Bt.isVisible = false
                    binding.var3Bt.isVisible = false
                    binding.var4Bt.isVisible = false
                    binding.resultTv.isVisible = true
                    binding.returnBt.isVisible = true
                    binding.resultTv.text = "Your result ${state.result}"
                    binding.returnBt.setOnClickListener {
                        viewModel.restart()
                    }
                }
            }
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}