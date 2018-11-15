package com.example.adityapatel.note;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private static final String TAG = "RecyclerViewAdapter";
    private Context mcontext;
   // private final NoteDataStore dataStore = NoteDataStoreImpl.sharedInstance();
    // private static FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    // private static DatabaseReference mDatabase_get = FirebaseDatabase.getInstance().getReference().child("user-notes").child(user.getUid()) ;
   // private ProgressBar mProgressBar;

    private final List<NoteData> noteList;

    public RecyclerViewAdapter(Context context) {
        noteList = new ArrayList<>();
        mcontext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, final int i) {
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
                final int position = holder.getAdapterPosition();
                //Toast.makeText(mcontext, "onlongclick", Toast.LENGTH_SHORT).show();


                final int ii = position;
                android.support.v7.app.AlertDialog.Builder builder;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    builder = new android.support.v7.app.AlertDialog.Builder(mcontext, android.R.style.Theme_Material_Dialog_Alert);
                } else {
                    builder = new android.support.v7.app.AlertDialog.Builder(mcontext);
                }
                builder.setTitle("Delete entry")
                        .setMessage("Are you sure you want to delete this entry?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                //dataStore.deleteNote(ii);
                                notifyDataSetChanged();
                                Toast.makeText(mcontext,"Item was deleted",Toast.LENGTH_SHORT).show();
                                //handle menu1 click
                            }
                        })
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // do nothing
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();


                return true;


            }
        });
        return holder;
    }

    void addNotes(List<NoteData> list) {
        int currentSize = noteList.size();
        noteList.clear();
        notifyItemRangeRemoved(0, currentSize);
        noteList.addAll(list);
        notifyItemRangeInserted(0, list.size());
    }


    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {

        try {
            //dataStore.storeNotes(mcontext);
            holder.note_Name.setText(noteList.get(position).getNote_name());
            holder.note_Text.setText(noteList.get(position).getNote_content());
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
        return noteList.size();
       // Log.d(TAG, Double.toString(dataStore.getNotes().get(0).getLatitude()));

        /*Log.d(TAG, "getItemCount: " + dataStore.getNotes().size());
        int n = dataStore.getNotes().size();
        return dataStore.getNotes().size();*/
    }

}

