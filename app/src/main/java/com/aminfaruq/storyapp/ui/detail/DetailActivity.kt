package com.aminfaruq.storyapp.ui.detail

import android.os.Bundle
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.aminfaruq.storyapp.databinding.ActivityDetailBinding
import com.aminfaruq.storyapp.di.Injection
import com.aminfaruq.storyapp.utils.Result
import com.bumptech.glide.Glide

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding
    private val viewModel: DetailViewModel by viewModels {
        Injection.provideStoryViewModelFactory(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
        }

        val id = intent.getStringExtra(EXTRA_ID)

        viewModel.getDetailId(id ?: "").observe(this) { result ->
            when (result) {
                is Result.Error -> {

                }

                is Result.Success -> {
                    val data = result.data.story
                    binding.tvDetailName.text = data.name
                    binding.tvDetailDescription.text = data.description
                    Glide.with(this)
                        .load(data.photoUrl)
                        .into(binding.ivDetailPhoto)
                }

                is Result.Loading -> {

                }
            }
        }

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    companion object {
        const val EXTRA_ID = "ID"
    }
}