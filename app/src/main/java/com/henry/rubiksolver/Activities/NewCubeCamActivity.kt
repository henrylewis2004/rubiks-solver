package com.henry.rubiksolver.Activities

import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.henry.rubiksolver.R
import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.media.Image
import android.net.Uri
import android.os.Build
import android.util.DisplayMetrics
import android.util.Log
import android.util.Size
import android.view.Surface
import android.view.View
import android.view.ViewTreeObserver
import android.view.WindowManager
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Toast
import androidx.annotation.OptIn
import androidx.camera.core.AspectRatio
import androidx.camera.core.CameraSelector
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.ImageProxy
import androidx.camera.core.Preview
import androidx.camera.core.UseCaseGroup
import androidx.camera.core.ViewPort
import androidx.camera.core.ViewPort.ScaleType
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.app.ActivityCompat
import com.google.common.util.concurrent.ListenableFuture
import java.io.File
import java.net.URI
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import com.henry.rubiksolver.faces

class NewCubeCamActivity : AppCompatActivity() {
    private lateinit var cameraProviderFuture: ListenableFuture<ProcessCameraProvider>
    private lateinit var cameraExecutor: ExecutorService
    private var imageCapture: ImageCapture? = null

    private lateinit var cubePreviewSquares: Array<ImageView>
    private lateinit var preview:Preview


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_new_cube_cam)

        cubePreviewSquares = arrayOf(
            findViewById<ImageView>(R.id.cubePreview00),
            findViewById<ImageView>(R.id.cubePreview01),
            findViewById<ImageView>(R.id.cubePreview02),
            findViewById<ImageView>(R.id.cubePreview10),
            findViewById<ImageView>(R.id.cubePreview11),
            findViewById<ImageView>(R.id.cubePreview12),
            findViewById<ImageView>(R.id.cubePreview20),
            findViewById<ImageView>(R.id.cubePreview21),
            findViewById<ImageView>(R.id.cubePreview22)
        )

        cameraExecutor = Executors.newSingleThreadExecutor()

        if(hasRequiredPermissions()){
            startCamera()
        }
        else{
            ActivityCompat.requestPermissions(this, CAMERAX_PERMISSIONS,0)
        }



    }

    private fun startCamera(): Unit{
        cameraProviderFuture = ProcessCameraProvider.getInstance(this)
        cameraProviderFuture.addListener({
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()

            val previewView = Preview.Builder().build()
            previewView.setSurfaceProvider(findViewById<PreviewView>(R.id.camViewFinder).surfaceProvider)
            preview = previewView


            imageCapture = ImageCapture.Builder().setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY).build()

            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

            try{
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(this, cameraSelector, imageCapture, previewView)

                findViewById<ImageButton>(R.id.camButton).setOnClickListener {

                    takePhoto() }
            }
            catch (e: Exception){
                Toast.makeText(this, "use case binding failed", Toast.LENGTH_SHORT).show()
            }

        }, ContextCompat.getMainExecutor(this))
    }

    private fun takePhoto(): Unit{
        val imageCapture = imageCapture ?: return
        findViewById<ImageButton>(R.id.camButton).isEnabled = false

        imageCapture.takePicture(
            ContextCompat.getMainExecutor(this),
            object : ImageCapture.OnImageCapturedCallback(){
                @OptIn(ExperimentalGetImage::class)
                override fun onCaptureSuccess(imageProxy: ImageProxy) {
                    try {
                        val image: Bitmap = imageProxy.toBitmap()
                        imageProxy.close()

                        val face: IntArray = faceAnalyse(image)
                        setResult(Activity.RESULT_OK, Intent().putExtra("cubeFaceResult", face))
                        finish()
                    }
                    catch (e: Exception){
                        Log.d("Errors", "camActivity: ${e.message.toString()}")
                        findViewById<ImageButton>(R.id.camButton).isEnabled = true
                    }


                }

                override fun onError(exception: ImageCaptureException) {
                    Log.d("Errors","camActivity: ${exception.message.toString()}")
                    Toast.makeText(baseContext,"Error taking photo: ${exception.message}", Toast.LENGTH_SHORT).show()
                }
            }

        )
    }




    private fun faceAnalyse(image: Bitmap): IntArray{ //analyse with cam
        var face = intArrayOf()
        val location = IntArray(2)

        val displayMetrics = resources.displayMetrics
        val screenPos: IntArray = intArrayOf(displayMetrics.widthPixels, displayMetrics.heightPixels)


        val centrePoint: IntArray = intArrayOf(screenPos[0]/2,screenPos[1]/2)
        val viewPos: IntArray = intArrayOf(centrePoint[0] - image.width/2, centrePoint[1] - image.height / 2)

        for (i in 0..8){
            try {
                cubePreviewSquares[i].getLocationOnScreen(location)
                location[0] = location[0] - viewPos[0]
                location[1] = location[1] - viewPos[1]

                val colour = image.getColor(location[0]+55, location[1]+55)
                face += rgbToColour(colour)

                Log.d("cubePreview", "x: ${location[0]} y: ${location[1]}, c: ${rgbToColour(colour)}")
            }
            catch (e: Exception){
                Log.d("cubePreview", "colour detection error: ${e.message}")
            }
        }
        var temp: Int = face[0] //need to rotate to match cube
        var order: IntArray = intArrayOf(0,6,8,2,1,3,7,5)


        for (i in 0..7) {
            if (i.equals(3) || i.equals(7)){
                face[order[i]] = temp
                temp = face[1]
            }
            else{face[order[i]] = face[order[i+1]]}
        }
        return face
    }

    private fun rgbToColour(colour: Color): Int{
        val hsv = FloatArray(3)
        Color.RGBToHSV((colour.red() * 10000).toInt(),(colour.green() * 10000).toInt(), (colour.blue() * 10000).toInt(), hsv)

        val hue: Float = hsv[0] //hue
        val sat: Float = hsv[1] //saturation
        val value: Float = hsv[2] //colour

        when{
            sat < 0.2f -> when{
                value > 0.6f -> return faces.WHITE.ordinal
                else -> return faces.RED.ordinal
            }
            else -> when {
                hue < 15 -> return faces.RED.ordinal
                hue < 45 -> return faces.ORANGE.ordinal
                hue < 70 -> return faces.YELLOW.ordinal
                hue < 160 -> return faces.GREEN.ordinal
                hue < 260 -> return faces.BLUE.ordinal
            }
        }
        return -1
    }


    private fun hasRequiredPermissions(): Boolean{
        return CAMERAX_PERMISSIONS.all {
            ContextCompat.checkSelfPermission(applicationContext,it) == PackageManager.PERMISSION_GRANTED
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        cameraExecutor.shutdown()
    }

    override fun onStop() {
        super.onStop()
        cameraExecutor.shutdown()
    }

    override fun onPause() {
        super.onPause()
        cameraExecutor.shutdown()
    }

    override fun onResume() {
        super.onResume()
        if (hasRequiredPermissions()){
            startCamera()
        }
        else{
            ActivityCompat.requestPermissions(this, CAMERAX_PERMISSIONS,0)
        }
    }


    companion object{
        private val CAMERAX_PERMISSIONS = arrayOf(
            Manifest.permission.CAMERA
        )
    }
}