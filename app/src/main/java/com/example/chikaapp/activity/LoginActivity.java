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
import android.widget.PopupMenu;
import android.widget.ProgressBar;
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

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.util.Locale;

import cc.cloudist.acplibrary.ACProgressConstant;
import cc.cloudist.acplibrary.ACProgressPie;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginActivity extends AppCompatActivity {

    APILogin apiLogin;


    ActivityLoginBinding binding;
    ACProgressPie dialog;
    Button light1, light2;
    MQTTService mqttService;


    private final String SharedReferencesFile = "sharedReferencesFile";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login);

        Intent intent = new Intent(LoginActivity.this, MainActivity.class);

        dialog = new ACProgressPie.Builder(this)
                .ringColor(Color.WHITE)
                .pieColor(Color.WHITE)
                .updateType(ACProgressConstant.PIE_AUTO_UPDATE)
                .build();
        dialog.setCancelable(true);

        binding.imgSetting.setOnClickListener(v -> showPopupMenu());

        binding.btnLogin.setOnClickListener(v -> {

            String user = binding.edtUserLogin.getText().toString();
            String password = binding.edtPassword.getText().toString();

            getToken(user, password);

        });

        binding.btnTest1.setOnClickListener(v -> {
            if (binding.btnTest1.getText().toString().equals("ON")) {
                mqttService.publish("0c38a97d-1564-4707-935c-18b4e9bcb0db", "0");
                binding.btnTest1.setText("OFF");
            } else {
                mqttService.publish("0c38a97d-1564-4707-935c-18b4e9bcb0db", "1");
                binding.btnTest1.setText("ON");
            }
        });

        binding.btnTest1.setOnClickListener(v -> {
            if (binding.btnTest2.getText().toString().equals("ON")) {
                mqttService.publish("7f704fdf-fa4b-44e2-b359-5ef19294196a", "0");
                binding.btnTest1.setText("OFF");
            } else {
                mqttService.publish("7f704fdf-fa4b-44e2-b359-5ef19294196a", "1");
                binding.btnTest2.setText("ON");
            }
        });

        binding.tvForgetPassword.setOnClickListener(v -> {
            Intent intent1 = new Intent(LoginActivity.this, ForgetPasswordActivity.class);
            startActivity(intent1);
        });

        binding.edtPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (binding.edtPassword.getText().toString().length() == 0) {
                    binding.imgShowPass.setVisibility(View.INVISIBLE);
                    binding.imgHidePass.setVisibility(View.INVISIBLE);

                } else {
                    binding.imgShowPass.setVisibility(View.VISIBLE);
                    binding.imgShowPass.setOnClickListener(v -> {
                        binding.imgShowPass.setVisibility(View.INVISIBLE);
                        binding.imgHidePass.setVisibility(View.VISIBLE);

                        binding.edtPassword.setTransformationMethod(null);
                        binding.edtPassword.setSelection(binding.edtPassword.getText().length());

                    });
                    binding.imgHidePass.setOnClickListener(v -> {
                        binding.imgHidePass.setVisibility(View.INVISIBLE);
                        binding.imgShowPass.setVisibility(View.VISIBLE);

                        binding.edtPassword.setTransformationMethod(new PasswordTransformationMethod());
                        binding.edtPassword.setSelection(binding.edtPassword.getText().length());
                    });
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


    }

    //Hiện menu setting
    private void showPopupMenu() {
        PopupMenu popupMenu = new PopupMenu(LoginActivity.this, binding.imgSetting);
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
                case R.id.menuSetIPAddress: {
                    binding.tvInputHost.setVisibility(View.VISIBLE);
                    binding.edtHost.setVisibility(View.VISIBLE);
                    binding.edtHost.requestFocus();
                }
            }
            return false;
        });
        popupMenu.show();
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
                Toast.makeText(LoginActivity.this, R.string.check_connect, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
