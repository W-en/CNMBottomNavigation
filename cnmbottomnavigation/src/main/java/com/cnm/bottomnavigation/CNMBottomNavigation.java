package com.cnm.bottomnavigation;

import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.content.ContextCompat;
import androidx.core.view.ViewCompat;

import java.util.ArrayList;

public class CNMBottomNavigation extends HorizontalScrollView {

    String TAG = getClass().getSimpleName();

    private int mElevation;
    private int mItemDrawableRes;

    // 当前选中item未知
    private int mCurrentItem;

    // 图片和文字之间距离，图片与父布局top距离，文字与父布局bottom距离
    private int mCenterOffset;
    private int mMarginTop;
    private int mMarginBottom;

    // 文字大小，图片宽高
    private int mTitleSize;
    private int mIconWidth;
    private int mIconHeight;

    // 选中颜色，未选中颜色
    private int mActiveColor;
    private int mInactiveColor;

    // 缩放状态下，缩放值
    private float mScaleSize = 1.2f;

    private Context mContext;
    private ArrayList<CNMBottomNavigationItem> mItems = new ArrayList<>();
    private ArrayList<View> mImageViews = new ArrayList<>();
    private ArrayList<View> mTextViews = new ArrayList<>();
    private OnSelectedListener mOnSelectedListener;

    private Mode mMode = Mode.FIXED;
    private State mState = State.NORMAL;

    public enum State {
        NORMAL,
        SCALE
    }

    public enum Mode {
        FIXED,
        SCROLL
    }

    public CNMBottomNavigation(@NonNull Context context) {
        super(context);
        mContext = context;
        init();
    }

    public CNMBottomNavigation(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        init();
    }

    public CNMBottomNavigation(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        init();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        createItems();
    }

    /**
     * 初始化
     */
    private void init() {
        mElevation = dpToPx(12);
        mCenterOffset = dpToPx(4);
        mMarginTop = dpToPx(4);
        mMarginBottom = dpToPx(4);
        mTitleSize = sp2px(12);
        mIconWidth = dpToPx(24);
        mIconHeight = dpToPx(24);
        mActiveColor = getColorPrimary();
        mInactiveColor = Color.GRAY;

        setBackgroundColor(Color.WHITE);
        // 隐藏滚动条
        setHorizontalScrollBarEnabled(false);
        ViewCompat.setElevation(this, mElevation);
    }

    /**
     * 创建items
     */
    private void createItems() {
        Log.e(TAG, "准备创建 items");
        if (mItems.size() == 0) {
            Log.e(TAG, "items == 0, 返回");
            return;
        }
        removeAllViews();
        mImageViews.clear();
        mTextViews.clear();
        LinearLayout parentContainer = new LinearLayout(mContext);
        parentContainer.setOrientation(LinearLayout.HORIZONTAL);
        parentContainer.setGravity(Gravity.CENTER);
//        LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, mLayoutHeight);
        addView(parentContainer/*, layoutParams*/);
        createItem(parentContainer);
        if (mItems.size() != 0) {
            post(new Runnable() {
                @Override
                public void run() {
                    requestLayout();
                }
            });
        }
    }

