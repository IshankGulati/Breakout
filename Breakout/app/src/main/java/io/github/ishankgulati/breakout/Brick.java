package io.github.ishankgulati.breakout;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

/**
 * Created by Hackbook on 12/26/2015.
 */
public class Brick extends VisibleGameObject {
    private boolean isVisible;

    public Brick(){
        isVisible = true;
        //int padding = 1;
    }

    public void setInvisible(){
        isVisible = false;
    }

    public boolean getVisibility(){
        return isVisible;
    }

    public void draw(Canvas canvas, Paint paint){
        paint.setColor(Color.argb(255, 249, 129, 0));
        if(this.isVisible) {
            super.draw(canvas, paint);
        }
    }

    @Override
    public void reset(){
        isVisible = true;
    }
}
