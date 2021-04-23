package com.example.duelt;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class Profile extends AppCompatActivity {

    LineChart lineChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        lineChart = findViewById(R.id.lineChart);
        LineDataSet lineDataSet = new LineDataSet(lineChartDataSet(), "data set");
        ArrayList<ILineDataSet> iLineDataSets = new ArrayList<>();
        iLineDataSets.add(lineDataSet);

        LineData lineData = new LineData();
        lineChart.setData(lineData);
        lineChart.invalidate();

    }


    private ArrayList<Entry> lineChartDataSet(){
        ArrayList<Entry> dataSet = new ArrayList<Entry>();

        dataSet.add(new Entry(0,10));
        dataSet.add(new Entry(1,10));
        dataSet.add(new Entry(2,20));
        dataSet.add(new Entry(3,30));
        dataSet.add(new Entry(4,40));
        dataSet.add(new Entry(5,50));
        dataSet.add(new Entry(6,60));
        dataSet.add(new Entry(7,70));
        dataSet.add(new Entry(8,80));

        return dataSet;
    }
}