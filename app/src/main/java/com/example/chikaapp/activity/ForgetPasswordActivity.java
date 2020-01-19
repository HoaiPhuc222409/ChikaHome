package com.example.chikaapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.example.chikaapp.databinding.ActivityForgetPasswordBinding;
import com.example.chikaapp.R;
import com.example.chikaapp.databinding.ActivityForgetPasswordBinding;

public class ForgetPasswordActivity extends AppCompatActivity {
    private static final String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
    ActivityForgetPasswordBinding binding;
    String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil.setContentView(this, R.layout.activity_forget_password);

        binding.tvSignIn.setOnClickListener(v -> {
            Intent intent=new Intent(ForgetPasswordActivity.this,LoginActivity.class);
            startActivity(intent);
        });

        binding.btnSendEmail.setOnClickListener(v -> {
            if (binding.edtEmail.getText().toString().length()==0)
            {
                binding.tvConfirmEmail1.setVisibility(View.VISIBLE);
                Toast.makeText(this, getResources().getText(R.string.check_email_again), Toast.LENGTH_SHORT).show();
            }
            else {
                if (email.matches(EMAIL_PATTERN)){
                    Toast.makeText(this, getResources().getText(R.string.ok), Toast.LENGTH_SHORT).show();
                    Intent intent =new Intent(ForgetPasswordActivity.this,LoginActivity.class);
                    startActivity(intent);

                }
                else {
                    Toast.makeText(this, getResources().getText(R.string.check_email_again), Toast.LENGTH_SHORT).show();
                    binding.edtEmail.requestFocus();
                }
            }

        });



        binding.edtEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                email = binding.edtEmail.getText().toString();
                if (email.matches(EMAIL_PATTERN)){
                    binding.tvConfirmEmail1.setVisibility(View.INVISIBLE);
                    binding.tvConfirmEmail.setVisibility(View.VISIBLE);
                }
                else {
                    binding.tvConfirmEmail.setVisibility(View.INVISIBLE);
                    binding.tvConfirmEmail1.setVisibility(View.VISIBLE);
                }
            }
        });
    }
}
