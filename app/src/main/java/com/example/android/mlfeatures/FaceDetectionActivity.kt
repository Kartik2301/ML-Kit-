package com.example.android.mlfeatures

import android.content.Intent
import android.graphics.*
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import com.google.firebase.ml.vision.FirebaseVision
import com.google.firebase.ml.vision.common.FirebaseVisionImage
import com.google.firebase.ml.vision.face.FirebaseVisionFace
import com.google.firebase.ml.vision.face.FirebaseVisionFaceDetectorOptions
import com.google.firebase.ml.vision.face.FirebaseVisionFaceLandmark
import kotlinx.android.synthetic.main.activity_face_detection.*
import java.io.IOException

class FaceDetectionActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_face_detection)
        SelectButton.setOnClickListener {
            val galleryIntent = Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
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
                } catch (e : IOException){
                    e.printStackTrace()
                }
            }
        }
    }
    private fun analyzeImage(image: Bitmap?){
        if(image!=null){
            imageView.setImageBitmap(null)
            val firebaseVisionImage = FirebaseVisionImage.fromBitmap(image)
            val options = FirebaseVisionFaceDetectorOptions.Builder()
                .setPerformanceMode(FirebaseVisionFaceDetectorOptions.ACCURATE)
                .setLandmarkMode(FirebaseVisionFaceDetectorOptions.ALL_LANDMARKS)
                .setClassificationMode(FirebaseVisionFaceDetectorOptions.ALL_CLASSIFICATIONS)
                .build()
            val faceDetector = FirebaseVision.getInstance().getVisionFaceDetector(options)
            faceDetector.detectInImage(firebaseVisionImage)
                .addOnSuccessListener{
                    val mutableImage = image.copy(Bitmap.Config.ARGB_8888,true)
                    detectFaces(it,mutableImage)
                    imageView.setImageBitmap(mutableImage)
                }
                .addOnFailureListener{

                }
        }
    }
    private fun detectFaces(faces: List<FirebaseVisionFace>?, image: Bitmap?){
        val canvas = Canvas(image!!)
        val facePaint = Paint()
        facePaint.color = Color.RED
        facePaint.style = Paint.Style.STROKE
        facePaint.strokeWidth = 8F
        val faceTextPaint = Paint()
        faceTextPaint.color = Color.RED
        faceTextPaint.textSize = 40F
        faceTextPaint.typeface = Typeface.DEFAULT_BOLD
        val landmarkPaint = Paint()
        landmarkPaint.color = Color.RED
        landmarkPaint.style = Paint.Style.FILL
        landmarkPaint.strokeWidth = 8F
        for(face in faces!!){
            canvas.drawRect(face.boundingBox,facePaint)
            if(face.getLandmark(FirebaseVisionFaceLandmark.LEFT_EYE)!=null){
                val leftEye = face.getLandmark(FirebaseVisionFaceLandmark.LEFT_EYE)!!
                canvas.drawCircle(leftEye.position.x,leftEye.position.y,8F,landmarkPaint)
            }
            if(face.getLandmark(FirebaseVisionFaceLandmark.RIGHT_EYE)!=null){
                val rightEye = face.getLandmark(FirebaseVisionFaceLandmark.RIGHT_EYE)!!
                canvas.drawCircle(rightEye.position.x,rightEye.position.y,8F,landmarkPaint)
            }
            if(face.getLandmark(FirebaseVisionFaceLandmark.NOSE_BASE)!=null){
                val noseBase = face.getLandmark(FirebaseVisionFaceLandmark.NOSE_BASE)!!
                canvas.drawCircle(noseBase.position.x,noseBase.position.y,8F,landmarkPaint)
            }
            if(face.getLandmark(FirebaseVisionFaceLandmark.LEFT_EAR)!=null){
                val leftEar = face.getLandmark(FirebaseVisionFaceLandmark.LEFT_EAR)!!
                canvas.drawCircle(leftEar.position.x,leftEar.position.y,8F,landmarkPaint)
            }
            if(face.getLandmark(FirebaseVisionFaceLandmark.RIGHT_EAR)!=null){
                val rightEar = face.getLandmark(FirebaseVisionFaceLandmark.RIGHT_EAR)!!
                canvas.drawCircle(rightEar.position.x,rightEar.position.y,8F,landmarkPaint)
            }
            if(face.getLandmark(FirebaseVisionFaceLandmark.MOUTH_LEFT)!=null&&face.getLandmark(FirebaseVisionFaceLandmark.MOUTH_BOTTOM)!=null
                &&face.getLandmark(FirebaseVisionFaceLandmark.MOUTH_RIGHT)!=null){
                val leftMouth = face.getLandmark(FirebaseVisionFaceLandmark.MOUTH_LEFT)!!
                val rightMouth = face.getLandmark(FirebaseVisionFaceLandmark.MOUTH_RIGHT)!!
                val bottomMouth = face.getLandmark(FirebaseVisionFaceLandmark.MOUTH_BOTTOM)!!
                canvas.drawLine(leftMouth.position.x,leftMouth.position.y,bottomMouth.position.x,bottomMouth.position.y,landmarkPaint)
                canvas.drawLine(bottomMouth.position.x,bottomMouth.position.y,rightMouth.position.x,rightMouth.position.y,landmarkPaint)
            }

            val smilingProbability = face.smilingProbability
            smile.text = "The probability of smiling is: "+ smilingProbability
            val left_eye_open_probability = face.leftEyeOpenProbability
            lefteye.text = "The probability of left eye being open is: "+ left_eye_open_probability
            val right_eye_open_probability = face.rightEyeOpenProbability
            righteye.text = "The probability of right eye being open is: "+ right_eye_open_probability
        }
    }
}
