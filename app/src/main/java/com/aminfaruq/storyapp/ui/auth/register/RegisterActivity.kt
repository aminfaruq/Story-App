package com.aminfaruq.storyapp.ui.auth.register

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.aminfaruq.storyapp.databinding.ActivityRegisterBinding
import com.aminfaruq.storyapp.di.Injection
import com.aminfaruq.storyapp.ui.auth.login.LoginActivity
import com.aminfaruq.storyapp.utils.Result

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private lateinit var viewModel: RegisterViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val authViewModelFactory = Injection.provideAuthViewModelFactory(this)

        // Membuat instance LoginViewModel
        viewModel = ViewModelProvider(this, authViewModelFactory)[RegisterViewModel::class.java]
        binding.loadingView.visibility = View.GONE
        binding.buttonRegister.setOnClickListener {
            val name = binding.editTextName.text.toString()
            val email = binding.editTextEmail.text.toString()
            val password = binding.editTextPassword.text.toString()

            // Validasi dan proses registrasi
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
                        Toast.makeText(this,"Gagal", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        binding.buttonLogin.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }

    }
}