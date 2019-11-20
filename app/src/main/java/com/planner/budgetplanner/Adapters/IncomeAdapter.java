package com.planner.budgetplanner.Adapters;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.planner.budgetplanner.Model.Income;
import com.planner.budgetplanner.R;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class IncomeAdapter extends MyItemAdapter<Income> {

    public IncomeAdapter(ArrayList<Income> list, IItemListner listner) {
        super(list, listner);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.income_item, viewGroup, false);

        return new IncomeAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyItemAdapter.ViewHolder viewHolder, int i) {
        super.onBindViewHolder(viewHolder, i);
    }

    public class ViewHolder extends MyItemAdapter.ViewHolder {

        private TextView titleTxt;
        private TextView profTxtImg;
        private TextView amountTxt;
        private TextView dateTxt;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTxt = itemView.findViewById(R.id.incomeItemTitle);
            profTxtImg = itemView.findViewById(R.id.incomeItemProf);
            amountTxt = itemView.findViewById(R.id.incomeItemAmount);
            dateTxt = itemView.findViewById(R.id.incomeItemDate);
        }

        @Override
        public void bindData(Object o) {
            Income income = (Income) o;

            titleTxt.setText(income.getTitle());
            profTxtImg.setText(income.getTitle().length() > 0 ? income.getTitle().substring(0, 1).toUpperCase() : "");
            amountTxt.setText(income.getAmount() + "rs");
            dateTxt.setText(income.getDate() + " " + income.getTime());
        }
    }
}