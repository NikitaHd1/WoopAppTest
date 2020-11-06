package com.example.woopapp

import android.app.Application
import androidx.lifecycle.*
import com.example.woopapp.events.SingleLiveEvent
import com.example.woopapp.models.Answer
import com.example.woopapp.models.CardItem
import com.example.woopapp.models.GameModel
import com.example.woopapp.models.GameCardsResponse
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val context = getApplication<Application>().applicationContext
    private val answers: MutableList<Answer> = mutableListOf()

    private val gameCardsLiveData: MutableLiveData<GameModel> by lazy {
        MutableLiveData<GameModel>().also {
            loadCards()
        }
    }

    private val resultsLiveData: MutableLiveData<Pair<List<Answer>, String>> by lazy {
        MutableLiveData<Pair<List<Answer>, String>>().also {
            calculateResults()
        }
    }

    val navigateToResultFragmentLiveData = SingleLiveEvent<Any>()
    val navigateToSelectionFragmentLiveData = SingleLiveEvent<Any>()

    private fun calculateResults() {
        viewModelScope.launch(Dispatchers.IO) {
            val title = gameCardsLiveData.value?.title ?: ""
            resultsLiveData.postValue(Pair(answers, title))
        }
    }

    private fun loadCards() {
        viewModelScope.launch(Dispatchers.IO) {
            val bufferedReader = context.assets
                .open(GAME_JSON)
                .bufferedReader()

            val gameModel = Gson().fromJson<GameCardsResponse>(
                bufferedReader,
                object : TypeToken<GameCardsResponse>() {}.type
            ).mapToGameCards()
            gameCardsLiveData.postValue(gameModel)
        }
    }

    fun getResults(): LiveData<Pair<List<Answer>, String>> = resultsLiveData

    fun getCurds(): LiveData<GameModel> = gameCardsLiveData

    fun cardSwiped(
        element: CardItem,
        answerString: String,
        itemCount: Int
    ) {
        answers.add(
            Answer(
                element.characterName,
                element.imageName,
                element.serial,
                answerString,
                element.serial.equals(answerString, true)
            )
        )
        if (itemCount <= 0) {
            navigateToResultFragmentLiveData.call()
        }
    }

    fun resetDataAndNavigateToSelectionFragment() {
        answers.clear()
        navigateToSelectionFragmentLiveData.call()
    }

    companion object {
        private const val GAME_JSON = "game.json"
    }
}