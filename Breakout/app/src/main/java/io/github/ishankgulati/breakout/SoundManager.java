package io.github.ishankgulati.breakout;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.AudioManager;
import android.media.SoundPool;
import android.util.Log;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Hackbook on 12/26/2015.
 */
public class SoundManager {

    SoundPool soundPool;
    int beep1ID = -1;
    int beep2ID = -1;
    int beep3ID = -1;
    int loseLifeID = -1;
    int explodeID = -1;
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
            descriptor = assetManager.openFd("beep1.ogg");
            beep1ID = soundPool.load(descriptor, 0);
            soundIds.put("beep1", beep1ID);

            descriptor = assetManager.openFd("beep2.ogg");
            beep2ID = soundPool.load(descriptor, 0);
            soundIds.put("beep2", beep2ID);

            descriptor = assetManager.openFd("beep3.ogg");
            beep3ID = soundPool.load(descriptor, 0);
            soundIds.put("beep3", beep3ID);

            descriptor = assetManager.openFd("loseLife.ogg");
            loseLifeID = soundPool.load(descriptor, 0);
            soundIds.put("loseLife", loseLifeID);

            descriptor = assetManager.openFd("explode.ogg");
            explodeID = soundPool.load(descriptor, 0);
            soundIds.put("explode", explodeID);

        }catch(IOException e){
            // Print an error message to the console
            Log.e("error", "failed to load sound files");
        }
    }

    public void play(String name){
        int id = soundIds.get(name);
        soundPool.play(id, 1, 1, 0, 0, 1);
    }

    public void stopAllSounds(){
    }
}
