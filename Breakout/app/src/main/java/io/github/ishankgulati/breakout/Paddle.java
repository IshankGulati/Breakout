package io.github.ishankgulati.breakout;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;

/**
 * Created by Hackbook on 12/26/2015.
 */
public class Paddle extends VisibleGameObject {

    public enum MovementState{Right, Left, Stopped}

    private float velocity, initialVelocity;
    private MovementState movementState;
    private int screenX, screenY;

    Paddle(){
        initialVelocity = 380.0f;
        velocity = initialVelocity;
        setSize(130, 20);
        movementState = MovementState.Stopped;
        screenX = BreakoutGame.BreakoutView.screenX;
        screenY = BreakoutGame.BreakoutView.screenY;
    }

    @Override
    public void update(long fps, long elapsedTime){
        PointF loc = getPosition();

        // collision with left and right walls
        if(loc.x < 0.0f){
            if(movementState == MovementState.Left){
                velocity = 0;
            }
            else{
                velocity = initialVelocity;
            }
        }
         if(loc. x + getWidth() > screenX){
             if(movementState == MovementState.Right){
                 velocity = 0;
             }
             else{
                 velocity = initialVelocity;
             }
         }

        if(movementState == MovementState.Right){
            loc.x = loc.x + velocity / fps;
        }

        if (movementState == MovementState.Left){
            loc.x = loc.x - velocity / fps;
        }

        setPosition(loc.x, loc.y);
    }

    public void setMovementState(MovementState state){
        movementState = state;
    }

    public MovementState getMovementState(){
        return movementState;
    }

    public void draw(Canvas canvas, Paint paint){
        paint.setColor(Color.argb(255, 255, 255, 255));
        super.draw(canvas, paint);
    }

    public float getVelocity(){
        return velocity;
    }

    public void reset(){
        super.reset();
        velocity = initialVelocity;
        movementState = MovementState.Stopped;
    }
}
