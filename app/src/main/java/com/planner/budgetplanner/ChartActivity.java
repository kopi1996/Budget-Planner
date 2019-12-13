package com.planner.budgetplanner;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.planner.budgetplanner.Utility.MyUtility;

import java.util.ArrayList;

public class ChartActivity extends AppCompatActivity {

    private BarChart incBarChart,expBarChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart);

        incBarChart=findViewById(R.id.incBarChart);
        expBarChart=findViewById(R.id.expBarChart);

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
}
