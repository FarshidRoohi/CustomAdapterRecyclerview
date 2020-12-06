package ir.farshid_roohi.customadapterrecyclerview

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import ir.farshid_roohi.customadapterrecycleview.extensions.onItemClickListener
import ir.farshid_roohi.customadapterrecycleview.extensions.onLoadMoreListener
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val myAdapter = MyAdapter()

        // onClick Error Button
        myAdapter.onRetryClicked = {
            myAdapter.loadingState()
            // fake request or load other items...
            recyclerView.postDelayed({
                myAdapter.loadedState(tempItems)
            }, 2000)
        }

        // set Data in List
        myAdapter.loadedState(tempItems)

        recyclerView.layoutManager = GridLayoutManager(this@MainActivity, 2)
        recyclerView.adapter = myAdapter


        // set onLoad More Listener for Pagination
        recyclerView.onLoadMoreListener {
            if (myAdapter.mustLoad) {
                myAdapter.loadingState()
                // failed state
                recyclerView.postDelayed({
                    myAdapter.failedState()
                }, 2000)
            }
        }


        // Set onClick Item Listener
        recyclerView.onItemClickListener({ position ->
            if (position == myAdapter.itemCount - 1 && !myAdapter.mustLoad) {
                return@onItemClickListener
            }
            Toast.makeText(applicationContext, "item click :  ${myAdapter.getItem(position)}$position", Toast.LENGTH_SHORT).show()
        }, { position ->
            if (position == myAdapter.itemCount - 1 && !myAdapter.mustLoad) {
                return@onItemClickListener
            }
            Toast.makeText(applicationContext, "item long click :  ${myAdapter.getItem(position)}$position", Toast.LENGTH_SHORT).show()
        })
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