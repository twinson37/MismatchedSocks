package com.example.my50_project.ATask;

import android.app.ProgressDialog;
import android.net.http.AndroidHttpClient;
import android.os.AsyncTask;
import android.util.JsonReader;
import android.util.Log;

import com.example.my50_project.Adapter.MyRecyclerViewAdapter;
import com.example.my50_project.DTO.MyItem;

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
import java.util.ArrayList;

import static com.example.my50_project.Common.CommonMethod.ipConfig;
import static com.example.my50_project.MainActivity.loginDTO;

public class ListSelect extends AsyncTask<Void, Void, String> {
    ArrayList<MyItem> myItemArrayList;
    MyRecyclerViewAdapter adapter;
    ProgressDialog progressDialog;

    public ListSelect(ArrayList<MyItem> myItemArrayList, MyRecyclerViewAdapter adapter) {
        this.myItemArrayList = myItemArrayList;
        this.adapter = adapter;
    }

    HttpClient httpClient;
    HttpPost httpPost;
    HttpResponse httpResponse;
    HttpEntity httpEntity;

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        //프로그레스바 보이게
    }

    @Override
    protected String doInBackground(Void... voids) {
        try {
            // MultipartEntityBuild 생성
            // 필수 부분
            MultipartEntityBuilder builder = MultipartEntityBuilder.create();
            builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
            builder.setCharset(Charset.forName("UTF-8"));

            String postURL = ipConfig + "/app/anSelectMulti";
            // /app/ 뒤에는 서버의 AController에 적힌 맵핑과 동일하게 적는다.

            // 전송
            InputStream inputStream = null;
            httpClient = AndroidHttpClient.newInstance("Android");
            httpPost = new HttpPost(postURL);
            httpPost.setEntity(builder.build());
            httpResponse = httpClient.execute(httpPost);    //여기 라인에서 DB에 보냄
            httpEntity = httpResponse.getEntity();
            inputStream = httpEntity.getContent();

            //응답
            readJsonStream(inputStream);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (httpEntity != null) {
                httpEntity = null;
            }
            if (httpResponse != null) {
                httpResponse = null;
            }
            if (httpPost != null) {
                httpPost = null;
            }
            if (httpClient != null) {
                httpClient = null;
            }
        } //try catch finally
        return "asdf";
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        //프로그레스바 안보이게
        adapter.notifyDataSetChanged();
    }

    public void readJsonStream(InputStream inputStream) throws IOException {
        JsonReader reader = new JsonReader(new InputStreamReader(inputStream, "UTF-8"));
        try {
            reader.beginArray();
            while (reader.hasNext()) {
                myItemArrayList.add(readMessage(reader));
            }
            reader.endArray();
        } finally {
            reader.close();
        }
    }

    public MyItem readMessage(JsonReader reader) throws IOException {
        String id = "", name = "", hire_date = "", image_path = "";

        reader.beginObject();
        while (reader.hasNext()) {
            String readStr = reader.nextName();
            if (readStr.equals("id")) {
                id = reader.nextString();
            } else if (readStr.equals("name")) {
                name = reader.nextString();
            } else if (readStr.equals("hire_date")) {
                String[] temp = reader.nextString().replace("월", "-").replace(",", "-")
                        .replace(" ", "").split("-");
                hire_date = temp[2] + "-" + temp[0] + "-" + temp[1];
            } else if (readStr.equals("image_path")) {
                image_path = reader.nextString();
            }else {
                reader.skipValue();
            }
        }
        reader.endObject();
        Log.d("listselect:myitem", id + "," + name + "," + hire_date + "," + image_path);
        return new MyItem(id, name, hire_date, image_path);
    }
}
