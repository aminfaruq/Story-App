package com.aminfaruq.storyapp.ui.auth.login

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.aminfaruq.storyapp.databinding.ActivityLoginBinding
import com.aminfaruq.storyapp.di.Injection
import com.aminfaruq.storyapp.ui.auth.register.RegisterActivity
import com.aminfaruq.storyapp.ui.home.HomeActivity
import com.aminfaruq.storyapp.utils.Result

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private val viewModel: LoginViewModel by viewModels {
        Injection.provideAuthViewModelFactory(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        binding.loadingView.visibility = View.GONE
        binding.buttonLogin.setOnClickListener {
            val email = binding.editTextEmail.text.toString()
            val password = binding.editTextPassword.text.toString()

            viewModel.login(email, password, this).observe(this) { result ->
                when (result) {
                    is Result.Loading -> {
                        binding.loadingView.visibility = View.VISIBLE
                    }

                    is Result.Success -> {
                        binding.loadingView.visibility = View.GONE
                        val intent = Intent(this, HomeActivity::class.java)
                        startActivity(intent)
                        finish()
                    }

                    is Result.Error -> {
                        binding.loadingView.visibility = View.GONE
                        Toast.makeText(this, "Gagal", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        binding.buttonRegister.setOnClickListener {
            val moveRegister = Intent(this, RegisterActivity::class.java)
            startActivity(moveRegister)
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        super.onBackPressed()
        finishAffinity()
    }
}