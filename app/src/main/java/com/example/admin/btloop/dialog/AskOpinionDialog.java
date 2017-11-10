package com.example.admin.btloop.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.StyleRes;

import com.example.admin.btloop.R;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Admin on 11/10/2017.
 */

public class AskOpinionDialog extends Dialog {
    private List<Float> list = new ArrayList<>();
    private PieChart pieChart;

    public AskOpinionDialog(@NonNull Context context, @StyleRes int themeResId, List<Float> list) {
        super(context, themeResId);
        this.list = list;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_ask_opinion);
        iniUI();
    }

    private void iniUI() {
        pieChart = (PieChart) findViewById(R.id.pie_chart);
        ArrayList<Entry> entries = new ArrayList<>();
        entries.add(new Entry(list.get(0), 0));
        entries.add(new Entry(list.get(1), 1));
        entries.add(new Entry(list.get(2), 2));
        entries.add(new Entry(list.get(3), 3));
        PieDataSet dataset = new PieDataSet(entries, "");
        ArrayList<String> labels = new ArrayList<String>();
        labels.add("A: " + list.get(0) + "%");
        labels.add("B: " + list.get(1) + "%");
        labels.add("C: " + list.get(2) + "%");
        labels.add("D: " + list.get(3) + "%");
        PieData data = new PieData(labels, dataset);
        dataset.setColors(ColorTemplate.COLORFUL_COLORS); //
        pieChart.setDescription("Chú thích");
        pieChart.setData(data);
        pieChart.animateY(1500);
    }
}
