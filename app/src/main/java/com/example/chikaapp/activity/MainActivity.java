package com.example.chikaapp.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.chikaapp.R;
import com.example.chikaapp.fragment.AddDeviceFragment;
import com.example.chikaapp.fragment.AddScriptFragment;
import com.example.chikaapp.fragment.ButtonNotUsedFragment;
import com.example.chikaapp.fragment.CameraFragment;
import com.example.chikaapp.fragment.CommunicationInterface;
import com.example.chikaapp.fragment.AddIRDeviceFragment;
import com.example.chikaapp.fragment.DetailScriptsFragment;
import com.example.chikaapp.fragment.DevicesFragment;
import com.example.chikaapp.fragment.EditUserInfoFragment;
import com.example.chikaapp.fragment.HomeFragment;
import com.example.chikaapp.fragment.ProductsFragment;
import com.example.chikaapp.fragment.RoomFragment;
import com.example.chikaapp.fragment.ScriptFragment;
import com.example.chikaapp.fragment.UserFragment;
import com.example.chikaapp.model.Image;
import com.example.chikaapp.model.Scripts;
import com.example.chikaapp.request.EditInfoUserRequest;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity implements CommunicationInterface {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        FragmentManager fragmentManager=getSupportFragmentManager();
//        FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
        setContentView(R.layout.activity_main);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        loadFragment(new HomeFragment());

    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment fragment;
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    fragment = new HomeFragment();
                    loadFragment(fragment);
                    return true;
                case R.id.navigation_room:
                    fragment = new RoomFragment();
                    loadFragment(fragment);
                    return true;
                case R.id.navigation_script:
                    fragment = new ScriptFragment();
                    loadFragment(fragment);
                    return true;
//                case R.id.navigation_camera:
//                    fragment = new CameraFragment();
//                    loadFragment(fragment);
//                    return true;
                case R.id.navigation_account:
                    fragment = new UserFragment();
                    loadFragment(fragment);
                    return true;
            }
            return false;
        }
    };

//    @Override
//    public void onBackPressed() {
//        AlertDialog.Builder builder
//                = new AlertDialog
//                .Builder(MainActivity.this);
//
//        builder.setMessage("Do you want to exit ?");
//        builder.setTitle("Alert !");
//        builder.setCancelable(false);
//        builder
//                .setPositiveButton(
//                        "Yes",
//                        (dialog, which) -> {
//
//                            finish();
//                        });
//        builder
//                .setNegativeButton(
//                        "No",
//                        (dialog, which) -> {
//                            dialog.cancel();
//                        });
//
//        // Create the Alert dialog
//        AlertDialog alertDialog = builder.create();
//
//        // Show the Alert Dialog box
//        alertDialog.show();
//    }

    public void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//        transaction.setCustomAnimations(R.anim.slide_in_bot,R.anim.slide_out_bot);
        transaction.replace(R.id.frame_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void RoomToDevices(String idRoom, String roomName) {
        FragmentManager manager=getSupportFragmentManager();
        FragmentTransaction transaction=manager.beginTransaction();
        transaction.addToBackStack(null);
        DevicesFragment devicesFragment=new DevicesFragment();
        Bundle bundle=new Bundle();
        bundle.putString("roomName",roomName);
        bundle.putString("idRoom",idRoom);
        devicesFragment.setArguments(bundle);
        transaction.replace(R.id.frame_container,devicesFragment);
        transaction.commit();
    }

    @Override
    public void DeviceToProducts(String idRoom) {
        FragmentManager manager=getSupportFragmentManager();
        FragmentTransaction transaction=manager.beginTransaction();
        transaction.addToBackStack(null);
        ProductsFragment devicesFragment=new ProductsFragment();
        Bundle bundle=new Bundle();
        bundle.putString("idRoom",idRoom);
        devicesFragment.setArguments(bundle);
        transaction.replace(R.id.frame_container,devicesFragment);
        transaction.commit();
    }


    @Override
    public void ProductsToButtonNotUsed(ArrayList arrayList, int maxButton, String idRoom, String topic, String type) {
        FragmentManager manager=getSupportFragmentManager();
        FragmentTransaction transaction=manager.beginTransaction();
        transaction.addToBackStack(null);
        ButtonNotUsedFragment buttonNotUsedFragment=new ButtonNotUsedFragment();
        Bundle bundle=new Bundle();
        bundle.putIntegerArrayList("button",arrayList);
        bundle.putInt("max",maxButton);
        bundle.putString("idRoom", idRoom);
        bundle.putString("topic", topic);
        bundle.putString("type", type);
        buttonNotUsedFragment.setArguments(bundle);
        transaction.replace(R.id.frame_container,buttonNotUsedFragment);
        transaction.commit();
    }

    @Override
    public void ButtonNotUsedToAddDevice(int button, String idRoom, String topic, String type) {
        FragmentManager manager=getSupportFragmentManager();
        FragmentTransaction transaction=manager.beginTransaction();
        transaction.addToBackStack(null);
        AddDeviceFragment addDeviceFragment = new AddDeviceFragment();
        Bundle bundle =new Bundle();
        bundle.putInt("swButton", button);
        bundle.putString("idRoom", idRoom);
        bundle.putString("topic", topic);
        bundle.putString("type", type);
        addDeviceFragment.setArguments(bundle);
        transaction.replace(R.id.frame_container,addDeviceFragment);
        transaction.commit();
    }

    @Override
    public void ProductsToAddIRDevice(String idRoom, String topic, String type) {
        FragmentManager manager=getSupportFragmentManager();
        FragmentTransaction transaction=manager.beginTransaction();
        transaction.addToBackStack(null);
        AddIRDeviceFragment addIRDeviceFragment = new AddIRDeviceFragment();
        Bundle bundle =new Bundle();
        bundle.putString("idRoom", idRoom);
        bundle.putString("topic", topic);
        bundle.putString("type", type);
        addIRDeviceFragment.setArguments(bundle);
        transaction.replace(R.id.frame_container,addIRDeviceFragment);
        transaction.commit();
    }

    @Override
    public void ScriptToDetailScripts(Scripts scripts) {
        FragmentManager manager=getSupportFragmentManager();
        FragmentTransaction transaction=manager.beginTransaction();
        transaction.addToBackStack(null);
        DetailScriptsFragment detailScriptsFragment = new DetailScriptsFragment();
        Bundle bundle =new Bundle();
        bundle.putSerializable("scripts", scripts);
        detailScriptsFragment.setArguments(bundle);
        transaction.replace(R.id.frame_container,detailScriptsFragment);
        transaction.commit();
    }

    @Override
    public void ScriptToAddScript(Image img) {
        FragmentManager manager=getSupportFragmentManager();
        FragmentTransaction transaction=manager.beginTransaction();
        Bundle bundle = new Bundle();
        bundle.putSerializable("image", img);
        AddScriptFragment addScriptFragment = new AddScriptFragment();
        addScriptFragment.setArguments(bundle);
        transaction.replace(R.id.frame_container,addScriptFragment);
        transaction.commit();
    }

    @Override
    public void DialogImageToAddScript(Image image) {
        FragmentManager manager=getSupportFragmentManager();
        FragmentTransaction transaction=manager.beginTransaction();
        Bundle bundle = new Bundle();
        bundle.putSerializable("image", image);
        Toast.makeText(this, "id"+image.getName_image(), Toast.LENGTH_SHORT).show();
        AddScriptFragment addScriptFragment = new AddScriptFragment();
        addScriptFragment.setArguments(bundle);
        transaction.replace(R.id.frame_container,addScriptFragment);
        transaction.commit();
    }

}
