package com.planner.budgetplanner.Adapters;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.planner.budgetplanner.Model.BudgetObject;
import com.planner.budgetplanner.R;

import java.util.ArrayList;

public class BudgetObjectAdapter extends MyItemAdapter<BudgetObject> {

    public BudgetObjectAdapter(ArrayList<BudgetObject> list, IItemListner listner) {
        super(list, listner);
    }

    @NonNull
    @Override
    public MyItemAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_view_text_item, viewGroup, false);

        return new BudgetObjectAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyItemAdapter.ViewHolder viewHolder, int i) {
        super.onBindViewHolder(viewHolder,i);
    }

    public class ViewHolder extends MyItemAdapter.ViewHolder
    {
        private TextView titleTxt;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTxt=itemView.findViewById(R.id.listItemTxt);
            itemView.setOnClickListener(this);
        }

        @Override
        public void bindData(Object o) {
            BudgetObject budgetObject=(BudgetObject) o;
            titleTxt.setText(budgetObject.getTitle());
        }
    }

}
