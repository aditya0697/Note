package com.example.adityapatel.note;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

public class ImageRecyclerViewAdapter extends RecyclerView.Adapter<ImageRecyclerViewAdapter.ViewHolder> {

    private List<String> mImagePaths = new ArrayList<String>();
    private LayoutInflater mLayoutInflater;
    private Context context;

    public ImageRecyclerViewAdapter(List<String> mImagePaths, Context context) {
        this.mImagePaths = mImagePaths;
        this.mLayoutInflater = LayoutInflater.from(context);
        this.context = context;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = mLayoutInflater.inflate(R.layout.note_image_list_item,parent,false);
        final ViewHolder holder = new ImageRecyclerViewAdapter.ViewHolder(view);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                /*String noteAtPostionTitle = dataStore.getNotes(mcontext).get(position).getNote_name();
                String noteAtPositionContent = dataStore.getNotes(mcontext).get(position).getNote_content();*/
                Intent intent = new Intent(context, DisplayImageActivity.class);
                intent.putExtra("picture", mImagePaths.get(position));
                context.startActivity(intent);
            }
        });
        return holder;

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        Bitmap imageBitmap = BitmapFactory.decodeFile(mImagePaths.get(i));
        viewHolder.imageView.setImageBitmap(imageBitmap);
    }

    void addImagePath(String path) {
       // mImagePaths.add(path);
        //notifyItemRangeInserted(0, list.size());
        notifyDataSetChanged();
        //notifyItemInserted(mImagePaths.size()-1);
    }

    @Override
    public int getItemCount() {
        return mImagePaths.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        LinearLayout parentLayout;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.note_image_item);
            parentLayout = itemView.findViewById(R.id.parent_layout);
        }
    }
}
