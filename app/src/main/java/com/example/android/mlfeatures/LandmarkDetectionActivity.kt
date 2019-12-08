package com.example.android.mlfeatures

import android.content.Intent
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import com.google.firebase.ml.vision.FirebaseVision
import com.google.firebase.ml.vision.cloud.landmark.FirebaseVisionCloudLandmark
import com.google.firebase.ml.vision.common.FirebaseVisionImage
import kotlinx.android.synthetic.main.activity_barcode_scanner.*
import java.io.IOException

class LandmarkDetectionActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_landmark_detection)
        SelectButton.setOnClickListener {
            val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(galleryIntent,100)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == 100){
            if(data!=null){
                val contentURI = data!!.data
                try{
                    val bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, contentURI)
                    analyzeImage(bitmap)
                    imageView!!.setImageBitmap(bitmap)
                } catch (e: IOException){
                    e.printStackTrace()
                }
            }
        }
    }
    private fun analyzeImage(image : Bitmap?){
        if(image!=null){
            imageView.setImageBitmap(null)
            val firebaseVisionImage = FirebaseVisionImage.fromBitmap(image)
            val landmarkDetector = FirebaseVision.getInstance().visionCloudLandmarkDetector
            landmarkDetector.detectInImage(firebaseVisionImage)
                .addOnSuccessListener{
                    val mutableImage = image.copy(Bitmap.Config.ARGB_8888,true)
                    detectLandmarks(it,mutableImage)
                    imageView!!.setImageBitmap(mutableImage)
                }
        }
    }
    private fun detectLandmarks(landmarks: List<FirebaseVisionCloudLandmark>, image: Bitmap?){
        for(landmark in landmarks!!){
            detailsText.setText(landmark.landmark+"\t"+landmark.confidence)
        }
    }
}
