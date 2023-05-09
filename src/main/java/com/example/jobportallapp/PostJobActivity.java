package com.example.jobportallapp;

import androidx.annotation.NonNull;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.jobportallapp.Model.Data;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Text;

public class PostJobActivity extends AppCompatActivity {
    private FloatingActionButton fabBtn;
    //Recycler view..

    private Toolbar toolbar;
    private RecyclerView recyclerView;

    //Firebase..
    private FirebaseAuth mAuth;

    private DatabaseReference jobPostDataBase;//Ссылка на базу данных

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_job);

        toolbar = findViewById(R.id.toolbar_post_job);
        setSupportActionBar(toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Job Portal App");

        fabBtn = findViewById(R.id.fab_add);

        mAuth =FirebaseAuth.getInstance();

        FirebaseUser mUser = mAuth.getCurrentUser();
        String uId = mUser.getUid();

        // Иницализация jobPostDataBase (ссылку базы данных) и название базы данных ("Job Post")
        jobPostDataBase = FirebaseDatabase.getInstance().getReference().child("Job Post").child(uId);

        //Иниацализация списка вакансии по id
        recyclerView = findViewById(R.id.recycler_job_post_id);

        // Добавление карусели
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setStackFromEnd(true);
        layoutManager.setReverseLayout(true);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);

        fabBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), InsertJobPostActivity.class));
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseRecyclerOptions<Data> options =
                new FirebaseRecyclerOptions.Builder<Data>()
                        .setQuery(jobPostDataBase, Data.class)
                        .build();

        FirebaseRecyclerAdapter<Data,MyViewHolder> adapter = new FirebaseRecyclerAdapter<Data, MyViewHolder>(options){

            @NonNull
            @Override
            public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.job_post_item, parent, false);
                return new MyViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull MyViewHolder viewHolder, int position, @NonNull Data model) {
                // Здесь реализуйте код для заполнения данных в ViewHolder
                viewHolder.setJobTitle(model.getTitle());
                viewHolder.setJobDate(model.getDate());
                viewHolder.setJobDescription(model.getDescription());
                viewHolder.setJobSkills(model.getSkills());
                viewHolder.setJobSalary(model.getSalary());
            }
        };

        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        View myView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            myView = itemView;
        }

        public void setJobTitle(String title){
            TextView mTitle = myView.findViewById(R.id.job_title);
            mTitle.setText(title);
        }
        public void setJobDate(String date){
            TextView mDate = myView.findViewById(R.id.job_date);
            mDate.setText(date);
        }
        public void setJobDescription(String description){
            TextView mDescription = myView.findViewById(R.id.job_description);
            mDescription.setText(description);
        }
        public void setJobSkills(String skills){
            TextView mSkills = myView.findViewById(R.id.job_skills);
            mSkills.setText(skills);
        }
        public void setJobSalary(String salary){
            TextView mSalary = myView.findViewById(R.id.job_salary);
            mSalary.setText(salary);
        }
    }
}