package com.sankin.forgame;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

public class GameActivity extends AppCompatActivity {

    private View mView;
    private ImageView mIvGun;
    private static int count = 0, imageCount = 0;
    private static long firClick, secClick;
    private int image[] = new int[]{
            R.drawable.ic_gun,
            R.drawable.ic_pao,
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        mView = findViewById(R.id.v_bg);
        mIvGun = findViewById(R.id.iv_gun);
        mIvGun.setImageResource(image[imageCount]);
        //
        mView.setOnTouchListener(new onDoubleClick());

    }

    class onDoubleClick implements View.OnTouchListener{
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if(MotionEvent.ACTION_DOWN == event.getAction()){
                count++;
                if(count == 1){
                    firClick = System.currentTimeMillis();

                } else if (count == 2){
                    secClick = System.currentTimeMillis();
                    if(secClick - firClick < 1000){
                        //双击事件
                        if(++imageCount>=image.length){
                            imageCount=0;
                        }
                        mIvGun.setImageResource(image[imageCount]);
                        Log.d("count", count+"");
                        Log.d("imageCount", imageCount+"");
                        Log.d("image[imageCount]", image[imageCount]+"");
                    }
                    count = 0;
                    firClick = 0;
                    secClick = 0;
                }
            }
            return true;
        }
    }
}