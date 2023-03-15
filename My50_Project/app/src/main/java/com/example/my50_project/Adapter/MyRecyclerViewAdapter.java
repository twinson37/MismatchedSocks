package com.example.my50_project.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.my50_project.DTO.MyItem;
import com.example.my50_project.R;

import java.util.ArrayList;

import static com.example.my50_project.Sub1.selItem;

public class MyRecyclerViewAdapter extends RecyclerView.Adapter<MyRecyclerViewAdapter.ItemViewHolder> {
    private static final String TAG = "main MyRVAdapter";

    Context mContext;
    ArrayList<MyItem> arrayList;

    public MyRecyclerViewAdapter(Context mContext, ArrayList<MyItem> arrayList) {
        this.mContext = mContext;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.myitem_view, parent, false);

        return new ItemViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, final int position) {
        Log.d(TAG, "onBindViewHolder: " + position);

        MyItem item = arrayList.get(position); //리스트의 인덱스를 가져와서
        holder.setItem(item); //화면을 만든다

        holder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: " + position); //position이 여기서 바뀌면 안되므로 final 속성을 붙이라는 경고가 뜬다

                selItem = arrayList.get(position);
                Toast.makeText(mContext, selItem.getName(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    //리사이클러뷰 내용 모두 지우기
    public void removeAllItem() {
        arrayList.clear();
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {
        //객체 선언
        LinearLayout parentLayout;
        TextView id, name, date;
        ImageView iv_image;
        ProgressBar progressBar;


        //뷰 홀더 클래스
        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);

            //객체 초기화
            parentLayout = itemView.findViewById(R.id.parentLayout);
            id = itemView.findViewById(R.id.tv_id);
            name = itemView.findViewById(R.id.tv_name);
            date = itemView.findViewById(R.id.tv_date);
            iv_image = itemView.findViewById(R.id.iv_img);
            progressBar = itemView.findViewById(R.id.progressBar);
        }

        public void setItem(MyItem item) {
            id.setText(item.getId());
            name.setText(item.getName());
            date.setText(item.getHire_date());

            Glide.with(itemView).load(item.getImage_path()).into(iv_image);
        }
    }
}
