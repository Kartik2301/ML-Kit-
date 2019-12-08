package com.example.android.mlfeatures

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.ml.naturallanguage.FirebaseNaturalLanguage
import com.google.firebase.ml.naturallanguage.languageid.FirebaseLanguageIdentificationOptions
import com.google.firebase.ml.naturallanguage.languageid.IdentifiedLanguage
import com.google.firebase.ml.vision.FirebaseVision
import kotlinx.android.synthetic.main.activity_language_detection.*

class LanguageDetectionActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_language_detection)
        SelectButton.setOnClickListener {
            val text = edit_text.text.toString()
            detectLanguage(text)
        }
    }
    private fun detectLanguage(text: String?){
        val options = FirebaseLanguageIdentificationOptions.Builder()
            .setConfidenceThreshold(0.2F)
            .build()
        val languageDetector = FirebaseNaturalLanguage.getInstance().getLanguageIdentification(options)
        languageDetector.identifyLanguage(text!!)
            .addOnSuccessListener{
                detailsText1.setText("Language is : $it")
            }
            .addOnFailureListener{

            }
        languageDetector.identifyPossibleLanguages(text!!)
            .addOnSuccessListener{
                printPossibleLanguages(it)
            }
            .addOnFailureListener{

            }
    }
    private fun printPossibleLanguages(languages: List<IdentifiedLanguage>){
         var result = "Possible Languages: "
         var index = 0
        for(language in languages){
            if(index ==0){
                result = result + language.languageCode
            }
            else {
                result = result + "," + language.languageCode
            }
            index++
        }
        detailsText2.setText(result)
    }
}
