package com.example.android.mlfeatures

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import com.google.firebase.ml.vision.FirebaseVision
import com.google.firebase.ml.vision.common.FirebaseVisionImage
import com.google.firebase.ml.vision.text.FirebaseVisionText
import kotlinx.android.synthetic.main.activity_text_recognization.*
import java.io.IOException


class TextRecognizationActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_text_recognization)
        SelectButton.setOnClickListener{
            val galleryIntent = Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI)

            startActivityForResult(galleryIntent, 100)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 100)
        {
            if (data != null)
            {
                val contentURI = data!!.data
                try
                {
                    val bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, contentURI)
                    analyzeImage(bitmap)
                    imageView!!.setImageBitmap(bitmap)

                }
                catch (e: IOException) {
                    e.printStackTrace()
                }

            }

        }
    }
    private fun analyzeImage(image: Bitmap){
        if(image!=null) {
            imageView.setImageBitmap(null)
            val firebaseVisionImage = FirebaseVisionImage.fromBitmap(image)
            val textRecognizer = FirebaseVision.getInstance().onDeviceTextRecognizer
            textRecognizer.processImage(firebaseVisionImage)
                .addOnSuccessListener{
                    val mutableImage = image.copy(Bitmap.Config.ARGB_8888, true)
                    recognizeText(it,mutableImage)
                    imageView.setImageBitmap(mutableImage)
                }

                .addOnFailureListener{  }
        }
    }
    private fun recognizeText(result: FirebaseVisionText?, image: Bitmap?){
        val canvas = Canvas(image!!)
        val rectPaint = Paint()
        rectPaint.color = Color.RED
        rectPaint.style = Paint.Style.STROKE
        rectPaint.strokeWidth = 4F
        val textPaint = Paint()
        textPaint.color = Color.RED
        textPaint.textSize = 40F
        var index = 0

        if (result != null) {
            for (block in result.textBlocks) {
                for (line in block.lines) {
                    canvas.drawRect(line.boundingBox!!, rectPaint)
                    canvas.drawText(index.toString(), line.cornerPoints!![2].x.toFloat(), line.cornerPoints!![2].y.toFloat(), textPaint)
                }
            }
        }
    }

}
