package ir.farshid_roohi.customadapterrecyclerview;

import android.databinding.ViewDataBinding;

import ir.farshid_roohi.customadapterrecyclerview.databinding.MyItemBinding;
import ir.farshid_roohi.customadapterrecycleview.AdapterRecyclerView;

/**
 * Created by Farshid Roohi.
 * CustomAdapterRecyclerView | Copyrights 1/2/19.
 */
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
