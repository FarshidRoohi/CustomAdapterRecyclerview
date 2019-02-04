package ir.farshid_roohi.customadapterrecycleview.viewHolder;

import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by Farshid Roohi.
 * CustomAdapterRecyclerView | Copyrights 1/1/19.
 */


public class ItemViewHolder extends RecyclerView.ViewHolder {
    public ViewDataBinding binding;

    public ItemViewHolder(ViewDataBinding viewDataBinding) {
        super(viewDataBinding.getRoot());
        this.binding = viewDataBinding;
    }
}