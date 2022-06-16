/*
 * Copyright (C) 2020 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.android.unscramble

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.android.unscramble.databinding.MainActivityBinding
import com.example.android.unscramble.ui.game.GameViewModel
import com.example.android.unscramble.ui.game.MAX_NO_OF_WORDS
import com.example.android.unscramble.ui.game.datas.entries
import com.example.android.unscramble.ui.game.datas.entries.Companion.username
import com.example.android.unscramble.ui.game.levelsinfo
import com.google.android.gms.ads.*
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    lateinit var binding: MainActivityBinding
    private lateinit var data : String
    lateinit var viewModel: GameViewModel
    private var mInterstitialAd: InterstitialAd? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this,R.layout.main_activity)
        actionBar?.setDisplayHomeAsUpEnabled(true)
        /********************* Google Ads...Admob and Adsense ****************/
        /********************* Google Ads...Admob and Adsense */
        MobileAds.initialize(this)
        var adRequest = AdRequest.Builder().build()
        InterstitialAd.load(this,"ca-app-pub-3940256099942544/1033173712", adRequest, object : InterstitialAdLoadCallback() {
            override fun onAdFailedToLoad(adError: LoadAdError) {
                Log.d("game", adError?.message)
                mInterstitialAd = null
            }

            override fun onAdLoaded(interstitialAd: InterstitialAd) {
                Log.d("game", "Ad was loaded.")
                mInterstitialAd = interstitialAd
            }
        })
        mInterstitialAd?.fullScreenContentCallback = object: FullScreenContentCallback() {
            override fun onAdDismissedFullScreenContent() {
                Log.d("game", "Ad was dismissed.")
            }

            override fun onAdFailedToShowFullScreenContent(p0: AdError) {
                Log.d("game", "Ad failed to show.")
            }

            override fun onAdShowedFullScreenContent() {
                Log.d("game", "Ad showed fullscreen content.")
                mInterstitialAd = null
            }
        }
       /* ********************* Google Ads...Admob and Adsense  */
        data = intent?.extras?.getString("levels").toString()
        levelsinfo =data
        viewModel = ViewModelProvider(this).get(GameViewModel::class.java)
        binding.testViewModel = viewModel
        binding.maxNoOfWords = MAX_NO_OF_WORDS
        binding.lifecycleOwner = this
        binding.submit.setOnClickListener { onSubmitWord() }
        binding.skip.setOnClickListener { onSkipWord() }



    }
    private fun onSubmitWord() {
        val playerWord = binding.textInputEditText.text.toString()
        // viewModel.levels.value
        if (viewModel.isUserWordCorrect(playerWord)) {
            setErrorTextField(false)
            if (viewModel.nextWord()) {
                Log.d("word","next word function invoked")
            } else {
                showFinalScoreDialog()
            }
        }
        else {
            setErrorTextField(true)
        }
    }

    private fun showFinalScoreDialog() {
        /********************* Google Ads...Admob and Adsense  */
        if (mInterstitialAd != null) {
            mInterstitialAd?.show(this)
        } else {
            Log.d("game", "The interstitial ad wasn't ready yet.")
        } 
        /********************* Google Ads...Admob and Adsense  */
        MaterialAlertDialogBuilder(this)
            .setTitle(getString(R.string.congratulations))
            .setMessage(getString(R.string.you_scored,viewModel.score.value))
            .setCancelable(false)
            .setNegativeButton(getString(R.string.exit)){_,_->
                exitGame()
            }
            .setPositiveButton(getString(R.string.play_again)){_,_->
                restartGame()
            }
            .show()

    }

    private fun restartGame() {
        viewModel.reinitializeData()
        setErrorTextField(false)
    }


    private fun onSkipWord() {
        if (viewModel.nextWord()) {
            binding.textViewUnscrambledWord.text=viewModel.currentScrambledWord.value
            setErrorTextField(false)

        } else {
            showFinalScoreDialog()
        }
    }


    private fun exitGame() {
        GlobalScope.launch(Dispatchers.IO) {
            if (entries.scorecompanion < viewModel.score.value!!) {
                entries.scorecompanion = viewModel.score.value!!}
                val sharedPref = this@MainActivity.getSharedPreferences("sharing", Context.MODE_PRIVATE)
                with (sharedPref.edit())
                {
                    putInt(username, com.example.android.unscramble.ui.game.datas.entries.scorecompanion)
                    apply()
                    commit()
                }
            }

    val intent = Intent()
    intent.putExtra("value",viewModel.score.value!!)
    setResult(Activity.RESULT_OK,intent)
    finish()
    }

    private fun setErrorTextField(error: Boolean) {
    if (error) {
        binding.textField.isErrorEnabled = true
        binding.textField.error = getString(R.string.try_again)
    } else {
        binding.textField.isErrorEnabled = false
        binding.textInputEditText.text = null
    }
    }

}