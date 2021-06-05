package com.ssacksri.memo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    public static SQLiteHelper sqLiteHelper;
    ImageView add_btn;
    ListView diary_list;
    ArrayList<MemoModel> list;
    MemoListAdapter adapter ;

    @Override
    protected void onResume(){
        super.onResume();
        list = new ArrayList<>();
        adapter = new MemoListAdapter(this,R.layout.memo_item,list);
        diary_list.setAdapter(adapter);
        updateDiaryList();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sqLiteHelper = new SQLiteHelper(this,"DIARY.sqlite",null,1);
        sqLiteHelper.queryData("CREATE TABLE IF NOT EXISTS DIARY(num INTEGER PRIMARY " +
                "KEY AUTOINCREMENT , date VARCHAR, comment VARCHAR ,image BLOB)");

        diary_list =(ListView)findViewById(R.id.diary_list);
        list = new ArrayList<>();
        adapter = new MemoListAdapter(this,R.layout.memo_item,list);
        diary_list.setAdapter(adapter);
        diary_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Intent intent = new Intent(MainActivity.this, MemoUpdateActivity.class);
                intent.putExtra("num",list.get(position).getNum());
                startActivity(intent);
            }
        });

        add_btn = findViewById(R.id.add_btn);
        add_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, MemoAddActivity.class);
                startActivity(intent);
            }
        });

    }

    private void updateDiaryList() {
        Cursor cursor = sqLiteHelper.getData("SELECT * FROM DIARY ORDER BY date");
        list.clear();
        while (cursor.moveToNext()){

            String num = cursor.getString(0);
            String date = cursor.getString(1);
            String comment = cursor.getString(2);
            byte[] image = cursor.getBlob(3);

            list.add(new MemoModel(num,date,comment,image));
        }
        adapter.notifyDataSetChanged();
    }

}




