package com.example.chikaapp.fragment;


import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.chikaapp.R;
import com.example.chikaapp.SharedPreferencesUtils;
import com.example.chikaapp.action.CustomToast;
import com.example.chikaapp.api.ApiRetrofit;
import com.example.chikaapp.api.UserUtils;
import com.example.chikaapp.model.User;
import com.example.chikaapp.request.EditInfoUserRequest;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * A simple {@link Fragment} subclass.
 */
public class EditUserInfoFragment extends Fragment {


    ImageView ed_phone, ed_email, ed_birthday, ed_address;
    Button btnDone;
    EditText edtPhone, edtEmail, edtBirthday, edtAddress;
    EditInfoUserRequest editInfoUserRequest;

    public EditUserInfoFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_edit_user_info, container, false);
        initialize(view);
        loadUser(getContext());
        btnDone.setOnClickListener(view1 -> {
            EditInfoUserRequest editInfoUserRequest= new EditInfoUserRequest(edtPhone.getText().toString(),
                    edtEmail.getText().toString(), edtBirthday.getText().toString(), edtAddress.getText().toString());
            Log.i("fuck", editInfoUserRequest.getPhone()+editInfoUserRequest.getAddress()+editInfoUserRequest.getBirthday());
            editUserInfo(getContext(), editInfoUserRequest);
        });
        return view;
    }

    public void initialize(View view){
        ed_phone=view.findViewById(R.id.img_edit_phone);
        ed_email=view.findViewById(R.id.img_edit_email);
        ed_birthday=view.findViewById(R.id.img_edit_birthday);
        ed_address=view.findViewById(R.id.img_edit_address);
        edtPhone = view.findViewById(R.id.edt_phone);
        edtEmail = view.findViewById(R.id.edt_email);
        edtBirthday = view.findViewById(R.id.edt_birthday);
        edtAddress = view.findViewById(R.id.edt_address);
        btnDone = view.findViewById(R.id.btn_done);
    }

    private void editUserInfo(Context context, EditInfoUserRequest editRequest){
        String token= SharedPreferencesUtils.loadToken(context);
        Retrofit retrofit=new Retrofit.Builder()
                .baseUrl(ApiRetrofit.URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        UserUtils userUtils=retrofit.create(UserUtils.class);
        Call<User> userCall=userUtils.editUser(token,editRequest);
        if (editRequest!=editInfoUserRequest){
            userCall.enqueue(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {
                    User user=response.body();
                    if (user!=null){
                        SharedPreferencesUtils.saveSelf(context,user);
                        loadUser(context);
                        CustomToast.makeText(context,"Edit success",CustomToast.LENGTH_LONG,CustomToast.SUCCESS,false).show();
                    }
                }
                @Override
                public void onFailure(Call<User> call, Throwable t) {
                    return;
                }
            });
        } else {
            CustomToast.makeText(getContext(), "Chưa có thông tin nào thay đổi, vui lòng thử lại!",CustomToast.LENGTH_LONG,CustomToast.WARNING,false).show();
        }

    }

    private void loadUser(Context context){
        String token= SharedPreferencesUtils.loadToken(context);
        Retrofit retrofit=new Retrofit.Builder()
                .baseUrl(ApiRetrofit.URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        UserUtils iAccount=retrofit.create(UserUtils.class);
        Call<User> userCall=iAccount.getUser(token);
        userCall.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                User user=response.body();
                if (user!=null){
                    SharedPreferencesUtils.saveSelf(context,user);
                    edtPhone.setText(user.getPhone());
                    edtEmail.setText(user.getEmail());
                    edtBirthday.setText(user.getBirthday());
                    edtAddress.setText(user.getAddress());
                    editInfoUserRequest = new EditInfoUserRequest(user.getPhone(),user.getEmail(), user.getBirthday(), user.getAddress());
                }
            }
            @Override
            public void onFailure(Call<User> call, Throwable t) {
                return;
            }
        });

    }

}
