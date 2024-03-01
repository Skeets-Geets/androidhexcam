package com.the.androidhexcam

import android.os.Bundle
import android.view.View
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.CameraSelector
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import android.widget.ImageView

class MainActivity : AppCompatActivity() {

    private lateinit var cameraExecutor: ExecutorService
    private lateinit var viewFinder: PreviewView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewFinder = findViewById(R.id.viewFinder)
        cameraExecutor = Executors.newSingleThreadExecutor()

        val gifImageView = findViewById<ImageView>(R.id.gifImageView)
        Glide.with(this).load(R.drawable.hexcamlaunch).into(gifImageView)

        // Set up the fade out animation
        val fadeOut = AlphaAnimation(1f, 0f).apply {
            duration = 500 // Fade out duration
            startOffset = 2500 // Start fading out after 2.5 seconds
            setAnimationListener(object : Animation.AnimationListener {
                override fun onAnimationStart(animation: Animation?) {
                    // Animation started
                }

                override fun onAnimationEnd(animation: Animation?) {
                    gifImageView.visibility = View.GONE // Hide GIF
                    startCamera() // Start the camera after GIF fades out
                }

                override fun onAnimationRepeat(animation: Animation?) {
                    // Animation repeats
                }
            })
        }

        gifImageView.startAnimation(fadeOut)
    }

    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)
        cameraProviderFuture.addListener({
            // Used to bind the lifecycle of cameras to the lifecycle owner
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()

            // Preview
            val preview = Preview.Builder()
                .build()
                .also {
                    it.setSurfaceProvider(viewFinder.surfaceProvider)
                }

            // Select back camera as a default
            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

            try {
                // Unbind use cases before rebinding
                cameraProvider.unbindAll()

                // Bind use cases to camera
                cameraProvider.bindToLifecycle(
                    this, cameraSelector, preview)
            } catch(exc: Exception) {
                // Handle any errors (for example, camera not available)
            }
        }, ContextCompat.getMainExecutor(this))
    }

    override fun onDestroy() {
        super.onDestroy()
        cameraExecutor.shutdown()
    }
}
