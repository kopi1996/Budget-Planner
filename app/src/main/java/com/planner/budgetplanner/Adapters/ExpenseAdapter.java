package com.planner.budgetplanner.Adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.planner.budgetplanner.Model.Expense;
import com.planner.budgetplanner.R;

import java.util.ArrayList;

public class ExpenseAdapter extends MyItemAdapter<Expense> {


    public ExpenseAdapter(ArrayList<Expense> list, MyItemAdapter.IItemListner listner) {
        super(list, listner);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.expense_item, viewGroup, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyItemAdapter.ViewHolder viewHolder, int i) {
        super.onBindViewHolder(viewHolder, i);
    }

    public class ViewHolder extends MyItemAdapter.ViewHolder
    {
        private TextView titleTxt;
        private TextView profileImg;
        private TextView amountTxt;
        private TextView dateTimeTxt;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTxt=itemView.findViewById(R.id.expItemTitle);
            profileImg=itemView.findViewById(R.id.expItemProf);
            amountTxt=itemView.findViewById(R.id.expItemAmount);
            dateTimeTxt=itemView.findViewById(R.id.expItemDate);
        }

        @Override
        public void bindData(Object o) {
            Expense expense=(Expense)o;
            titleTxt.setText(expense.getTitle());
            profileImg.setText(expense.getTitle().length()>0?expense.getTitle().substring(0,1).toUpperCase():"");
            amountTxt.setText(expense.getAmount()+"rs");
            dateTimeTxt.setText(expense.getTimestamp().toString());
        }
    }
}
