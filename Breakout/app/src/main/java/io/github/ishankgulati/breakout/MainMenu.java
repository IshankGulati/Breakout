package io.github.ishankgulati.breakout;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.view.SurfaceHolder;

import java.util.Iterator;
import java.util.Vector;

/**
 * Created by Hackbook on 12/27/2015.
 */
public class MainMenu {
    private int screenX, screenY;
    public enum MenuResult { Exit, Play }

    public class MenuItem{
        public RectF rect;
        public MenuResult action;
    }
    private Vector<MenuItem> _menuItems;

    MainMenu(){
        screenX = BreakoutGame.screenX;
        screenY = BreakoutGame.screenY;

        _menuItems = new Vector<MenuItem>();

        MenuItem playButton = new MenuItem();
        playButton.rect.left = 0;
        playButton.rect.top = screenY / 4 + screenY / 15;
        playButton.rect.bottom = screenY / 2 - screenY / 15;
        playButton.rect.right = screenY;
        playButton.action = MenuResult.Play;

        MenuItem exitButton = new MenuItem();
        exitButton.rect.left = 0;
        exitButton.rect.top = screenY / 2 + screenY / 15;
        exitButton.rect.bottom = screenY * 3 / 4 - screenY / 15;
        exitButton.rect.right = screenY;
        exitButton.action = MenuResult.Exit;

        _menuItems.addElement(playButton);
        _menuItems.addElement(exitButton);
    }

    public Vector<MenuItem> getMenuItems(){
        return _menuItems;
    }

    public void show(SurfaceHolder holder, Canvas canvas, Paint paint){
        if(holder.getSurface().isValid()){
            canvas = holder.lockCanvas();
            canvas.drawColor(Color.argb(255, 26, 128, 182));

            paint.setColor(Color.argb(255, 255, 255, 255));
            paint.setTextSize(screenY / 6);

            int xPos = (canvas.getWidth() / 2);
            int yPos = (int) ((canvas.getHeight() / 4) - ((paint.descent() + paint.ascent()) / 2));
            canvas.drawText("Play", xPos, yPos, paint);

            yPos = (int) ((canvas.getHeight() / 2) - ((paint.descent() + paint.ascent()) / 2));
            canvas.drawText("Exit", xPos, yPos, paint);

            holder.unlockCanvasAndPost(canvas);
        }
    }
}
