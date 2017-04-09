package com.webwalker.appdemo.activity;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.PorterDuff;
import android.graphics.RectF;
import android.graphics.Shader;
import android.os.Bundle;
import android.support.v4.view.animation.PathInterpolatorCompat;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;

import com.webwalker.appdemo.R;
import com.webwalker.appdemo.common.Params;
import com.webwalker.appdemo.views.CommonCanvasView;
import com.webwalker.appdemo.views.DrawCanvasView;
import com.webwalker.appdemo.views.WaveView;
import com.webwalker.appdemo.views.XfermodeView;
import com.webwalker.framework.interfaces.ICallback;
import com.webwalker.framework.paint.PaintDemo;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DrawActivity extends BaseActivity {
    @BindView(R.id.xfermodeView)
    XfermodeView xfermodeView;
    @BindView(R.id.v_path_canvas)
    DrawCanvasView pathCanvas;
    @BindView(R.id.v_canvas_wave)
    WaveView waveView;
    @BindView(R.id.v_common_canvas)
    CommonCanvasView commonCanvas;

    private Bitmap src;
    private Bitmap dst;

    public DrawActivity() {
    }

    public DrawActivity(Params params) {
        super(params);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_draw);
        ButterKnife.bind(this);

        init();
//        normal();
//        drawPathAnim();
        saveRestore(); //钟表，绘图技巧
//        touchPaint();
//        waveView();
//        clip();
//        clipWithBitmapShader();
    }

    @Override
    public String getLabel() {
        return "DrawPaint";
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_xfermode, menu);
        return true;
    }

    //PorterDuffXfermode多种模式叠加
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        xfermodeView.setVisibility(View.VISIBLE);
        String title = item.getTitle().toString();
        setTitle(title);
        xfermodeView.setXfermode(PorterDuff.Mode.valueOf(title));
        return super.onOptionsItemSelected(item);
    }

    private void init() {
        src = BitmapFactory.decodeResource(getResources(), R.mipmap.anminal1)
                .copy(Bitmap.Config.ARGB_8888, true);
        dst = Bitmap.createBitmap(src.getWidth(), src.getHeight(), Bitmap.Config.ARGB_8888);
    }

    private void drawPathAnim() {
        pathCanvas.setVisibility(View.VISIBLE);
        //ObjectAnimator.ofInt(wrapper,"width",300).setDuration(3000).start();//如果是普通属性则可不用自定义属性，或者简单重写，
        //不需要再使用这样的代理了
//        Animator animator = ObjectAnimator.ofFloat(pathCanvas, DrawCanvasView.Percentage, 0.0f, 1.0f);
//        animator.setDuration(2000);
//        animator.start();

        ObjectAnimator animator1 = ObjectAnimator.ofInt(pathCanvas, "pointRadius", 0, 300, 100);
        animator1.setDuration(2000);
        animator1.setRepeatCount(-1);
        animator1.setRepeatMode(ValueAnimator.REVERSE);
        animator1.start();
    }

    private void drawArc() {
        final Paint paint = new Paint(); //设置一个笔刷大小是3的黄色的画笔
        paint.setColor(Color.YELLOW);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setStrokeWidth(3);
        commonCanvas.setCallback(new ICallback<Canvas>() {
            @Override
            public void action(Canvas canvas) {
                //绘制弧线区域
                RectF rect = new RectF(0, 0, 100, 100);
                canvas.drawArc(rect, //弧线所使用的矩形区域大小
                        0,  //开始角度
                        90, //扫过的角度
                        false, //是否使用中心
                        paint);
                //绘制弧线区域
//                canvas.drawArc(rect, //弧线所使用的矩形区域大小
//                        0,  //开始角度
//                        90, //扫过的角度
//                        true, //是否使用中心
//                        paint);
            }
        });
    }

    private void drawOval() {
        final Paint paint = new Paint(); //设置一个笔刷大小是3的黄色的画笔
        paint.setColor(Color.YELLOW);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setStrokeWidth(3);
        commonCanvas.setCallback(new ICallback<Canvas>() {
            @Override
            public void action(Canvas canvas) {
                //定义一个矩形区域
                RectF oval = new RectF(0, 0, 200, 300);
                //矩形区域内切椭圆
                canvas.drawOval(oval, paint);
            }
        });
    }

    private void drawPosText() {
        final Paint paint = new Paint(); //设置一个笔刷大小是3的黄色的画笔
        paint.setColor(Color.YELLOW);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setStrokeWidth(3);
        commonCanvas.setCallback(new ICallback<Canvas>() {
            @Override
            public void action(Canvas canvas) {
                //按照既定点 绘制文本内容
                canvas.drawPosText("Android777", new float[]{
                        10, 10, //第一个字母在坐标10,10
                        20, 20, //第二个字母在坐标20,20
                        30, 30, //....
                        40, 40,
                        50, 50,
                        60, 60,
                        70, 70,
                        80, 80,
                        90, 90,
                        100, 100
                }, paint);
            }
        });
    }

    private void drawTextOnPath() {
        final Paint paint = new Paint(); //设置一个笔刷大小是3的黄色的画笔
        paint.setColor(Color.YELLOW);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setStrokeWidth(3);
        commonCanvas.setCallback(new ICallback<Canvas>() {
            @Override
            public void action(Canvas canvas) {
                Path path = new Path(); //定义一条路径
                path.moveTo(10, 10); //移动到 坐标10,10
                path.lineTo(50, 60);
                path.lineTo(200, 80);
                path.lineTo(10, 10);

//          canvas.drawPath(path, paint);
                canvas.drawTextOnPath("Android777开发者博客", path, 10, 10, paint);
            }
        });
    }

    //canvas变换
    private void saveRestore() {
        final Paint paint = new Paint(); //设置一个笔刷大小是3的黄色的画笔
        paint.setColor(Color.YELLOW);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setStrokeWidth(3);
        commonCanvas.setCallback(new ICallback<Canvas>() {
                                     @Override
                                     public void action(Canvas canvas) {
                                         paint.setAntiAlias(true);
                                         paint.setStyle(Paint.Style.STROKE);
                                         canvas.drawColor(Color.LTGRAY);
                                         //移动后0,0变成了屏幕的中心点，也是圆心，便于绘画
                                         canvas.translate(canvas.getWidth() / 2, 200); //将位置移动画纸的坐标点:150,150
                                         canvas.drawCircle(0, 0, 100, paint); //画圆圈

                                         //使用path绘制路径文字
                                         canvas.save();
                                         canvas.translate(-75, -75); //画完圆后位移新的绘画画布
                                         Path path = new Path();
                                         //在矩形区域X：0-150， Y：0-150范围内设计圆弧路径，
                                         //-180+180，起始点-180度，结束点0度， -180是9点钟方向，180也是3点钟方向
                                         path.addArc(new RectF(0, 0, 150, 150), -180, 180);
                                         Paint citePaint = new Paint(paint);
                                         citePaint.setTextSize(14);
                                         citePaint.setStrokeWidth(1);
                                         //将圆弧水平位移一下
                                         canvas.drawTextOnPath("http://www.android777.com", path, 28, 0, citePaint);
                                         canvas.restore();

                                         //画刻度
                                         Paint tmpPaint = new Paint(paint); //小刻度画笔对象
                                         tmpPaint.setStrokeWidth(1);
                                         float y = 100;
                                         int count = 60; //总刻度数
                                         for (int i = 0; i < count; i++) {
                                             if (i % 5 == 0) { //大刻度
                                                 canvas.drawLine(0f, y, 0, y + 12f, paint);
                                                 canvas.drawText(String.valueOf(i / 5 + 1), -4f, y + 25f, tmpPaint);

                                             } else {
                                                 canvas.drawLine(0f, y, 0f, y + 5f, tmpPaint); //小刻度
                                             }
                                             canvas.rotate(360 / count, 0f, 0f); //以圆心为中心点，旋转画纸，便于刻度逐一绘制上去
                                         }

                                         //绘制指针
                                         tmpPaint.setColor(Color.GRAY);
                                         tmpPaint.setStrokeWidth(4);
                                         canvas.drawCircle(0, 0, 7, tmpPaint);
                                         tmpPaint.setStyle(Paint.Style.FILL);
                                         tmpPaint.setColor(Color.YELLOW);
                                         canvas.drawCircle(0, 0, 5, tmpPaint);
                                         canvas.drawLine(0, 10, 0, -65, paint);
                                     }
                                 }
        );
    }

    private void touchPaint() {
        final ArrayList<PointF> graphics = new ArrayList<PointF>();
        final Paint paint = new Paint(); //设置一个笔刷大小是3的黄色的画笔
        paint.setColor(Color.YELLOW);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setStrokeWidth(20);
        commonCanvas.setCallback(new ICallback<Canvas>() {
            @Override
            public void action(Canvas canvas) {
                for (PointF point : graphics) {
                    canvas.drawPoint(point.x, point.y, paint);
                }
            }
        });
        commonCanvas.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                graphics.add(new PointF(event.getX(), event.getY()));
                commonCanvas.invalidate(); //重新绘制区域
                return true;
            }
        });
    }

    private void waveView() {
        waveView.setVisibility(View.VISIBLE);
        Animator animator = ObjectAnimator.ofInt(waveView, WaveView.RADIUS, 122, 192);
        animator.setInterpolator(PathInterpolatorCompat.create(0.2f, 0f, 0.24f, 1f));
        animator.setDuration(1120);
        final ValueAnimator fadeAnimator = ObjectAnimator.ofFloat(waveView, View.ALPHA, 1.0f, 0.0f);
        fadeAnimator.setInterpolator(PathInterpolatorCompat.create(0.33f, 0f, 0.66f, 1f));
        fadeAnimator.setDuration(1120);
        final AnimatorSet alertAnimator = new AnimatorSet();
        alertAnimator.playTogether(animator, fadeAnimator);
        alertAnimator.start();
    }

    //基础绘图
    private void normal() {
        commonCanvas.setCallback(new ICallback<Canvas>() {
            @Override
            public void action(Canvas canvas) {
                PaintDemo.normal(getResources(), canvas, R.mipmap.anminal1);
            }
        });
    }

    //裁剪画布区域实现指定形状的图
    private void clip() {
        commonCanvas.setCallback(new ICallback<Canvas>() {
            @Override
            public void action(Canvas canvas) {
                int radius = src.getWidth() / 2; //src为我们要画上去的图，跟上一个示例中的bitmap一样。
                Canvas c = new Canvas(dst);
                Paint paint = new Paint();
                paint.setColor(Color.BLACK);
                paint.setAntiAlias(true);
                Path path = new Path();
                path.addCircle(radius, radius, radius, Path.Direction.CW);
                c.clipPath(path); //裁剪区域
                c.drawBitmap(src, 0, 0, paint); //把图画上去
                canvas.drawBitmap(dst, 0, 0, paint); //把图画上去
            }
        });
    }

    //裁剪画布区域实现指定形状的图
    private void clipWithBitmapShader() {
        commonCanvas.setCallback(new ICallback<Canvas>() {
            @Override
            public void action(Canvas canvas) {
                int radius = src.getWidth() / 2;
                BitmapShader bitmapShader = new BitmapShader(src, Shader.TileMode.REPEAT, Shader.TileMode.REPEAT);
                Canvas c = new Canvas(dst);
                Paint paint = new Paint();
                paint.setAntiAlias(true);
                paint.setShader(bitmapShader);
                c.drawCircle(radius, radius, radius, paint);

                canvas.drawBitmap(dst, 0, 0, paint); //把图画上去
            }
        });
    }
}
