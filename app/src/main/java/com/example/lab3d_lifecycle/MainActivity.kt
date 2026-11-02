package com.example.lab3d_lifecycle

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.SavedStateHandle // Added import
import androidx.lifecycle.ViewModel

// D1. Save with SavedStateHandle. Change your ViewModel:
class CounterViewModel(private val state: SavedStateHandle) : ViewModel() {
    var count: Int
        get() = state["count"] ?: 0
        set(value) { state["count"] = value }

    init { Log.d("LC_VM", "ViewModel created") }
    override fun onCleared() { Log.d("LC_VM", "ViewModel destroyed") }
}

class MainActivity : LoggingActivity(){
    // Initialize ViewModel
    private val vm: CounterViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // D2. In onCreate, log the stored value:
        Log.d("LC_PREF", "saved=${getSharedPreferences("lab", MODE_PRIVATE).getInt("saved", -1)}")

        val tvCount= findViewById<TextView>(R.id.tvCount)
        val btnPlus= findViewById<Button>(R.id.btnPlus)
        val btnDialog= findViewById<Button>(R.id.btnDialog)
        val btnSecondScreen= findViewById<Button>(R.id.btnSecondScreen)
        val btnTransparentScreen= findViewById<Button>(R.id.btnTransparentScreen)
        val btnClose= findViewById<Button>(R.id.btnClose)

        // Set initial text from ViewModel
        tvCount.text = "Count: ${vm.count}"

        btnPlus.setOnClickListener {
            vm.count++
            tvCount.text="Count: ${vm.count}"
        }

        btnDialog.setOnClickListener {
            AlertDialog.Builder(this).setTitle("Dialog")
                .setMessage("See Logcat").setPositiveButton("OK", null).show()
        }

        btnSecondScreen.setOnClickListener {
            val intent = Intent(this, SecondActivity::class.java)
            startActivity(intent)
        }

        btnTransparentScreen.setOnClickListener {
            val intent = Intent(this, TransparentActivity::class.java)
            startActivity(intent)
        }

        btnClose.setOnClickListener {
            finish()
        }
    }

    // D2. Save to persistent storage. Add onStop() to MainActivity:
    override fun onStop() {
        super.onStop()
        getSharedPreferences("lab", MODE_PRIVATE).edit().putInt("saved", vm.count).apply()
    }
}