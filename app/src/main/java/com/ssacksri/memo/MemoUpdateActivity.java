package com.ssacksri.memo;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import java.io.ByteArrayOutputStream;

public class MemoUpdateActivity extends AppCompatActivity {

    TextView date_text;
    EditText  comment_text;
    ImageView add_btn,delete_btn,image_text;
    String num;
    public static SQLiteHelper sqLiteHelper;
    AlertDialog dialog;
    Uri imageUri;
    private final int GET_GALLERY_IMAGE = 200;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memo_update);

        sqLiteHelper = new SQLiteHelper(this,"DIARY.sqlite",null,1);

        Intent intent = getIntent();
        num = intent.getStringExtra("num");


        date_text= findViewById(R.id.date_text);
        comment_text= findViewById(R.id.comment_text);
        image_text = findViewById(R.id.image_text);

        Cursor cursor = sqLiteHelper.getData("SELECT date,comment,image FROM DIARY WHERE num= '" +num+"'  ");
        while (cursor.moveToNext()){
            String date = cursor.getString(0);
            date_text.setText(date);
            String comment = cursor.getString(1);
            comment_text.setText(comment);
            byte[] Image = cursor.getBlob(2);
                Bitmap bitmap = BitmapFactory.decodeByteArray(Image,0,Image.length);
                image_text.setImageBitmap(bitmap);


        }
        delete_btn = findViewById(R.id.delete_btn);
        delete_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogDelete(num);
            }
        });

        image_text.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ActivityCompat.requestPermissions(
                        MemoUpdateActivity.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        GET_GALLERY_IMAGE
                );
            }
        });
        add_btn= findViewById(R.id.add_btn);
        add_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String comment = comment_text.getText().toString();
                if (comment.equals("") ) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(MemoUpdateActivity.this);
                    dialog = builder.setMessage("내용을 입력해주세요.")
                            .setNegativeButton("확인", null)
                            .create();
                    dialog.show();
                    return;

                }
                try {
                    sqLiteHelper.updateDiary(
                            comment,
                            imageViewToByte(image_text),
                            num
                    );
                    Toast.makeText(getApplicationContext(),"수정했습니다",Toast.LENGTH_SHORT).show();
                    finish();
                }
                catch (Exception e){
                    e.printStackTrace();
                }

            }
        });


    }

    private void showDialogDelete(final String num){
        AlertDialog.Builder dialog = new AlertDialog.Builder(MemoUpdateActivity.this);
        dialog.setTitle("삭제");
        dialog.setMessage("선택한 메모를 삭제하시겠습니까?");
        dialog.setPositiveButton("네", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                try {
                    sqLiteHelper.deleteDiary(num);
                    Toast.makeText(getApplicationContext(),"삭제되었습니다",Toast.LENGTH_SHORT).show();
                    finish();
                }
                catch (Exception e){
                    Log.e("error", e.getMessage());
                }
            }
        });
        dialog.setNegativeButton("아니오", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        dialog.show();
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



