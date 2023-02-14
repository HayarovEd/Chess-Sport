package betanges.hipgame.ui.dashboard

import betanges.hipgame.data.Quest

sealed class QuizState {
    class Game(val quest: Quest) : QuizState()
    class Result (val result: Int) : QuizState()
}

