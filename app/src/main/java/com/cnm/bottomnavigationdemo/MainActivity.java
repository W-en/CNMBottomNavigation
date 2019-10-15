package com.cnm.bottomnavigationdemo;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.cnm.bottomnavigation.CNMBottomNavigation;
import com.cnm.bottomnavigation.CNMBottomNavigationItem;

public class MainActivity extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener {

    CNMBottomNavigation cnm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ((CheckBox) findViewById(R.id.addItem)).setOnCheckedChangeListener(this);
        ((CheckBox) findViewById(R.id.state)).setOnCheckedChangeListener(this);
        ((CheckBox) findViewById(R.id.mode)).setOnCheckedChangeListener(this);
        ((CheckBox) findViewById(R.id.marginTop)).setOnCheckedChangeListener(this);
        ((CheckBox) findViewById(R.id.marginBottom)).setOnCheckedChangeListener(this);
        ((CheckBox) findViewById(R.id.activeColor)).setOnCheckedChangeListener(this);
        ((CheckBox) findViewById(R.id.inactiveColor)).setOnCheckedChangeListener(this);

        cnm = findViewById(R.id.cnm);

        cnm.addItem(new CNMBottomNavigationItem("Item 1", R.mipmap.settings_selected, R.mipmap.settings_unselected))
                .addItem(new CNMBottomNavigationItem("Item 1", R.mipmap.settings_selected, R.mipmap.settings_unselected))
                .addItem(new CNMBottomNavigationItem("Item 1", R.mipmap.settings_selected, R.mipmap.settings_unselected))
                .setState(CNMBottomNavigation.State.SCALE)
                .setMode(CNMBottomNavigation.Mode.FIXED)
                .setCenterOffset(20)
                .setMarginTop(20)
                .setMarginBottom(20)
                .setItemDrawable(R.drawable.item_bg)
                .setScaleSize(1.3f)
                .setActiveColor(Color.RED)
                .setInactiveColor(Color.GRAY)
                .setOnSelectedListener(new CNMBottomNavigation.OnSelectedListener() {
                    @Override
                    public void onSelectedListener(int position) {

                    }
                })
                .setCurrentItem(3);
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case R.id.addItem:
                cnm.addItem(new CNMBottomNavigationItem("Item 1", (R.mipmap.settings_selected), (R.mipmap.settings_unselected)));
                break;
            case R.id.state:
                cnm.setState(isChecked ? CNMBottomNavigation.State.NORMAL : CNMBottomNavigation.State.SCALE);
                CheckBox state = findViewById(R.id.state);
                state.setText(isChecked ? "设置State:  NORMAL" : "设置State:  SCALE");
                break;
            case R.id.mode:
                cnm.setMode(isChecked ? CNMBottomNavigation.Mode.FIXED : CNMBottomNavigation.Mode.SCROLL);
                CheckBox mode = findViewById(R.id.mode);
                mode.setText(isChecked ? "设置Mode:  FIXED" : "设置Mode:  SCROLL");
                break;
            case R.id.marginTop:
                break;
            case R.id.marginBottom:
                break;
            case R.id.activeColor:
                break;
            case R.id.inactiveColor:
                break;
        }
    }
}
