package bk.stavki.na.sport.app.ui.dashboard

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import bk.stavki.na.sport.app.data.questions
import bk.stavki.na.sport.app.ui.dashboard.QuizState.Game
import bk.stavki.na.sport.app.ui.dashboard.QuizState.Result

class QuizViewModel : ViewModel() {

    private var currrentResult = 0
    private var numberQuest = 0
    private val _data = MutableLiveData<QuizState>(
        Game(
            quest = questions.invoke()[numberQuest]
        )
    )
    val data = _data


    fun sendAnswer(answer: String) {
        if (answer == questions.invoke()[numberQuest].rightAnswer) {
            currrentResult++
        }
        if (numberQuest== questions.invoke().size-1) {
            _data.value = Result(currrentResult)
        } else {
            numberQuest++
            _data.value = Game(questions.invoke()[numberQuest])
        }
    }

    fun restart() {
        currrentResult = 0
        numberQuest = 0
        _data.value = Game(questions.invoke()[numberQuest])
    }


}