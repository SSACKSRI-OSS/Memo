package com.ssacksri.memo;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class MemoListAdapter extends BaseAdapter {

    private Context context;
    private int layout;
    private ArrayList<MemoModel> diaryList;

    public MemoListAdapter(Context context, int layout, ArrayList<MemoModel> diaryList) {
        this.context = context;
        this.layout = layout;
        this.diaryList = diaryList;
    }

    @Override
    public int getCount() {
        return diaryList.size();
    }

    @Override
    public Object getItem(int i) {
        return diaryList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    private class ViewHolder{
        TextView title ,date ;
        ImageView image;

    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        View row = view;
        ViewHolder holder = new ViewHolder();
        if (row ==null){
            LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(layout,null);
            holder.title = row.findViewById(R.id.title);
            holder.date = row.findViewById(R.id.date);
            holder.image = row.findViewById(R.id.image);
            row.setTag(holder);
        } else {
            holder = (ViewHolder)row.getTag();
        }

        MemoModel model = diaryList.get(i);
        byte[] Image = model.getImage();
        Bitmap bitmap = BitmapFactory.decodeByteArray(Image,0,Image.length);
        holder.image.setImageBitmap(bitmap);
        holder.title.setText(model.getComment());
        holder.date.setText(model.getDate());

        return row;
    }


}
