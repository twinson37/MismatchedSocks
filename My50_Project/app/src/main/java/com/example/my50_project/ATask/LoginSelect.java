package com.example.my50_project.ATask;

import android.net.http.AndroidHttpClient;
import android.os.AsyncTask;
import android.util.JsonReader;
import android.util.Log;

import com.example.my50_project.Common.CommonMethod;
import com.example.my50_project.DTO.MemberDTO;

import static com.example.my50_project.Common.CommonMethod.ipConfig;
import static com.example.my50_project.MainActivity.loginDTO;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

public class LoginSelect extends AsyncTask<Void, Void, String> {
    private static final String TAG = "main LoginSelect";

    String id, passwd;

    // 데이터베이스에 삽입결과 0보다크면 삽입성공, 같거나 작으면 실패
    // 필수 부분
    //String state = ""; 로그인에서는 state를 안쓰기때문에 필요없다

    HttpClient httpClient;
    HttpPost httpPost;
    HttpResponse httpResponse;
    HttpEntity httpEntity;

    public LoginSelect(String id, String passwd) {
        this.id = id;
        this.passwd = passwd;
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

            String postURL = ipConfig + "/login";
            // /app/ 뒤에는 서버의 AController에 적힌 맵핑과 동일하게 적는다.

            // 전송
            InputStream inputStream = null;
            httpClient = AndroidHttpClient.newInstance("Android");
            httpPost = new HttpPost(postURL);
            httpPost.setEntity(builder.build());
            httpResponse = httpClient.execute(httpPost);    //여기 라인에서 DB에 보냄
            httpEntity = httpResponse.getEntity();
            inputStream = httpEntity.getContent();

            //응답 : JSON형태로 받음
            loginDTO = readMessage(inputStream);
            Log.d(TAG, "doInBackground: " + loginDTO.getId());

        } catch (Exception e) {
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
        } //try catch finally
        return "LoginDTO select complete";
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        Log.d(TAG, "onPostExecute: " + result);
    }

    public MemberDTO readMessage(InputStream inputStream) throws IOException {
        JsonReader reader = new JsonReader(new InputStreamReader(inputStream, "UTF-8"));

        String id = "", name = "", phonenumber = "", address = "";

        //비밀번호는 가져올 필요가 없고, 보안을 위해 가져오지 않는다.
        reader.beginObject();
        while (reader.hasNext()) {
            String readStr = reader.nextName();
            if (readStr.equals("id")) {
                id = reader.nextString();
            } else if (readStr.equals("name")) {
                name = reader.nextString();
            } else if (readStr.equals("phonenumber")) {
                phonenumber = reader.nextString();
            } else if (readStr.equals("address")) {
                address = reader.nextString();
            }else {
                reader.skipValue();
            }
        }
        reader.endObject();
        Log.d(TAG, id + "," + name + "," + phonenumber + "," + address);
        return new MemberDTO(id, name, phonenumber, address);
    }
}