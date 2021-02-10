
package com.baykus.messageapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.baykus.messageapp.Model.Users;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.Serializable;
import java.util.HashMap;

public class LoginActivity extends AppCompatActivity {
    private EditText editTextPassword, editTextEmail;
    private TextView textViewSignUp, textViewPassword;
    private Button buttonLogin;
    public Switch switch1;
    private ImageView imageViewLogin;
    private FirebaseUser firebaseUser;
    private DatabaseReference reference;
    private FirebaseAuth mAuth;
    private ProgressDialog progressDialog;
    private Users users;
    private Animation animationDownToUp, animationUpToDown, animationLeftToRight, animationRightToLeft, animationScale, animationScaleButton;
    private boolean durum;

    @Override
    protected void onStart() {
        super.onStart();


        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser != null) {
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        buttonLogin = findViewById(R.id.buttonLogin);
        imageViewLogin = findViewById(R.id.imageViewLogin);
        textViewSignUp = findViewById(R.id.textViewSignUp);
        textViewPassword = findViewById(R.id.textViewPassword);
        switch1 = findViewById(R.id.switch1);
        animationDownToUp = AnimationUtils.loadAnimation(LoginActivity.this, R.anim.down_to_up);
        animationUpToDown = AnimationUtils.loadAnimation(LoginActivity.this, R.anim.up_to_down);
        animationLeftToRight = AnimationUtils.loadAnimation(LoginActivity.this, R.anim.left_to_right);
        animationRightToLeft = AnimationUtils.loadAnimation(LoginActivity.this, R.anim.right_to_left);
        animationScale = AnimationUtils.loadAnimation(LoginActivity.this, R.anim.scale);
        animationScaleButton = AnimationUtils.loadAnimation(LoginActivity.this, R.anim.scale_button);

        buttonLogin.setAnimation(animationScale);
        buttonLogin.setAnimation(animationDownToUp);
        imageViewLogin.setAnimation(animationUpToDown);

        textViewSignUp.setAnimation(animationScale);


        switch1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    durum = true;

                } else {

                    getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    durum = false;
                }

            }
        });

        mAuth = FirebaseAuth.getInstance();

        editTextEmail.setAnimation(animationRightToLeft);
        editTextPassword.setAnimation(animationLeftToRight);
        textViewPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(LoginActivity.this, "Unutmasaydın:(", Toast.LENGTH_SHORT).show();

                startActivity(new Intent(LoginActivity.this, ResetPasswordActivity.class));

            }
        });


        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                progressDialog = new ProgressDialog(LoginActivity.this);
                progressDialog.setTitle("Oturum Açılıyor");
                progressDialog.setMessage("Hesabınıza Giriş Yapılıyor.Lütfen Bekleyiniz...");
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.show();

                String user_passwordl = editTextPassword.getText().toString().trim();
                String user_emaill = editTextEmail.getText().toString().trim();


                if (TextUtils.isEmpty(user_passwordl) || TextUtils.isEmpty(user_emaill)) {
                    Toast.makeText(LoginActivity.this, "Lütfen boş alanları doldurunuz...", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                } else {

                    mAuth.signInWithEmailAndPassword(user_emaill, user_passwordl)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {


                                        buttonLogin.setAnimation(animationScaleButton);
                                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);
                                        progressDialog.dismiss();
                                        finish();
                                    } else {
                                        Toast.makeText(LoginActivity.this, "Giriş başarısız...", Toast.LENGTH_SHORT).show();
                                        progressDialog.dismiss();
                                    }
                                }
                            });


                }

            }
        });

        siginUp();

    }


    public void siginUp() {
        textViewSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                alertShow();

            }
        });

    }


    private void alertShow() {
        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.alert_sign_up, null);

        final EditText editTextUserNameS = view.findViewById(R.id.editTextUserNameS);
        final EditText editTextPasswordS = view.findViewById(R.id.editTextPasswordS);
        final EditText editTextEmailS = view.findViewById(R.id.editTextEmailS);
        final Button buttonSiginUp = view.findViewById(R.id.buttonSignUp);

        AlertDialog.Builder adBuilder = new AlertDialog.Builder(this);
        adBuilder.setTitle("Hyena");
        adBuilder.setView(view);


        adBuilder.setNegativeButton("İptal", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });


        buttonSiginUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String user_name = editTextUserNameS.getText().toString();
                String user_email = editTextEmailS.getText().toString().trim();
                String user_password = editTextPasswordS.getText().toString().trim();


                if (TextUtils.isEmpty(user_name) || TextUtils.isEmpty(user_password) || TextUtils.isEmpty(user_email)) {
                    Toast.makeText(LoginActivity.this, "Lütfen boş alanları doldurunuz...", Toast.LENGTH_SHORT).show();

                } else if (user_password.length() < 6) {
                    Toast.makeText(LoginActivity.this, "Şifreniz minimum 6 karakter olmalıdır...", Toast.LENGTH_SHORT).show();

                } else {
                    progressDialog = new ProgressDialog(LoginActivity.this);
                    progressDialog.setTitle("Hyena");
                    progressDialog.setMessage("Hesabınız oluşturuluyor,lütfen bekleyiniz...");
                    progressDialog.setCanceledOnTouchOutside(false);
                    progressDialog.show();
                    //Yeni kullanici ekleme
                    register(user_name, user_email, user_password);


                }


            }


        });
        adBuilder.create().show();
    }

    private void register(String username, String email, String password) {

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser firebaseUser = mAuth.getCurrentUser();
                            String userId = firebaseUser.getUid();

                            reference = FirebaseDatabase.getInstance().getReference("Users").child(userId);

                            HashMap<String, String> hashMap = new HashMap<>();
                            hashMap.put("id", userId);
                            hashMap.put("userName", username);
                            hashMap.put("imageURL", "default");
                            hashMap.put("search", username.toLowerCase());

                            reference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                    if (task.isSuccessful()) {
                                        progressDialog.dismiss();
                                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);
                                        finish();
                                    }

                                }
                            });

                        } else {

                            Toast.makeText(LoginActivity.this, "Kayıt Başarısız...",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }



}