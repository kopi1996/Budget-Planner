package com.planner.budgetplanner.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.planner.budgetplanner.Model.Category;
import com.planner.budgetplanner.R;

import java.util.ArrayList;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {

    private ArrayList<Category> list;
    private final IItemListner listner;

    public CategoryAdapter(ArrayList<Category> list,IItemListner listner) {
        this.list = list;
        this.listner=listner;
    }

    @NonNull
    @Override
    public CategoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.category_item, viewGroup, false);

        return new CategoryAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryAdapter.ViewHolder holder, int i) {
        holder.bindData(list.get(i));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void removeItem(int position) {
        list.remove(position);
        notifyItemRemoved(position);
    }

    public void restoreItem(Category item, int position) {
        list.add(position, item);
        notifyItemInserted(position);
    }

    public ArrayList<Category> getData() {
        return list;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView titleTxt;
        private TextView spentTxt;
        private TextView budgetTxt;
        private TextView remainTxt;
        private ProgressBar progressBar;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            titleTxt = itemView.findViewById(R.id.cateTitle);
            spentTxt = itemView.findViewById(R.id.cateSpentTxt);
            budgetTxt = itemView.findViewById(R.id.cateBudgetTxt);
            remainTxt = itemView.findViewById(R.id.cateRemainTxt);
            progressBar = itemView.findViewById(R.id.cateProgressBar);
        }


        @Override
        public void onClick(View v) {
            listner.onItemClick(v, getAdapterPosition());
        }

        public void bindData(Category category) {
            titleTxt.setText(category.getTitle());
            spentTxt.setText(category.getSpent() + "rs");
            budgetTxt.setText(category.getBudget() + "rs");
            remainTxt.setText(category.getRemaining() + "rs");

            int per = (int) ((category.getSpent() / category.getBudget()) * 100);

            progressBar.setProgress(per);
        }
    }


    public interface IItemListner
    {
        void onItemClick(View view,int pos);
    }
}
