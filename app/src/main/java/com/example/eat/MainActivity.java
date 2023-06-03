package com.example.eat;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.myapplication.R;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
public class MainActivity extends Activity {

    EditText edit;
    TextView text;
    String str; //= edit.getText().toString();


    String key="Kz9SWzAXKdBc%2F16leusx9Mi65rCCzbm6DOtk3RTaeoOyzhVEux8V5BRxkum8tSOEbLGmUVTMfnE5eGVJGVpSPg%3D%3D";

    String data;

    public MainActivity() throws UnsupportedEncodingException {
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);







        edit= findViewById(R.id.edit);
        text= findViewById(R.id.text);

        str = edit.getText().toString();

        try{  String entpName = URLEncoder.encode(str, "UTF-8");
            String itemName = URLEncoder.encode("한미아스피린장용정100밀리그램", "UTF-8"); }
        catch(UnsupportedEncodingException e) {
            e.printStackTrace();
        }


    }

    //Button을 클릭했을 때 자동으로 호출되는 callback method
    public void mOnClick(View v) {
        if (v.getId() == R.id.button) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        data = getXmlData();
                    } catch (UnsupportedEncodingException e) {
                        throw new RuntimeException(e);
                    }

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            text.setText(data);
                        }
                    });
                }
            }).start();
        }
        if (v.getId() == R.id.button2) {
            Intent intent = new Intent(MainActivity.this, DrugInfoActivity.class);
            startActivity(intent);
        }

    }


    String getXmlData() throws UnsupportedEncodingException {
        StringBuffer buffer = new StringBuffer();

        String str = edit.getText().toString();
        String itemName = URLEncoder.encode(str, "UTF-8");

        String queryUrl = "http://apis.data.go.kr/1471000/DrbEasyDrugInfoService/getDrbEasyDrugList?"
                + "&itemName=" + itemName
                + "&numOfRows=3&pageNo=1&ServiceKey=" + key;

        try {
            URL url = new URL(queryUrl);
            InputStream is = url.openStream();

            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser xpp = factory.newPullParser();
            xpp.setInput(new InputStreamReader(is, "UTF-8"));

            String tag;

            int eventType = xpp.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                switch (eventType) {
                    case XmlPullParser.START_DOCUMENT:
                        buffer.append("파싱 시작...\n\n");
                        break;

                    case XmlPullParser.START_TAG:
                        tag = xpp.getName();

                        if (tag.equals("item")) {
                            buffer.append("\n\n=== 약 정보 ===\n");
                        } else if (tag.equals("entpName")) {
                            buffer.append("제조사: ");
                            xpp.next();
                            buffer.append(xpp.getText());
                            buffer.append("\n");
                        } else if (tag.equals("itemName")) {
                            buffer.append("약 이름: ");
                            xpp.next();
                            buffer.append(xpp.getText());
                            buffer.append("\n");
                        } else if (tag.equals("efcyQesitm")) {
                            buffer.append("목적: ");
                            xpp.next();
                            buffer.append(xpp.getText());
                            buffer.append("\n");
                        }

                        break;

                    case XmlPullParser.TEXT:
                        break;

                    case XmlPullParser.END_TAG:
                        tag = xpp.getName();

                        if (tag.equals("item")) {
                            buffer.append("========================\n\n");
                        }

                        break;
                }

                eventType = xpp.next();
            }

        } catch (Exception e) {
            e.printStackTrace();
            buffer.append("파싱 예외\n");
        }

        buffer.append("파싱 끝\n");

        return buffer.toString();
    }}
