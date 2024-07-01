package com.dicoding.firstintermediateandroidsubmission.view.login

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.dicoding.firstintermediateandroidsubmission.R
import com.dicoding.firstintermediateandroidsubmission.data.Result
import com.dicoding.firstintermediateandroidsubmission.view.ViewModelFactory
import com.dicoding.firstintermediateandroidsubmission.customviews.MyButton
import com.dicoding.firstintermediateandroidsubmission.customviews.MyEditText
import com.dicoding.firstintermediateandroidsubmission.databinding.ActivityLoginBinding
import com.dicoding.firstintermediateandroidsubmission.view.main.MainActivity
import com.dicoding.firstintermediateandroidsubmission.view.signup.SignupActivity

class LoginActivity : AppCompatActivity() {
    private val viewModel by viewModels<LoginViewModel> {
        ViewModelFactory.getInstance(this)
    }
    private lateinit var binding: ActivityLoginBinding

    private lateinit var submitButton: MyButton
    private lateinit var emailEditText: MyEditText
    private lateinit var passwordEditText: MyEditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()
        setupAction()

        submitButton = findViewById(R.id.loginButton)
        emailEditText = findViewById(R.id.ed_login_email)
        passwordEditText = findViewById(R.id.ed_login_password)

        setMyButtonEnable()

        val textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                setMyButtonEnable()
            }

            override fun afterTextChanged(s: Editable) {
            }
        }

        emailEditText.addTextChangedListener(textWatcher)
        passwordEditText.addTextChangedListener(textWatcher)

        binding.ToSignupText.setOnClickListener {
            val intent = Intent(this@LoginActivity, SignupActivity::class.java)
            startActivity(intent)
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
            val email = binding.edLoginEmail.text.toString()
            val password = binding.edLoginPassword.text.toString()
            viewModel.login(email, password).observe(this@LoginActivity) { result ->
                if (result != null) {
                    when (result) {
                        is Result.Loading -> {
                            showLoading(true)
                        }

                        is Result.Success -> {
                            val message = "Login was Successful"
                            showToast(message)
                            showLoading(false)

                            val intent = Intent(this@LoginActivity, MainActivity::class.java)
                            startActivity(intent)
                            finish()
                        }

                        is Result.Error -> {
                            val message = "Login  was Unsuccessful"
                            showToast(message)
                            showLoading(false)
                        }

                    }
                }
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    // Metode untuk mengubah disable dan enable pada button
    private fun setMyButtonEnable() {
        val inputTexts = listOf(
            emailEditText.text.toString(),
            passwordEditText.text.toString()
        )
        val isValid = inputTexts.all { it.isNotEmpty() } &&
                (emailEditText.error == null && passwordEditText.error == null)
        submitButton.isEnabled = isValid
    }

}