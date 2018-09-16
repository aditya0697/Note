package com.example.adityapatel.note;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private static final String TAG = "RecyclerViewAdapter";
    private Context mcontext;
    private final NoteDataStore dataStore;


    public RecyclerViewAdapter(Context context) {
        // note = mnote_name;
        mcontext = context;
        dataStore = NoteDataStoreImpl.sharedInstance(mcontext);

    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        Log.d("recycle", "in recycleview");
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_listitem, parent, false);
        final ViewHolder holder = new ViewHolder(view);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                /*String noteAtPostionTitle = dataStore.getNotes(mcontext).get(position).getNote_name();
                String noteAtPositionContent = dataStore.getNotes(mcontext).get(position).getNote_content();*/
                Intent intent = new Intent(mcontext, Edit_note.class);
                intent.putExtra("position", position);
                mcontext.startActivity(intent);
            }
        });

        holder.parent_Layout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                int position = holder.getAdapterPosition();
                Toast.makeText(mcontext, "onlongclick", Toast.LENGTH_SHORT).show();
                dataStore.deleteNote(position);
                notifyDataSetChanged();
                return true;
            }
        });
        return holder;
    }


    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {

        try {
            //dataStore.storeNotes(mcontext);
            holder.note_Name.setText(dataStore.getNotes().get(position).getNote_name());
            holder.note_Text.setText(dataStore.getNotes().get(position).getNote_content());
        } catch (NullPointerException e) {
            Log.d("nullpointer", e.getMessage());
        }
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView note_Name;
        TextView note_Date;
        TextView note_Text;
        ConstraintLayout parent_Layout;

        public ViewHolder(View itemView) {
            super(itemView);
            note_Name = itemView.findViewById(R.id.noteName);
            note_Date = itemView.findViewById(R.id.noteDate);
            note_Text = itemView.findViewById(R.id.noteText);
            parent_Layout = itemView.findViewById(R.id.parent_layout);

        }
    }

    @Override
    public int getItemCount() {
        Log.d(TAG, "getItemCount: " + dataStore.getNotes().size());
        return dataStore.getNotes().size();
    }

}

