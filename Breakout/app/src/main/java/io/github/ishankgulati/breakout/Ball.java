package io.github.ishankgulati.breakout;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;

import java.util.Random;

/**
 * Created by Hackbook on 12/26/2015.
 */
public class Ball extends VisibleGameObject{
    private float _velocity;
    private float _angle;
    private int screenX, screenY;

    Ball(){
        setSize(10, 10);
        _velocity = 300;

        Random generator = new Random();
        _angle = generator.nextInt(360);
        if(_angle >= 70.0f && _angle <= 110.0f){
            _angle += 30;
        }
        if(_angle >= 250.0f && _angle <= 290.0f){
            _angle -= 30;
        }
        screenX = BreakoutGame.screenX;
        screenY = BreakoutGame.screenY;
    }

    @Override
    public void update(long fps){
        float xVelocity = _velocity * linearVelocityX(_angle);
        float yVelocity = _velocity * linearVelocityY(_angle);


        RectF rect = getBoundingRect();

        float left = rect.left + (xVelocity / fps);
        float top = rect.top + (yVelocity / fps);
        float right = rect.left + getWidth();
        float bottom = rect.top + getHeight();

        // collision with right and left wall
        if(left < 0 || right > screenX){
            _angle = 360.0f - _angle;
            xVelocity = -xVelocity;

            if(_angle >= 70.0f && _angle <= 110.0f){
                _angle += 30;
            }
            if(_angle >= 250.0f && _angle <= 290.0f){
                _angle -= 30;
            }
            /*
            if(left < 0){
                clearObstacleX(2);
            }
            if(right > screenX){
                clearObstacleX(12);
            }
            */
        }

        // collision with top
        if(top < 0){
            yVelocity = -yVelocity;
            _angle = 360.0f - _angle;
            //clearObstacleY(12);
        }

        // collision with bottom
        if(bottom > screenY){
            BreakoutGame.BreakoutView.getScoreBoard().decrementLife();
            boolean result = BreakoutGame.BreakoutView.checkVictory();
            if(result){
                return;
            }
            reset();
        }

        // collision with bricks
        int numBricks = BreakoutGame.BreakoutView.getNumberOfBricks();
        for(int i=0; i<numBricks; i++){
            String name = "Brick"+ Integer.toString(i);
            Brick brick = (Brick) BreakoutGame.BreakoutView.getObjectManager().get(name);
            if(brick.getVisibility()){
                if(RectF.intersects(brick.getBoundingRect(), getBoundingRect())){
                    brick.setInvisible();
                    yVelocity = -yVelocity;
                    BreakoutGame.BreakoutView.getScoreBoard().incrementScore();
                }
            }
        }

        // collision with paddle
        Paddle paddle = (Paddle) BreakoutGame.BreakoutView.getObjectManager().get("Paddle");

        if(paddle != null){
            RectF paddleRect = paddle.getBoundingRect();
            if(RectF.intersects(paddleRect, getBoundingRect())){
                yVelocity = -yVelocity;
                if(paddleRect.top < getBoundingRect().top + getHeight()/2){
                    // collision at edge
                    _angle = 360.0f - _angle;
                }
                else {
                    // collision at top
                    _angle = 180.0f - _angle;
                }

                if(_angle < 0.0f){
                    _angle += 360.0f;
                }
                if(_angle > 360.0f){
                    _angle = 360.0f - _angle;
                }

                // If ball is inside paddle
                if(getBoundingRect().top + getHeight() > paddle.getBoundingRect().top){
                    setPosition(getPosition().x, paddle.getBoundingRect().top - getWidth()/2 - 1);
                }

                // adding spin to ball
                if(paddle.getMovementState() == Paddle.MovementState.Left){
                    _angle -= 30.0f;

                    if(_angle < 0.0f){
                        _angle += 360.0f;
                    }
                }

                else if(paddle.getMovementState() == Paddle.MovementState.Right){
                    _angle += 30.0f;

                    if(_angle > 360.0f){
                        _angle -= 360.0f;
                    }
                }
                _velocity += 2.0f;
            }
        }

        rect.left = rect.left + (xVelocity / fps);
        rect.top = rect.top + (yVelocity / fps);
        rect.right = rect.left + getWidth();
        rect.bottom = rect.top + getHeight();


        setPosition(rect.left, rect.top);
    }

    private float linearVelocityX(float angle){
        angle -= 90;
        if(angle < 0){
            angle += 360;
        }
        return (float)Math.cos(angle * 3.14159 / 180.0);
    }

    private float linearVelocityY(float angle){
        angle -= 90;
        if(angle < 0){
            angle += 360;
        }
        return (float)Math.sin(angle * 3.14159 / 180.0);
    }

    public void reset(){
        super.reset();
        _velocity = 300;

        Random generator = new Random();
        _angle = generator.nextInt(360);
        if(_angle >= 70.0f && _angle <= 110.0f){
            _angle += 30;
        }
        if(_angle >= 250.0f && _angle <= 290.0f){
            _angle -= 30;
        }
    }

    public void draw(Canvas canvas, Paint paint){
        paint.setColor(Color.argb(255, 255, 255, 255));
        super.draw(canvas, paint);
    }

    public void clearObstacleY(float y){
        RectF rect = getBoundingRect();
        rect.bottom = y;
        rect.top = y - getHeight();
        setBoundingRect(rect);
    }

    public void clearObstacleX(float x){
        RectF rect = getBoundingRect();
        rect.left = x;
        rect.right = x + getWidth();
        setBoundingRect(rect);
    }
}
