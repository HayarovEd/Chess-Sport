package com.edurda77.chessSport.ui.dashboard

import com.edurda77.chessSport.data.Quest

sealed class QuizState {
    class Game(val quest: Quest) : QuizState()
    class Result (val result: Int) : QuizState()
}

