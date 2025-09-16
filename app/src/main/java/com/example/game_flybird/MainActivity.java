package com.example.game_flybird;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends Activity {

    private int zmkuang;
    int zmGao;
    //小球的变量
    float qiu_size=16;
    float qiu_sudu=3;
    float qiu_shang=90;
    float qiuX;
    float qiuY;
    //柱子的变量
    float zhu_gao;
    float zhu_gao_2;
    float zhu_kuan=80;
    float zhu_kuan_2=80;
    float zhuX;
    float zhuY;
    float zhuX_2;
    float zhuY_2;

    int num=0;//小球通过柱子数量
    boolean jieshu=false;//判断游戏是否结束的标志



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //去掉状态栏（全屏运行）
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //获取游戏视图实例
        MyGameView myGameView = new MyGameView(this);
        setContentView(myGameView);
        //获取屏幕的宽和高
        //获取窗口的一个管理器
        WindowManager windowManager = getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        display.getMetrics(metrics);
        //获取屏幕的宽和高
        zmkuang = metrics.widthPixels;
        zmGao = metrics.heightPixels;

    }

    class MyGameView extends View{
        Paint paint=new Paint();

        public MyGameView(Context context) {
            super(context);
        }

        @Override
        protected void onDraw(@NonNull Canvas canvas) {
            super.onDraw(canvas);
            //设置画笔属性
            paint.setStyle(Paint.Style.FILL);
            paint.setAntiAlias(true);//抗锯齿
            if(jieshu){
                //执行游戏结束后的效果
            }
            else{
                //执行游戏进行中的效果
                paint.setColor(Color.RED);
                paint.setTextSize(80);
                canvas.drawText(num+"",zmkuang/2 - 10,80,paint);
                canvas.drawCircle(qiuX,qiuY,qiu_size,paint);
                //绘制上柱子
                paint.setColor(Color.rgb(80,80,200));
                canvas.drawRect(zhuX,zhuY,zhuX+zhu_kuan,zhuY+zhu_gao,paint);
                //绘制下柱子
                canvas.drawRect(zhuX_2,zhuY_2,zhuX_2+zhu_kuan_2,zhuY_2+zhu_gao_2,paint);

            }
        }
    }
}