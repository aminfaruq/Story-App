package com.aminfaruq.storyapp.ui.home

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.aminfaruq.storyapp.R
import com.aminfaruq.storyapp.databinding.ActivityDetailBinding
import com.aminfaruq.storyapp.databinding.ActivityHomeBinding
import com.aminfaruq.storyapp.di.Injection
import com.aminfaruq.storyapp.ui.auth.login.LoginActivity
import com.aminfaruq.storyapp.ui.detail.DetailActivity
import com.aminfaruq.storyapp.ui.home.adapter.LoadingStateAdapter
import com.aminfaruq.storyapp.ui.maps.MapsActivity
import com.aminfaruq.storyapp.ui.upload.UploadActivity
import com.aminfaruq.storyapp.utils.SharedPreferencesHelper
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@Suppress("DEPRECATION")
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

        setupToolbar()
        setupRecyclerView()
        setupUploadLauncher()
        setupFab()

        observeViewModel()
    }

    private fun observeViewModel() {
        viewModel.story.observe(this) { pagingData ->
            adapter.submitData(lifecycle, pagingData)
        }

        lifecycleScope.launch {
            adapter.loadStateFlow.collectLatest { loadStates ->
                binding.swipeRefreshLayout.isRefreshing = loadStates.refresh is LoadState.Loading
                if (loadStates.refresh is LoadState.Error) {
                    val errorState = loadStates.refresh as LoadState.Error
                    showError("Failed to load data: ${errorState.error.localizedMessage}")
                }
            }
        }
    }

    private fun showError(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    private fun setupRecyclerView() {
        binding.swipeRefreshLayout.setOnRefreshListener {
            adapter.refresh()
        }
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = StoryListAdapter(this).apply {
            binding.recyclerView.adapter = this.withLoadStateFooter(
                footer = LoadingStateAdapter { retry() }
            )
        }
    }

    private fun setupFab() {
        binding.fab.setOnClickListener {
            val intent = Intent(this, UploadActivity::class.java)
            uploadLauncher.launch(intent)
        }
    }

    private fun setupUploadLauncher() {
        uploadLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                adapter.refresh()
                binding.recyclerView.scrollToPosition(0)
            }
        }
    }

    private fun setupToolbar() {
        val toolbar: Toolbar = binding.toolbar
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.baseline_translate_24)
        toolbar.setNavigationOnClickListener {
            startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
        }
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
            R.id.action_maps -> {
                val intent = Intent(this, MapsActivity::class.java)
                startActivity(intent)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        super.onBackPressed()
        onBackPressedDispatcher.onBackPressed()
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
