package com.example.eat;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Calender extends AppCompatActivity {

    CalendarView cal;
    TextView tv_text;
    private LinearLayout layout;
    private List<CheckBox> checkBoxes = new ArrayList<>();
    private String selectedDate = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calender);

        layout = findViewById(R.id.calenderLayout);

        Button button5 = findViewById(R.id.button5);
        button5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Calender.this, DrugInfoActivity.class);
                startActivity(intent);
            }
        });

        // SharedPreferences에서 모든 약의 이름 가져오기
        SharedPreferences sharedPref = this.getSharedPreferences("DrugItems", Context.MODE_PRIVATE);
        Map<String, ?> allEntries = sharedPref.getAll();

        // SharedPreferences에서 체크박스 상태 저장
        final SharedPreferences checkBoxStatePrefs = getSharedPreferences("CheckBoxStates", Context.MODE_PRIVATE);
        final SharedPreferences.Editor stateEditor = checkBoxStatePrefs.edit();

        cal = findViewById(R.id.cal);
        tv_text = findViewById(R.id.tv_text);

        cal.setOnDateChangeListener((view, year, month, day) -> {
            selectedDate = year + "-" + (month + 1) + "-" + day;
            tv_text.setText(selectedDate);

            // 모든 약의 이름에 대해 체크박스를 생성합니다.
            layout.removeAllViews();
            checkBoxes.clear();
            for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
                final String itemName = entry.getValue().toString();

                // 동적으로 체크리스트 생성
                final CheckBox checkBox = new CheckBox(this);
                checkBox.setText(itemName);
                checkBox.setTextColor(Color.BLACK);
                checkBox.setTextSize(18);

                // CheckBox 상태 불러오기
                String key = selectedDate + "_" + itemName;
                boolean isChecked = checkBoxStatePrefs.getBoolean(key, false);
                checkBox.setChecked(isChecked);

                // CheckBox 클릭 시 상태 저장
                checkBox.setOnClickListener(v -> {
                    stateEditor.putBoolean(key, checkBox.isChecked());
                    stateEditor.apply();
                });

                // LinearLayout에 체크리스트 추가
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT);
                layout.addView(checkBox, layoutParams);

                // Add the checkbox to the list
                checkBoxes.add(checkBox);
            }
        });
    }
}
