package io.github.ishankgulati.breakout;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.SurfaceHolder;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Hackbook on 12/26/2015.
 */
public class GameObjectManager {

    private ConcurrentHashMap<String, VisibleGameObject> gameObjects = new ConcurrentHashMap
            <String, VisibleGameObject>();

    public void add(String name, VisibleGameObject gameObject){
        gameObjects.put(name, gameObject);
    }

    public void remove(String name){
        gameObjects.remove(name);
    }

    public int getObjectCount(){
        return gameObjects.size();
    }

    public VisibleGameObject get(String name){
        return gameObjects.get(name);
    }

    public void drawAll(SurfaceHolder ourHolder, Canvas canvas, Paint paint){
        if(ourHolder.getSurface().isValid()) {
            canvas = ourHolder.lockCanvas();
            canvas.drawColor(Color.argb(255, 26, 128, 182));
            for (ConcurrentHashMap.Entry<String, VisibleGameObject> entry : gameObjects.entrySet()) {
                VisibleGameObject value = entry.getValue();
                value.draw(canvas, paint);
            }
            BreakoutGame.BreakoutView.drawScoreBoard();
            ourHolder.unlockCanvasAndPost(canvas);
        }
    }

    public void updateAll(long fps){
        for(ConcurrentHashMap.Entry<String, VisibleGameObject> entry : gameObjects.entrySet()){
            VisibleGameObject value = entry.getValue();
            value.update(fps);
        }
    }

    public void resetAll(){
        for(ConcurrentHashMap.Entry<String, VisibleGameObject> entry : gameObjects.entrySet()){
            VisibleGameObject value = entry.getValue();
            value.reset();
        }
    }
}
