package io.github.ishankgulati.breakout;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.AudioManager;
import android.media.SoundPool;
import android.util.Log;
import android.media.MediaPlayer;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Hackbook on 12/26/2015.
 */
public class SoundManager {

    public SoundPool soundPool;
    public MediaPlayer player;
    int beep1ID = -1;
    int beep2ID = -1;
    int beep3ID = -1;
    int loseLifeID = -1;
    int explodeID = -1;
    int winID = -1;
    int loseID = -1;
    int streamId = -1;
    private ConcurrentHashMap<String, Integer> soundIds;

    SoundManager(Context context){
        soundPool = new SoundPool(10, AudioManager.STREAM_MUSIC,0);
        soundIds = new ConcurrentHashMap<String, Integer>();

        // load sounds
        try{
            // Create objects of the 2 required classes
            AssetManager assetManager = context.getAssets();
            AssetFileDescriptor descriptor;

            // Load our fx in memory ready for use
            descriptor = assetManager.openFd("beep1.wav");
            beep1ID = soundPool.load(descriptor, 0);
            soundIds.put("beep1", beep1ID);

            descriptor = assetManager.openFd("beep2.wav");
            beep2ID = soundPool.load(descriptor, 0);
            soundIds.put("beep2", beep2ID);

            descriptor = assetManager.openFd("beep3.wav");
            beep3ID = soundPool.load(descriptor, 0);
            soundIds.put("beep3", beep3ID);

            descriptor = assetManager.openFd("loseLife.wav");
            loseLifeID = soundPool.load(descriptor, 0);
            soundIds.put("loseLife", loseLifeID);

            descriptor = assetManager.openFd("explode.wav");
            explodeID = soundPool.load(descriptor, 0);
            soundIds.put("explode", explodeID);

            descriptor = assetManager.openFd("win.wav");
            winID = soundPool.load(descriptor, 0);
            soundIds.put("win", winID);

            descriptor = assetManager.openFd("lose.wav");
            loseID = soundPool.load(descriptor, 0);
            soundIds.put("lose", loseID);


            // Adding music
            player = MediaPlayer.create(context, R.raw.soundtrack);
            player.setLooping(true);

        }catch(IOException e){
            // Print an error message to the console
            Log.e("error", "failed to load sound files");
        }
    }

    public void playSound(String name){
        int id = soundIds.get(name);
        streamId = soundPool.play(id, 1, 1, 0, 0, 1);
    }


    public void playMusic(){
        if(!player.isPlaying()){
            player.start();
        }
    }

    public void stopAllSounds(){
        if(player.isPlaying()) {
            player.stop();
            try {
                player.prepare();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        soundPool.stop(streamId);
    }
}
