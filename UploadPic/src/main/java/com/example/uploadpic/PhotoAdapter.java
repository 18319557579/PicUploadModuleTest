package com.example.uploadpic;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

/**
 * @desc
 * @info Created by Steven on 2021-06-18
 */
public class PhotoAdapter extends RecyclerView.Adapter<PhotoAdapter.ViewHolder>{

    List<ImageBean> imageBeanList;

    public int choseIndex = -1;

    public PhotoAdapter(List<ImageBean> imageBeanList) {
        this.imageBeanList = imageBeanList;
    }

    public void setList(List<ImageBean> imageBeanList) {
        this.imageBeanList = imageBeanList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_photo, parent, false);
        ViewHolder holder = new ViewHolder(view);

        return holder;
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ImageBean imageBean = imageBeanList.get(position);
        Glide.with(AppGlobal.getInstance()).load(imageBean.getAbFilePath()).into(holder.imgFile);

//        ImageBean bean = imageBeanList.get(position);
        if (position != choseIndex) {
            holder.tvPoint.setBackgroundResource(R.drawable.check_off);
            holder.imgFile.setColorFilter(null);  //设置选中加深效果

        }else {
            holder.tvPoint.setBackgroundResource(R.drawable.check_on);
            holder.imgFile.setColorFilter(R.color.black);  //设置选中加深效果
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Daisy", "点击了位置：" + position);

                if (choseIndex == -1) {
                    choseIndex = position;
                    notifyItemChanged(position);
                } else if (choseIndex == position) {
                    choseIndex = -1;
                    notifyItemChanged(position);
                } else {
                    notifyItemChanged(choseIndex);
                    choseIndex = position;
                    notifyItemChanged(choseIndex);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return imageBeanList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgFile;
        TextView tvPoint;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgFile = itemView.findViewById(R.id.img_file);
            tvPoint = itemView.findViewById(R.id.tv_point);
        }
    }

    public String getUri() {
        if (choseIndex == -1)
            return null;

        return imageBeanList.get(choseIndex).getAbFilePath();
    }
}
