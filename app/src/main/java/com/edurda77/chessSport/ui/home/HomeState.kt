package com.edurda77.chessSport.ui.home

import com.edurda77.chessSport.data.RemoteData

sealed class HomeState {
    class SuccessConnect(
        val remoteData: RemoteData
    ) : HomeState()
    class NoInternet(val message: String) : HomeState()
    class Error(val message: String) : HomeState()
    object Loading: HomeState()
}
