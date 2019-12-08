package com.example.android.mlfeatures

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        textRecognize.setOnClickListener {
            val intent = Intent(this@MainActivity,TextRecognizationActivity::class.java)
            startActivity(intent)
        }
        FaceDetection.setOnClickListener {
            val intent = Intent(this@MainActivity, FaceDetectionActivity::class.java)
            startActivity(intent)
        }
        BarcodeScan.setOnClickListener {
            val intent = Intent(this@MainActivity, BarcodeScanner::class.java)
            startActivity(intent)
        }
        ImageLabel.setOnClickListener {
            val intent = Intent(this@MainActivity, ImageLabelling::class.java)
            startActivity(intent)
        }
        LandmarkDetection.setOnClickListener {
            val intent = Intent(this@MainActivity, LandmarkDetectionActivity::class.java)
            startActivity(intent)
        }
        LanguageDetection.setOnClickListener {
            val intent = Intent(this@MainActivity, LanguageDetectionActivity::class.java)
            startActivity(intent)
        }
        SmartReplies.setOnClickListener {
            val intent = Intent(this@MainActivity, SmartReply::class.java)
            startActivity(intent)
        }
    }
}
