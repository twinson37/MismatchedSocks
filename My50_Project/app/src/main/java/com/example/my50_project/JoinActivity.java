package com.example.my50_project;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.my50_project.ATask.JoinInsert;

import java.util.concurrent.ExecutionException;

public class JoinActivity extends AppCompatActivity {
    private static final String TAG = "main JoinActivity";

    String state;

    EditText etId, etPasswd, etName, etPhoneNum, etAddress;
    Button btnJoin, btnCancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join);

        etId = findViewById(R.id.etId);
        etPasswd = findViewById(R.id.etPasswd);
        etName = findViewById(R.id.etName);
        etPhoneNum = findViewById(R.id.etPhoneNum);
        etAddress = findViewById(R.id.etAddress);
        btnJoin = findViewById(R.id.btnJoin);
        btnCancel = findViewById(R.id.btnCancel);

        //가입 버튼
        btnJoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id = etId.getText().toString();
                String passwd = etPasswd.getText().toString();
                String name = etName.getText().toString();
                String phonenumber =  etPhoneNum.getText().toString();
                String address = etAddress.getText().toString();

                JoinInsert joinInsert = new JoinInsert(id, passwd, name, phonenumber, address);
                try {
                    state = joinInsert.execute().get().trim();   //.get() : 데이터가 도착하기 전에 조회하는 것을 방지
                    Log.d(TAG, "onClick: " + state);
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                if(state.equals("1")) {
                    Log.d(TAG, "onClick: 삽입 성공");
                    finish();
                } else {
                    Log.d(TAG, "onClick: 삽입 실패");
                    finish();
                }

            }
        });

        //취소 버튼
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }
}
