package io.github.ishankgulati.breakout;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.SurfaceHolder;

/**
 * Created by Hackbook on 12/26/2015.
 */
public class SplashScreen {
    private int screenX, screenY;

    SplashScreen(){
        screenX = BreakoutGame.screenX;
        screenY = BreakoutGame.screenY;
    }

    public void show(SurfaceHolder holder, Canvas canvas, Paint paint){
        if(holder.getSurface().isValid()){
            canvas = holder.lockCanvas();
            canvas.drawColor(Color.argb(255, 26, 128, 182));

            paint.setColor(Color.argb(255, 249, 129, 0));
            paint.setTextSize(150);
            int xPos = (canvas.getWidth() / 2);
            int yPos = (int) ((canvas.getHeight() / 2) - ((paint.descent() + paint.ascent()) / 2));
            canvas.drawText("Breakout", xPos, yPos, paint);

            paint.setColor(Color.argb(255, 255, 255, 255));
            paint.setTextSize(50);
            yPos = (int) ((canvas.getHeight() * 3 / 4) - ((paint.descent() + paint.ascent()) / 2));
            canvas.drawText("Press any key to continue", xPos, yPos, paint);
            holder.unlockCanvasAndPost(canvas);
        }
    }
}

