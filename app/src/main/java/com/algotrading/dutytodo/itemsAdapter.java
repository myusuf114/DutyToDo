package com.algotrading.dutytodo;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

// Responsible displaying data from the model into a row in the recycler view
public class itemsAdapter extends RecyclerView.Adapter<itemsAdapter.ViewHolder>{

    public interface onClickListener{
        void onItemClicked(int i);

    }
    public interface OnLongClickListener{
        void onItemLongClicked(int i);
    }

    List<String> items;
    OnLongClickListener longClickListener;
    onClickListener clickListener;

    public itemsAdapter(List<String> items, OnLongClickListener longClickListener, onClickListener clickListener) {
        this.items = items;
        this.longClickListener = longClickListener;
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        // use layout inflator to inflate a view

        View todoView = LayoutInflater.from(viewGroup.getContext()).inflate((android.R.layout.simple_list_item_1), viewGroup, false);
        // wrap it inside a viewHolder and return it
        return new ViewHolder(todoView);
    }

    // Responsible for binding data to particular view holder
    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        // grab the item at i
        String item = items.get(i);
        // bind the item into the specified view holder
        viewHolder.bind(item);

    }

    // Tells the RV how many items in a list
    @Override
    public int getItemCount() {
        return items.size();
    }

    // Container to provide easy access  to views  that represent each row of the list
    class ViewHolder extends RecyclerView.ViewHolder{

        TextView tvItem;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvItem = itemView.findViewById(android.R.id.text1);
        }
        // Update the view inside the view holder with the data
        public void bind(String item) {
            tvItem.setText(item);
            tvItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    clickListener.onItemClicked(getAdapterPosition());

                }

            });
            tvItem.setOnLongClickListener(new View.OnLongClickListener(){
                @Override
                public boolean onLongClick(View view) {
                    // notify the listen which position was long pressed
                    longClickListener.onItemLongClicked(getAdapterPosition());
                    return true;
                }
            });
        }

    }
}
