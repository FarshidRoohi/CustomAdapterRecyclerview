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

    private lateinit var myAdapter: MyAdapter
    private val tempItems: List<String>
        get() {
            val items: MutableList<String> = ArrayList()
            for (i in 0..21) {
                items.add("item ")
            }
            return items
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        myAdapter = MyAdapter().also { adapter ->
            adapter.onRetryClicked = {
                adapter.loadingState()
                // fake request or load other items...
                recyclerView.postDelayed({
                    adapter.loadedState(tempItems)
                }, 2000)
            }
        }
        myAdapter.loadedState(tempItems)

        initRecyclerView()

    }

    private fun initRecyclerView() {

        recyclerView.apply {
            layoutManager = GridLayoutManager(this@MainActivity, 2)
            adapter = myAdapter

            // handled endless recyclerView
            onLoadMoreListener {
                if (myAdapter.mustLoad) {
                    myAdapter.loadingState()
                    // failed state
                    recyclerView.postDelayed({
                        myAdapter.failedState()
                    }, 2000)
                }
            }

            // handled click item in recyclerView
            onItemClickListener({ position ->
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
    }

}