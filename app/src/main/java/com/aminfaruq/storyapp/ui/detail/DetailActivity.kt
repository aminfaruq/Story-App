package com.aminfaruq.storyapp.ui.detail

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.aminfaruq.storyapp.R
import com.aminfaruq.storyapp.databinding.ActivityDetailBinding
import com.aminfaruq.storyapp.di.Injection
import com.aminfaruq.storyapp.utils.Result
import com.bumptech.glide.Glide

@Suppress("DEPRECATION")
class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding
    private val viewModel: DetailViewModel by viewModels {
        Injection.provideStoryViewModelFactory(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            title = getString(R.string.detail_activity_title)
            titleColor = resources.getColor(android.R.color.white, null)
        }

        val id = intent.getStringExtra(EXTRA_ID)
        binding.loadingView.visibility = View.GONE
        viewModel.getDetailId(id ?: "").observe(this) { result ->
            when (result) {
                is Result.Loading -> {
                    binding.loadingView.visibility = View.VISIBLE
                }

                is Result.Success -> {
                    binding.loadingView.visibility = View.GONE
                    val data = result.data.story
                    binding.tvDetailName.text = data.name
                    binding.tvDetailDescription.text = data.description
                    Glide.with(this)
                        .load(data.photoUrl)
                        .into(binding.ivDetailPhoto)
                }

                is Result.Error -> {
                    Toast.makeText(this, getString(R.string.failed_info), Toast.LENGTH_SHORT).show()
                    binding.loadingView.visibility = View.GONE
                }
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    companion object {
        const val EXTRA_ID = "ID"
    }
}
