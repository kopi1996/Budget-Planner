package com.planner.budgetplanner;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;

public class ChartActivity extends AppCompatActivity {

    private LineChart lineChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart);

        lineChart=findViewById(R.id.lineChart);

        ArrayList lineEntries = new ArrayList<>();
        lineEntries.add(new Entry(2f, 0));
        lineEntries.add(new Entry(4f, 1));
        lineEntries.add(new Entry(6f, 1));
        lineEntries.add(new Entry(8f, 3));
        lineEntries.add(new Entry(7f, 4));
        lineEntries.add(new Entry(3f, 3));
        LineDataSet lineDataSet = new LineDataSet(lineEntries, "");
        LineData lineData = new LineData(lineDataSet);
        lineChart.setData(lineData);
        lineDataSet.setColors(ColorTemplate.JOYFUL_COLORS);
        lineDataSet.setValueTextColor(Color.BLACK);
        lineDataSet.setValueTextSize(18f);
        otherChart();
    }

    private void otherChart() {
//        LineChartView lineChartView = findViewById(R.id.chart);
//        String[] axisData = {"Jan", "Feb", "Mar", "Apr", "May", "June", "July", "Aug", "Sept",
//                "Oct", "Nov", "Dec"};
//        float[] yAxisData = {50, 17.8f, 15.5f, 30, 20, 60, 15, 40, 45, 10, 90, 18};
//
//        List yAxisValues = new ArrayList();
//        List axisValues = new ArrayList();
//        Line line = new Line(yAxisValues);
//
//        for (int i = 0; i < axisData.length; i++) {
//            axisValues.add(i, new AxisValue(i).setLabel(axisData[i]));
//        }
//
//        for (int i = 0; i < yAxisData.length; i++) {
//            PointValue pointValue = new PointValue(i, yAxisData[i]);
//            pointValue.setLabel(yAxisData[i]+"");
//            yAxisValues.add(pointValue);
//        }
//
//        List lines = new ArrayList();
//        lines.add(line);
//
//        LineChartData data = new LineChartData();
//        data.setLines(lines);
//
//        lineChartView.setLineChartData(data);
//        Axis axis = new Axis();
//        axis.setValues(axisValues);
//        data.setAxisXBottom(axis);
//
//        Axis yAxis = new Axis();
//        data.setAxisYLeft(yAxis);
//
//        line.setColor(Color.parseColor("#9C27B0"));
//
//        axis.setTextSize(16);
//
//        axis.setTextColor(Color.parseColor("#03A9F4"));
//
//        yAxis.setTextColor(Color.parseColor("#03A9F4"));
//        yAxis.setTextSize(16);
//
//        yAxis.setName("Sales in millions");
//        axis.setName("Month");

    }
}
