package com.planner.budgetplanner.Adapters;

import android.app.Activity;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.planner.budgetplanner.Model.Category;
import com.planner.budgetplanner.R;
import com.planner.budgetplanner.SwipeToDeleteCallBack;

import java.util.ArrayList;

import static android.support.constraint.Constraints.TAG;

public abstract class MyItemAdapter<T> extends RecyclerView.Adapter<MyItemAdapter.ViewHolder> {

    protected ArrayList<T> list;
    protected IItemListner listner;

    public void initialize(ArrayList<T> list,IItemListner listner)
    {
        this.list=list;
        this.listner=listner;
        notifyDataSetChanged();
    }

    public void setList(ArrayList<T> list)
    {
        this.list=list;
        notifyDataSetChanged();
    }

    public MyItemAdapter(ArrayList<T> list, IItemListner listner) {
        this.list = list;
        this.listner = listner;
    }

    public void removeItem(int position) {
        list.remove(position);
        notifyItemRemoved(position);
        Log.i(TAG, "removeItem: "+position);
        //notifyItemRangeRemoved(0, list.size());
    }

    public void restoreItem(T item, int position) {
        list.add(position, item);
        notifyItemInserted(position);
    }

    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        viewHolder.bindData(list.get(i));
    }


    public int getItemCount() {
        return list.size();
    }

    public ArrayList<T> getData() {
        return list;
    }

    public void enableSwipeToDeleteAndUndo(final View v, final RecyclerView recyclerView) {
        SwipeToDeleteCallBack swipeToDeleteCallback = new SwipeToDeleteCallBack(v.getContext()) {
            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {

                final int position = viewHolder.getAdapterPosition();
                final T item = getData().get(position);

                removeItem(position);

                Snackbar snackbar = Snackbar
                        .make(v, "Item was removed from the list.", Snackbar.LENGTH_LONG);
                snackbar.setAction("UNDO", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        restoreItem(item, position);
                        recyclerView.scrollToPosition(position);
                    }
                });

                snackbar.setActionTextColor(Color.YELLOW);
                snackbar.show();
            }
        };

        ItemTouchHelper itemTouchhelper = new ItemTouchHelper(swipeToDeleteCallback);
        itemTouchhelper.attachToRecyclerView(recyclerView);
    }


    public void enableSwipeToDeleteAndUndo(final View v, final RecyclerView recyclerView, final IItemSwipeListner<T> listner) {
        SwipeToDeleteCallBack swipeToDeleteCallback = new SwipeToDeleteCallBack(v.getContext()) {
            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {

                final int position = viewHolder.getAdapterPosition();
                final T item = getData().get(position);

                removeItem(position);
                listner.onRemove(item,position);
                Snackbar snackbar = Snackbar
                        .make(v, "Item was removed from the list.", Snackbar.LENGTH_LONG);
                snackbar.setAction("UNDO", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        restoreItem(item, position);
                        listner.onRestore(item,position);
                        recyclerView.scrollToPosition(position);
                    }
                });

                snackbar.setActionTextColor(Color.YELLOW);
                snackbar.show();
            }
        };

        ItemTouchHelper itemTouchhelper = new ItemTouchHelper(swipeToDeleteCallback);
        itemTouchhelper.attachToRecyclerView(recyclerView);
    }

    public abstract class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            listner.onClick(v,getAdapterPosition());
        }

        public abstract void bindData(T t);
    }

    public static interface IItemListner
    {
        void onClick(View v,int pos);
    }

    public static interface IItemSwipeListner<T>
    {
        void onRemove(T item,int pos);
        void onRestore(T item,int pos);
    }
}
