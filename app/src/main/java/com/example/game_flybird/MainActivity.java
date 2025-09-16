package com.example.game_flybird;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

import androidx.annotation.NonNull;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends Activity {

    private int zmkuang;
    int zmGao;
    // 小球的变量
    float qiu_size = 16;
    float qiu_sudu = 3;
    float qiu_shang = 90;
    float qiuX;
    float qiuY;
    // 柱子的变量
    float zhu_gao;
    float zhu_gao_2;
    float zhu_kuan = 80;
    float zhu_kuan_2 = 80;
    float zhuX;
    float zhuY;
    float zhuX_2;
    float zhuY_2;
    float zhu_sudu;

    int num = 0; // 小球通过柱子数量
    boolean jieshu = false; // 判断游戏是否结束的标志
    MyGameView myGameView;

    // 将Timer和TimerTask改为类成员变量
    private Timer timer;
    private TimerTask timerTask;

    // 计分相关变量
    private boolean scored = false; // 标记当前柱子对是否已计分

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 去掉状态栏（全屏运行）
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        // 获取游戏视图实例
        myGameView = new MyGameView(this);
        setContentView(myGameView);
        // 获取屏幕的宽和高
        // 获取窗口的一个管理器
        WindowManager windowManager = getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        display.getMetrics(metrics);
        // 获取屏幕的宽和高
        zmkuang = metrics.widthPixels;
        zmGao = metrics.heightPixels;
        play();
    }

    public void play() {
        // 取消之前的定时器
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
        if (timerTask != null) {
            timerTask.cancel();
            timerTask = null;
        }

        jieshu = false;
        // 设置两根柱子的初始位置
        zhuX = zmkuang - zhu_kuan;
        zhuY = 0;
        zhuX_2 = zmkuang - zhu_kuan;
        zhuY_2 = zmGao;
        zhu_gao = zmGao / 2 - 200;
        zhu_gao_2 = zmGao - zhu_gao - 200;
        num = 0;
        zhu_sudu = 5;
        scored = false; // 重置计分标记

        myGameView.setOnTouchListener(shoushi);
        qiu_sudu = 3.5f;
        qiu_shang = 90;
        qiuX = 50;
        qiuY = zmGao / 2;
        handler.sendEmptyMessage(0x123);

        timer = new Timer();
        timerTask = new TimerTask() {
            @Override
            public void run() {
                // 设置小球跟柱子的坐标
                qiuY = qiuY + qiu_sudu;
                zhuX = zhuX - zhu_sudu;
                zhuX_2 = zhuX_2 - zhu_sudu;

                // 检测是否通过柱子
                if (!scored && zhuX + zhu_kuan < qiuX) {
                    num++; // 增加分数
                    scored = true; // 标记当前柱子对已计分
                }

                // 如果柱子移出屏幕左侧，重置柱子并重置计分标记
                if (zhuX + zhu_kuan < 0) {
                    zhuX = zmkuang;
                    zhuX_2 = zmkuang;
                    scored = false; // 重置计分标记
                }

                // 碰撞检测 - 在游戏循环中持续检测
                // 1. 检测是否碰到上下边界
                if (qiuY - qiu_size <= 0 || qiuY + qiu_size >= zmGao) {
                    jieshu = true;
                }

                // 2. 检测是否碰到柱子
                // 上柱子碰撞检测
                if (qiuX + qiu_size > zhuX && qiuX - qiu_size < zhuX + zhu_kuan) {
                    if (qiuY - qiu_size < zhu_gao) {
                        jieshu = true;
                    }
                }

                // 下柱子碰撞检测
                if (qiuX + qiu_size > zhuX_2 && qiuX - qiu_size < zhuX_2 + zhu_kuan_2) {
                    if (qiuY + qiu_size > zmGao - zhu_gao_2) {
                        jieshu = true;
                    }
                }

                handler.sendEmptyMessage(0x123);
            }
        };
        timer.schedule(timerTask, 0, 15);
    }

    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0x123) {
                myGameView.invalidate();
            }
        }
    };

    View.OnTouchListener shoushi = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    // 手指点击事件 - 使小球向上飞
                    qiuY = qiuY - qiu_shang;
                    handler.sendEmptyMessage(0x123);
                    break;
            }
            return true;
        }
    };

    class MyGameView extends View {
        Paint paint = new Paint();

        public MyGameView(Context context) {
            super(context);
        }

        @Override
        protected void onDraw(@NonNull Canvas canvas) {
            super.onDraw(canvas);
            // 设置画笔属性
            paint.setStyle(Paint.Style.FILL);
            paint.setAntiAlias(true); // 抗锯齿
            if (jieshu) {
                // 执行游戏结束后的效果
                paint.setColor(Color.RED);
                paint.setTextSize(80);
                canvas.drawText("游戏结束", zmkuang / 2 - 160, zmGao / 2 - 80, paint);

                // 显示最终得分
                canvas.drawText("得分: " + num, zmkuang / 2 - 100, zmGao / 2, paint);

                paint.setTextSize(40);
                canvas.drawText("点击屏幕重新开始", zmkuang / 2 - 160, zmGao / 2 + 80, paint);

                // 设置触摸监听器以重新开始游戏
                this.setOnTouchListener(new OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        if (event.getAction() == MotionEvent.ACTION_DOWN) {
                            // 移除当前触摸监听器，避免多次触发
                            setOnTouchListener(null);
                            play();
                        }
                        return true;
                    }
                });
            } else {
                // 执行游戏进行中的效果
                paint.setColor(Color.RED);
                paint.setTextSize(80);
                canvas.drawText(num + "", zmkuang / 2 - 10, 80, paint);
                canvas.drawCircle(qiuX, qiuY, qiu_size, paint);
                // 绘制上柱子
                paint.setColor(Color.rgb(80, 80, 200));
                canvas.drawRect(zhuX, zhuY, zhuX + zhu_kuan, zhuY + zhu_gao, paint);
                // 绘制下柱子
                canvas.drawRect(zhuX_2, zmGao - zhu_gao_2, zhuX_2 + zhu_kuan_2, zmGao, paint);
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 在Activity销毁时取消定时器，避免内存泄漏
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
        if (timerTask != null) {
            timerTask.cancel();
            timerTask = null;
        }
    }
}