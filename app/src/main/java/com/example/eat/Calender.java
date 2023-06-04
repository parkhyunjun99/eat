package com.example.eat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.CalendarView;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.myapplication.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Calender extends AppCompatActivity {

    CalendarView cal;
    TextView tv_text;
    private LinearLayout layout;

    private List<CheckBox> checkBoxes = new ArrayList<>();
    private SharedPreferences checkBoxStatePrefs;
    private String currentDate;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calender);

        layout = findViewById(R.id.calenderLayout);
        checkBoxStatePrefs = getSharedPreferences("CheckBoxStates", Context.MODE_PRIVATE);

        SharedPreferences checkBoxPrefs = getSharedPreferences("CheckBoxes", Context.MODE_PRIVATE);
        Map<String, ?> allEntries = checkBoxPrefs.getAll();

        for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
            String itemName = entry.getValue().toString();

            CheckBox checkBox = new CheckBox(this);
            checkBox.setText(itemName);
            checkBox.setTextColor(Color.BLACK);
            checkBox.setTextSize(18);

            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            layout.addView(checkBox, layoutParams);

            checkBoxes.add(checkBox);
        }

        cal = findViewById(R.id.cal);
        tv_text = findViewById(R.id.tv_text);

        cal.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int day) {
                String newDate = year + "-" + (month + 1) + "-" + day;
                tv_text.setText(newDate);

                // Save the state of checkboxes for the old date
                if (currentDate != null) {
                    SharedPreferences.Editor editor = checkBoxStatePrefs.edit();
                    for (CheckBox checkBox : checkBoxes) {
                        String key = currentDate + "_" + checkBox.getText().toString();
                        boolean isChecked = checkBox.isChecked();
                        editor.putBoolean(key, isChecked);
                    }
                    editor.apply();
                }

                // Load the state of checkboxes for the new date
                for (CheckBox checkBox : checkBoxes) {
                    String key = newDate + "_" + checkBox.getText().toString();
                    boolean wasChecked = checkBoxStatePrefs.getBoolean(key, false);
                    checkBox.setChecked(wasChecked);
                }

                currentDate = newDate;
            }
        });
    }
}

