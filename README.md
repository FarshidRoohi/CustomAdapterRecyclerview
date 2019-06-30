# Custom Adapter RecyclerView

[ ![Download](https://api.bintray.com/packages/farshidroohi/CustomAdapterRecyclerview/CustomAdapterRecyclerview/images/download.svg?version=0.2) ](https://bintray.com/farshidroohi/CustomAdapterRecyclerview/CustomAdapterRecyclerview/0.2/link)
  ![API](https://img.shields.io/badge/API-18%2B-blue.svg?style=flat)


Very simple use android recyclerView adapter and endlessScrolled in android support library recyclerView 

- =Clean uses
- Ability add custom layout in progress pagination and default layout
- Support linear, Grid, StaggeredGrid LayoutManger for endless and show layout progressView

##### screenShot: 
 
 <img src="https://raw.githubusercontent.com/FarshidRoohi/CustomAdapterRecyclerview/master/art/custom_adapter.gif" alt="screen show" width="270px" height="500px">


 ###### via gradle (Androidx):   
  
```Gradle  
  implementation 'androidx.recyclerview:recyclerview:1.0.0'
  implementation 'ir.farshid_roohi:customAdapterRecycleView:1.0.0'
 ```  
 <hr>

 ###### via gradle:   
  
```Gradle  
  implementation 'com.android.support:recyclerview-v7:28.0.0'
  implementation 'ir.farshid_roohi:customAdapterRecycleView:0.3'
 ```  
 <hr>
 
> - Your project should support **DataBinding** 

# How to use DataBinding on your project

```Gradle
  
android {  
 dataBinding {  
	  enabled = true  
	  }
	...
  defaultConfig {  
	...
  }  
  buildTypes {  
  ...
 }  
```
for more information [It](https://developer.android.com/topic/libraries/data-binding)


## Example

#### Sample Adapter 

```Java

public class MyAdapter extends AdapterRecyclerView<String> {

    @Override
    public int getItemLayout(int viewType) {
        return R.layout.my_item;
    }

    // optional method override layout progress custom
    @Override
    public int onProgressLayout() {
        return R.layout.my_custom_progress_item;
    }

    @Override
    public void onBindView(ViewDataBinding viewDataBinding, int position, int viewType, String element) {
        MyItemBinding itemBinding = (MyItemBinding) viewDataBinding;
        itemBinding.txtTitle.setText(element);
    }
}
```

```Java
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        
        final MyAdapter adapter = new MyAdapter();
        adapter.endLessScrolled(recyclerView);
        adapter.addItems(getTempItems());
        recyclerView.setAdapter(adapter);


        // handled click item in recyclerView
        adapter.setOnClickItemListener(recyclerView, new OnClickItemListener<String>() {
            @Override
            public void onClickItem(int position, String element) {
                Toast.makeText(getApplicationContext(), "item click : " + element, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onLongClickItem(int position, String element) {
                Toast.makeText(getApplicationContext(), "item long click : " + element, Toast.LENGTH_SHORT).show();

            }
        });


        // handled endless recyclerView
        adapter.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                adapter.showLoading();

                // request or load other items...
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        adapter.addItems(getTempItems());
                    }
                }, 2500);
            }
        });
        
        
        
 // get temp list item
    public List<String> getTempItems() {
        List<String> items = new ArrayList<>();

        for (int i = 0; i <= 15; i++) {
            items.add("item ");
        }

        return items;
    }
```