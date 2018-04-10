package aunguyen.quanlycongviec.Activity;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.style.RelativeSizeSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Toast;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.MPPointF;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import aunguyen.quanlycongviec.R;

public class StatisticActivity extends AppCompatActivity implements View.OnClickListener{

    private Toolbar toolbarGraphTotal;

    private PieChart pcTotal;
    private Button btnStartTime;
    private Button btnEndTime;
    private Button btnStatistic;

    private FirebaseDatabase database;

    private ArrayList<PieEntry> received = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistic);

        setUpFirebase();

        setUpToolbar();

        addControls();

        addEvents();

        loadData();
    }
    private void setUpFirebase() {

        database = FirebaseDatabase.getInstance();
    }

    private void setUpToolbar() {
        toolbarGraphTotal = findViewById(R.id.toolbar_graph_total);
        setSupportActionBar(toolbarGraphTotal);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void addControls() {

        pcTotal = findViewById(R.id.pc_total);
        btnStartTime = findViewById(R.id.btn_start_time);
        btnEndTime = findViewById(R.id.btn_end_time);
        btnStatistic = findViewById(R.id.btn_statistic);

        pieChart(pcTotal, received);
        edtEnable(false);

    }

    private SpannableString generateCenterSpannableText() {

        SpannableString s = new SpannableString("Có Công Việc Được Giao");
        s.setSpan(new RelativeSizeSpan(2f), 0, s.length(), 0);
//        if (state.equals(getString(R.string.message_graph_received))) {
//            s.setSpan(new ForegroundColorSpan(Color.BLUE), 0, s.length(), 0);
//        } else {
//            s.setSpan(new ForegroundColorSpan(Color.RED), 0, s.length(), 0);
//        }
        return s;
    }

    private void addEvents() {
        btnStartTime.setOnClickListener(this);
        btnEndTime.setOnClickListener(this);
        btnStatistic.setOnClickListener(this);
    }

    private void loadData() {
        received.add(new PieEntry(25f, "Chưa Nhận"));
        received.add(new PieEntry(75f, "Đã Nhận"));

    }

    private void pieChart(PieChart pieChart, ArrayList arrayList) {
        pieChart.setUsePercentValues(true);
        pieChart.getDescription().setEnabled(false);
        pieChart.setExtraOffsets(5, 10, 5, 5);
        pieChart.setDragDecelerationFrictionCoef(0.99f);
        pieChart.setDrawHoleEnabled(true);
        pieChart.setHoleColor(Color.WHITE);
        pieChart.setTransparentCircleAlpha(110);
        pieChart.setHoleRadius(58f);
        pieChart.setTransparentCircleRadius(50f);
        pieChart.setCenterText(generateCenterSpannableText());
        pieChart.setDrawCenterText(true);
        pieChart.setRotationAngle(0);
        pieChart.setRotationEnabled(true);
        pieChart.setHighlightPerTapEnabled(true);
        Legend l = pieChart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        l.setOrientation(Legend.LegendOrientation.VERTICAL);
        l.setDrawInside(false);
        l.setXEntrySpace(7f);
        l.setYEntrySpace(0f);
        l.setYOffset(0f);
        pieChart.setEntryLabelColor(Color.BLACK);
        pieChart.setEntryLabelTextSize(12f);
        PieDataSet dataSet = new PieDataSet(arrayList, getString(R.string.message_graph_state));
        dataSet.setDrawIcons(false);
        dataSet.setIconsOffset(new MPPointF(0, 40));
        dataSet.setSliceSpace(3f);
        dataSet.setSelectionShift(6f);

        ArrayList<Integer> colors = new ArrayList<>();
        for (int c : ColorTemplate.VORDIPLOM_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.JOYFUL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.COLORFUL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.LIBERTY_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.PASTEL_COLORS)
            colors.add(c);

        colors.add(ColorTemplate.getHoloBlue());
        dataSet.setColors(colors);

        PieData data = new PieData(dataSet);
        data.setValueTextSize(10f);
        data.setValueFormatter(new PercentFormatter());
        data.setValueTextColor(Color.BLACK);
        pieChart.setData(data);
        pieChart.animateX(1000);
        pieChart.invalidate();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_start_time:
                startTime();
                break;

            case R.id.btn_end_time:
                endTime();
                checkTime();
                break;

            case R.id.btn_statistic:
                statistic();
                break;
        }

    }
    private void checkTime(){
        if(compareDate(btnStartTime.getText().toString(),btnEndTime.getText().toString())){
            Log.i("AB",""+btnStartTime.getText().toString());
            Log.i("AB",""+btnEndTime.getText().toString());
            edtEnable(true);
        }else {
            edtEnable(false);
        }
    }

    private void startTime() {
        String startTime = btnStartTime.getText().toString();
        int dayDef, mouthDef, yearDef;

        String split[] = startTime.split("/");
        dayDef =  Integer.parseInt(split[0]);
        mouthDef =  Integer.parseInt(split[1]) - 1;
        yearDef =  Integer.parseInt(split[2]);

        DatePickerDialog dialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                String day, month, year;
                day = (i2 < 10) ? "0"+i2 : ""+i2;

                i1+=1;
                month = (i1 < 10) ? "0"+i1 : ""+i1;
                year = ""+i;

                btnStartTime.setText(day + "/" + month + "/" + year);

                checkTime();

            }
        }, yearDef, mouthDef, dayDef);

        dialog.show();
    }

    private void endTime() {
        String endTime = btnEndTime.getText().toString();
        int dayDef, mouthDef, yearDef;

        String split[] = endTime.split("/");
        dayDef =  Integer.parseInt(split[0]);
        mouthDef =  Integer.parseInt(split[1]) - 1;
        yearDef =  Integer.parseInt(split[2]);

        DatePickerDialog dialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                String day, month, year;
                day = (i2 < 10) ? "0"+i2 : ""+i2;

                i1+=1;
                month = (i1 < 10) ? "0"+i1 : ""+i1;
                year = ""+i;
                btnEndTime.setText(day + "/" + month + "/" + year);
                checkTime();

            }
        }, yearDef, mouthDef, dayDef);

        dialog.show();
    }

    private void statistic() {
        Toast.makeText(this, "OKKKK", Toast.LENGTH_SHORT).show();
        //load data
        edtEnable(false);
    }

    private void edtEnable(boolean answer) {
        btnStatistic.setEnabled(answer);
    }
    private boolean compareDate(String start, String end) {
        String splitStart[] = start.split("/");
        int dayStart = Integer.parseInt(splitStart[0]);
        int mouthStart = Integer.parseInt(splitStart[1]) - 1;
        int yearStart = Integer.parseInt(splitStart[2]);

        String splitEnd[] = end.split("/");
        int dayEnd = Integer.parseInt(splitEnd[0]);
        int mouthEnd = Integer.parseInt(splitEnd[1]) - 1;
        int yearEnd = Integer.parseInt(splitEnd[2]);

        return (yearEnd >= yearStart && mouthEnd >= mouthStart && dayEnd >= dayStart);
    }
}
