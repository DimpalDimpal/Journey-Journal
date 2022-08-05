package com.ismt.journeyjournal.Journal;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.ismt.journeyjournal.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

public class JournalRVAdapter extends ListAdapter<Journal, JournalRVAdapter.JournalViewHolder> {

    //below code is for creating a variable for on item click listener.
    private OnItemClickListener listener;
    private Context context;

    //below code is for creating a constructor class for our adapter class.
    public JournalRVAdapter() {
        super(DIFF_CALLBACK);
    }



    //below code is for creating a call back for item of recycler view.
    private static final DiffUtil.ItemCallback<Journal> DIFF_CALLBACK = new DiffUtil.ItemCallback<Journal>() {
        @Override
        public boolean areItemsTheSame(Journal oldItem, Journal newItem) {
            return oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(Journal oldItem, Journal newItem) {
            // below line is to check the course name, description and course duration.
            return oldItem.getTitle().equals(newItem.getTitle()) &&
                    oldItem.getText().equals(newItem.getText()) &&
                    oldItem.getDate().equals(newItem.getDate());
        }
    };
    @NonNull
    @Override
    public JournalViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // below line is use to inflate our layout
        // file for each item of our recycler view.
        View item = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_row, parent, false);

        context = parent.getContext();
        return new JournalViewHolder(item);
    }

    @Override
    public void onBindViewHolder(@NonNull JournalViewHolder holder, @SuppressLint("RecyclerView") final int position) {

        // below line of code is use to set data to
        // each item of our recycler view.
        Journal journal = getJournalAt(position);
        String filePath = journal.getImage();
        File imgFile = new  File(filePath);

        if(imgFile.exists()){
            Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
            holder.jImage.setImageBitmap(myBitmap);

        }
        holder.jTitle.setText(journal.getTitle());
        holder.jDesc.setText(journal.getText());
        holder.jDate.setText(journal.getDate());

        //below line of code is to create on click listener for share
        holder.share.setOnClickListener(view -> {
            StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
            StrictMode.setVmPolicy(builder.build());
            Drawable drawable = holder.jImage.getDrawable();
            String text = holder.jTitle.getText().toString();
            Bitmap bitmap = drawableToBitmap(drawable);//put here your image id
            String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/LatestShare.png";
            OutputStream out = null;
            File file = new File(path);
            try {
                out = new FileOutputStream(file);
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
                out.flush();
                out.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            path = file.getPath();
            Uri bmpUri = Uri.parse("file://" + path);
            Intent shareIntent = new Intent();
            shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Test");
            shareIntent.putExtra(Intent.EXTRA_TEXT, text);
            shareIntent.setType("image/png");
            shareIntent.putExtra(Intent.EXTRA_STREAM, bmpUri);
            shareIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(Intent.createChooser(shareIntent, "Share with"));

        });

    }

    //below line of code is for converting the drawable to bitmap
    public static Bitmap drawableToBitmap (Drawable drawable) {
        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable)drawable).getBitmap();
        }

        int width = drawable.getIntrinsicWidth();
        width = width > 0 ? width : 1;
        int height = drawable.getIntrinsicHeight();
        height = height > 0 ? height : 1;
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bitmap;
    }

    //below code is for creating a method to get journal modal for a specific position.
    public Journal getJournalAt(int position) {
        return getItem(position);
    }

    @Override
    public int getItemCount() {
        return super.getItemCount();
    }

    public class JournalViewHolder extends RecyclerView.ViewHolder {
        //below line of code is of view holder class to create a variable for each view.
        TextView jTitle, jDesc, jDate;
        ImageView jImage;
        ImageButton share;

        JournalViewHolder(@NonNull View itemView) {
            super(itemView);
            //below code is for initializing each view of our recycler view.
            jTitle = itemView.findViewById(R.id.jTitle);
            jDesc = itemView.findViewById(R.id.jDesc);
            jDate = itemView.findViewById(R.id.jDate);
            jImage = itemView.findViewById(R.id.loadRImage);
            share = itemView.findViewById(R.id.icShare);

            //below code is for adding on click listener for each item of recycler view.
            itemView.setOnClickListener(v -> {
                // inside on click listener we are passing
                // position to our item of recycler view.
                int position = getAdapterPosition();
                if (listener != null && position != RecyclerView.NO_POSITION) {
                    listener.onItemClick(getItem(position));
                }
            });

        }

    }


    public interface OnItemClickListener {
        void onItemClick(Journal journal);
    }
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
}