    /**
     * 创建item
     *
     * @param parentContainer item 父容器
     */
    @SuppressLint("ResourceType")
    private void createItem(LinearLayout parentContainer) {
        int itemWidth = getItemWidth();
        for (int i = 0; i < mItems.size(); i++) {
            Log.e(TAG, "开始创建第 " + i + " 个item");
            final int itemIndex = i;
            final LinearLayout itemContainer = createItemContainer(itemWidth);
            Drawable backgroundDrawable = getBackgroundDrawable();
            if (null != backgroundDrawable) {
                itemContainer.setBackgroundDrawable(backgroundDrawable);
            }
            itemContainer.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (itemIndex != mCurrentItem) {
                        updateItems(itemIndex);
                    }
                }
            });

            ImageView icon = createImageView();
            Drawable drawableIcon = getDrawable(mItems.get(i).getIconRes());
            if (null != drawableIcon) {
                icon.setImageDrawable(updateDrawableColor(drawableIcon, mInactiveColor));
            } else {
                icon.setImageDrawable(getDrawable(mItems.get(i).getInactiveIconRes()));
            }
            itemContainer.addView(icon);

            TextView title = createTextView();
            title.setText(mItems.get(i).getTitle());
            itemContainer.addView(title);

            mImageViews.add(icon);
            mTextViews.add(title);

            parentContainer.addView(itemContainer);
        }
        updateItems(mCurrentItem);
    }

    /**
     * 更新item
     *
     * @param itemIndex 当前item
     */
    private void updateItems(int itemIndex) {
        if (mImageViews.size() == 0 || mTextViews.size() == 0) {
            return;
        }
        if (null != mOnSelectedListener) {
            mOnSelectedListener.onSelectedListener(itemIndex);
        }

        if (mState == State.NORMAL) {
            normalState(itemIndex);
        } else if (mState == State.SCALE) {
            scaleState(itemIndex);
        }

        mCurrentItem = itemIndex;
    }

    /**
     * 正常状态
     *
     * @param itemIndex 当前item
     */
    private void normalState(int itemIndex) {
        for (int i = 0; i < mItems.size(); i++) {
            ImageView iv = (ImageView) mImageViews.get(i);
            TextView tv = (TextView) mTextViews.get(i);
            Drawable icon = getDrawable(mItems.get(i).getIconRes());
            if (i == itemIndex) {
                if (null != icon) {
                    iv.setImageDrawable(updateDrawableColor(icon, mActiveColor));
                } else {
                    iv.setImageDrawable(getDrawable(mItems.get(i).getActiveIconRes()));
                }
                tv.setTextColor(mActiveColor);
            } else if (i == mCurrentItem) {
                if (null != icon) {
                    iv.setImageDrawable(updateDrawableColor(icon, mInactiveColor));
                } else {
                    iv.setImageDrawable(getDrawable(mItems.get(i).getInactiveIconRes()));
                }
                tv.setTextColor(mInactiveColor);
            }
        }
    }

    /**
     * 缩放状态
     *
     * @param itemIndex 当前item
     */
    private void scaleState(int itemIndex) {
        for (int i = 0; i < mItems.size(); i++) {
            ImageView iv = (ImageView) mImageViews.get(i);
            TextView tv = (TextView) mTextViews.get(i);
            Drawable icon = getDrawable(mItems.get(i).getIconRes());
            if (i == itemIndex) {
                if (null != icon) {
                    iv.setImageDrawable(updateDrawableColor(icon, mActiveColor));
                } else {
                    iv.setImageDrawable(getDrawable(mItems.get(i).getActiveIconRes()));
                }
                tv.setTextColor(mActiveColor);
                scaleAnimator(iv, 1f, mScaleSize);
                scaleAnimator(tv, 1f, mScaleSize);
            } else if (i == mCurrentItem) {
                if (null != icon) {
                    iv.setImageDrawable(updateDrawableColor(icon, mInactiveColor));
                } else {
                    iv.setImageDrawable(getDrawable(mItems.get(i).getInactiveIconRes()));
                }
                tv.setTextColor(mInactiveColor);
                scaleAnimator(iv, mScaleSize, 1f);
                scaleAnimator(tv, mScaleSize, 1f);
            }
        }
    }

    /**
     * 创建 item 容器
     *
     * @param width 容器宽度
     * @return 容器
     */
    private LinearLayout createItemContainer(int width) {
        LinearLayout linearLayout = new LinearLayout(mContext);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width, LinearLayout.LayoutParams.MATCH_PARENT);
        linearLayout.setLayoutParams(params);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.setGravity(Gravity.CENTER);
        return linearLayout;
    }

    /**
     * 创建image view
     *
     * @return image view
     */
    private ImageView createImageView() {
        ImageView imageView = new ImageView(mContext);
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        MarginLayoutParams params = new MarginLayoutParams(MarginLayoutParams.WRAP_CONTENT, MarginLayoutParams.WRAP_CONTENT);
        params.setMargins(0, mMarginTop, 0, mCenterOffset / 2);
        params.width = mIconWidth;
        params.height = mIconHeight;
        imageView.setLayoutParams(params);
        return imageView;
    }

    /**
     * 创建text view
     *
     * @return text view
     */
    private TextView createTextView() {
        TextView textView = new TextView(mContext);
        textView.setGravity(Gravity.CENTER);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, mTitleSize);
        textView.setTextColor(mInactiveColor);
        // 去除默认padding
        textView.setIncludeFontPadding(false);
        MarginLayoutParams params = new MarginLayoutParams(MarginLayoutParams.WRAP_CONTENT, MarginLayoutParams.WRAP_CONTENT);
        params.setMargins(0, mCenterOffset / 2, 0, mMarginBottom);
        textView.setLayoutParams(params);
        return textView;
    }

    /**
     * 更新drawable颜色
     *
     * @param drawable drawable
     * @param color    color
     * @return drawable
     */
    private Drawable updateDrawableColor(Drawable drawable, int color) {
        drawable.clearColorFilter();
        drawable.mutate().setColorFilter(color, PorterDuff.Mode.SRC_IN);
        drawable.invalidateSelf();
        return drawable;
    }

    /**
     * 获取主题颜色
     *
     * @return 颜色
     */
    private int getColorPrimary() {
        TypedValue typedValue = new TypedValue();
        mContext.getTheme().resolveAttribute(R.attr.colorPrimary, typedValue, true);
        return typedValue.data;
    }

    private Drawable getBackgroundDrawable() {
        int drawableRes = mItemDrawableRes;
        Drawable drawable = null;
        if (0 == drawableRes){
            if (Build.VERSION.SDK_INT >= 21){
                int[] attrs = new int[]{android.R.attr.selectableItemBackgroundBorderless};
                TypedArray a = mContext.getTheme().obtainStyledAttributes(attrs);
                drawable = a.getDrawable(0);
                a.recycle();
            }
        } else {
            drawable = getDrawable(drawableRes);
        }
        return drawable;
    }

    private Drawable getDrawable(int drawableRes){
        if (drawableRes != 0) {
            try {
                return AppCompatResources.getDrawable(mContext, drawableRes);
            } catch (Resources.NotFoundException e) {
                return ContextCompat.getDrawable(mContext, drawableRes);
            }
        }
        return null;
    }

    private int getItemWidth() {
        int itemWidth = 0;
        if (mMode == Mode.FIXED) {
            itemWidth = (getMeasuredWidth() - getPaddingLeft() - getPaddingRight()) / mItems.size();
        } else if (mMode == Mode.SCROLL) {
            if (mItems.size() > 5) {
                // 如果为 scroll 模式，并且item.size > 5, 默认宽度为 1 / 5
                itemWidth = (getMeasuredWidth() - getPaddingLeft() - getPaddingRight()) / 5;
            } else {
                // 如果 < 5
                itemWidth = (getMeasuredWidth() - getPaddingLeft() - getPaddingRight()) / mItems.size();
            }
        }
        return itemWidth;
    }

    public CNMBottomNavigation setOnSelectedListener(OnSelectedListener listener) {
        this.mOnSelectedListener = listener;
        return this;
    }

    public interface OnSelectedListener {
        void onSelectedListener(int position);
    }

    //***************************************************************************************************************

    /**
     * 添加item
     *
     * @param item item
     * @return {@link CNMBottomNavigation}
     */
    public CNMBottomNavigation addItem(CNMBottomNavigationItem item) {
        mItems.add(item);
        createItems();
        return this;
    }

    /**
     * 删除指定位置item
     *
     * @param position 位置
     * @return {@link CNMBottomNavigation}
     */
    public CNMBottomNavigation removeByPosition(int position) {
        mItems.remove(position);
        createItems();
        return this;
    }

    /**
     * item 状态 {@link State}
     *
     * @param state normal or scale
     * @return {@link CNMBottomNavigation}
     */
    public CNMBottomNavigation setState(State state) {
        this.mState = state;
        createItems();
        return this;
    }

    /**
     * item 模式 {@link Mode}
     *
     * @param mode fixed or scroll
     * @return {@link CNMBottomNavigation}
     */
    public CNMBottomNavigation setMode(Mode mode) {
        this.mMode = mode;
        createItems();
        return this;
    }

    /**
     * item {@link State#SCALE} 状态下的缩放值
     *
     * @param scaleSize 缩放值
     * @return {@link CNMBottomNavigation}
     */
    public CNMBottomNavigation setScaleSize(float scaleSize) {
        this.mScaleSize = scaleSize;
        createItems();
        return this;
    }

    /**
     * image view 与 text view 中间的距离
     *
     * @param offset 距离
     * @return {@link CNMBottomNavigation}
     */
    public CNMBottomNavigation setCenterOffset(int offset) {
        this.mCenterOffset = offset;
        createItems();
        return this;
    }

    /**
     * 设置image view 顶部与布局顶部距离
     *
     * @param top 距离
     * @return {@link CNMBottomNavigation}
     */
    public CNMBottomNavigation setMarginTop(int top) {
        this.mMarginTop = top;
        createItems();
        return this;
    }

    /**
     * 设置text view底部与布局底部距离
     *
     * @param bottom 距离
     * @return {@link CNMBottomNavigation}
     */
    public CNMBottomNavigation setMarginBottom(int bottom) {
        this.mMarginBottom = bottom;
        createItems();
        return this;
    }

    /**
     * 设置当前item
     *
     * @param position 位置
     */
    public void setCurrentItem(int position) {
        if (position >= mItems.size()) {
            return;
        }
        this.mCurrentItem = position;
        updateItems(position);
    }

    /**
     * 设置elevation
     *
     * @param elevation value
     * @return {@link CNMBottomNavigation}
     */
    private CNMBottomNavigation setElevation(int elevation) {
        this.mElevation = elevation;
        ViewCompat.setElevation(this, mElevation);
        return this;
    }

    /**
     * item 选中颜色
     *
     * @param activeColor 选中颜色
     * @return {@link CNMBottomNavigation}
     */
    public CNMBottomNavigation setActiveColor(int activeColor) {
        this.mActiveColor = activeColor;
        createItems();
        return this;
    }

    /**
     * item 未选中颜色
     *
     * @param inactiveColor 未选中颜色
     * @return {@link CNMBottomNavigation}
     */
    public CNMBottomNavigation setInactiveColor(int inactiveColor) {
        this.mInactiveColor = inactiveColor;
        createItems();
        return this;
    }

    /**
     *  设置item drawable
     * @return {@link CNMBottomNavigation}
     */
    public CNMBottomNavigation setItemDrawable(@DrawableRes int drawableRes){
        this.mItemDrawableRes = drawableRes;
        createItems();
        return this;
    }

    /**
     *  设置title文字大小 默认12sp
     * @param size 大小
     * @return {@link CNMBottomNavigation}
     */
    public CNMBottomNavigation setTitleSize(int size){
        this.mTitleSize = size;
        createItems();
        return this;
    }

    /**
     *  设置icon宽高 默认24dp
     * @param width 宽度
     * @param height 高度
     * @return {@link CNMBottomNavigation}
     */
    public CNMBottomNavigation setIconWidthHeight(int width, int height){
        this.mIconWidth = width;
        this.mIconHeight = height;
        createItems();
        return this;
    }

    //***************************************************************************************************************

    //===============================================================================================================

    private final float DENSITY = Resources.getSystem().getDisplayMetrics().density;

    private float getFontDensity() {
        return mContext.getResources().getDisplayMetrics().scaledDensity;
    }

    /**
     * 把以 dp 为单位的值，转化为以 px 为单位的值
     *
     * @param dpValue 以 dp 为单位的值
     * @return px value
     */
    private int dpToPx(int dpValue) {
        return (int) (dpValue * DENSITY + 0.5f);
    }

    /**
     * 单位转换: sp -> px
     *
     * @param sp 以 px 为单位的值
     * @return px值
     */
    private int sp2px(int sp) {
        return (int) (getFontDensity() * sp + 0.5);
    }

    //===============================================================================================================

    /**
     * 缩放动画
     *
     * @param view  目标view
     * @param start 开始值
     * @param end   结束值
     */
    private void scaleAnimator(final View view, float start, float end) {
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(start, end);
        valueAnimator.setDuration(150);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (float) animation.getAnimatedValue();
                view.setScaleX(value);
                view.setScaleY(value);
            }
        });
        valueAnimator.start();
    }
}
