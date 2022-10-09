# Custom Adapter RecyclerView

  ![API](https://img.shields.io/badge/API-14%2B-blue.svg?style=flat) [![Android Arsenal](https://img.shields.io/badge/Android%20Arsenal-Custom%20Adapter%20RecyclerView-blue.svg?style=flat)](https://android-arsenal.com/details/1/7759)



Very simple use android recyclerView adapter and endlessScrolled in android support library recyclerView 

- Manage Loading and Error Layout in The List Row
- Ability add custom layout in progress pagination and default layout
- Support linear, Grid, StaggeredGrid LayoutManger for endless
- Clean to uses

##### screenShot: 
 
 <img src="https://raw.githubusercontent.com/FarshidRoohi/CustomAdapterRecyclerview/master/art/custom_adapter.gif" alt="screen show" width="270px" height="450px">

[Recyclerview version](https://developer.android.com/jetpack/androidx/releases/)

 ###### gradle :   
```Gradle  
  implementation 'androidx.recyclerview:recyclerview:$VERSION'
  implementation 'io.github.farshidroohi:customAdapterRecycleView:2.1.2'
 ```  
 <hr>
 
## Example

#### Sample Adapter 

```Kotlin

class MyAdapter : AdapterRecyclerView<String?>(R.layout.my_item, R.layout.progress_view, R.layout.item_error,R.id.btnTrayAgain) {

    override fun onBindView(viewHolder: ItemViewHolder, position: Int, context: Context, element: String?) {
        val binding = MyItemBinding.bind(viewHolder.itemView)
        binding.myTitle.text = element
    }

}
```
[View Binding:](https://developer.android.com/topic/libraries/view-binding)

```Gradle
android {
...

  buildFeatures {
        viewBinding = true
   }
   
}

```

Sample Single View Type Adapter [Code](https://github.com/FarshidRoohi/CustomAdapterRecyclerview/tree/master/sample/src/main/java/io/github/customadapterrecyclerview/singleViewType):

```Kotlin

class MyAdapter : AdapterRecyclerView<String?>(
    R.layout.my_item,
    R.layout.progress_view,
    R.layout.item_error,
    R.id.btnTrayAgain
) {

    override fun onBindView(
        viewHolder: RecyclerView.ViewHolder,
        position: Int,
        context: Context,
        element: String?
    ) {
        val binding = MyItemBinding.bind(viewHolder.itemView)
        binding.txtTitle.text = element
    }

}

```



Sample Use Adapter:

```Kotlin

    val myAdapter = MyAdapter()
    
    myAdapter.onRetryClicked = {
         // onClick Error Button
     }
     
     
    recyclerView.onLoadMoreListener {
           // set onLoad More Listener for Pagination
     }
       
    recyclerView.onItemClickListener(
           onClickItem = { position ->
                // OnClick Item
        }, onLongClickItem = { position ->
               // OnLong Click Item
     })

```


Multi View Type Sample: [Code](https://github.com/FarshidRoohi/CustomAdapterRecyclerview/tree/master/sample/src/main/java/io/github/customadapterrecyclerview/multiViewType)
