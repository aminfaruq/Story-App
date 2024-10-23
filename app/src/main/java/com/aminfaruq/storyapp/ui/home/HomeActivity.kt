package com.aminfaruq.storyapp.ui.home

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.Menu
import android.view.MenuItem
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityOptionsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.aminfaruq.storyapp.data.response.story.StoryItemResponse
import com.aminfaruq.storyapp.databinding.ActivityDetailBinding
import com.aminfaruq.storyapp.databinding.ActivityHomeBinding
import com.aminfaruq.storyapp.di.Injection
import com.aminfaruq.storyapp.ui.detail.DetailActivity
import com.aminfaruq.storyapp.ui.upload.UploadActivity
import androidx.core.util.Pair
import com.aminfaruq.storyapp.R
import com.aminfaruq.storyapp.ui.auth.login.LoginActivity
import com.aminfaruq.storyapp.utils.SharedPreferencesHelper

class HomeActivity : AppCompatActivity(), OnItemClickListener {
    private lateinit var binding: ActivityHomeBinding
    private lateinit var detailBinding: ActivityDetailBinding

    private lateinit var adapter: StoryListAdapter
    private lateinit var uploadLauncher: ActivityResultLauncher<Intent>

    private val viewModel: HomeViewModel by viewModels {
        Injection.provideStoryViewModelFactory(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        detailBinding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val toolbar: Toolbar = binding.toolbar
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.baseline_translate_24)
        toolbar.setNavigationOnClickListener {
            startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
        }

        viewModel.requestStoryList()

        binding.swipeRefreshLayout.setOnRefreshListener {
            viewModel.requestStoryList()
        }

        uploadLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    viewModel.requestStoryList()
                }
            }

        val layoutManager = LinearLayoutManager(this)
        binding.recyclerView.layoutManager = layoutManager

        viewModel.listStory.observe(this) {
            setupList(it.listStory)
            binding.swipeRefreshLayout.isRefreshing = false
        }

        viewModel.isLoading.observe(this) {
            binding.swipeRefreshLayout.isRefreshing = it
        }

        viewModel.isError.observe(this) {
            binding.swipeRefreshLayout.isRefreshing = false
        }

        binding.fab.setOnClickListener {
            val intent = Intent(this, UploadActivity::class.java)
            uploadLauncher.launch(intent)
        }
    }

    private fun setupList(it: List<StoryItemResponse>) {
        adapter = StoryListAdapter(it, this)
        binding.recyclerView.adapter = adapter
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.story_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val sharedPreferencesHelper = SharedPreferencesHelper(this)

        return when (item.itemId) {
            R.id.action_logout -> {
                sharedPreferencesHelper.clearToken()
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finish()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }


    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        super.onBackPressed()
        finishAffinity()
    }

    override fun onItemClick(id: String) {
        val moveIntoDetail = Intent(this, DetailActivity::class.java)
        moveIntoDetail.putExtra(DetailActivity.EXTRA_ID, id)
        val optionsCompat: ActivityOptionsCompat =
            ActivityOptionsCompat.makeSceneTransitionAnimation(
                this,
                Pair(detailBinding.ivDetailPhoto, "profile"),
                Pair(detailBinding.tvDetailName, "name"),
                Pair(detailBinding.tvDetailDescription, "description"),
            )
        startActivity(moveIntoDetail, optionsCompat.toBundle())
    }
}
