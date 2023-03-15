package com.example.my50_project;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.my50_project.ATask.ListSelect;
import com.example.my50_project.Adapter.MyRecyclerViewAdapter;
import com.example.my50_project.DTO.MyItem;

import java.util.ArrayList;

import static com.example.my50_project.Common.CommonMethod.isNetworkConnected;

public class Sub1 extends AppCompatActivity {
    private static final String TAG = "main Sub1";

    public static MyItem selItem = null;
    Button button1, button2, button3, button4;
    RecyclerView recyclerView;
    MyRecyclerViewAdapter adapter;
    ArrayList<MyItem> myItemArrayList;

    ListSelect listSelect;

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub1);
        button1 = findViewById(R.id.button1);
        button2 = findViewById(R.id.button2);
        button3 = findViewById(R.id.button3);
        button4 = findViewById(R.id.button4);

        //리사이클러 뷰 시작
        recyclerView = findViewById(R.id.recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);

        myItemArrayList = new ArrayList<>();
        adapter = new MyRecyclerViewAdapter(this, myItemArrayList);

        recyclerView.setAdapter(adapter);

        if(isNetworkConnected(this) == true) {
            listSelect = new ListSelect(myItemArrayList, adapter);
            listSelect.execute();
        } else {
            Toast.makeText(this, "인터넷 연결 실패!", Toast.LENGTH_SHORT).show();
        }

        //버튼1 설정
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Sub1Insert.class);
                startActivity(intent);
            }
        });
    }

    //이미 화면이 있을때 받는 곳
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Log.d(TAG, "onNewIntent: 호출됨");

        //새로고침하면서 이미지가 겹치는 현상 없애기 위해...
        adapter.removeAllItem();

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("데이터 업로딩");
        progressDialog.setMessage("데이터 업로딩 중입니다\n" + "잠시만 기다려주세요 ...");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
        processIntent(intent);
    }

    private void processIntent(Intent intent) {
        try {
            if(intent != null){
                listSelect = new ListSelect(myItemArrayList, adapter);
                listSelect.execute().get();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        if(progressDialog != null) {
            progressDialog.dismiss();
        }
    }
}