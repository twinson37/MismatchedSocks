package com.example.my50_project;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.my50_project.ATask.ListInsert;
import com.example.my50_project.Common.CommonMethod;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import static com.example.my50_project.Common.CommonMethod.ipConfig;
import static com.example.my50_project.Common.CommonMethod.isNetworkConnected;

public class Sub1Insert extends AppCompatActivity {
    //로그캣 설정
    private static final String TAG = "main Sub1Insert";

    //객체 선언
    EditText etId, etName;
    DatePicker datePicker;

    String id = "", name = "", date = "";
    Button photoBtn;
    Button photoLoad;
    Button btnAdd;
    Button btnCancel;

    ImageView imageView;

    public String imageRealPath, imageDbPath;

    final int CAMERA_REQUEST = 1000;
    final int LOAD_IMAGE = 1001;

    File file = null;
    long fileSize = 0;

    java.text.SimpleDateFormat tmpDateFormat;
    //--------------------------------------------

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub1_insert);

        //심플 데이트 포맷 설정
        tmpDateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss");

        //객체 초기화
        etId = findViewById(R.id.etId);
        etName = findViewById(R.id.etName);
        datePicker = findViewById(R.id.datePicker);

        photoBtn = findViewById(R.id.btnPhoto);
        photoLoad = findViewById(R.id.btnLoad);
        btnAdd = findViewById(R.id.btn_add);
        btnCancel = findViewById(R.id.btnCancel);

        imageView = findViewById(R.id.imageView);

        //날짜 객체 설정
        Date tempDate = new Date();
        //출력 형식을 연도가 4자리, 월, 일이 2자리로 나오게 설정
        //월은 0 ~ 11로 되어있어서 +1을 해줘야 올바르게 출력 됨
        date = new DecimalFormat("0000").format(tempDate.getYear()) + "/" +
                new DecimalFormat("00").format(tempDate.getMonth() + 1)
                + "/" + new DecimalFormat("00").format(tempDate.getDay());

        datePicker.init(datePicker.getYear(), datePicker.getMonth(), datePicker.getDayOfMonth(), new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                //날짜가 변경되면 변경된 날짜가 나오게 설정
                date = new DecimalFormat("0000").format(year) + "/" +
                        new DecimalFormat("00").format(monthOfYear + 1)
                        + "/" + new DecimalFormat("00").format(dayOfMonth);

            }
        });

        //사진 찍기 버튼 설정
        photoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try { //파일을 만들고 절대 경로를 logcat에 출력
                    file = createFile();
                    Log.d(TAG, "onClick: " + file.getAbsolutePath());
                } catch (Exception e) {
                    e.printStackTrace();
                }


                //이미지뷰와 인텐트 설정
                imageView.setVisibility(View.VISIBLE);
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) { // API24 이상 부터
                    intent.putExtra(MediaStore.EXTRA_OUTPUT,
                            FileProvider.getUriForFile(getApplicationContext(),
                                    getApplicationContext().getPackageName() + ".fileprovider", file));
                    Log.d("sub1:appId", getApplicationContext().getPackageName());
                }else {
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
                }

                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(intent, CAMERA_REQUEST);
                }
            }
        });

        photoLoad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_PICK);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), LOAD_IMAGE);
            }
        });

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //네트워크가 정상적으로 연결되어 있으면
                if(isNetworkConnected(getApplicationContext()) == true) {
                    if(fileSize <= 30000000) { //파일 크기가 30메가 보다 작아야 업로드 할 수 있음
                        id = etId.getText().toString();
                        name = etName.getText().toString();

                        ListInsert listInsert = new ListInsert(id, name, date, imageDbPath, imageRealPath);
                        try {
                            listInsert.execute().get();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                        Intent showIntent = new Intent(getApplicationContext(), Sub1.class);
                        showIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK |   // 이 엑티비티 플래그를 사용하여 엑티비티를 호출하게 되면 새로운 태스크를 생성하여 그 태스크안에 엑티비티를 추가하게 됩니다. 단, 기존에 존재하는 태스크들중에 생성하려는 엑티비티와 동일한 affinity(관계, 유사)를 가지고 있는 태스크가 있다면 그곳으로 새 엑티비티가 들어가게됩니다.
                                Intent.FLAG_ACTIVITY_SINGLE_TOP | // 엑티비티를 호출할 경우 호출된 엑티비티가 현재 태스크의 최상단에 존재하고 있었다면 새로운 인스턴스를 생성하지 않습니다. 예를 들어 ABC가 엑티비티 스택에 존재하는 상태에서 C를 호출하였다면 여전히 ABC가 존재하게 됩니다.
                                Intent.FLAG_ACTIVITY_CLEAR_TOP); // 만약에 엑티비티스택에 호출하려는 엑티비티의 인스턴스가 이미 존재하고 있을 경우에 새로운 인스턴스를 생성하는 것 대신에 존재하고 있는 엑티비티를 포그라운드로 가져옵니다. 그리고 엑티비티스택의 최상단 엑티비티부터 포그라운드로 가져올 엑티비티까지의 모든 엑티비티를 삭제합니다.
                        startActivity(showIntent);

                        finish();
                    } else {
                        // 알림창 띄움
                        final AlertDialog.Builder builder = new AlertDialog.Builder(getApplicationContext());
                        builder.setTitle("알림");
                        builder.setMessage("파일 크기가 30MB초과하는 파일은 업로드가 제한되어 있습니다.\n30MB이하 파일로 선택해 주십시요!!!");
                        builder.setPositiveButton("예", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        });
                        builder.show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "인터넷이 연결되어 있지 않습니다.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }


    private File createFile() throws IOException {
        String imageFileName = "My" + tmpDateFormat.format(new Date()) + ".jpg";
        File storageDir = Environment.getExternalStorageDirectory();
        File curFile = new File(storageDir, imageFileName);
        return curFile;
    }

    //데이터를 주고나서 받을 일이 있을때는 무조건 onActivityResult
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {
            try {
                // 이미지 돌리기 및 리사이즈
                Bitmap newBitmap = CommonMethod.imageRotateAndResize(file.getAbsolutePath());
                if(newBitmap != null){
                    imageView.setImageBitmap(newBitmap);
                }else{
                    Toast.makeText(this, "이미지가 null 입니다...", Toast.LENGTH_SHORT).show();
                }

                //이미지 경로 설정
                imageRealPath = file.getAbsolutePath();
                String uploadFileName = imageRealPath.split("/")[imageRealPath.split("/").length - 1];
                imageDbPath = ipConfig + "/app/resources/" + uploadFileName;

                fileSize = imageRealPath.length();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if(requestCode == LOAD_IMAGE && resultCode == RESULT_OK) {
            try {
                String path = "";

                //데이타에서 Uri 얻기
                Uri selectImageUri = data.getData();
                if(selectImageUri != null) {
                    //Uri에서 경로 얻기
                    path = getPathFromURI(selectImageUri);
                }

                // 이미지 돌리기 및 리사이즈
                Bitmap newBitmap = CommonMethod.imageRotateAndResize(path);
                if(newBitmap != null){
                    imageView.setImageBitmap(newBitmap);
                }else{
                    Toast.makeText(this, "이미지가 null 입니다...", Toast.LENGTH_SHORT).show();
                }

                //이미지 경로 설정
                imageRealPath = path;
                Log.d("Sub1Add", "imageFilePathA Path : " + imageRealPath);
                String uploadFileName = imageRealPath.split("/")[imageRealPath.split("/").length - 1];
                imageDbPath = ipConfig + "/app/resources/" + uploadFileName;

                fileSize = imageRealPath.length();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    //URI에서 실제 경로 추출하는 메서드
    public String getPathFromURI(Uri contentUri) {
        String res = null;
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(contentUri, proj, null, null, null);
        if (cursor.moveToFirst()) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            res = cursor.getString(column_index);
        }
        cursor.close();
        return res;
    }
}
