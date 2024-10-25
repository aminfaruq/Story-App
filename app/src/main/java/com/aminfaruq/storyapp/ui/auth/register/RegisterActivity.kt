package com.aminfaruq.storyapp.ui.auth.register

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.aminfaruq.storyapp.databinding.ActivityRegisterBinding
import com.aminfaruq.storyapp.di.Injection
import com.aminfaruq.storyapp.ui.auth.login.LoginActivity
import com.aminfaruq.storyapp.utils.Result

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private val viewModel: RegisterViewModel by viewModels {
        Injection.provideAuthViewModelFactory(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        binding.loadingView.visibility = View.GONE
        binding.buttonRegister.setOnClickListener {
            val name = binding.editTextName.text.toString()
            val email = binding.editTextEmail.text.toString()
            val password = binding.editTextPassword.text.toString()
            doRegister(name, email, password)
        }

        binding.buttonLogin.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }

    }

    private fun doRegister(name: String, email: String, password: String) {
        viewModel.register(name, email, password).observe(this) { result ->
            when (result) {
                is Result.Loading -> {
                    binding.loadingView.visibility = View.VISIBLE
                }

                is Result.Success -> {
                    binding.loadingView.visibility = View.GONE
                    val intent = Intent(this, LoginActivity::class.java)
                    startActivity(intent)
                    finish()
                }

                is Result.Error -> {
                    binding.loadingView.visibility = View.GONE
                    Toast.makeText(this, result.error, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
