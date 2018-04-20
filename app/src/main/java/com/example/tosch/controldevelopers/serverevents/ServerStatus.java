package com.example.tosch.controldevelopers.serverevents;

public final class ServerStatus {
    private boolean isPlaying;
    private double volume;
    private int currentTime;

    public ServerStatus(boolean isPlaying, double volume, int currentTime) {
        this.isPlaying = isPlaying;
        this.volume = volume;
        this.currentTime = currentTime;
    }

    public boolean isPlaying() {
        return isPlaying;
    }

    public double getVolume() {
        return volume;
    }

    public int getCurrentTime() {
        return currentTime;
    }

    @Override
    public String toString() {
        return "StreamStatus{" +
                "isPlaying=" + isPlaying +
                ", volume=" + volume +
                ", currentTime=" + currentTime +
                '}';
    }
}
