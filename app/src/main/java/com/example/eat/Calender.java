package com.example.eat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.CalendarView;
import android.widget.TextView;

import com.example.myapplication.R;

import java.util.Map;

public class Calender extends AppCompatActivity {

    CalendarView cal;
    TextView tv_text, textView2;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calender);

        cal = findViewById(R.id.cal);
        tv_text = findViewById(R.id.tv_text);
        textView2 = findViewById(R.id.textView2);

        cal.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int day) {
                tv_text.setText( year + "년" + (month + 1) + "월" + day + "일");
            }
        });

        // SharedPreferences에서 체크박스 텍스트 가져오기
        SharedPreferences checkBoxPrefs = getSharedPreferences("CheckBoxes", Context.MODE_PRIVATE);
        Map<String, ?> allEntries = checkBoxPrefs.getAll();

        StringBuilder checkBoxTexts = new StringBuilder();
        for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
            checkBoxTexts.append(entry.getValue()).append("\n");
        }

        // 체크박스 텍스트를 TextView에 설정
        textView2.setText(checkBoxTexts.toString());
    }
}
