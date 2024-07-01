package com.dicoding.firstintermediateandroidsubmission.view.welcome

import android.animation.ValueAnimator
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.WindowInsets
import android.view.WindowManager
import android.view.animation.LinearInterpolator
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.dicoding.firstintermediateandroidsubmission.view.ViewModelFactory
import com.dicoding.firstintermediateandroidsubmission.databinding.ActivityWelcomeBinding
import com.dicoding.firstintermediateandroidsubmission.view.login.LoginActivity
import com.dicoding.firstintermediateandroidsubmission.view.main.MainActivity
import com.dicoding.firstintermediateandroidsubmission.view.signup.SignupActivity

class WelcomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityWelcomeBinding
    private lateinit var welcomeViewModel: WelcomeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWelcomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //viewmodel
        val factory: ViewModelFactory = ViewModelFactory.getInstance(applicationContext)
        welcomeViewModel = viewModels<WelcomeViewModel> {
            factory
        }.value

        welcomeViewModel.isUserLoggedIn.observe(this) { isUserLoggedIn ->
            if (isUserLoggedIn == true) {
                // The user is logged in, so navigate to the main activity.
                startActivity(Intent(this, MainActivity::class.java))
                finish() // Finish the current activity so the user can't go back.
            } else {
                setupView()
                setupAction()
                playAnimation()
            }
        }

    }

    private fun setupView() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.hide()
    }

    private fun setupAction() {
        binding.loginButton.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))

        }

        binding.signupButton.setOnClickListener {
            startActivity(Intent(this, SignupActivity::class.java))
        }
    }

    private fun playAnimation() {
        val screenWidth = resources.displayMetrics.widthPixels.toFloat()
        val imageWidth = binding.backgroundObjects.width

        ValueAnimator.ofFloat(0f, screenWidth / 2).apply {
            duration = 6000
            repeatCount = ValueAnimator.INFINITE
            repeatMode = ValueAnimator.REVERSE
            interpolator = LinearInterpolator()

            addUpdateListener { animation ->
                val translationX = (animation.animatedValue as Float)
                binding.backgroundObjects.translationX = -translationX + imageWidth
            }
        }
            .start()
    }
}