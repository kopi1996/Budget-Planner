package com.planner.budgetplanner;

import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.planner.budgetplanner.Model.BudgetObject;
import com.planner.budgetplanner.Utility.DateUtility;
import com.planner.budgetplanner.Utility.MyUtility;

import java.util.ArrayList;
import java.util.Date;

public class ChartActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private static final String TAG = "Chart";
    private BarChart incBarChart,expBarChart;
    private Spinner incTypeSpinner, incmSecondSpinner, incThirdSpinner;

    private int incSelectYear=0,incSelectMonth=-1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart);

        incBarChart=findViewById(R.id.incBarChart);
        expBarChart=findViewById(R.id.expBarChart);

        initSpinner();

        ArrayList entries = new ArrayList<>();
        entries.add(new BarEntry(0, 110.6f));
        entries.add(new BarEntry(1, 27));
        entries.add(new BarEntry(2, 38));
        entries.add(new BarEntry(3, 40));
        entries.add(new BarEntry(4, 59));
        entries.add(new BarEntry(5, 64));

        String months[]={"Jan","Feb","Mar","Apr","May","Jun"};

        drawBarChart(incBarChart,"Income",entries,months);
        drawBarChart(expBarChart,"Expense",entries,months);
    }

    private void initSpinner() {
        incTypeSpinner = findViewById(R.id.incTypeSelect);
        incmSecondSpinner = findViewById(R.id.incSelectTwo);
        incThirdSpinner = findViewById(R.id.incSelectThree);

        incTypeSpinner.setOnItemSelectedListener(this);

        ArrayList<String> typeList = new ArrayList<>();
        typeList.add("DailyInterval");
        typeList.add("MonthlyInterval");
        typeList.add("YearlyInterval");

        ArrayAdapter<String> typeAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_selectable_list_item, typeList);
        incTypeSpinner.setAdapter(typeAdapter);
        incTypeSpinner.setSelection(0);

        resetIncomeThirdSpinners(ChartType.DAILY);
        incSelectYear=minYear(MyUtility.currentUser.getIncomes());
        resetIncomeSecodSpinner(ChartType.DAILY);

    }

    private void resetIncomeThirdSpinners(ChartType type) {
        if (type == ChartType.DAILY) {
            int minYear = minYear(MyUtility.currentUser.getIncomes());
            int maxYear = maxYear(MyUtility.currentUser.getIncomes());

            ArrayList<String> yearList = new ArrayList<>();

            for (int i = minYear; i <= maxYear; i++) {
                yearList.add(Integer.toString(i));
            }

            ArrayAdapter<String> yearAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_selectable_list_item, yearList);
            incThirdSpinner.setAdapter(yearAdapter);
            if (yearList.size() > 0)
                incThirdSpinner.setSelection(0);
            incThirdSpinner.setVisibility(View.VISIBLE);
        } else
            incThirdSpinner.setVisibility(View.INVISIBLE);
    }

    private void resetIncomeSecodSpinner(ChartType type) {
        if (type == ChartType.MONTHLY) {
            int minYear = minYear(MyUtility.currentUser.getIncomes());
            int maxYear = maxYear(MyUtility.currentUser.getIncomes());

            ArrayList<String> yearList = new ArrayList<>();

            for (int i = minYear; i <= maxYear; i++) {
                yearList.add(Integer.toString(i));
            }

            ArrayAdapter<String> yearAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_selectable_list_item, yearList);
            incmSecondSpinner.setAdapter(yearAdapter);
            if (yearList.size() > 0)
                incmSecondSpinner.setSelection(0);
            incmSecondSpinner.setVisibility(View.VISIBLE);
        } else if (type == ChartType.DAILY) {
            int minMonth = minMonthForYear(MyUtility.currentUser.getIncomes(), incSelectYear);
            int maxMont = maxMonthFromYear(MyUtility.currentUser.getIncomes(), incSelectYear);

            ArrayList<String> yearList = new ArrayList<>();

            for (int i = minMonth; i <= maxMont; i++) {
                yearList.add(DateUtility.getSortNameForMonth(i));
            }

            ArrayAdapter<String> yearAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_selectable_list_item, yearList);
            incmSecondSpinner.setAdapter(yearAdapter);
            if (yearList.size() > 0)
                incmSecondSpinner.setSelection(0);
            incmSecondSpinner.setVisibility(View.VISIBLE);
        } else {
            incmSecondSpinner.setVisibility(View.INVISIBLE);
        }
    }

    private int minYear(BudgetObject[] objs)
    {
        if(objs.length==0)
            return -1;
        int minYear=DateUtility.getYearFromDate(objs[0].getTimestamp().toDate());
        for (int i = 1; i < objs.length; i++) {
            int yearFromDate = DateUtility.getYearFromDate(objs[i].getTimestamp().toDate());
            if(minYear>yearFromDate)
                minYear=yearFromDate;
        }

        return minYear;
    }

    private int maxYear(BudgetObject[] objs)
    {
        if(objs.length==0)
            return -1;
        int maxYear=DateUtility.getYearFromDate(objs[0].getTimestamp().toDate());
        for (int i = 1; i < objs.length; i++) {
            int yearFromDate = DateUtility.getYearFromDate(objs[i].getTimestamp().toDate());
            if(maxYear<yearFromDate)
                maxYear=yearFromDate;
        }

        return maxYear;
    }

    private int minMonthForYear(BudgetObject[] objs,int year) {

        if (objs.length == 0)
            return -1;
        int minDate = -1;

        for (int i = 0; i < objs.length; i++) {
            if (DateUtility.getYearFromDate(objs[i].getTimestamp().toDate()) != year)
                continue;
            if (minDate == -1)
                minDate = DateUtility.getMonthFromDate(objs[i].getTimestamp().toDate());
            else if (minDate > DateUtility.getMonthFromDate(objs[i].getTimestamp().toDate())) {
                minDate = DateUtility.getMonthFromDate(objs[i].getTimestamp().toDate());
            }
        }

        return minDate;
    }

    private int maxMonthFromYear(BudgetObject[] objs,int year) {

        if (objs.length == 0)
            return -1;
        int maxDate = -1;

        for (int i = 0; i < objs.length; i++) {
            if (DateUtility.getYearFromDate(objs[i].getTimestamp().toDate()) != year)
                continue;
            if (maxDate == -1)
                maxDate = DateUtility.getMonthFromDate(objs[i].getTimestamp().toDate());
            else if (maxDate < DateUtility.getMonthFromDate(objs[i].getTimestamp().toDate())) {
                maxDate = DateUtility.getMonthFromDate(objs[i].getTimestamp().toDate());
            }
        }

        return maxDate;
    }



    private void drawBarChart(BarChart barChart,String title,ArrayList<BarEntry> entries,String[] xAxisLabels)
    {
        barChart.setDrawBarShadow(false);
        barChart.setDrawValueAboveBar(true);
        barChart.setPinchZoom(false);
        barChart.setDragEnabled(false);
        barChart.setDoubleTapToZoomEnabled(false);
        //barChart.getXAxis().setDrawAxisLine(false);
        barChart.getAxisLeft().setDrawGridLines(false);
        barChart.getAxisRight().setDrawGridLines(false);
        barChart.getAxisRight().setEnabled(false);
        barChart.getXAxis().setDrawGridLines(false);
        barChart.getDescription().setText("");

        BarDataSet dataSet=new BarDataSet(entries,title);
        dataSet.setValueTextSize(10);
        BarData data=new BarData(dataSet);
        dataSet.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                String txt=Float.toString(value);
                if (value >= 1000)
                {
                    txt= MyUtility.wrapDecPointDouble((value/1000)) +"k";
                }
                return txt;
            }
        });

        dataSet.setColors(getResources().getColor(R.color.colorAccent));
        barChart.setData(data);
        XAxis xAxis = barChart.getXAxis();
        xAxis.setValueFormatter(new IndexAxisValueFormatter(xAxisLabels));
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularity(1);
        xAxis.setGranularityEnabled(true);
        xAxis.setAxisLineWidth(1f);
        barChart.getAxisLeft().setAxisLineWidth(1f);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        int viewId = parent.getId();

        if (viewId == R.id.incTypeSelect) {
            Log.i(TAG, "onItemSelected: "+position);
            if (position == 0) {
                resetIncomeThirdSpinners(ChartType.DAILY);
                if (incSelectYear == 0)
                    incSelectYear = minYear(MyUtility.currentUser.getIncomes());
                resetIncomeSecodSpinner(ChartType.DAILY);
                if (incSelectMonth == -1)
                    incSelectMonth = minMonthForYear(MyUtility.currentUser.getIncomes(), incSelectYear);
            } else if (position == 1) {
                resetIncomeThirdSpinners(ChartType.MONTHLY);
                resetIncomeSecodSpinner(ChartType.MONTHLY);
                if (incSelectYear == 0)
                    incSelectYear = minYear(MyUtility.currentUser.getIncomes());
            } else {
                resetIncomeSecodSpinner(ChartType.YEARLY);
                resetIncomeThirdSpinners(ChartType.YEARLY);
            }
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private enum ChartType
    {
        DAILY,MONTHLY,YEARLY
    }
}
