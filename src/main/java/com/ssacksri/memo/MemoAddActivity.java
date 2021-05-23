package com.ssacksri.memo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MemoAddActivity extends AppCompatActivity {

    TextView date_text;
    EditText  comment_text;
    ImageView add_btn,image_text;
    private final int GET_GALLERY_IMAGE = 200;
    public static SQLiteHelper sqLiteHelper;
    AlertDialog dialog;
    long now = System.currentTimeMillis();
    Date mDate = new Date(now);
    SimpleDateFormat simpleDate = new SimpleDateFormat("yyyy/MM/dd");
    String time = simpleDate.format(mDate);
    Uri imageUri;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memo_add);

        sqLiteHelper = new SQLiteHelper(this,"DIARY.sqlite",null,1);


        date_text= findViewById(R.id.date_text);
        date_text.setText(time);
        comment_text= findViewById(R.id.comment_text);

        image_text = findViewById(R.id.image_text);
        image_text.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ActivityCompat.requestPermissions(
                        MemoAddActivity.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        GET_GALLERY_IMAGE
                );
            }
        });
        add_btn= findViewById(R.id.add_btn);
        add_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String date = date_text.getText().toString();
                String comment = comment_text.getText().toString();
                byte[] image = new byte[0];
                if (comment.equals("")) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(MemoAddActivity.this);
                    dialog = builder.setMessage("내용을 입력해주세요.")
                            .setNegativeButton("확인", null)
                            .create();
                    dialog.show();
                    return;

                }
                if (imageUri == null){
                    try {
                        sqLiteHelper.insertDiary(
                                date,
                                comment,
                               image
                        );
                        Toast.makeText(getApplicationContext(),"기록했습니다",Toast.LENGTH_SHORT).show();
                        finish();
                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }
                } else {
                    try {
                        sqLiteHelper.insertDiary(
                                date,
                                comment,
                                imageViewToByte(image_text)
                        );
                        Toast.makeText(getApplicationContext(),"기록했습니다",Toast.LENGTH_SHORT).show();
                        finish();
                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }
                }


            }
        });
    }

    public static byte[] imageViewToByte(ImageView imageView){
        Bitmap bitmap = ((BitmapDrawable)imageView.getDrawable()).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100,stream);
        byte[] byteArray = stream.toByteArray();
        return byteArray;
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == GET_GALLERY_IMAGE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(Intent.createChooser(intent, "Select Image"), GET_GALLERY_IMAGE);
            } else {
                Toast.makeText(getApplicationContext(), "갤러리 권한을 허용해주세요", Toast.LENGTH_SHORT).show();
            }
            return;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == GET_GALLERY_IMAGE && resultCode == RESULT_OK ) {
            imageUri = data.getData();
            image_text.setImageURI(imageUri);
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

}