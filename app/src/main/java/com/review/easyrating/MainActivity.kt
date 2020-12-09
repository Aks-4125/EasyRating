package com.review.easyrating

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.method.ScrollingMovementMethod
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.android.play.core.review.ReviewManagerFactory
import com.review.easyrating.databinding.ActivityMainBinding
import java.lang.Exception


class MainActivity : AppCompatActivity() {
    companion object {
        var mAdCounter = 0
    }
    private lateinit var binding: ActivityMainBinding
    private val TAG = "EasyRating"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        setSupportActionBar(binding.toolbar)
        binding.toolbar

        binding.textviewLog.movementMethod = ScrollingMovementMethod()


        binding.buttonFirst.setOnClickListener {
            startActivityForResult(Intent(this, ImageActivity::class.java), 1234)
            mAdCounter++
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        binding.textviewLog.text = "You have completed $mAdCounter/5 tasks"

        if (requestCode == 1234) {
            Log.d(TAG,"$mAdCounter Task completed")
            binding.progressBarStep.progress = mAdCounter
            if (mAdCounter == 5) {
                Log.d(TAG,"Processing request for Rating dialog")
                binding.textviewFirst.text = "Congrats! App Rating Dialog has been requested"
                mAdCounter = 0
                binding.progressBarStep.progress = 0
                Handler(Looper.getMainLooper()).postDelayed({
                    inAppReview()
                }, 3000)
            }
        }
    }

    private fun inAppReview() {
        val reviewManager = ReviewManagerFactory.create(this)
        val requestReviewFlow = reviewManager.requestReviewFlow()
        requestReviewFlow.addOnCompleteListener { request ->
            binding.textviewLog.append("\nrequesting dialog...")
            Log.d(TAG,"Rating dialog task requested")
            try {
                if (request.isSuccessful) {
                    binding.textviewLog.append("\n We got the ReviewInfo object..")
                    // We got the ReviewInfo object
                    Log.d(TAG,"task is successful, now launching review flow")
                    val reviewInfo = request.result
                    val flow = reviewManager.launchReviewFlow(this, reviewInfo)
                    flow.addOnCompleteListener {
                        binding.textviewLog.append("\n The flow has finished... completed!!")
                        // The flow has finished. The API does not indicate whether the user
                        // reviewed or not, or even whether the review dialog was shown. Thus, no
                        // matter the result, we continue our app flow.
                        Log.d(TAG,"Flow has been finished. Dialog should be on the display now")
                    }
                    flow.addOnSuccessListener{
                        binding.textviewLog.append("\n The flow has been succeed")
                        Log.d(TAG,"Flow success listner called")
                    }
                } else {
                    binding.textviewLog.append("\n There was some problem, Requested result was not successful from SDK")
                    Log.d(TAG,"Requested result was not successful ${request.exception?.message}")
                    // There was some problem, continue regardless of the result.
                }

            }catch (ex: Exception){
                binding.textviewLog.append("\n There was an exception ${ex.message}")
                Log.e(TAG,"There was an exception ${ex.message}",ex)

            }
        }
    }

}