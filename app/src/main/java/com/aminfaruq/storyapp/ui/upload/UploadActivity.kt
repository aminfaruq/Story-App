package com.aminfaruq.storyapp.ui.upload

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import com.aminfaruq.storyapp.R
import com.aminfaruq.storyapp.data.response.story.MessageResponse
import com.aminfaruq.storyapp.databinding.ActivityUploadBinding
import com.aminfaruq.storyapp.di.Injection
import com.aminfaruq.storyapp.ui.upload.CameraActivity.Companion.CAMERAX_RESULT
import com.aminfaruq.storyapp.utils.Result
import com.aminfaruq.storyapp.utils.reduceFileImage
import com.aminfaruq.storyapp.utils.uriToFile
import com.yalantis.ucrop.UCrop
import java.io.File

class UploadActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUploadBinding
    private var currentImageUri: Uri? = null

    private val viewModel: UploadViewModel by viewModels {
        Injection.provideStoryViewModelFactory(this)
    }

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            handlePermissionResult(isGranted)
        }

    private val launcherCropImage = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        handleCropResult(result.resultCode, result.data)
    }

    private val launcherGallery = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri -> handleGalleryResult(uri) }

    private val launcherIntentCameraX = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result -> handleCameraXResult(result.resultCode, result.data) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUploadBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            title = "Upload Story"
        }

        setupUI()

        if (!allPermissionsGranted()) {
            requestPermissionLauncher.launch(REQUIRED_PERMISSION)
        }
    }

    private fun handleCropResult(resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK) {
            val resultUri = data?.let { UCrop.getOutput(it) }
            resultUri?.let {
                currentImageUri = it
                showImage()
            }
        } else if (resultCode == UCrop.RESULT_ERROR) {
            val cropError = UCrop.getError(data!!)
            showToast(cropError?.message ?: "Crop error")
        }
    }

    private fun startCrop(sourceUri: Uri) {
        val destinationUri = Uri.fromFile(File(cacheDir, "cropped_image.jpg"))
        val uCrop = UCrop.of(sourceUri, destinationUri)
            .withAspectRatio(1f, 1f)
            .withMaxResultSize(1000, 1000)
        launcherCropImage.launch(uCrop.getIntent(this))
    }


    private fun setupUI() {
        binding.apply {
            loadingView.visibility = View.GONE
            galleryButton.setOnClickListener { startGallery() }
            cameraXButton.setOnClickListener { startCameraX() }
            uploadButton.setOnClickListener { uploadImage() }
        }
    }

    private fun handlePermissionResult(isGranted: Boolean) {
        val message = if (isGranted) {
            "Permission request granted"
        } else {
            "Permission request denied"
        }
        showToast(message)
    }

    private fun allPermissionsGranted(): Boolean =
        ContextCompat.checkSelfPermission(this, REQUIRED_PERMISSION) == PackageManager.PERMISSION_GRANTED

    private fun startGallery() {
        launcherGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private fun handleGalleryResult(uri: Uri?) {
        uri?.let {
            startCrop(it)
        } ?: Log.d("Photo Picker", "No media selected")
    }

    private fun startCameraX() {
        val intent = Intent(this, CameraActivity::class.java)
        launcherIntentCameraX.launch(intent)
    }

    private fun handleCameraXResult(resultCode: Int, data: Intent?) {
        if (resultCode == CAMERAX_RESULT) {
            currentImageUri = data?.getStringExtra(CameraActivity.EXTRA_CAMERAX_IMAGE)?.toUri()
            showImage()
        }
    }

    private fun showImage() {
        currentImageUri?.let {
            Log.d("Image URI", "showImage: $it")
            binding.previewImageView.setImageURI(it)
        }
    }

    private fun uploadImage() {
        val uri = currentImageUri
        if (uri == null) {
            showToast(getString(R.string.empty_image_warning))
            return
        }

        val imageFile = uriToFile(uri, this).reduceFileImage()
        val description = binding.edAddDescription.text.toString()

        viewModel.uploadImage(imageFile, description).observe(this) { result ->
            handleUploadResult(result)
        }
    }

    private fun handleUploadResult(result: Result<MessageResponse>) {
        when (result) {
            is Result.Loading -> showLoading(true)
            is Result.Success -> onUploadSuccess(result.data.toString())
            is Result.Error -> onUploadError(result.error)
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

    private fun onUploadSuccess(message: String) {
        showLoading(false)
        showToast(message)
        setResult(Activity.RESULT_OK)
        finish()
    }

    private fun onUploadError(error: String) {
        showLoading(false)
        showToast(error)
    }

    private fun showLoading(isLoading: Boolean) {
        binding.loadingView.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    companion object {
        private const val REQUIRED_PERMISSION = Manifest.permission.CAMERA
    }
}
