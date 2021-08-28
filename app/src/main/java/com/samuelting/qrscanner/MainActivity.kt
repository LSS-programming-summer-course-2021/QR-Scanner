package com.samuelting.qrscanner

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.google.zxing.integration.android.IntentIntegrator
import java.util.*

val itemType = object : TypeToken<MutableList<Entry>>() {}.type

class MainActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var emptyView: TextView
    private lateinit var entryList: MutableList<Entry>
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedPreferences = getSharedPreferences("QRScanner", MODE_PRIVATE)
        setContentView(R.layout.activity_main)

        emptyView = findViewById(R.id.empty_view)
        recyclerView = findViewById(R.id.entryList)
        recyclerView.layoutManager = LinearLayoutManager(this)
        val gson = Gson()
        val json = sharedPreferences.getString("QRScanner", null)
        if (json == "" || json == null){
            Log.d("QRScanner", "Not found")
            entryList = mutableListOf()
        }else{
            Log.d("QRScanner", json )
            entryList = gson.fromJson(json, itemType)
        }
        recyclerView.adapter = EntryAdapter(this, entryList)
        if (entryList.count() > 0){
            emptyView.visibility = View.GONE
        }else{
            emptyView.visibility = View.VISIBLE
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return true
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.about){
            openAboutActivity();
        }
        if (item.itemId == R.id.scan){
            openQRScanner()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun openQRScanner(){
        var integrator = IntentIntegrator(this);
        //integrator.setDesiredBarcodeFormats(IntentIntegrator.ONE_D_CODE_TYPES);
        integrator.setPrompt("Scan a barcode");
        integrator.setCameraId(0);  // Use a specific camera of the device
        integrator.setBeepEnabled(false);
        integrator.setBarcodeImageEnabled(true);
        integrator.setOrientationLocked(false);
        integrator.initiateScan();
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        if (result != null) {
            if (result.contents == null) {
                Toast.makeText(this, "無效的QR code", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(this, result.contents, Toast.LENGTH_LONG).show()
                entryList.add(Entry(result.contents, Date()))
                if (entryList.count() > 0){
                    emptyView.visibility = View.GONE
                }else{
                    emptyView.visibility = View.VISIBLE
                }
            }
            saveScanResult()
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    private fun openAboutActivity(){
        var intent = Intent(this, AboutActivity::class.java)
        startActivity(intent);
    }

    private fun saveScanResult(){
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        val gson = Gson()
        val json: String = gson.toJson(entryList)
        editor.putString("QRScanner", json)
        editor.commit()
    }
}