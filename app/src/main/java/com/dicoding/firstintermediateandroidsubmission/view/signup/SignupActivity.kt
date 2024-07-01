package com.dicoding.firstintermediateandroidsubmission.view.signup

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
import com.dicoding.firstintermediateandroidsubmission.view.ViewModelFactory
import com.dicoding.firstintermediateandroidsubmission.customviews.MyButton
import com.dicoding.firstintermediateandroidsubmission.customviews.MyEditText
import com.dicoding.firstintermediateandroidsubmission.data.Result
import com.dicoding.firstintermediateandroidsubmission.databinding.ActivitySignupBinding
import com.dicoding.firstintermediateandroidsubmission.view.login.LoginActivity

class SignupActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignupBinding
    private lateinit var signupViewModel: SignupViewModel

    private lateinit var submitButton: MyButton
    private lateinit var nameEditText: MyEditText
    private lateinit var emailEditText: MyEditText
    private lateinit var passwordEditText: MyEditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //viewmodel
        val factory: ViewModelFactory = ViewModelFactory.getInstance(applicationContext)
        signupViewModel = viewModels<SignupViewModel> {
            factory
        }.value

        setupView()
        setupAction()

        binding.ToLoginText.setOnClickListener {
            // Create an intent to navigate to the login activity
            val intent = Intent(this@SignupActivity, LoginActivity::class.java)
            startActivity(intent)
        }

        submitButton = findViewById(R.id.signupButton)
        nameEditText = findViewById(R.id.ed_register_name)
        emailEditText = findViewById(R.id.ed_register_email)
        passwordEditText = findViewById(R.id.ed_register_password)

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

        nameEditText.addTextChangedListener(textWatcher)
        emailEditText.addTextChangedListener(textWatcher)
        passwordEditText.addTextChangedListener(textWatcher)
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
        binding.signupButton.setOnClickListener {
            val name = binding.edRegisterName.text.toString()
            val email = binding.edRegisterEmail.text.toString()
            val password = binding.edRegisterPassword.text.toString()

            signupViewModel.register(name, email, password).observe(this@SignupActivity) { result ->
                if (result != null) {
                    when (result) {
                        is Result.Loading -> {
                            showLoading(true)
                        }

                        is Result.Success -> {
                            val message = "Login was Successful"
                            showToast(message)
                            showLoading(false)

                            //Selesai register pindah ke menu login
                            val intent = Intent(this@SignupActivity, LoginActivity::class.java)
                            startActivity(intent)
                            finish()
                        }

                        is Result.Error -> {
                            val message = "Register  was Unsuccessful"
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
            nameEditText.text.toString(),
            emailEditText.text.toString(),
            passwordEditText.text.toString()
        )
        val isValid = inputTexts.all { it.isNotEmpty() } &&
                (emailEditText.error == null && passwordEditText.error == null)
        submitButton.isEnabled = isValid
    }

}