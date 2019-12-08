package com.example.android.mlfeatures

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.google.firebase.ml.naturallanguage.FirebaseNaturalLanguage
import com.google.firebase.ml.naturallanguage.smartreply.FirebaseTextMessage
import com.google.firebase.ml.naturallanguage.smartreply.SmartReplySuggestionResult
import kotlinx.android.synthetic.main.activity_smart_reply.*
import java.util.ArrayList

class SmartReply : AppCompatActivity() {
    private val smartReplyConversation = ArrayList<SmartReplyModel>()
    private val firebaseSmartReplyConversation = ArrayList<FirebaseTextMessage>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_smart_reply)
        replies.setOnClickListener {
            val message = editText.text.toString().trim()
            generateSmartReplies(message)
        }
    }
    private fun generateSmartReplies(message: String?){
        editText.text.clear()
        smartReplyConversation.add(SmartReplyModel(true, message!!))
        firebaseSmartReplyConversation.add(FirebaseTextMessage.createForRemoteUser(message!!, System.currentTimeMillis(),"id_"))
        FirebaseNaturalLanguage.getInstance().smartReply.suggestReplies(firebaseSmartReplyConversation)
            .addOnSuccessListener { result ->
                if (result.status == SmartReplySuggestionResult.STATUS_SUCCESS && result.suggestions.size == 3) {
                    textView1.setText(result.suggestions[0].text.toString())
                    textView2.setText(result.suggestions[1].text.toString())
                    textView3.setText(result.suggestions[2].text.toString())
                }
            }
    }
}
