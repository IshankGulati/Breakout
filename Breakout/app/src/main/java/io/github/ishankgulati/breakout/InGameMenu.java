package io.github.ishankgulati.breakout;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.view.SurfaceHolder;

import java.util.Vector;

/**
 * Created by Hackbook on 12/27/2015.
 */
public class InGameMenu {
    private int screenX, screenY;
    public enum InMenuResult { Exit, Resume, Restart }

    public class MenuItem{
        public RectF rect;
        public InMenuResult action;
    }
    private Vector<MenuItem> _inMenuItems;

    InGameMenu(){
        screenX = BreakoutGame.screenX;
        screenY = BreakoutGame.screenY;

        _inMenuItems = new Vector<MenuItem>();

        MenuItem resumeButton = new MenuItem();
        resumeButton.rect.left = 0;
        resumeButton.rect.top = screenY / 4 + screenY / 20;
        resumeButton.rect.bottom = screenY * 3 / 8 - screenY / 20;
        resumeButton.rect.right = screenY;
        resumeButton.action = InMenuResult.Resume;

        MenuItem restartButton = new MenuItem();
        restartButton.rect.left = 0;
        restartButton.rect.top = screenY / 2 + screenY / 20;
        restartButton.rect.bottom = screenY * 5 / 8 - screenY / 20;
        restartButton.rect.right = screenY;
        restartButton.action = InMenuResult.Restart;

        MenuItem exitButton = new MenuItem();
        exitButton.rect.left = 0;
        exitButton.rect.top = screenY * 3 / 4 + screenY / 20;
        exitButton.rect.bottom = screenY * 7 / 8 - screenY / 20;
        exitButton.rect.right = screenY;
        exitButton.action = InMenuResult.Exit;

        _inMenuItems.addElement(resumeButton);
        _inMenuItems.addElement(restartButton);
        _inMenuItems.addElement(exitButton);
    }

    public Vector<MenuItem> getMenuItems(){
        return _inMenuItems;
    }

    public void show(SurfaceHolder holder, Canvas canvas, Paint paint){
        if(holder.getSurface().isValid()){
            canvas = holder.lockCanvas();
            canvas.drawColor(Color.argb(255, 26, 128, 182));

            paint.setColor(Color.argb(255, 255, 255, 255));
            paint.setTextSize(screenY / 8);

            int xPos = (screenX / 2);
            int yPos = (int) ((screenY / 4) - ((paint.descent() + paint.ascent()) / 2));
            canvas.drawText("Resume", xPos, yPos, paint);

            yPos = (int) ((screenY / 2) - ((paint.descent() + paint.ascent()) / 2));
            canvas.drawText("Restart", xPos, yPos, paint);

            yPos = (int) ((screenY * 3 / 4) - ((paint.descent() + paint.ascent()) / 2));
            canvas.drawText("Exit", xPos, yPos, paint);

            holder.unlockCanvasAndPost(canvas);
        }
    }
}
