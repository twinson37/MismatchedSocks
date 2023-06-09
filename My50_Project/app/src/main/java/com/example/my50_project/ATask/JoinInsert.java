package com.example.my50_project.ATask;

import android.net.http.AndroidHttpClient;
import android.os.AsyncTask;
import android.util.Log;

import com.example.my50_project.Common.CommonMethod;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

public class JoinInsert extends AsyncTask<Void, Void, String> {
    private static final String TAG = "main JoinInsert";

    // 데이터베이스에 삽입결과 0보다크면 삽입성공, 같거나 작으면 실패
    // 필수 부분
    String state = "";

    HttpClient httpClient;
    HttpPost httpPost;
    HttpResponse httpResponse;
    HttpEntity httpEntity;

    //메인 액티비티의 변수를 받기 위해 변수를 선언하고 생성자를 만든다.
    //무언가를 받고자 할때는 대부분 생성자를 이용한다.
    String id, passwd, name, phonenumber, address;

    public JoinInsert(String id, String passwd, String name, String phonenumber, String address) {
        this.id = id;
        this.passwd = passwd;
        this.name = name;
        this.phonenumber = phonenumber;
        this.address = address;
    }

    //기본으로 생성되는 메서드, 지금은 필요 없다
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(Void... voids) {
        try {
            // MultipartEntityBuild 생성
            // 필수 부분
            MultipartEntityBuilder builder = MultipartEntityBuilder.create();
            builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
            builder.setCharset(Charset.forName("UTF-8"));

            // 문자열 및 데이터 추가
            builder.addTextBody("id", id, ContentType.create("Multipart/related", "UTF-8"));
            builder.addTextBody("passwd", passwd, ContentType.create("Multipart/related", "UTF-8"));
            builder.addTextBody("name", name, ContentType.create("Multipart/related", "UTF-8"));
            builder.addTextBody("phonenumber", phonenumber, ContentType.create("Multipart/related", "UTF-8"));
            builder.addTextBody("address", address, ContentType.create("Multipart/related", "UTF-8"));

            String postURL = CommonMethod.ipConfig + "/app/anJoin";

            // 전송
            InputStream inputStream = null;
            httpClient = AndroidHttpClient.newInstance("Android");
            httpPost = new HttpPost(postURL);
            httpPost.setEntity(builder.build());
            httpResponse = httpClient.execute(httpPost);    //여기 라인에서 DB에 보냄
            httpEntity = httpResponse.getEntity();
            inputStream = httpEntity.getContent();

            // 응답
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
            StringBuilder stringBuilder = new StringBuilder();
            String line = null;
            while ((line = bufferedReader.readLine()) != null){
                stringBuilder.append(line + "\n");
            }
            state = stringBuilder.toString();

            inputStream.close();

        }  catch (Exception e) {
            e.printStackTrace();
        }finally {
            if(httpEntity != null){
                httpEntity = null;
            }
            if(httpResponse != null){
                httpResponse = null;
            }
            if(httpPost != null){
                httpPost = null;
            }
            if(httpClient != null){
                httpClient = null;
            }

        }

        return state;
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);

        Log.d(TAG, "onPostExecute: " + result);
    }
}
