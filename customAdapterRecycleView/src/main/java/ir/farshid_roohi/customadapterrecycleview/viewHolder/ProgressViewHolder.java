package ir.farshid_roohi.customadapterrecycleview.viewHolder;

import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;

/**
 * Created by Farshid Roohi.
 * CustomAdapterRecyclerView | Copyrights 1/1/19.
 */


public class ProgressViewHolder extends RecyclerView.ViewHolder {

    ViewDataBinding progressBinding;

    public ProgressViewHolder(ViewDataBinding viewDataBinding) {
        super(viewDataBinding.getRoot());
        progressBinding = viewDataBinding;
    }
}