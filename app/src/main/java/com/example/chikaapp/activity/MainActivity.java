package com.example.chikaapp.activity;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.chikaapp.R;
import com.example.chikaapp.fragment.CameraFragment;
import com.example.chikaapp.fragment.CommunicationInterface;
import com.example.chikaapp.fragment.DevicesFragment;
import com.example.chikaapp.fragment.HomeFragment;
import com.example.chikaapp.fragment.RoomFragment;
import com.example.chikaapp.fragment.ScriptFragment;
import com.example.chikaapp.fragment.UserFragment;
import com.example.chikaapp.model.Devices;
import com.google.android.material.bottomnavigation.BottomNavigationView;


public class MainActivity extends AppCompatActivity implements CommunicationInterface {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FragmentManager fragmentManager=getSupportFragmentManager();
        FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
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
                case R.id.navigation_camera:
                    fragment = new CameraFragment();
                    loadFragment(fragment);
                    return true;
                case R.id.navigation_account:
                    fragment = new UserFragment();
                    loadFragment(fragment);
                    return true;
            }
            return false;
        }
    };

    private void loadFragment(Fragment fragment) {
        // load fragment
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void sendData(String idRoom, String roomName) {
        Toast.makeText(this, "Du lieu" + idRoom, Toast.LENGTH_SHORT).show();
        FragmentManager manager=getSupportFragmentManager();
        FragmentTransaction transaction=manager.beginTransaction();
        DevicesFragment devicesFragment=new DevicesFragment();
        Bundle bundle=new Bundle();
        bundle.putString("roomName",roomName);
        bundle.putString("idRoom",idRoom);
        devicesFragment.setArguments(bundle);
        transaction.replace(R.id.frame_container,devicesFragment);
        transaction.commit();
    }

}
