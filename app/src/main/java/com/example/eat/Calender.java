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

    // 체크박스들을 저장할 리스트를 선언
    private List<CheckBox> checkBoxes = new ArrayList<>();

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calender);

        layout = findViewById(R.id.calenderLayout);

        // SharedPreferences에서 CheckBox 텍스트 불러오기
        SharedPreferences checkBoxPrefs = getSharedPreferences("CheckBoxes", Context.MODE_PRIVATE);
        Map<String, ?> allEntries = checkBoxPrefs.getAll();

        // 모든 약의 이름에 대해 체크박스를 생성합니다.
        for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
            String itemName = entry.getValue().toString();

            // 동적으로 체크리스트 생성
            CheckBox checkBox = new CheckBox(this);
            checkBox.setText(itemName);
            checkBox.setTextColor(Color.BLACK);
            checkBox.setTextSize(18);

            // LinearLayout에 체크리스트 추가
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            layout.addView(checkBox, layoutParams);

            // 생성한 체크박스를 리스트에 추가
            checkBoxes.add(checkBox);
        }

        cal = findViewById(R.id.cal);
        tv_text = findViewById(R.id.tv_text);

        cal.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int day) {
                tv_text.setText(year + "년" + (month + 1) + "월" + day + "일");

                // 날짜가 변경될 때마다 체크박스를 모두 해제
                for (CheckBox checkBox : checkBoxes) {
                    checkBox.setChecked(false);
                }
            }
        });
    }
}
