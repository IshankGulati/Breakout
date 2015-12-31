package io.github.ishankgulati.breakout;


import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.SurfaceHolder;

import java.util.Vector;

/**
 * Created by Hackbook on 12/27/2015.
 */
public class MainMenu {
    private int screenX, screenY;
    public enum MenuResult { Exit, Play }
    MenuItem playButton;
    MenuItem exitButton;

    public class MenuItem{
        public Rect rect;
        public MenuResult action;
    }
    private Vector<MenuItem> _menuItems;

    MainMenu(){
        screenX = BreakoutGame.BreakoutView.screenX;
        screenY = BreakoutGame.BreakoutView.screenY;

        _menuItems = new Vector<MenuItem>();
        Paint paint;
        paint = new Paint();
        paint.setTextSize(130);

        String text = "Play";
        Rect bounds = new Rect();
        paint.getTextBounds(text, 0, text.length(), bounds);

        int height = (int) (paint.descent() - paint.ascent());
        //distance between two buttons
        int dist = screenY/10;

        playButton = new MenuItem();
        playButton.rect = new Rect();
        playButton.rect.left = (screenX / 2) - (bounds.width() / 2);
        playButton.rect.top = (screenY / 2) - height - dist/2;
        playButton.rect.bottom = playButton.rect.top + height;
        playButton.rect.right = playButton.rect.left + bounds.width();
        playButton.action = MenuResult.Play;

        text = "Settings";
        paint.getTextBounds(text, 0, text.length(), bounds);
        exitButton = new MenuItem();
        exitButton.rect = new Rect();
        exitButton.rect.left = (screenX / 2) - (bounds.width() / 2);
        exitButton.rect.top = (screenY / 2) + dist/2;
        exitButton.rect.bottom = exitButton.rect.top + height;
        exitButton.rect.right = exitButton.rect.left + bounds.width();
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
            paint.setTextSize(130);

            float height = paint.descent() - paint.ascent();
            float offset = (height / 2) - paint.descent();

            canvas.drawText("Play", playButton.rect.left, playButton.rect.bottom - offset, paint);

            canvas.drawText("Settings", exitButton.rect.left, exitButton.rect.bottom - offset, paint);

            holder.unlockCanvasAndPost(canvas);
        }
    }
}
