package com.engine.manager;

public class AudioManager {
    private String currentMusic;
    private String currentSound;
    private boolean musicEnabled = true;
    private boolean soundEnabled = true;

    public void playMusic(String musicId) {
        if (!musicEnabled) {
            System.out.println("♪ Music is disabled");
            return;
        }

        this.currentMusic = musicId;
        System.out.println("♪ Playing music: " + musicId);
    }

    public void stopMusic() {
        if (currentMusic != null) {
            System.out.println("♪ Music stopped: " + currentMusic);
            currentMusic = null;
        } else {
            System.out.println("♪ No music is playing");
        }
    }

    public void playSound(String soundId) {
        if (!soundEnabled) {
            System.out.println("♫ Sound effects are disabled");
            return;
        }

        System.out.println("♫ Sound effect: " + soundId);
    }

    public void setMusicEnabled(boolean enabled) {
        this.musicEnabled = enabled;
        if (!enabled && currentMusic != null) {
            stopMusic();
        }
        System.out.println("Music " + (enabled ? "enabled" : "disabled"));
    }

    public void setSoundEnabled(boolean enabled) {
        this.soundEnabled = enabled;
        System.out.println("Sound effects " + (enabled ? "enabled" : "disabled"));
    }

    public String getCurrentMusic() {
        return currentMusic;
    }

    public boolean isMusicEnabled() {
        return musicEnabled;
    }

    public boolean isSoundEnabled() {
        return soundEnabled;
    }

    public void showAudioStatus() {
        System.out.println("\n=== Audio Status ===");
        System.out.println("Current music: " + (currentMusic != null ? currentMusic : "none"));
        System.out.println("Music enabled: " + musicEnabled);
        System.out.println("Sound enabled: " + soundEnabled);
    }
}