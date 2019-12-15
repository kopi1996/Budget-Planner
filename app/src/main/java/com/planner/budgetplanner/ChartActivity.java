package com.planner.budgetplanner;

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

public class ChartActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private static final String TAG = "Chart";
    private BarChart incBarChart,expBarChart;
    private Spinner incTypeSpinner, incmSecondSpinner, incThirdSpinner;
    private Spinner expTypeSpinner,expSecondSpinner,expThirdSpinner;

    private int incSelectYear=0,incSelectMonth=-1;
    private int expSelectYear=0,expSelectMonth=-1;
    private ChartType incChartType=ChartType.DAILY,expChartType=ChartType.DAILY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart);

        incBarChart=findViewById(R.id.incBarChart);
        expBarChart=findViewById(R.id.expBarChart);

        initSpinner();

    }

    private void initSpinner() {
        incTypeSpinner = findViewById(R.id.incTypeSelect);
        incmSecondSpinner = findViewById(R.id.incSelectTwo);
        incThirdSpinner = findViewById(R.id.incSelectThree);

        expTypeSpinner = findViewById(R.id.expTypeSelect);
        expSecondSpinner = findViewById(R.id.expSelectTwo);
        expThirdSpinner = findViewById(R.id.expSelectThree);

        incTypeSpinner.setOnItemSelectedListener(this);
        incmSecondSpinner.setOnItemSelectedListener(this);
        incThirdSpinner.setOnItemSelectedListener(this);

        expTypeSpinner.setOnItemSelectedListener(this);
        expSecondSpinner.setOnItemSelectedListener(this);
        expThirdSpinner.setOnItemSelectedListener(this);

        ArrayList<String> typeList = new ArrayList<>();
        typeList.add("DailyInterval");
        typeList.add("MonthlyInterval");
        typeList.add("YearlyInterval");

        ArrayAdapter<String> typeAdapter = new ArrayAdapter<String>(this, R.layout.spinner_text, typeList);
        incTypeSpinner.setAdapter(typeAdapter);
        incTypeSpinner.setSelection(0);

        expTypeSpinner.setAdapter(typeAdapter);
        expTypeSpinner.setSelection(0);

        resetIncomeThirdSpinners(ChartType.DAILY);
        incSelectYear = minYear(MyUtility.currentUser.getIncomes());
        resetIncomeSecodSpinner(ChartType.DAILY);
        setupChartData(incBarChart, "Income", ChartType.DAILY, MyUtility.currentUser.getIncomes(), incSelectMonth, incSelectYear);

        resetExpenseThirdSpinners(ChartType.DAILY);
        expSelectYear = minYear(MyUtility.currentUser.getExpenses());
        resetExpenseSecodSpinner(ChartType.DAILY);
        setupChartData(expBarChart, "Expense", ChartType.DAILY, MyUtility.currentUser.getExpenses(), expSelectMonth, expSelectYear);
    }

    private void resetIncomeThirdSpinners(ChartType type) {
        if (type == ChartType.DAILY) {
            int minYear = minYear(MyUtility.currentUser.getIncomes());
            int maxYear = maxYear(MyUtility.currentUser.getIncomes());

            ArrayList<String> yearList = new ArrayList<>();

            for (int i = minYear; i <= maxYear; i++) {
                yearList.add(Integer.toString(i));
            }

            ArrayAdapter<String> yearAdapter = new ArrayAdapter<String>(this,R.layout.spinner_text, yearList);
            incThirdSpinner.setAdapter(yearAdapter);
            if (yearList.size() > 0)
                incThirdSpinner.setSelection(0);
            incThirdSpinner.setVisibility(View.VISIBLE);
        } else
            incThirdSpinner.setVisibility(View.INVISIBLE);
    }

    private void resetExpenseThirdSpinners(ChartType type) {
        if (type == ChartType.DAILY) {
            int minYear = minYear(MyUtility.currentUser.getExpenses());
            int maxYear = maxYear(MyUtility.currentUser.getExpenses());

            ArrayList<String> yearList = new ArrayList<>();

            for (int i = minYear; i <= maxYear; i++) {
                yearList.add(Integer.toString(i));
            }

            ArrayAdapter<String> yearAdapter = new ArrayAdapter<String>(this,R.layout.spinner_text, yearList);
            expThirdSpinner.setAdapter(yearAdapter);
            if (yearList.size() > 0)
                expThirdSpinner.setSelection(0);
            expThirdSpinner.setVisibility(View.VISIBLE);
        } else
            expThirdSpinner.setVisibility(View.INVISIBLE);
    }

    private void resetIncomeSecodSpinner(ChartType type) {
        if (type == ChartType.MONTHLY) {
            int minYear = minYear(MyUtility.currentUser.getIncomes());
            int maxYear = maxYear(MyUtility.currentUser.getIncomes());

            ArrayList<String> yearList = new ArrayList<>();

            for (int i = minYear; i <= maxYear; i++) {
                yearList.add(Integer.toString(i));
            }

            ArrayAdapter<String> yearAdapter = new ArrayAdapter<String>(this, R.layout.spinner_text, yearList);
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

            ArrayAdapter<String> yearAdapter = new ArrayAdapter<String>(this, R.layout.spinner_text, yearList);
            incmSecondSpinner.setAdapter(yearAdapter);
            if (yearList.size() > 0)
                incmSecondSpinner.setSelection(0);
            incmSecondSpinner.setVisibility(View.VISIBLE);
        } else {
            incmSecondSpinner.setVisibility(View.INVISIBLE);
        }
    }

    private void resetExpenseSecodSpinner(ChartType type) {
        if (type == ChartType.MONTHLY) {
            int minYear = minYear(MyUtility.currentUser.getExpenses());
            int maxYear = maxYear(MyUtility.currentUser.getExpenses());

            ArrayList<String> yearList = new ArrayList<>();

            for (int i = minYear; i <= maxYear; i++) {
                yearList.add(Integer.toString(i));
            }

            ArrayAdapter<String> yearAdapter = new ArrayAdapter<String>(this, R.layout.spinner_text, yearList);
            expSecondSpinner.setAdapter(yearAdapter);
            if (yearList.size() > 0)
                expSecondSpinner.setSelection(0);
            expSecondSpinner.setVisibility(View.VISIBLE);
        } else if (type == ChartType.DAILY) {
            int minMonth = minMonthForYear(MyUtility.currentUser.getExpenses(), expSelectYear);
            int maxMont = maxMonthFromYear(MyUtility.currentUser.getExpenses(), expSelectYear);

            ArrayList<String> yearList = new ArrayList<>();

            for (int i = minMonth; i <= maxMont; i++) {
                yearList.add(DateUtility.getSortNameForMonth(i));
            }

            ArrayAdapter<String> yearAdapter = new ArrayAdapter<String>(this, R.layout.spinner_text, yearList);
            expSecondSpinner.setAdapter(yearAdapter);
            if (yearList.size() > 0)
                expSecondSpinner.setSelection(0);
            expSecondSpinner.setVisibility(View.VISIBLE);
        } else {
            expSecondSpinner.setVisibility(View.INVISIBLE);
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

    private int minDate(BudgetObject[] objs,int month,int year)
    {
        int minDate=-1;

        for (BudgetObject obj : objs) {
            if(DateUtility.getMonthFromDate(obj.getTimestamp().toDate())+1!=month&&DateUtility.getYearFromDate(obj.getTimestamp().toDate())!=year)
                continue;
            int dayFromDate = DateUtility.getDayFromDate(obj.getTimestamp().toDate());
            if(minDate==-1)
                minDate=dayFromDate;
            else if(minDate>dayFromDate)
                minDate=dayFromDate;
        }

        return minDate;
    }

    private int maxDate(BudgetObject[] objs,int month,int year) {
        int maxDate = -1;

        for (BudgetObject obj : objs) {
            if (DateUtility.getMonthFromDate(obj.getTimestamp().toDate()) + 1 != month && DateUtility.getYearFromDate(obj.getTimestamp().toDate()) != year)
                continue;
            int dayFromDate = DateUtility.getDayFromDate(obj.getTimestamp().toDate());
            if (maxDate == -1)
                maxDate = dayFromDate;
            else if (maxDate < dayFromDate)
                maxDate = dayFromDate;
        }

        return maxDate;
    }

    private void drawBarChart(BarChart barChart,String title,ArrayList<BarEntry> entries,String[] xAxisLabels) {
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

        BarDataSet dataSet = new BarDataSet(entries, title);
        dataSet.setValueTextSize(10);
        BarData data = new BarData(dataSet);
        dataSet.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                String txt = Float.toString(value);
                if (value >= 1000) {
                    txt = MyUtility.wrapDecPointDouble((value / 1000)) + "k";
                } else if (value == 0)
                    txt = "";
                return txt;
            }
        });

        dataSet.setColors(getResources().getColor(R.color.colorAccent));
        barChart.setData(data);
        barChart.invalidate();
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
            if (position == 0) {
                incChartType = ChartType.DAILY;
                resetIncomeThirdSpinners(ChartType.DAILY);
                if (incSelectYear == 0)
                    incSelectYear = minYear(MyUtility.currentUser.getIncomes());
                resetIncomeSecodSpinner(ChartType.DAILY);
                if (incSelectMonth == -1)
                    incSelectMonth = minMonthForYear(MyUtility.currentUser.getIncomes(), incSelectYear);

                setupChartData(incBarChart,"Income",ChartType.DAILY,MyUtility.currentUser.getIncomes(),incSelectMonth,incSelectYear);
            } else if (position == 1) {
                incChartType = ChartType.MONTHLY;
                resetIncomeThirdSpinners(ChartType.MONTHLY);
                resetIncomeSecodSpinner(ChartType.MONTHLY);
                if (incSelectYear == 0)
                    incSelectYear = minYear(MyUtility.currentUser.getIncomes());
                setupChartData(incBarChart,"Income",ChartType.MONTHLY,MyUtility.currentUser.getIncomes(),incSelectMonth,incSelectYear);
            } else {
                incChartType = ChartType.YEARLY;
                resetIncomeSecodSpinner(ChartType.YEARLY);
                resetIncomeThirdSpinners(ChartType.YEARLY);
                setupChartData(incBarChart,"Income",ChartType.YEARLY,MyUtility.currentUser.getIncomes(),incSelectMonth,incSelectYear);
            }
        } else if (viewId == R.id.incSelectTwo) {
            if (incChartType == ChartType.DAILY) {
                incSelectMonth = DateUtility.getMonthFromShortName(incmSecondSpinner.getSelectedItem().toString().trim());
                Log.i(TAG, "onItemSelected month: "+incSelectMonth);
                setupChartData(incBarChart,"Income",ChartType.DAILY,MyUtility.currentUser.getIncomes(),incSelectMonth,incSelectYear);
            } else if (incChartType == ChartType.MONTHLY) {
                incSelectYear = Integer.parseInt(incmSecondSpinner.getSelectedItem().toString().trim());
                setupChartData(incBarChart,"Income",ChartType.MONTHLY,MyUtility.currentUser.getIncomes(),incSelectMonth,incSelectYear);
            }
        }
        else if(viewId==R.id.incSelectThree) {
            if (incChartType == ChartType.DAILY) {
                incSelectYear = Integer.parseInt(incThirdSpinner.getSelectedItem().toString().trim());
                resetIncomeSecodSpinner(ChartType.DAILY);
                setupChartData(incBarChart,"Income",ChartType.DAILY,MyUtility.currentUser.getIncomes(),incSelectMonth,incSelectYear);
            }
        }
        else if(viewId==R.id.expTypeSelect)
        {
            if (position == 0) {
                expChartType = ChartType.DAILY;
                resetExpenseThirdSpinners(ChartType.DAILY);
                if (expSelectYear == 0)
                    expSelectYear = minYear(MyUtility.currentUser.getExpenses());
                resetExpenseSecodSpinner(ChartType.DAILY);
                if (expSelectMonth == -1)
                    expSelectMonth = minMonthForYear(MyUtility.currentUser.getExpenses(), expSelectYear);

                setupChartData(expBarChart,"Expense",ChartType.DAILY,MyUtility.currentUser.getExpenses(),expSelectMonth,expSelectYear);
            } else if (position == 1) {
                expChartType = ChartType.MONTHLY;
                resetExpenseThirdSpinners(ChartType.MONTHLY);
                resetExpenseSecodSpinner(ChartType.MONTHLY);
                if (expSelectYear == 0)
                    expSelectYear = minYear(MyUtility.currentUser.getExpenses());
                setupChartData(expBarChart,"Expense",ChartType.MONTHLY,MyUtility.currentUser.getExpenses(),expSelectMonth,expSelectYear);
            } else {
                expChartType = ChartType.YEARLY;
                resetExpenseSecodSpinner(ChartType.YEARLY);
                resetExpenseThirdSpinners(ChartType.YEARLY);
                setupChartData(expBarChart,"Expense",ChartType.YEARLY,MyUtility.currentUser.getExpenses(),expSelectMonth,expSelectYear);
            }
        }
        else if(viewId==R.id.expSelectTwo)
        {
            if (expChartType == ChartType.DAILY) {
                expSelectMonth=DateUtility.getMonthFromShortName(expSecondSpinner.getSelectedItem().toString().trim());
                setupChartData(expBarChart,"Expense", ChartType.DAILY, MyUtility.currentUser.getExpenses(),expSelectMonth,expSelectYear);
            } else if (expChartType == ChartType.MONTHLY) {
                expSelectYear = Integer.parseInt(expSecondSpinner.getSelectedItem().toString().trim());
                setupChartData(expBarChart,"Expense",ChartType.MONTHLY,MyUtility.currentUser.getExpenses(),expSelectMonth,expSelectYear);
            }
        }
        else if(viewId==R.id.expSelectThree)
        {
            if (expChartType == ChartType.DAILY) {
                expSelectYear = Integer.parseInt(expThirdSpinner.getSelectedItem().toString().trim());
                resetExpenseSecodSpinner(ChartType.DAILY);
                setupChartData(expBarChart,"Expense",ChartType.DAILY,MyUtility.currentUser.getExpenses(),expSelectMonth,expSelectYear);
            }
        }
    }
    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private void setupChartData(BarChart chart,String title,ChartType type,BudgetObject[] objs,int month,int year) {
        if (type == ChartType.DAILY) {
            int minDate = minDate(objs, month, month);
            int maxDate = maxDate(objs, month, year);

            String[] dateLabel = new String[DateUtility.getMaxDateForMonth(month, year)];

            for (int i = 0; i < dateLabel.length; i++) {
                dateLabel[i] = Integer.toString(i + 1);
            }

            ArrayList<BarEntry> barEntries = new ArrayList<>();
            double[] budgetAmounts = getBudgetAmount(objs, minDate, maxDate, month, year);
            for (int i = 0; i < budgetAmounts.length; i++) {
                barEntries.add(new BarEntry(i, (float) budgetAmounts[i]));
            }
            drawBarChart(chart, title, barEntries, dateLabel);
        } else if (type == ChartType.MONTHLY) {
            String[] monthLabel = new String[12];
            ArrayList<BarEntry> barEntries = new ArrayList<>();
            for (int i = 0; i < monthLabel.length; i++) {
                monthLabel[i] = DateUtility.getSortNameForMonth(i);
                double[] budgetAmount = getBudgetAmount(objs, 1, DateUtility.getMaxDateForMonth(i, year), i, year);
                double sum = 0;
                for (double v : budgetAmount) {
                    sum += v;
                }
                barEntries.add(new BarEntry(i, (float) sum));
            }

            drawBarChart(chart, title, barEntries, monthLabel);
        } else {
            int minYear = minYear(objs);
            int maxYear = maxYear(objs);

            String[] yearLabel = new String[maxYear - minYear + 1];
            ArrayList<BarEntry> barEntries = new ArrayList<>();

            for (int i = minYear; i <= maxYear; i++) {
                yearLabel[i - minYear] = Integer.toString(i);
                double sum = 0;
                for (int m = 0; m < 12; m++) {
                    double[] budgetAmount = getBudgetAmount(objs, 1, DateUtility.getMaxDateForMonth(m, i), m, i);

                    for (double v : budgetAmount) {
                        sum += v;
                    }
                }
                barEntries.add(new BarEntry(i - minYear, (float) sum));
            }

            drawBarChart(chart, title, barEntries, yearLabel);
        }
    }

    private double[] getBudgetAmount(BudgetObject[] objs,int minDate,int maxDate,int month,int year) {
        double arr[] = new double[DateUtility.getMaxDateForMonth(month, year)];
        for (BudgetObject obj : objs) {
            int dayFromDate = DateUtility.getDayFromDate(obj.getTimestamp().toDate());
            if (dayFromDate < minDate && dayFromDate > maxDate) continue;
            int monthFromDate = DateUtility.getMonthFromDate(obj.getTimestamp().toDate());
            if (monthFromDate != month) continue;
            int yearFromDate = DateUtility.getYearFromDate(obj.getTimestamp().toDate());
            if (yearFromDate != year) continue;

            arr[dayFromDate-1] = obj.getAmount();
        }

        return arr;
    }

    private enum ChartType
    {
        DAILY,MONTHLY,YEARLY
    }
}
