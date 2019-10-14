package com.example.catchergame;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity
{

    private TextView scoreLabel;
    private TextView livesLabel;
    private TextView startLabel;
    private ImageView box;
    private ImageView orange;
    private ImageView pink;
    private ImageView black;

    // Size
    private int frameHeight;
    private int boxSize;
    private int screenWidth;
    private int screenHeight;

    // Position
    private int boxY;
    private int orangeX;
    private int orangeY;
    private int pinkX;
    private int pinkY;
    private int blackX;
    private int blackY;

    // Score
    private int score = 0;
    private int lives = 3;

    // Initialize class
    private Handler handler = new Handler();
    private Timer timer = new Timer();
    private SoundPlayer sound;

    // Status Check
    private boolean action_flag = false;
    private boolean start_flag = false;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sound = new SoundPlayer(this);

        scoreLabel = (TextView) findViewById(R.id.scoreLabel);
        livesLabel = (TextView) findViewById(R.id.livesLabel);
        startLabel = (TextView) findViewById(R.id.startLabel);
        box = (ImageView) findViewById(R.id.box);
        orange = (ImageView) findViewById(R.id.orange);
        pink = (ImageView) findViewById(R.id.pink);
        black = (ImageView) findViewById(R.id.black);

        // Get screen size
        WindowManager wm = getWindowManager();
        Display disp = wm.getDefaultDisplay();
        Point size = new Point();
        disp.getSize(size);

        screenWidth = size.x;
        screenHeight = size.y;

        // Move objects off-screen
        orange.setX(-80);
        orange.setY(-80);
        pink.setX(-80);
        pink.setY(-80);
        black.setX(-80);
        black.setY(-80);

        scoreLabel.setText("Score: 0");
    }

    public void changePos()
    {
        hitCheck();

        // Orange
        orangeX -= 12;
        if(orangeX < 0)
        {
            orangeX = screenWidth + 20;
            orangeY = (int) Math.floor(Math.random() * (frameHeight - orange.getHeight()));
        }
        orange.setX(orangeX);
        orange.setY(orangeY);

        // Black
        blackX -= 18;
        if(blackX < 0)
        {
            blackX = screenWidth + 20;
            blackY = (int) Math.floor(Math.random() * (frameHeight - black.getHeight()));
        }
        black.setX(blackX);
        black.setY(blackY);

        // Pink
        pinkX -= 24;
        if(pinkX < 0)
        {
            pinkX = screenWidth + 5000;
            pinkY = (int) Math.floor(Math.random() * (frameHeight - pink.getHeight()));
        }
        pink.setX(pinkX);
        pink.setY(pinkY);

        // Box
        if(action_flag)
        {
            boxY -= 20;
        }
        else
        {
            boxY += 20;
        }

        //Check box position
        if(boxY < 0) {boxY = 0;}
        if(boxY > frameHeight - boxSize) {boxY = frameHeight - boxSize;}

        box.setY(boxY);

        scoreLabel.setText("Score: " + score);
        livesLabel.setText("Lives: " + lives);
    }

    public void hitCheck()
    {
        // Checks if the center of a ball is inside the box.

        // Orange
        int orangeCenterX = orangeX + orange.getWidth() / 2;
        int orangeCenterY = orangeY + orange.getHeight() / 2;

        if(0 <= orangeCenterX && orangeCenterX <= boxSize && boxY <= orangeCenterY && orangeCenterY <= boxY + boxSize)
        {
            score += 10;
            orangeX = -100;
            sound.playScoreSound();
        }

        // Pink
        int pinkCenterX = pinkX + pink.getWidth() / 2;
        int pinkCenterY = pinkY + pink.getHeight() / 2;

        if(0 <= pinkCenterX && pinkCenterX <= boxSize && boxY <= pinkCenterY && pinkCenterY <= boxY + boxSize)
        {
            score += 50;
            pinkX = -100;
            sound.playScoreSound();
        }

        // Black
        int blackCenterX = blackX + black.getWidth() / 2;
        int blackCenterY = blackY + black.getHeight() / 2;

        if(0 <= blackCenterX && blackCenterX <= boxSize && boxY <= blackCenterY && blackCenterY <= boxY + boxSize)
        {
            --lives;
            blackX = -100;
            sound.playHitSound();

            if(lives == 0) // Game over
            {
                timer.cancel();
                timer = null;

                // Show result
                Intent intent = new Intent(getApplicationContext(), ResultActivity.class);
                intent.putExtra("SCORE", score);
                startActivity(intent);
            }
        }
    }

    public boolean onTouchEvent(MotionEvent me)
    {
        if(!start_flag)
        {
            start_flag = true;

            FrameLayout frame = (FrameLayout)  findViewById(R.id.frame);
            frameHeight = frame.getHeight();
            boxY = (int) box.getY();
            boxSize = box.getHeight();

            startLabel.setVisibility(View.GONE);

            timer.schedule(new TimerTask()
            {
                @Override
                public void run()
                {
                    handler.post(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            changePos();
                        }
                    });
                }
            }, 0, 20);
        }
        else
        {
            if(me.getAction() == MotionEvent.ACTION_DOWN)
            {
                action_flag = true;
            }
            else if(me.getAction() == MotionEvent.ACTION_UP)
            {
                action_flag = false;
            }
        }



        return true;
    }
}
