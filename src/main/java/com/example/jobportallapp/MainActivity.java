package com.example.jobportallapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;


/**
 *Класс MainActivity отвечает за главную активность приложения.
 *Он содержит реализацию кнопок на главном меню (activity_main.xml).
 *Эта активность позволяет пользователю войти в систему или перейти на страницу регистрации.
 *@author Taspolat
 */
public class MainActivity extends AppCompatActivity {

    private EditText email;
    private EditText password;
    private Button btnLogin;
    private Button btnRegistration;

    /**
     * Firebase mAuth создаем переменную класса Firebase для использования авторизации
     */
    private FirebaseAuth mAuth;

    //Progress dialog
    private ProgressDialog mDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();

        if(mAuth.getCurrentUser()!=null){
            startActivity(new Intent(getApplicationContext(),HomeActivity.class));
        }

        mDialog = new ProgressDialog(this);
        loginFunction();
    }
    private void  loginFunction(){
        email = findViewById(R.id.email_login);
        password = findViewById(R.id.login_password);
        btnLogin = findViewById(R.id.btn_login);
        btnRegistration = findViewById(R.id.btn_regi);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mEmail = email.getText().toString().trim();
                String pass = password.getText().toString().trim();
                if(TextUtils.isEmpty(mEmail)){
                    email.setError("Заполните поле");
                }
                if(TextUtils.isEmpty(pass)){
                    email.setError("Заполните поле");
                }

                mDialog.setMessage("В процессе..");
                mDialog.show();

                mAuth.signInWithEmailAndPassword(mEmail,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(getApplicationContext(),"Авторизация прошла успешно",Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(),HomeActivity.class));
                        }else {
                            Toast.makeText(getApplicationContext(),"Ошибка входа",Toast.LENGTH_SHORT).show();
                        }
                        mDialog.dismiss();
                    }
                });
            }
        });
        btnRegistration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),RegistrationActivity.class));
            }
        });

    }
}