package com.example.catchergame;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;

import com.example.catchergame.R;

public class SoundPlayer
{
    private static SoundPool soundPool;
    private static int scoreSound;
    private static int hitSound;

    public SoundPlayer(Context context)
    {
        soundPool = new SoundPool(2, AudioManager.STREAM_MUSIC, 0);

        scoreSound = soundPool.load(context, R.raw.score, 1);
        hitSound = soundPool.load(context, R.raw.hit, 1);
    }

    public void playScoreSound()
    {
        soundPool.play(scoreSound, 1.0f, 1.0f, 1, 0, 1.0f);
    }

    public void playHitSound()
    {
        soundPool.play(hitSound, 1.0f, 1.0f, 1, 0, 1.0f);
    }
}
