![logo](https://raw.githubusercontent.com/W-en/CNMBottomNavigation/master/images/logo.png)
# CNMBottomNavigation
[![](https://www.jitpack.io/v/W-en/CNMBottomNavigation.svg)](https://www.jitpack.io/#W-en/CNMBottomNavigation)
### 效果Gif:
![gif](https://raw.githubusercontent.com/W-en/CNMBottomNavigation/master/images/cnm.gif)
### Step 1. Add the JitPack repository to your build file
```
allprojects {
	repositories {
		...
		maven { url 'https://www.jitpack.io' }
	}
}
```
### Step 2. Add the dependency  
```
dependencies {
	implementation 'com.github.W-en:CNMBottomNavigation:Tag'
}
```
```
        cnm = findViewById(R.id.cnm);

        cnm.addItem(new CNMBottomNavigationItem("Item 1", R.mipmap.settings_selected, R.mipmap.settings_unselected)) 
           .addItem(new CNMBottomNavigationItem("Item 2", R.mipmap.settings_selected, R.mipmap.settings_unselected))
           .addItem(new CNMBottomNavigationItem("Item 3", R.mipmap.settings_selected, R.mipmap.settings_unselected))
//         .addItem(new CNMBottomNavigationItem("Item", R.mipmap.settings_selected)) // 如果你的icon是透明的，可以只传一张图片
           .setState(CNMBottomNavigation.State.NORMAL)  // state 默认normal
           .setMode(CNMBottomNavigation.Mode.FIXED) 	// mode 默认fixed
           .setCenterOffset(20) 			// imageview 和 textview 之间的距离 默认 4dp
           .setMarginTop(20) 				// imageview top 与父布局距离 默认 4dp
           .setMarginBottom(20) 			// textview bottom 与父布局距离 默认 4dp
           .setItemDrawable(R.drawable.item_bg) 	// 设置item drawable 5.0以上默认有水波纹效果
           .setScaleSize(1.3f) 				// 缩放模式下，缩放大小
           .setActiveColor(Color.RED) 			// 选中颜色 默认主题颜色
           .setInactiveColor(Color.GRAY) 		// 未选中颜色 默认gray
           .setOnSelectedListener(new CNMBottomNavigation.OnSelectedListener() {
                    @Override
                    public void onSelectedListener(int position) {
                        // 点击事件
                    }
           })
           .setCurrentItem(3); 				// 设置当前选中的item
```
