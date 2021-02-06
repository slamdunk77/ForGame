package com.sankin.forgame.ContentThread;

import android.widget.ImageView;

import java.util.Random;

public class MoveThread extends Thread{
    private ImageView imageView;
    private boolean flag;

    public MoveThread(ImageView imageView) {
        this.imageView = imageView;
        flag = true;
    }

    public ImageView getImageView() {
        return imageView;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    @Override
    public void run() {
        while (flag) {
            //随机移动
            Random random = new Random();
            int a = random.nextInt() % 3;
            //做移动操作
            try {
                sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
