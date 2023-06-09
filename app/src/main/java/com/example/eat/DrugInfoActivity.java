package com.example.eat;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;

import java.util.Map;

public class DrugInfoActivity extends AppCompatActivity {

    private LinearLayout layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mainlayout);

        layout = findViewById(R.id.linearlayout2);
        layout.setGravity(Gravity.CENTER);

        // SharedPreferences에서 모든 약의 이름 가져오기
        SharedPreferences sharedPref = this.getSharedPreferences("DrugItems", Context.MODE_PRIVATE);
        Map<String, ?> allEntries = sharedPref.getAll();

        // SharedPreferences에서 체크박스 상태 저장
        final SharedPreferences checkBoxStatePrefs = getSharedPreferences("CheckBoxStates", Context.MODE_PRIVATE);
        final SharedPreferences.Editor stateEditor = checkBoxStatePrefs.edit();

        // 모든 약의 이름에 대해 체크박스를 생성합니다.
        for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
            final String itemName = entry.getValue().toString();

            // 동적으로 체크리스트 생성
            final CheckBox checkBox = new CheckBox(this);
            checkBox.setText(itemName);
            checkBox.setTextColor(Color.BLACK);
            checkBox.setTextSize(18);

            // CheckBox 상태 불러오기
            boolean isChecked = checkBoxStatePrefs.getBoolean(itemName, false);
            checkBox.setChecked(isChecked);

            // CheckBox 클릭 시 상태 저장
            checkBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    stateEditor.putBoolean(itemName, checkBox.isChecked());
                    stateEditor.apply();
                }
            });



            // LinearLayout에 체크리스트 추가
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            layout.addView(checkBox, layoutParams);
        }

        Button addButton = findViewById(R.id.button2);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 추가 버튼 클릭 시, Dist 액티비티로 이동
                Intent intent = new Intent(DrugInfoActivity.this, Dist.class);
                startActivity(intent);
            }
        });

        Button SButton = findViewById(R.id.button0);
        SButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // SButton 클릭 시, MainActivity로 이동
                Intent intent = new Intent(DrugInfoActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                finish();
            }
        });

        Button calButton = findViewById(R.id.button3);
        calButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // calButton 클릭 시, Calender 액티비티로 이동
                Intent intent = new Intent(DrugInfoActivity.this, Calender.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                finish();
            }
        });


    }
}
