package edu.mohamedshiha.gameca;


import android.os.CountDownTimer;

public abstract class CountUpTimer extends CountDownTimer {
    private static final long INTERVAL_MS = 1000;
    private final long duration;
    public float CurrentSeconds;

    protected CountUpTimer(long durationMs) {
        super(durationMs, INTERVAL_MS);
        this.duration = durationMs;
        CurrentSeconds =0;
    }

    public abstract void onTick(float second);

    @Override
    public void onTick(long msUntilFinished) {
        float second = ((duration - msUntilFinished) / 1000);
        CurrentSeconds += (second - CurrentSeconds);
        this.onTick(CurrentSeconds);
    }

    @Override
    public void onFinish() {
        onTick(duration / 1000);
    }

}