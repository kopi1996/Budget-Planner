package com.planner.budgetplanner;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.planner.budgetplanner.Model.Category;
import com.planner.budgetplanner.Utility.MyUtility;

import java.util.ArrayList;

public class PieChartActivity extends CustomAppBarActivity implements OnChartValueSelectedListener {

    PieChart pieChart;
    PieData pieData;
    PieDataSet pieDataSet;
    ArrayList pieEntries;
    ArrayList PieEntryLabels;

    private TextView catNameLabel,catAmountLabel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pie_chart);
        initialize("Reports");
    }

    @Override
    protected void initialize(String title) {
        super.initialize(title);
        enableBackBtn();
        catNameLabel=findViewById(R.id.catChartName);
        catAmountLabel=findViewById(R.id.catChartAmount);
        pieChart = findViewById(R.id.pieChart);
        if(MyUtility.currentUser.getCategories().length==0) return;
        catNameLabel.setText(MyUtility.currentUser.getCategories()[0].getTitle()+" :");
        catAmountLabel.setText(MyUtility.currentUser.getCategories()[0].getSpent()+" "+MyUtility.currentUser.getCurrencyType());
        getEntries();
        pieDataSet = new PieDataSet(pieEntries, "");
        pieDataSet.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                String txt = MyUtility.wrapDecPointDouble(value) +" %";
                return txt;
            }
        });
        pieData = new PieData(pieDataSet);
        pieChart.setData(pieData);
        pieChart.getDescription().setText("Category");
        pieChart.setOnChartValueSelectedListener(this);
        pieDataSet.setColors(ColorTemplate.JOYFUL_COLORS);
        pieDataSet.setSliceSpace(2f);
        pieDataSet.setValueTextColor(Color.WHITE);
        pieDataSet.setValueTextSize(10f);
        pieDataSet.setSliceSpace(5f);
    }

    private void getEntries() {
        pieEntries = new ArrayList<>();
        Category[] categories = MyUtility.currentUser.getCategories();
        double sum=0;
        for (Category category : categories) {
            sum+=category.getSpent();
        }
        for (Category category : categories) {
            pieEntries.add(new PieEntry((float) ((category.getSpent()/sum)*100),category.getTitle()));
        }
    }

    private Category getCategoryWithName(String name)
    {
        Category category=null;

        Category[] categories = MyUtility.currentUser.getCategories();
        for (Category category1 : categories) {
            if(name.equals(category1.getTitle().trim()))
            {
                category=category1;
                break;
            }
        }

        return category;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onValueSelected(Entry e, Highlight h) {
        Category cat = getCategoryWithName(((PieEntry)e).getLabel().trim());
        if (cat != null) {
            catNameLabel.setText(cat.getTitle() + " :");
            catAmountLabel.setText(cat.getSpent() + " " + MyUtility.currentUser.getCurrencyType());
        }
    }

    @Override
    public void onNothingSelected() {

    }
}
