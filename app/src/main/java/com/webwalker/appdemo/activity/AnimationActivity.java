package com.webwalker.appdemo.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ArgbEvaluator;
import android.animation.IntEvaluator;
import android.animation.LayoutTransition;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.graphics.Color;
import android.graphics.Path;
import android.graphics.PointF;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.view.animation.PathInterpolatorCompat;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.webwalker.appdemo.R;
import com.webwalker.appdemo.common.Params;
import com.webwalker.framework.animation.CustomInterpolator;
import com.webwalker.framework.animation.MyAnimation;
import com.webwalker.framework.animation.ScaleDisappear;
import com.webwalker.framework.animation.drawable.ShrinkView;
import com.webwalker.framework.utils.DeviceUtil;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AnimationActivity extends BaseActivity implements CompoundButton.OnCheckedChangeListener {
    @BindView(R.id.iv_anim_icon)
    ImageView imageView;
    @BindView(R.id.iv_anim_icon_reverse)
    ImageView reverseImageView;
    @BindView(R.id.v_common_anim)
    FrameLayout commonView;
    @BindView(R.id.iv_anim_001)
    ImageView iv001;
    @BindView(R.id.iv_anim_002)
    ImageView iv002;

    @BindView(R.id.id_container)
    LinearLayout lContainer;
    @BindView(R.id.id_appear)
    CheckBox mAppear;
    @BindView(R.id.id_change_appear)
    CheckBox mChangeAppear;
    @BindView(R.id.id_disappear)
    CheckBox mDisAppear;
    @BindView(R.id.id_change_disappear)
    CheckBox mChangeDisAppear;

    private GridLayout mGridLayout;
    private LayoutTransition mTransition;
    private int mVal;

    private int screenWidth = 0;
    private int screenHeight = 0;
    private boolean value = false;
    private static final String TAG = "animation";

    public AnimationActivity() {
    }

    public AnimationActivity(Params params) {
        super(params);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animation);
        ButterKnife.bind(this);
        init();

//        alpha01();
//        alpha02();
//        scale01();
//        translate01();
//        rotate01();
//        propertyValuesHolder();
//        others();
//        margin();
//        scaleValueAnimator();
//        fadeOut();
//        verticalRun();
//        paowuxian();
//        hideOrShowListViewAnimator(100, 600);
//        flip();
//        alphaAnimator();
//        Animatable(); //AnimalDrawable的方式实现动画
//        scaleDisappear();
//        animateProperties();
//        pathInterpolatorCompat();
        layoutTransition();
//        camer3D();
    }

    @Override
    public String getLabel() {
        return super.getLabels();
    }

    /**
     * 添加按钮
     *
     * @param view
     */
    public void addBtn(View view) {
        final Button button = new Button(this);
        button.setText((++mVal) + "");
        mGridLayout.addView(button, Math.min(1, mGridLayout.getChildCount()));
        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mGridLayout.removeView(button);
            }
        });
    }

    private void init() {
        screenWidth = DeviceUtil.getScreenWidth(this);
        screenHeight = DeviceUtil.getScreenHeight(this);
    }

    //alpha
    private void alpha01() {
        //alpha渐变从1到0再到1，后面可以设置无限的参数
        ValueAnimator animator = ObjectAnimator.ofFloat(imageView, View.ALPHA, 1.0f, 0.0f, 1.0f);
        animator.setInterpolator(PathInterpolatorCompat.create(0.33f, 0f, 0.66f, 1f));
        animator.setDuration(3000); //动画时间

        AnimatorSet sets = new AnimatorSet();
        sets.playTogether(animator);
        sets.start();
    }

    //alpha
    private void alpha02() {
        //第一个参数为 view对象，第二个参数为 动画改变的类型，第三，第四个参数依次是开始透明度和结束透明度。
        ObjectAnimator animator = ObjectAnimator.ofFloat(imageView, "alpha", 0f, 1f);
        animator.setDuration(1000);//设置动画时间
        animator.setInterpolator(new DecelerateInterpolator());//设置动画插入器，减速
        animator.setRepeatCount(-1);//设置动画重复次数，这里-1代表无限
        animator.setRepeatMode(ValueAnimator.REVERSE);//设置动画循环模式。
        animator.start();//启动动画。
    }

    private void scale01() {
        AnimatorSet sets = new AnimatorSet();//组合动画
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(imageView, "scaleX", 0f, 1f, 1f, 0f);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(imageView, "scaleY", 0f, 1f, 1f, 0f);

        //scaleX.setRepeatCount(-1);
        //scaleY.setRepeatCount(-1);

        sets.setDuration(2000);
        sets.setInterpolator(new DecelerateInterpolator());
        sets.play(scaleX).with(scaleY);//两个动画同时开始
        sets.start();
    }

    private void translate01() {
        AnimatorSet sets = new AnimatorSet();
        ObjectAnimator translationLeft = ObjectAnimator.ofFloat(imageView, "X", imageView.getX(), 300);
        translationLeft.setInterpolator(new AccelerateInterpolator());
        translationLeft.setDuration(1500);

        ObjectAnimator translationUp = ObjectAnimator.ofFloat(imageView, "Y", imageView.getY(), 300);
        translationUp.setInterpolator(new DecelerateInterpolator());
        translationUp.setDuration(1500);

        ObjectAnimator translationX = ObjectAnimator.ofFloat(imageView, "translationX", 0.0f, screenWidth / 2);
        translationX.setRepeatMode(ValueAnimator.REVERSE);
        translationX.setDuration(1000);

        ObjectAnimator translationY = ObjectAnimator.ofFloat(imageView, "translationY", 0.0f, screenHeight / 2);
        translationY.setRepeatMode(ValueAnimator.REVERSE);
        translationY.setRepeatCount(-1);
        translationY.setDuration(1000);

        sets.playSequentially(translationLeft, translationUp, translationX, translationY);
        sets.start();
    }

    private void rotate01() {
        AnimatorSet sets = new AnimatorSet();
        //X轴
        ObjectAnimator anim = ObjectAnimator.ofFloat(imageView, "rotationX", 0f, 180f);
        anim.setDuration(2000);
        ObjectAnimator anim2 = ObjectAnimator.ofFloat(imageView, "rotationX", 180f, 0f);
        anim2.setDuration(2000);
        //Y轴
        ObjectAnimator anim3 = ObjectAnimator.ofFloat(imageView, "rotationY", 0f, 180f);
        anim3.setDuration(2000);
        ObjectAnimator anim4 = ObjectAnimator.ofFloat(imageView, "rotationY", 180f, 0f);
        anim4.setDuration(2000);

        //Z轴
        ObjectAnimator anim5 = ObjectAnimator.ofFloat(imageView, "rotation", 0.0f, 360.0f);
        anim5.setRepeatMode(ValueAnimator.REVERSE);
        anim5.setRepeatCount(-1);
        anim5.setDuration(2000);

        sets.play(anim).before(anim2); //先执行anim动画之后在执行anim2
        sets.play(anim3).before(anim4);
        sets.play(anim5);
        sets.start();
    }

    //代替animatorset
    public void propertyValuesHolder() {
        PropertyValuesHolder pvhX = PropertyValuesHolder.ofFloat("alpha", 1f, 0f, 1f);
        PropertyValuesHolder pvhY = PropertyValuesHolder.ofFloat("scaleX", 1f, 0, 1f);
        PropertyValuesHolder pvhZ = PropertyValuesHolder.ofFloat("scaleY", 1f, 0, 1f);
        ObjectAnimator.ofPropertyValuesHolder(imageView, pvhX, pvhY, pvhZ).setDuration(2000).start();
    }

    private void others() {
        ObjectAnimator animator = ObjectAnimator.ofInt(imageView,
                "backgroundColor", Color.RED, Color.BLUE, Color.GRAY, Color.GREEN);
        animator.setInterpolator(new DecelerateInterpolator());
        animator.setDuration(1500);
        animator.setRepeatCount(-1);
        animator.setRepeatMode(ValueAnimator.REVERSE);
        /*
         * ArgbEvaluator：这种评估者可以用来执行类型之间的插值整数值代表ARGB颜色。
         * FloatEvaluator：这种评估者可以用来执行浮点值之间的插值。
         * IntEvaluator：这种评估者可以用来执行类型int值之间的插值。
         * RectEvaluator：这种评估者可以用来执行类型之间的插值矩形值。
         *
         * 由于本例是改变View的backgroundColor属性的背景颜色所以此处使用ArgbEvaluator
         */
        animator.setEvaluator(new ArgbEvaluator());
        animator.start();
    }

    //使用ValueAnimator改变Imageview的margin的值
    private void margin() {
        //1.调用ofInt(int...values)方法创建ValueAnimator对象
        ValueAnimator mAnimator = ValueAnimator.ofInt(0, 1080 - imageView.getWidth());
        //2.为目标对象的属性变化设置监听器
        mAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                // 3.为目标对象的属性设置计算好的属性值
                int animatorValue = (int) animation.getAnimatedValue();
                ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) imageView.getLayoutParams();
                marginLayoutParams.leftMargin = animatorValue;
                imageView.setLayoutParams(marginLayoutParams);
            }
        });
        //4.设置动画的持续时间、是否重复及重复次数等属性
        mAnimator.setDuration(2000);
        mAnimator.setRepeatCount(-1);
        //相反方向回来
        mAnimator.setRepeatMode(ValueAnimator.REVERSE);
        //5.为ValueAnimator设置目标对象并开始执行动画
        mAnimator.setTarget(imageView);
        mAnimator.start();
    }

    /**
     * 使用ValueAnimator实现图片缩放动画
     */
    public void scaleValueAnimator() {
        //1.设置目标属性名及属性变化的初始值和结束值
        PropertyValuesHolder scaleX = PropertyValuesHolder.ofFloat("scaleX", 1.0f, 0.0f);
        PropertyValuesHolder scaleY = PropertyValuesHolder.ofFloat("scaleY", 1.0f, 0.0f);
        ValueAnimator animator = ValueAnimator.ofPropertyValuesHolder(scaleX, scaleY);
        //2.为目标对象的属性变化设置监听器
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                // 3.根据属性名获取属性变化的值分别为ImageView目标对象设置X和Y轴的缩放值
                float animatorValueScaleX = (float) animation.getAnimatedValue("scaleX");
                float animatorValueScaleY = (float) animation.getAnimatedValue("scaleY");
                imageView.setScaleX(animatorValueScaleX);
                imageView.setScaleY(animatorValueScaleY);
            }
        });
        //4.为ValueAnimator设置自定义的Interpolator
        animator.setInterpolator(new CustomInterpolator());
        //5.设置动画的持续时间、是否重复及重复次数等属性
        animator.setDuration(2000);
        animator.setRepeatCount(-1);
        animator.setRepeatMode(ValueAnimator.REVERSE);
        //6.为ValueAnimator设置目标对象并开始执行动画
        animator.setTarget(imageView);
        animator.start();
    }

    /**
     * 隐藏或显示控件的动画
     */
    public void hideOrShowListViewAnimator(final int startValue, final int endValue) {
        //1.设置属性的初始值和结束值，没有设置要操作的属性，需要在addUpdateListener自己控制，更灵活
        ValueAnimator animator = ValueAnimator.ofInt(0, 100);
        //2.为目标对象的属性变化设置监听器
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int animatorValue = (int) animation.getAnimatedValue();
                float fraction = animatorValue / 100f;
                IntEvaluator mEvaluator = new IntEvaluator();
                //3.使用IntEvaluator计算属性值并赋值给imageView的高
                imageView.getLayoutParams().height = mEvaluator.evaluate(fraction, startValue, endValue);
                imageView.setTranslationX(animatorValue);
                imageView.requestLayout();
            }
        });
        animator.setInterpolator(new LinearInterpolator());
        animator.setDuration(500);
        //6.为ValueAnimator设置目标对象并开始执行动画
        animator.setTarget(imageView);
        animator.start();
    }

    /**
     * 翻转动画效果
     */
    public void flip() {
        //创建View从Visible到Gone的动画
        ObjectAnimator visibleAnimator = ObjectAnimator.ofFloat(imageView, "rotationX", 0.0f, 90.0f);
        visibleAnimator.setInterpolator(new AccelerateInterpolator());
        visibleAnimator.setDuration(500);
        //创建View从Gone到Visible的动画
        final ObjectAnimator invisibleAnimator = ObjectAnimator.ofFloat(reverseImageView, "rotationX", -90.0f, 0.0f);
        invisibleAnimator.setInterpolator(new DecelerateInterpolator());
        invisibleAnimator.setDuration(500);

        visibleAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                imageView.setVisibility(View.GONE);
                invisibleAnimator.start();
                reverseImageView.setVisibility(View.VISIBLE);
            }
        });
        visibleAnimator.setRepeatCount(-1);
        visibleAnimator.setRepeatMode(ValueAnimator.RESTART);
        visibleAnimator.start();
    }

    /**
     * 自由落体
     */
    public void verticalRun() {
        ValueAnimator animator = ValueAnimator.ofFloat(0, screenHeight - imageView.getHeight());
        animator.setTarget(imageView);
        animator.setDuration(1000).start();
//      animator.setInterpolator(value)
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                imageView.setTranslationY((Float) animation.getAnimatedValue());
            }
        });
    }

    /**
     * 抛物线
     */
    public void paowuxian() {
        ValueAnimator valueAnimator = new ValueAnimator();
        valueAnimator.setDuration(3000);
        valueAnimator.setObjectValues(new PointF(0, 0));
        valueAnimator.setInterpolator(new LinearInterpolator());
        valueAnimator.setEvaluator(new TypeEvaluator<PointF>() {
            // fraction = t / duration
            @Override
            public PointF evaluate(float fraction, PointF startValue, PointF endValue) {
                Log.e(TAG, fraction * 3 + "");
                // x方向200px/s ，则y方向0.5 * 10 * t
                PointF point = new PointF();
                point.x = 200 * fraction * 3;
                point.y = 0.5f * 200 * (fraction * 3) * (fraction * 3);
                return point;
            }
        });

        valueAnimator.start();
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                PointF point = (PointF) animation.getAnimatedValue();
                imageView.setX(point.x);
                imageView.setY(point.y);
            }
        });
    }

    public void fadeOut() {
        ObjectAnimator anim = ObjectAnimator.ofFloat(imageView, "alpha", 0.5f);
        anim.setDuration(1000);
        anim.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                Log.e(TAG, "onAnimationStart");
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
                // TODO Auto-generated method stub
                Log.e(TAG, "onAnimationRepeat");
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                Log.e(TAG, "onAnimationEnd");
                ViewGroup parent = (ViewGroup) imageView.getParent();
                if (parent != null)
                    parent.removeView(imageView);
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                // TODO Auto-generated method stub
                Log.e(TAG, "onAnimationCancel");
            }
        });
        anim.start();
    }

    //动画drawable, ShrinkDrawable
    private void Animatable() {
        commonView.setVisibility(View.VISIBLE);
        ShrinkView shrinkView = new ShrinkView(this);
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(100, 100);
        shrinkView.setLayoutParams(params);
        commonView.addView(shrinkView);
    }

    private void scaleDisappear() {
//        commonView.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
//            @Override
//            public boolean onPreDraw() {
//                ScaleDisappear.animToTagOnWindows(AnimationActivity.this, iv001, iv002, 0.5f);
//                commonView.getViewTreeObserver().removeOnPreDrawListener(this);
//                return true;
//            }
//        });
        commonView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (value) return;
                ScaleDisappear.animToTagOnWindows(AnimationActivity.this, iv001, iv002, 1f);
                value = true;
            }
        });
    }

    //也可以使用placeholder或animationSet
    /*
    *     PropertyValuesHolder pvhX = PropertyValuesHolder.ofFloat("alpha", 1f,
                0f, 1f);
        PropertyValuesHolder pvhY = PropertyValuesHolder.ofFloat("y", 0,
                mScreenHeight / 2, 0);
        ObjectAnimator.ofPropertyValuesHolder(mBlueBall, pvhX, pvhY).setDuration(1000).start();
    * */
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void animateProperties() {
        // need API12
        imageView.animate()//
                .alpha(0)//
                .y(screenHeight / 2).setDuration(1000)
                // need API 12
                .withStartAction(new Runnable() {
                    @Override
                    public void run() {
                        Log.e(TAG, "START");
                    }
                    // need API 16
                }).withEndAction(new Runnable() {

            @Override
            public void run() {
                Log.e(TAG, "END");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        imageView.setY(0);
                        imageView.setAlpha(1.0f);
                    }
                });
            }
        }).start();
    }

    //路径插值器
    public void pathInterpolatorCompat() {
        Path path = new Path();
        //x1,y1，x2,y2,x3,y3
        path.cubicTo(0.1f, 0f, 0.6f, 1f, 0.9f, 1f);
        //path.addCircle(0f, 0f, 100, Path.Direction.CCW);
        path.lineTo(1f, 1f);
        ObjectAnimator animator = ObjectAnimator.ofFloat(imageView, View.TRANSLATION_X, 500);
        animator.setInterpolator(PathInterpolatorCompat.create(path));
        animator.start();
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        mTransition = new LayoutTransition();
        // 当一个View在ViewGroup中出现时，对此View设置的动画
//        mTransition.setAnimator(LayoutTransition.APPEARING,
//                (mAppear.isChecked() ? mTransition
//                        .getAnimator(LayoutTransition.APPEARING) : null));
        mTransition.setAnimator(LayoutTransition.APPEARING, (mAppear
                .isChecked() ? ObjectAnimator.ofFloat(this, "scaleX", 0, 1)
                : null));
        //当一个View在ViewGroup中出现时，对此View对其他View位置造成影响，对其他View设置的动画
        mTransition.setAnimator(LayoutTransition.CHANGE_APPEARING,
                (mChangeAppear.isChecked() ? mTransition
                        .getAnimator(LayoutTransition.CHANGE_APPEARING)
                        : null));
        //当一个View在ViewGroup中消失时，对此View设置的动画
        mTransition.setAnimator(LayoutTransition.DISAPPEARING,
                (mDisAppear.isChecked() ? mTransition
                        .getAnimator(LayoutTransition.DISAPPEARING) : null));
        // 当一个View在ViewGroup中消失时，对此View对其他View位置造成影响，对其他View设置的动画
        mTransition.setAnimator(LayoutTransition.CHANGE_DISAPPEARING,
                (mChangeDisAppear.isChecked() ? mTransition
                        .getAnimator(LayoutTransition.CHANGE_DISAPPEARING)
                        : null));
        //LayoutTransition.CHANGE 不是由于View出现或消失造成对其他View位置造成影响，然后对其他View设置的动画。
        mGridLayout.setLayoutTransition(mTransition);
    }

    public void layoutTransition() {
        imageView.setVisibility(View.GONE);
        lContainer.setVisibility(View.VISIBLE);
        mAppear = (CheckBox) findViewById(R.id.id_appear);
        mChangeAppear = (CheckBox) findViewById(R.id.id_change_appear);
        mDisAppear = (CheckBox) findViewById(R.id.id_disappear);
        mChangeDisAppear = (CheckBox) findViewById(R.id.id_change_disappear);
        mAppear.setOnCheckedChangeListener(this);
        mChangeAppear.setOnCheckedChangeListener(this);
        mDisAppear.setOnCheckedChangeListener(this);
        mChangeDisAppear.setOnCheckedChangeListener(this);

        // 创建一个GridLayout
        mGridLayout = new GridLayout(this);
        // 设置每列5个按钮
        mGridLayout.setColumnCount(5);
        // 添加到布局中
        lContainer.addView(mGridLayout);
        //默认动画全部开启
        mTransition = new LayoutTransition();
        mGridLayout.setLayoutTransition(mTransition);
    }

    //3d场景动画
    public void camer3D() {
        MyAnimation animation = new MyAnimation();
        animation.start();
        imageView.setAnimation(animation);
    }
}