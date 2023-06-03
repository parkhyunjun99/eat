package com.example.eat;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.myapplication.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {

    private List<Data> items;
    private Context context;

    private FirebaseFirestore firestore;
    private ArrayList<Data> mData = new ArrayList<>();

    public RecyclerAdapter(Context context) {
        firestore = FirebaseFirestore.getInstance();
        this.context = context;
        this.items = new ArrayList<>();

    }

    public void addItem(Data item) {
        mData.add(item);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView textView1;
        TextView textView2;
        TextView textView3;
        ImageView imageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            textView1 = itemView.findViewById(R.id.textView1);
            textView2 = itemView.findViewById(R.id.textView2);
            textView3 = itemView.findViewById(R.id.textView3);
            imageView = itemView.findViewById(R.id.imageView);
        }

        void onBind(Data data) {
            textView1.setText(data.getItemName());
            textView2.setText(data.getItemImage());
            textView3.setText(data.getOpenDt());

            Glide.with(itemView.getContext())
                    .load(data.getItemImage())
                    .into(imageView);

            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    Data clickedItem = mData.get(position);
                    Log.d("RecyclerAdapter", "Clicked item name: " + clickedItem.getItemName());

                    // 클릭한 약물 데이터를 데이터베이스에 저장하는 코드 작성
                    Map<String, Object> dataMap = new HashMap<>();
                    dataMap.put("itemName", clickedItem.getItemName());
                    dataMap.put("itemImage", clickedItem.getItemImage());

                    firestore.collection("selectedItems")
                            .add(dataMap)
                            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                @Override
                                public void onSuccess(DocumentReference documentReference) {
                                    String documentId = documentReference.getId();
                                    // 저장 성공 시 필요한 작업 수행
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    // 저장 실패 시 필요한 작업 수행
                                }
                            });

                    // SharedPreferences에 선택한 약의 이름을 추가
                    SharedPreferences sharedPref = context.getSharedPreferences("DrugItems", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putString(clickedItem.getItemName(), clickedItem.getItemName());
                    editor.apply();

                    // DrugInfoActivity로 이동하는 코드 작성
                    Intent intent = new Intent(itemView.getContext(), DrugInfoActivity.class);
                    intent.putExtra("itemName", clickedItem.getItemName());
                    itemView.getContext().startActivity(intent);
                }
            });
            Log.d("RecyclerAdapter", "Data item name: " + data.getItemName());
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(R.layout.activity_item_recycler_view, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Data item = mData.get(position);
        Log.d("RecyclerAdapter", "Item at position " + position + ": " + item.getItemName());
        holder.onBind(item);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public void setItems(List<Data> items) {
        this.items = items;
        notifyDataSetChanged();
    }
}
