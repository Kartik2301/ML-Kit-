package com.example.android.mlfeatures

import android.content.Intent
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.ListView
import com.google.firebase.ml.vision.FirebaseVision
import com.google.firebase.ml.vision.common.FirebaseVisionImage
import com.google.firebase.ml.vision.label.FirebaseVisionImageLabel
import com.google.firebase.ml.vision.label.FirebaseVisionOnDeviceImageLabelerOptions
import kotlinx.android.synthetic.main.activity_image_labelling.*
import java.io.IOException
import java.util.Collections

class ImageLabelling : AppCompatActivity() {
    lateinit  var  listView: ListView
    var LabelList: ArrayList<ImageLabellingModel> = ArrayList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image_labelling)
        listView = findViewById(R.id.forLabels) as ListView
        SelectButton.setOnClickListener {
            LabelList.clear()
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
                    imageView.setImageBitmap(bitmap)
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
            val options = FirebaseVisionOnDeviceImageLabelerOptions.Builder()
                .setConfidenceThreshold(0.7F)
                .build()
            val labelDetector = FirebaseVision.getInstance().getOnDeviceImageLabeler(options)
            labelDetector.processImage(firebaseVisionImage)
                .addOnSuccessListener{
                    val mutableImage = image.copy(Bitmap.Config.ARGB_8888,true)
                    labelImage(it,mutableImage)
                    imageView.setImageBitmap(mutableImage)
                }
                .addOnFailureListener{}
        }
    }
    private fun labelImage(labels: List<FirebaseVisionImageLabel>?, image: Bitmap?){
        for(label in labels!!){
            LabelList.add(ImageLabellingModel(label.text,label.confidence))
        }
        val myAdapter = ImageLabelingAdapter(this, R.layout.list_item_labelling, LabelList)
        listView.setAdapter(myAdapter)
    }

    override fun onStart() {
        super.onStart()
    }
}
