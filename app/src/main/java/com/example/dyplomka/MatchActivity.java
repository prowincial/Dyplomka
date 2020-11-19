package com.example.dyplomka;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.chaquo.python.PyObject;
import com.chaquo.python.Python;
import com.chaquo.python.android.AndroidPlatform;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Arrays;

public class MatchActivity extends AppCompatActivity {

    private ArrayList<Team> teamsList;
    private Context mContext;
    private int pos1 =0;
    private int pos2 =2;
    private PyObject pyObject;
    private Python py;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match);
        final TextView liga = (TextView) findViewById(R.id.liga);
        Intent intent = getIntent();
        String league =intent.getStringExtra("some");
        liga.setText(league);
        getLeagueTeams();
        firstLogo();
        secondLogo();

        if(!Python.isStarted())
            Python.start(new AndroidPlatform(this));
        final TextView res1 = (TextView) findViewById(R.id.res1);
        final TextView res2 = (TextView) findViewById(R.id.res2);
        final TextView res3 = (TextView) findViewById(R.id.res3);
        final TextView zheka1 = (TextView) findViewById(R.id.name1);
        final TextView zheka3 = (TextView) findViewById(R.id.name2);

        Button bu = (Button) findViewById(R.id.knopka);

        bu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                init();
                PyObject obj = pyObject.callAttr("result", zheka1.getText().toString(), zheka3.getText().toString());
                ArrayList<String> list = new ArrayList<String>(Arrays.asList(obj.toString().replaceAll("[\\[\\]()]","").split(",")));
                res1.setText(list.get(0));
                res2.setText(list.get(1));
                res3.setText(list.get(2));
            }
        });
    }

    private void init(){
        py = Python.getInstance();
        pyObject  = py.getModule("logika");
    }

    private void getLeagueTeams(){
        teamsList = new ArrayList<>();
        mContext = getApplicationContext();
        FirebaseFirestore firFir = FirebaseFirestore.getInstance();
        firFir.collection("Premier League")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {

                            for (QueryDocumentSnapshot document : task.getResult()) {
                                teamsList.add(new Team (document.getString("name"), document.getString("link")));
                                Log.w("Document",document.getId() + " => " + document.getData());
                            }
                        } else {
                            Log.w("Document", "Error getting documents.", task.getException());
                        }
                        TextView zheka1 = (TextView) findViewById(R.id.name1);
                        zheka1.setText(teamsList.get(pos1).getName());

                        ImageView zheka2 = (ImageView) findViewById(R.id.logo1);
                        final ProgressBar progressBar1 = (ProgressBar) findViewById(R.id.progressBar1);
                        Glide.with(mContext).load(teamsList.get(pos1).getLink()).listener(new RequestListener<Drawable>() {
                            @Override
                            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                progressBar1.setVisibility(View.GONE);
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                progressBar1.setVisibility(View.GONE);
                                return false;
                            }
                        }).into(zheka2);
                        TextView zheka3 = (TextView) findViewById(R.id.name2);
                        zheka3.setText(teamsList.get(pos2).getName());
                        ImageView zheka4 = (ImageView) findViewById(R.id.logo2);
                        final ProgressBar progressBar2 = (ProgressBar) findViewById(R.id.progressBar2);
                        Glide.with(mContext).load(teamsList.get(pos2).getLink()).listener(new RequestListener<Drawable>() {
                            @Override
                            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                progressBar2.setVisibility(View.GONE);
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                progressBar2.setVisibility(View.GONE);
                                return false;
                            }
                        }).into(zheka4);
                    }
                });
    }

    private void firstLogo(){
        final ImageView zheka2 = (ImageView) findViewById(R.id.logo1);
        final TextView zheka1 = (TextView) findViewById(R.id.name1);

        zheka2.setOnTouchListener(new OnSwipeTouchListener(this){
            @Override
            public void onSwipeLeft() {
                final ProgressBar progressBar1 = (ProgressBar) findViewById(R.id.progressBar1);
                pos1-=1;
                if (pos1 < 0){
                    pos1=teamsList.size();
                }
                Glide.with(mContext).load(teamsList.get(pos1).getLink()).into(zheka2);
                zheka1.setText(teamsList.get(pos1).getName());
            }

            @Override
            public void onSwipeRight() {
                final ProgressBar progressBar1 = (ProgressBar) findViewById(R.id.progressBar1);
                pos1+=1;
                if (pos1 >19){
                    pos1=0;
                }
                Glide.with(mContext).load(teamsList.get(pos1).getLink()).into(zheka2);
                zheka1.setText(teamsList.get(pos1).getName());
            }
        });
    }

    private void secondLogo(){
        final ImageView zheka2 = (ImageView) findViewById(R.id.logo2);
        final TextView zheka1 = (TextView) findViewById(R.id.name2);
        zheka2.setOnTouchListener(new OnSwipeTouchListener(this){
            @Override
            public void onSwipeLeft() {
                final ProgressBar progressBar1 = (ProgressBar) findViewById(R.id.progressBar1);
                pos2-=1;
                if (pos2 < 0){
                    pos2=teamsList.size();
                }
                Glide.with(mContext).load(teamsList.get(pos2).getLink()).into(zheka2);
                zheka1.setText(teamsList.get(pos2).getName());
            }

            @Override
            public void onSwipeRight() {
                final ProgressBar progressBar1 = (ProgressBar) findViewById(R.id.progressBar1);
                pos2+=1;
                if (pos2 >19){
                    pos2=0;
                }
                Glide.with(mContext).load(teamsList.get(pos2).getLink()).into(zheka2);
                zheka1.setText(teamsList.get(pos2).getName());
            }


        });
    }




}