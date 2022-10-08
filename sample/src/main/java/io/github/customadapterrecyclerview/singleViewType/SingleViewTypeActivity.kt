package io.github.customadapterrecyclerview.singleViewType

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import io.github.farshidroohi.extensions.onItemClickListener
import io.github.farshidroohi.extensions.onLoadMoreListener
import ir.farshid_roohi.customadapterrecyclerview.R
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class SingleViewTypeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_single_view_type)

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)

        val singleViewTypeAdapter = SingleViewTypeAdapter()

        // onClick Error Button
        singleViewTypeAdapter.onRetryClicked = {
            singleViewTypeAdapter.loadingState()
            // fake request or load other items...
            recyclerView.postDelayed({
                singleViewTypeAdapter.loadedState(tempItems)
            }, 2000)
        }

        // set Data in List
        singleViewTypeAdapter.loadedState(tempItems)

        recyclerView.layoutManager = GridLayoutManager(this@SingleViewTypeActivity, 2)
        recyclerView.adapter = singleViewTypeAdapter


        // set onLoad More Listener for Pagination
        recyclerView.onLoadMoreListener {
            if (singleViewTypeAdapter.mustLoad) {
                singleViewTypeAdapter.loadingState()
                // failed state
                recyclerView.postDelayed({
                    singleViewTypeAdapter.failedState()
                }, 2000)
            }
        }


        // Set onClick Item Listener
        recyclerView.onItemClickListener(onClickItem = { position ->
            showToast("onClickItem :  ${singleViewTypeAdapter.getItem(position)}$position")
        }, onLongClickItem = { position ->
            showToast("onLongClickItem :  ${singleViewTypeAdapter.getItem(position)}$position")
        })
    }

    fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    // Generate Fake Data For List
    private val tempItems: List<String>
        get() {
            val items: MutableList<String> = ArrayList()
            for (i in 0..21) {
                items.add("item ")
            }
            return items
        }
}