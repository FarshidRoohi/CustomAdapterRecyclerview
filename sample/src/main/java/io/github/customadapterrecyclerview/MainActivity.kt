package io.github.customadapterrecyclerview

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import io.github.customadapterrecyclerview.multiViewType.MultiViewTypeActivity
import io.github.customadapterrecyclerview.singleViewType.SingleViewTypeActivity
import ir.farshid_roohi.customadapterrecyclerview.R

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<Button>(R.id.btn_single_view_type).setOnClickListener {
            startActivity(Intent(this, SingleViewTypeActivity::class.java))
        }
        findViewById<Button>(R.id.btn_multi_view_type).setOnClickListener {
            startActivity(Intent(this, MultiViewTypeActivity::class.java))
        }

    }
}