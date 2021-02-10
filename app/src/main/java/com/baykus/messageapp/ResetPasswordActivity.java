package com.baykus.messageapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ResetPasswordActivity extends AppCompatActivity {

    private Toolbar toolbarPassword;
    private EditText send_email;
    private Button buttonReset;

    private ProgressDialog progressDialog;

    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
        toolbarPassword = findViewById(R.id.toolbar_password);

        buttonReset = findViewById(R.id.buttonReset);
        send_email = findViewById(R.id.send_email);


        setSupportActionBar(toolbarPassword);
        getSupportActionBar().setTitle("Şifre Güncelleme");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        firebaseAuth = FirebaseAuth.getInstance();
        buttonReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String email = send_email.getText().toString();

                if (email.equals("")){

                    Toast.makeText(ResetPasswordActivity.this, "Lütfen geçerli bir mail adresi giriniz", Toast.LENGTH_SHORT).show();

                }else {

                    progressDialog = new ProgressDialog(ResetPasswordActivity.this);
                    progressDialog.setTitle("Oturum Açılıyor");
                    progressDialog.setMessage("Hesabınıza Giriş Yapılıyor.Lütfen Bekleyiniz...");
                    progressDialog.setCanceledOnTouchOutside(false);
                    progressDialog.show();

                    firebaseAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){

                                Toast.makeText(ResetPasswordActivity.this, "Lütfen E Postanızı kontrol edin.", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(ResetPasswordActivity.this,LoginActivity.class));
                                progressDialog.dismiss();
                            }else{
                                String error = task.getException().getMessage();
                                Toast.makeText(ResetPasswordActivity.this, error, Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();
                            }
                        }
                    });
                }

            }
        });
    }
}