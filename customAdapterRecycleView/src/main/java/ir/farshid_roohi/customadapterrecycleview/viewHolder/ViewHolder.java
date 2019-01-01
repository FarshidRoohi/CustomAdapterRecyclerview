package ir.farshid_roohi.customadapterrecycleview.viewHolder;

import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;

/**
 * Created by Farshid Roohi.
 * CustomAdapterRecyclerView | Copyrights 1/1/19.
 */


public class ViewHolder extends RecyclerView.ViewHolder {
    public ViewDataBinding binding;

    public ViewHolder(ViewDataBinding viewDataBinding) {
        super(viewDataBinding.getRoot());
        this.binding = viewDataBinding;
    }
}