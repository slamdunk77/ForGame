package com.sankin.forgame;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.ImageView;

import com.sankin.forgame.ContentThread.MoveThread;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class GameActivity extends AppCompatActivity implements SensorEventListener {
    private float[] angel = new float[3];
    private SensorManager sensorManager = null;
    private Sensor gyroSensor = null;
    private List<MoveThread> list = new ArrayList<>(); //敌人序列
    private final static int MAX_ATTACKER = 10;


    @SuppressWarnings("deprecation")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        //添加敌人
        /*
        image = new ImageView(GameActivity.this);
        image.setId(View.generateViewId());
        image.setImageResource(R.drawable.ic_attacker);
        ConstraintLayout.LayoutParams layoutParams = new ConstraintLayout.LayoutParams(R.dimen.enemy, R.dimen.enemy);
        layoutParams.width = getResources().getDimensionPixelOffset(R.dimen.enemy);
        layoutParams.height = getResources().getDimensionPixelOffset(R.dimen.enemy);
        layoutParams.setMargins(400, 100, 0, 0);
        addContentView(image, layoutParams);
        */

        //获取传感器信息
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        gyroSensor = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);

    }

    @Override
    protected void onPause() {
        super.onPause();
        //解除传感器监听
        sensorManager.unregisterListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //为传感器注册监听器
        sensorManager.registerListener(this, gyroSensor,
                SensorManager.SENSOR_DELAY_NORMAL);

        //产生敌人
        new Thread(){
            @Override
            public void run(){
                while(true){
                    try{
                        Thread.sleep(20000);
                        generateAttacker();
                    }catch (InterruptedException e){
                        e.printStackTrace();
                    }
                }
            }
        }.start();
    }

    //传感器变化
    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_GYROSCOPE) {
            angel[0] = event.values[0]; //x
            angel[1] = event.values[1]; //y
            angel[2] = event.values[2]; //z
            synchronized (list) {
                checkDirection();
            }
        }
    }

    public void checkDirection() {
        for (MoveThread move : list) {
            ImageView image = move.getImageView();
            /*
                检查角度，夹角小于10°认为瞄准，杀死该敌人
                move.setFlag(false);
                list.remove(move);
             */
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    //产生敌人、此时双方攻击均无效
    public void generateAttacker() {
        synchronized (list) {
            while (list.size() + 1 > MAX_ATTACKER) {
                try {
                    list.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            //随机生成敌人位置
            ImageView attacker = new ImageView(this);

            MoveThread move = new MoveThread(attacker);
            move.start();
            list.add(move);
            list.notifyAll();
        }
    }
}