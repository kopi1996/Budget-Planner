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

public class CategoryAdapter extends MyItemAdapter<Category> {


    public CategoryAdapter(ArrayList<Category> list, MyItemAdapter.IItemListner listner) {
        super(list, listner);
    }

    @NonNull
    @Override
    public CategoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.category_item, viewGroup, false);

        return new CategoryAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyItemAdapter.ViewHolder viewHolder, int i) {
        super.onBindViewHolder(viewHolder, i);
    }

    public class ViewHolder extends MyItemAdapter.ViewHolder {

        private TextView titleTxt;
        private TextView spentTxt;
        private TextView budgetTxt;
        private TextView remainTxt;
        private ProgressBar progressBar;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTxt = itemView.findViewById(R.id.cateTitle);
            spentTxt = itemView.findViewById(R.id.cateSpentTxt);
            budgetTxt = itemView.findViewById(R.id.cateBudgetTxt);
            remainTxt = itemView.findViewById(R.id.cateRemainTxt);
            progressBar = itemView.findViewById(R.id.cateProgressBar);
        }

        @Override
        public void bindData(Object o) {
            Category category = (Category) o;
            titleTxt.setText(category.getTitle());
            spentTxt.setText(category.getSpent() + "rs");
            budgetTxt.setText(category.getBudget() + "rs");
            remainTxt.setText(category.getRemaining() + "rs");

            int per = (int) ((category.getSpent() / category.getBudget()) * 100);

            progressBar.setProgress(per);
        }
    }
}
