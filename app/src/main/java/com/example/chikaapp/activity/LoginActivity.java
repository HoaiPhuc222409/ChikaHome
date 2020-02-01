package com.example.chikaapp.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.example.chikaapp.R;
import com.example.chikaapp.SharedPreferencesUtils;
import com.example.chikaapp.api.APILogin;
import com.example.chikaapp.api.ApiRetrofit;
import com.example.chikaapp.databinding.ActivityLoginBinding;
import com.example.chikaapp.model.Token;
import com.example.chikaapp.mqtt.MQTTService;
import com.example.chikaapp.request.LoginRequest;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.util.Locale;

import cc.cloudist.acplibrary.ACProgressConstant;
import cc.cloudist.acplibrary.ACProgressPie;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener, MQTTService.listener{

    APILogin apiLogin;


    ImageView imgSetting, imgShowPass, imgHidePass;
    Button btnLogin, btnTest1, btnTest2;
    TextView tvForgetPassword;

    EditText edtUsername, edtPassword;

    ACProgressPie dialog;
    MQTTService mqttService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initialize();

        dialog = new ACProgressPie.Builder(this)
                .ringColor(Color.WHITE)
                .pieColor(Color.WHITE)
                .updateType(ACProgressConstant.PIE_AUTO_UPDATE)
                .build();
        dialog.setCancelable(true);

        edtPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (edtPassword.getText().toString().length() == 0) {
                    imgShowPass.setVisibility(View.INVISIBLE);
                    imgHidePass.setVisibility(View.INVISIBLE);

                } else {
                    imgShowPass.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


    }

    //Hiện menu setting
    private void showPopupMenu() {
        PopupMenu popupMenu = new PopupMenu(LoginActivity.this, imgSetting);
        popupMenu.getMenuInflater().inflate(R.menu.setting_menu, popupMenu.getMenu());

        popupMenu.setOnMenuItemClickListener(item -> {
            switch (item.getItemId()) {
                case R.id.menuEng: {
                    changeLanguage("en");
                    break;
                }
                case R.id.menuVietnamese: {
                    changeLanguage("vi");
                    break;
                }
            }
            return false;
        });
        popupMenu.show();
    }

    public void initialize(){
        imgSetting=findViewById(R.id.imgSetting);
        btnLogin=findViewById(R.id.btnLogin);
        btnTest1=findViewById(R.id.btnTest1);
        btnTest2=findViewById(R.id.btnTest2);
        imgHidePass=findViewById(R.id.imgHidePass);
        imgShowPass=findViewById(R.id.imgShowPass);
        edtUsername=findViewById(R.id.edtUserLogin);
        edtPassword=findViewById(R.id.edtPassword);
        tvForgetPassword=findViewById(R.id.tvForgetPassword);

        btnLogin.setOnClickListener(this);
        btnTest1.setOnClickListener(this);
        btnTest2.setOnClickListener(this);
        tvForgetPassword.setOnClickListener(this);
        imgShowPass.setOnClickListener(this);
        imgHidePass.setOnClickListener(this);
        imgSetting.setOnClickListener(this);
        mqttService=MQTTService.getInstance(getApplicationContext(), this);

    }

    public void changeLanguage(String language) {
        Locale locale = new Locale(language);
        Configuration configuration = new Configuration();
        configuration.locale = locale;
        getBaseContext().getResources().updateConfiguration(configuration, getBaseContext().getResources().getDisplayMetrics());
        Intent intent = new Intent(LoginActivity.this, LoginActivity.class);
        startActivity(intent);
    }


    public void getToken(String username, String password) {
        dialog.show();
        Gson gson = new GsonBuilder().setLenient().create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ApiRetrofit.URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        apiLogin = retrofit.create(APILogin.class);

        Call<Token> call = apiLogin.loginAccount(new LoginRequest(username, password));
        call.enqueue(new Callback<Token>() {
            @Override
            public void onResponse(Call<Token> call, Response<Token> response) {
                Token token = response.body();
                if (token != null) {
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    dialog.dismiss();
                    SharedPreferencesUtils.saveToken(getApplicationContext(), token);
                    startActivity(intent);
                } else {
                    dialog.dismiss();
                    Toast.makeText(LoginActivity.this, "Login Fail", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Token> call, Throwable t) {
                dialog.dismiss();
                Log.i("error3",t.toString());
                Toast.makeText(LoginActivity.this, R.string.check_connect, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnLogin:{
                String user = edtUsername.getText().toString();
                String password = edtPassword.getText().toString();

                if (user.isEmpty()){
                    edtUsername.setError("Not available");
                } else if (password.isEmpty()){
                    edtPassword.setError("Not available");
                } else {
                    getToken(user, password);
                }
                break;
            }
            case R.id.imgHidePass:{
                imgHidePass.setVisibility(View.INVISIBLE);
                imgShowPass.setVisibility(View.VISIBLE);

                edtPassword.setTransformationMethod(new PasswordTransformationMethod());
                edtPassword.setSelection(edtPassword.getText().length());

                break;
            }
            case R.id.imgShowPass:{
                imgShowPass.setVisibility(View.INVISIBLE);
                imgHidePass.setVisibility(View.VISIBLE);

                edtPassword.setTransformationMethod(null);
                edtPassword.setSelection(edtPassword.getText().length());

                break;
            }
            case R.id.tvForgetPassword:{
                Intent intent1 = new Intent(LoginActivity.this, ForgetPasswordActivity.class);
                startActivity(intent1);

                break;
            }
            case R.id.imgSetting:{
                showPopupMenu();
                break;
            }
            case R.id.btnTest1:{
                if (btnTest1.getText().toString().equals("ON")) {
                    mqttService.publish("0c38a97d-1564-4707-935c-18b4e9bcb0db", "0");
                    Toast.makeText(this, "ok", Toast.LENGTH_SHORT).show();
                    btnTest1.setText("OFF");
                } else {
                    mqttService.publish("0c38a97d-1564-4707-935c-18b4e9bcb0db", "1");
                    Toast.makeText(this, "ok", Toast.LENGTH_SHORT).show();
                    btnTest1.setText("ON");
                }
                break;
            }
            case R.id.btnTest2:{
                if (btnTest2.getText().toString().equals("ON")) {
                    mqttService.publish("7f704fdf-fa4b-44e2-b359-5ef19294196a", "0");
                    Toast.makeText(this, "ok", Toast.LENGTH_SHORT).show();
                    btnTest2.setText("OFF");
                } else {
                    mqttService.publish("7f704fdf-fa4b-44e2-b359-5ef19294196a", "1");
                    Toast.makeText(this, "ok", Toast.LENGTH_SHORT).show();
                    btnTest2.setText("ON");
                }
                break;
            }
        }
    }

    @Override
    public void onReceive(String mess) {
        if(mess.equals("1")){
            btnTest1.setText("ON");
        }else btnTest1.setText("OFF");
    }
}
