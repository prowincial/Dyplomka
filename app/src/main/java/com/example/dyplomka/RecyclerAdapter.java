package com.example.dyplomka;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {
    private static final String Taq = "RecyclerView";
    private Context mContext;
    private ArrayList<Leagues> leagueslist;
    private OnNoteListener mOnNoteListener;

    public RecyclerAdapter(Context mContext, ArrayList<Leagues> leagueslist, OnNoteListener onNoteListener){
        this.mContext=mContext;
        this.leagueslist=leagueslist;
        this.mOnNoteListener=onNoteListener;
    }


    @NonNull
    @Override
    public RecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.league_item, parent, false);
        return  new ViewHolder(view, mOnNoteListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.textView.setText(leagueslist.get(position).getName());

        Glide.with(mContext).load(leagueslist.get(position).getImageUrl()).apply(RequestOptions.circleCropTransform()).into(holder.imageView);

    }

    @Override
    public int getItemCount() {
        return leagueslist.size();
    }

    public class  ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        ImageView imageView;
        TextView textView;
        OnNoteListener onNoteListener;


        public ViewHolder(@NonNull View itemView, OnNoteListener onNoteListener) {
            super(itemView);
            textView = itemView.findViewById(R.id.text_view);
            imageView = itemView.findViewById(R.id.image_view);
            this.onNoteListener = onNoteListener;
            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            onNoteListener.onNoteClick(getAdapterPosition());

        }
    }
    public interface OnNoteListener{
        void onNoteClick(int position);
    }
}
