package com.example.jobportallapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.jobportallapp.Model.Data;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Text;

public class AllJobActivity extends AppCompatActivity {


    // Получаем ссылку на базу данных Firebase
    DatabaseReference database = FirebaseDatabase.getInstance().getReference();

    private Toolbar toolbar;

    //Recycler
    private RecyclerView recyclerView;
    private Button btn_apply;

    //Firebase
    private DatabaseReference mAllJobPost;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_job);

        btn_apply = findViewById(R.id.btn_apply);

        toolbar = findViewById(R.id.posted_all_job);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("All Job Post");
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Database

        mAllJobPost = FirebaseDatabase.getInstance().getReference().child("Public database");
        mAllJobPost.keepSynced(true);

        recyclerView = findViewById(R.id.recycler_posted_all_job);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);

    }

    @Override
    protected void onStart(){
        super.onStart();

        FirebaseRecyclerOptions<Data> firebaseRecyclerOptions =
                new FirebaseRecyclerOptions.Builder<Data>()
                        .setQuery(mAllJobPost, Data.class)
                        .build();

        FirebaseRecyclerAdapter<Data, AllJobPostViewHolder> adapter = new FirebaseRecyclerAdapter<Data, AllJobPostViewHolder>(firebaseRecyclerOptions) {

            @NonNull
            @Override
            public AllJobPostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.alljobpost,parent,false);
                return new AllJobPostViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull AllJobPostViewHolder viewHolder, int i, @NonNull final Data model) {

                viewHolder.setJobTitle(model.getTitle());
                viewHolder.setJobDate(model.getDate());
                viewHolder.setJobDescription(model.getDescription());
                viewHolder.setJobSkills(model.getSkills());
                viewHolder.setJobSalary(model.getSalary());

                viewHolder.myview.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent intent = new Intent(getApplicationContext(),JobDetailsActivity.class);

                        intent.putExtra("title",model.getTitle());
                        intent.putExtra("date", model.getDate());
                        intent.putExtra("description",model.getDescription());
                        intent.putExtra("skills",model.getSkills());
                        intent.putExtra("salary",model.getSalary());
                        intent.putExtra("email",model.getEmail());

                        startActivity(intent);
                        btn_apply.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String email = intent.getStringExtra("email");
                                String subject = "Тема письма";
                                String body = "Текст письма";

                                Uri uri = Uri.parse("mailto:" + email)
                                        .buildUpon()
                                        .appendQueryParameter("subject", subject)
                                        .appendQueryParameter("body", body)
                                        .build();

                                Intent intent = new Intent(Intent.ACTION_SENDTO, uri);

                                if (intent.resolveActivity(getPackageManager()) != null) {
                                    startActivity(intent);
                                }
                            }
                        });




                    }
                });

            }


        };


        adapter.startListening();
        recyclerView.setAdapter(adapter);

    }

    public static class AllJobPostViewHolder extends RecyclerView.ViewHolder{

        View myview;

        public AllJobPostViewHolder(@NonNull View itemView) {
            super(itemView);
            myview=itemView;
        }

        public void setJobTitle(String title){
            TextView mTitle = myview.findViewById(R.id.all_job_post_title);
            mTitle.setText(title);
        }

        public void setJobDate(String date){
            TextView mDate = myview.findViewById(R.id.all_job_post_date);
            mDate.setText(date);
        }

        public void setJobDescription(String description){
            TextView mDescription = myview.findViewById(R.id.all_job_post_description);
            mDescription.setText(description);
        }

        public void setJobSkills(String skills){
            TextView mSkills = myview.findViewById(R.id.all_job_post_skills);
            mSkills.setText(skills);
        }

        public void setJobSalary(String salary){
            TextView mSalary = myview.findViewById(R.id.all_job_post_salary);
            mSalary.setText(salary);
        }


    }


}