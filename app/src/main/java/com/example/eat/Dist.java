package com.example.eat;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Dist extends AppCompatActivity {

    private FirebaseFirestore firestore;

    private TextView itemNameTextView;
    private TextView itemNameTextView2;

    private String apiUrl = "https://apis.data.go.kr/1471000/MdcinGrnIdntfcInfoService01/getMdcinGrnIdntfcInfoList01?serviceKey=Kz9SWzAXKdBc%2F16leusx9Mi65rCCzbm6DOtk3RTaeoOyzhVEux8V5BRxkum8tSOEbLGmUVTMfnE5eGVJGVpSPg%3D%3D&numOfRows=300&pageNo=1&type=json";

    private RecyclerView recyclerView;
    private RecyclerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_druglist);
        recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new RecyclerAdapter(this);
        recyclerView.setAdapter(adapter);

        firestore = FirebaseFirestore.getInstance();

        fetchDataFromAPI();
    }

    private void fetchDataFromAPI() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection = null;
                BufferedReader bufferedReader = null;

                try {
                    URL url = new URL(apiUrl);
                    connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");

                    int responseCode = connection.getResponseCode();
                    if (responseCode == HttpURLConnection.HTTP_OK) {
                        InputStream inputStream = connection.getInputStream();
                        InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                        bufferedReader = new BufferedReader(inputStreamReader);
                        StringBuilder stringBuilder = new StringBuilder();
                        String line;
                        while ((line = bufferedReader.readLine()) != null) {
                            stringBuilder.append(line);
                        }
                        String response = stringBuilder.toString();

                        JSONObject jsonObject = new JSONObject(response);
                        JSONObject body = jsonObject.getJSONObject("body");
                        JSONArray items = body.getJSONArray("items");

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                for (int i = 0; i < items.length(); i++) {
                                    JSONObject item = null;
                                    try {
                                        item = items.getJSONObject(i);
                                    } catch (JSONException e) {
                                        throw new RuntimeException(e);
                                    }

                                    String itemName = null;
                                    try {
                                        itemName = item.getString("ITEM_NAME");
                                    } catch (JSONException e) {
                                        throw new RuntimeException(e);
                                    }
                                    String itemImage = null;
                                    try {
                                        itemImage = item.getString("ITEM_IMAGE");
                                    } catch (JSONException e) {
                                        throw new RuntimeException(e);
                                    }

                                    Log.d("API Data", "Item Name: " + itemName + ", Image URL: " + itemImage);

                                    Data newItem = new Data(itemName, itemImage);
                                    adapter.addItem(newItem);

                                    Map<String, Object> data = new HashMap<>();
                                    data.put("itemName", itemName);

                                    firestore.collection("items")
                                            .add(data)
                                            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                @Override
                                                public void onSuccess(DocumentReference documentReference) {
                                                    Log.d("Firestore", "Document added with ID: " + documentReference.getId());
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Log.e("Firestore", "Error adding document", e);
                                                }
                                            });
                                }
                                setupButtonClickEvent(); // fetchDataFromAPI() 실행 완료 후에 버튼 클릭 이벤트 처리
                            }
                        });
                    } else {
                        Log.e("API Error", "Response Code: " + responseCode);
                    }
                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                } finally {
                    if (bufferedReader != null) {
                        try {
                            bufferedReader.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    if (connection != null) {
                        connection.disconnect();
                    }
                }
            }
        }).start();
    }

    private void setupButtonClickEvent() {
        Button backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setContentView(R.layout.mainlayout);
                itemNameTextView = findViewById(R.id.itemNameTextView);


                // 데이터베이스에서 선택한 약물 데이터 가져오기
                firestore.collection("selectedItems")
                        .get()
                        .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                            @Override
                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                // 선택한 약물 데이터를 화면에 표시하는 코드 작성
                                List<Data> selectedItems = new ArrayList<>();
                                for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                                    String itemName = document.getString("itemName");
                                    String itemImage = document.getString("itemImage");
                                    Data selectedItem = new Data(itemName, itemImage);
                                    selectedItems.add(selectedItem);
                                }

                                // 리사이클러뷰에 선택한 약물 데이터 설정
                                adapter.setItems(selectedItems);
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.e("Firestore", "Error getting selected items", e);
                            }
                        });
            }
        });
    }
}