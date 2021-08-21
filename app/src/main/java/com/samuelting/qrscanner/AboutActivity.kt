package com.samuelting.qrscanner

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity


class AboutActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about)

        findViewById<TextView>(R.id.telephone).setOnClickListener {
            openPhoneCallScreen()
        }

        findViewById<TextView>(R.id.email).setOnClickListener {
            openEmailClient()
        }
    }

    private fun openPhoneCallScreen(){
        val phoneIntent = Intent(Intent.ACTION_DIAL)
        phoneIntent.data = Uri.parse("tel:27802291");
        try {
            startActivity(phoneIntent)
            finish()
        } catch (ex: ActivityNotFoundException) {
            Toast.makeText(
                this,
                "Call failed, please try again later.", Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun openEmailClient(){
        val emailIntent = Intent(Intent.ACTION_SENDTO)
        emailIntent.data = Uri.parse("mailto:");
        emailIntent.putExtra(Intent.EXTRA_EMAIL, "samueltingko@gmail.com");
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Hello! I have a few questions to ask");
        try {
            startActivity(emailIntent)
            finish()
        } catch (ex: ActivityNotFoundException) {
            Toast.makeText(
                    this,
                    "Call failed, please try again later.", Toast.LENGTH_SHORT
            ).show()
        }
    }
}