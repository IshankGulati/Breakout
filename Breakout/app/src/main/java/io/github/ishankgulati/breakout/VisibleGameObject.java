package io.github.ishankgulati.breakout;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RectF;

/**
 * Created by Hackbook on 12/26/2015.
 */
public class VisibleGameObject {
    private RectF rect;

    private float width;
    private float height;
    private float x;
    private float y;

    // starting coordinates of object
    private float initialX;
    private float initialY;

    public void setPosition(float x, float y){
        this.x = x;
        this.y = y;

        RectF temp = new RectF(x, y, x+width, y+height);
        setBoundingRect(temp);
    }

    public void setInitialPosition(float x, float y){
        this.x = x; initialX = x;
        this.y = y; initialY = y;
    }

    public void setSize(float width, float height){
        this.width = width;
        this.height = height;
        rect = new RectF(x, y, x + width, y + height);
    }

    public void setBoundingRect(float updatedX){
        rect.left = updatedX;
        rect.right = updatedX + width;
    }

    public void setBoundingRect(RectF r){
        rect.left = r.left;
        rect.right = r.right;
        rect.top = r.top;
        rect.bottom = r.bottom;
    }

    public PointF getPosition(){
        PointF loc = new PointF();
        loc.x = x;
        loc.y = y;
        return loc;
    }

    public void draw(Canvas canvas, Paint paint){
        canvas.drawRect(rect, paint);
    }

    public void reset(){
        setPosition(initialX, initialY);
        setSize(width, height);
    }

    public float getHeight(){
        return height;
    }

    public float getWidth(){
        return width;
    }

    public RectF getBoundingRect(){
        return rect;
    }

    public void update(long fps){}

}
