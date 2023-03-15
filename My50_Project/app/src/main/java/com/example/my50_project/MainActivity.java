package com.example.my50_project;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.my50_project.ATask.LoginSelect;
import com.example.my50_project.DTO.MemberDTO;

import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    // 로그인이 성공하면 static 로그인DTO 변수에 담아서
    // 어느곳에서나 접근할 수 있게 한다
    public static MemberDTO loginDTO = null;

    EditText etID, etPASSWD;
    Button btnLogin, btnJoin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        checkDangerousPermissions();

        etID = findViewById(R.id.etId);
        etPASSWD = findViewById(R.id.etPASSWD);
        btnLogin = findViewById(R.id.btnLogin);
        btnJoin = findViewById(R.id.btnJoin);

        //로그인 버튼
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(etID.getText().toString().length() != 0 &&
                    etPASSWD.getText().toString().length() != 0) {  //아이디와 비밀번호의 길이가 0이 아니면
                   String id = etID.getText().toString();
                   String passwd = etPASSWD.getText().toString();

                    LoginSelect loginSelect = new LoginSelect(id, passwd);
                    try {
                       loginSelect.execute().get();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    if(loginDTO != null) {  //로그인 정보가 있으면, 서브1 화면을 띄움
                        Log.d(TAG, "onClick: id:" + loginDTO.getId());

                        Intent intent = new Intent(getApplicationContext(), Sub1.class);
                        startActivity(intent);
                    } else { //로그인 정보가 맞지 않으면 토스트창 띄우고 id, pw칸 지우고 id칸에 포커스
                        Toast.makeText(MainActivity.this, "아이디나 비밀번호가 틀렸습니다.", Toast.LENGTH_SHORT).show();
                        etID.setText("");
                        etPASSWD.setText("");
                        etID.requestFocus();
                    }
                }
            }
        });

        //회원 가입 버튼
        btnJoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), JoinActivity.class);
                startActivity(intent);
            }
        });
    }


    private void checkDangerousPermissions() {
        String[] permissions = {
                Manifest.permission.INTERNET,
                Manifest.permission.ACCESS_NETWORK_STATE,
                Manifest.permission.ACCESS_WIFI_STATE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA
        };

        int permissionCheck = PackageManager.PERMISSION_GRANTED;
        for (int i = 0; i < permissions.length; i++) {
            permissionCheck = ContextCompat.checkSelfPermission(this, permissions[i]);
            if (permissionCheck == PackageManager.PERMISSION_DENIED) {
                break;
            }
        }

        if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "권한 있음", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "권한 없음", Toast.LENGTH_LONG).show();

            if (ActivityCompat.shouldShowRequestPermissionRationale(this, permissions[0])) {
                Toast.makeText(this, "권한 설명 필요함.", Toast.LENGTH_LONG).show();
            } else {
                ActivityCompat.requestPermissions(this, permissions, 1);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == 1) {
            for (int i = 0; i < permissions.length; i++) {
                if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, permissions[i] + " 권한이 승인됨.", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(this, permissions[i] + " 권한이 승인되지 않음.", Toast.LENGTH_LONG).show();
                }
            }
        }
    }
}

