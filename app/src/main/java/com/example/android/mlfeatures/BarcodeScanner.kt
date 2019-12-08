package com.example.android.mlfeatures

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import com.google.firebase.ml.vision.FirebaseVision
import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcode
import com.google.firebase.ml.vision.common.FirebaseVisionImage
import kotlinx.android.synthetic.main.activity_barcode_scanner.*
import kotlinx.android.synthetic.main.activity_face_detection.SelectButton
import kotlinx.android.synthetic.main.activity_face_detection.imageView
import java.io.IOException

class BarcodeScanner : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_barcode_scanner)
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
                try {
                    val bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver,contentURI)
                    analyzeImage(bitmap)
                    imageView!!.setImageBitmap(bitmap)
                } catch (e: IOException){
                    e.printStackTrace()
                }
            }
        }
    }
    private fun analyzeImage(image: Bitmap?){
        if(image!=null){
            imageView.setImageBitmap(null)
            val firebaseVisionImage = FirebaseVisionImage.fromBitmap(image)
            val barcodeDetector = FirebaseVision.getInstance().visionBarcodeDetector
            barcodeDetector.detectInImage(firebaseVisionImage)
                .addOnSuccessListener{
                    val mutableImage = image.copy(Bitmap.Config.ARGB_8888,true)
                    scanBarcode(it,mutableImage)
                    imageView!!.setImageBitmap(mutableImage)
                }
                .addOnFailureListener{

                }
        }
    }
    private fun scanBarcode(barcodes: List<FirebaseVisionBarcode>?, image: Bitmap?){
        val canvas = Canvas(image!!)
        val rectPaint = Paint()
        rectPaint.color = Color.RED
        rectPaint.strokeWidth = 8F
        rectPaint.style = Paint.Style.STROKE
        for(barcode in barcodes!!){
            canvas.drawRect(barcode.boundingBox!!,rectPaint)
            val valueType = barcode.valueType
            when(valueType){
                FirebaseVisionBarcode.TYPE_WIFI -> {
                    val ssid = barcode.wifi!!.ssid
                    val password = barcode.wifi!!.password
                    val type = barcode.wifi!!.encryptionType
                    detailsText.text = ssid + "\n" + password + "\n" + type
                    detailsText.setTextColor(Color.BLACK)
                    detailsText.setClickable(false);
                }
                FirebaseVisionBarcode.TYPE_URL -> {
                    val title = barcode.url!!.title
                    val url = barcode.url!!.url
                    detailsText.text = title + "\n" + url
                    detailsText.setTextColor(Color.BLUE)
                    detailsText.setOnClickListener {
                        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                        startActivity(browserIntent)
                    }
                }
            }
        }
    }
}
