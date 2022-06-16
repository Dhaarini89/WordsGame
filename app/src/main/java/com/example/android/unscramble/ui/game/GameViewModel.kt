package com.example.android.unscramble.ui.game

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.android.unscramble.ui.game.datas.entries

class GameViewModel : ViewModel() {

    private var finalvalue: String = ""
    private val _score = MutableLiveData(0)
    private val _currentWordCount = MutableLiveData(0)
    private val _currentScrambledWord = MutableLiveData<String>()
    private var wordsList: MutableList<String> = mutableListOf()
    private lateinit var currentWord: String
    private lateinit var levelslist: List<String>
    val currentScrambledWord: LiveData<String>
        get() = _currentScrambledWord

    val score: LiveData<Int>
        get() = _score

    val currentWordCount: LiveData<Int>
        get() = _currentWordCount

    init {
        var levels = levelsinfo.toString()
        when (levels) {
            "நிறங்கள்" -> {
                levelslist = allWordsListLevel1
            }
            "உணவு பொருட்கள்" -> {
                _score.value = 200
                levelslist = allWordsListLevel2
            }
            "உடல் பாகங்கள்" -> {
                _score.value = 400
                levelslist = allWordsListLevel3
            }
            "உயிரினங்கள்" -> {
                _score.value = 600
                levelslist = allWordsListLevel4
            }
            "பழங்கள் மற்றும் காய்கறிகள்" -> {
                _score.value = 800
                levelslist = allWordsListLevel5
            }
            "வீட்டு உபயோக பொருட்கள்" -> {
                _score.value = 1000
                levelslist = allWordsListLevel6
            }
            "தமிழ் இலக்கியம்" -> {
                _score.value = 1200
                levelslist = allWordsListLevel7
            }
            "அரசியல் அறிவியல்" -> {
                _score.value = 1400
                levelslist = allWordsListLevel8
            }
            "கணினி அறிவியல்" -> {
                _score.value = 1600
                levelslist = allWordsListLevel9
            }
        }
        Log.d("GameFragment", "GameViewModel created!")
        getNextWord()
    }

    override fun onCleared() {
        super.onCleared()
        Log.d("GameFragment", "GameViewModel destroyed!")
    }

    private fun getNextWord() {
        _currentWordCount.value = (_currentWordCount.value)?.inc()

        currentWord = levelslist.random()
        var word = currentWord.toCharArray()
        while (String(word).equals(currentWord, false)) {
            var wordint = IntArray(word.size)
            for (i in 0..word.size - 1) {
                wordint[i] = word[i].code
            }
            val wordintlist = wordint.toList()
            val finallist = wordintlist.zipWithNext() { s1, s2 -> checkvowels(s1, s2) }
            val shufflevalue = finallist.shuffled()
            for (i in 0..shufflevalue.size - 1) {
                if (shufflevalue[i] != "")
                    finalvalue += shufflevalue[i]
            }

            word = finalvalue.toCharArray()
            finalvalue = ""
        }
        if (wordsList.contains(currentWord)) {
            _currentWordCount.value = (_currentWordCount.value)?.dec()
            getNextWord()

        } else
            _currentScrambledWord.value = String(word)
        wordsList.add(currentWord)
    }

    fun reinitializeData() {
        _score.value = 0
        _currentWordCount.value = 0
        wordsList.clear()
        getNextWord()
    }

    fun nextWord(): Boolean {
        return if (_currentWordCount.value!! < MAX_NO_OF_WORDS) {
            getNextWord()
            true
        } else false
    }

    private fun increaseScore() {
        _score.value = (_score.value)?.plus(SCORE_INCREASE)
    }

    fun isUserWordCorrect(playerWord: String): Boolean {

        if (playerWord.equals(currentWord, true)) {
            increaseScore()
            return true
        }
        return false
    }

    fun checkvowels(s1: Int, s2: Int): String {
        val tamilvowels =
            listOf(3006, 3007, 3008, 3009, 3010, 3014, 3015, 3016, 3018, 3019, 3020, 3021)
        if (s1 in tamilvowels) {
            return ""
        }
        if (s2 in tamilvowels) {
            var temp: String = s1.toChar().toString()
            temp = temp + s2.toChar().toString()
            return temp
        } else
            return s1.toChar().toString()
    }


}