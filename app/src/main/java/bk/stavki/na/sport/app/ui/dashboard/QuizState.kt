package bk.stavki.na.sport.app.ui.dashboard

import bk.stavki.na.sport.app.data.Quest

sealed class QuizState {
    class Game(val quest: Quest) : QuizState()
    class Result (val result: Int) : QuizState()
}

