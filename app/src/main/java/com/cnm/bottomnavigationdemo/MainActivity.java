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

            cnm.addItem(new CNMBottomNavigationItem("Item 1", R.mipmap.settings_selected/*, R.mipmap.settings_unselected*/))
                .addItem(new CNMBottomNavigationItem("Item 2", R.mipmap.settings_selected/*, R.mipmap.settings_unselected*/))
                .addItem(new CNMBottomNavigationItem("Item 3", R.mipmap.settings_selected/*, R.mipmap.settings_unselected*/))
//              .addItem(new CNMBottomNavigationItem("Item", R.mipmap.settings_selected)) // 如果你的icon是透明的，可以只传一张图片
                .setState(CNMBottomNavigation.State.NORMAL)  // state 默认normal
                .setMode(CNMBottomNavigation.Mode.FIXED) // mode 默认fixed
                .setCenterOffset(10) // imageview 和 textview 之间的距离 默认 4dp
                .setMarginTop(10) // imageview top 与父布局距离 默认 4dp
                .setMarginBottom(10) // textview bottom 与父布局距离 默认 4dp
//                .setItemDrawable(R.drawable.item_bg) // 设置item drawable 5.0以上默认有水波纹效果
                .setScaleSize(1.3f) // 缩放模式下，缩放大小 默认1.2f
                .setActiveColor(Color.RED) // 选中颜色 默认主题颜色
                .setInactiveColor(Color.GRAY) // 未选中颜色 默认gray
                 .setTitleSize(28) // 文字大小 // 默认 12sp
                 .setIconWidthHeight(50, 50) // icon宽高 默认 24dp
                .setOnSelectedListener(new CNMBottomNavigation.OnSelectedListener() {
                    @Override
                    public void onSelectedListener(int position) {
                        // 点击事件
                    }
                })
                .setCurrentItem(3); // 设置当前选中的item
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case R.id.addItem:
                cnm.addItem(new CNMBottomNavigationItem("Item 1", R.mipmap.settings_selected));
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
