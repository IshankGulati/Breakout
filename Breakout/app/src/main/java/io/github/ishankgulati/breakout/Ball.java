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
    private float _velocity, initialVelocity;
    private float _angle;
    private int screenX, screenY;
    private long elapsedTimeSinceStart;
    private final long minCollisionInterval;
    private long collisionTime;
    Ball(){
        setSize(10, 10);
        initialVelocity = 300;
        _velocity = initialVelocity;

        Random generator = new Random();
        _angle = generator.nextInt(360);
        if(_angle >= 70.0f && _angle <= 110.0f){
            _angle += 40;
        }
        if(_angle >= 250.0f && _angle <= 290.0f){
            _angle -= 40;
        }
        screenX = BreakoutGame.BreakoutView.screenX;
        screenY = BreakoutGame.BreakoutView.screenY;
        elapsedTimeSinceStart = 0;
        minCollisionInterval = 200;
        collisionTime = 0;
    }

    @Override
    public void update(long fps, long elapsedTime){
        elapsedTimeSinceStart += elapsedTime;
        if(elapsedTimeSinceStart < 3000)
            return;

        float xVelocity = _velocity * linearVelocityX(_angle);
        float yVelocity = _velocity * linearVelocityY(_angle);


        RectF rect = getBoundingRect();

        float left = rect.left + (xVelocity / fps);
        float top = rect.top + (yVelocity / fps);
        float right = rect.right + (xVelocity / fps);
        float bottom = rect.bottom + (yVelocity / fps);

        // collision with right and left wall
        if(left <= 0 || right >= screenX){
            _angle = 360.0f - _angle;
            xVelocity = -xVelocity;

            if(_angle >= 70.0f && _angle <= 110.0f){
                _angle += 40;
            }
            if(_angle >= 250.0f && _angle <= 290.0f){
                _angle -= 40;
            }

            BreakoutGame.BreakoutView.getSoundManager().playSound("beep3");
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
        if(top <= 0){
            yVelocity = -yVelocity;
            _angle = 180.0f - _angle;

            if(_angle < 0.0f){
                _angle += 360.0f;
            }
            if(_angle > 360.0f){
                _angle = _angle - 360.0f;
            }

            BreakoutGame.BreakoutView.getSoundManager().playSound("beep2");
            //clearObstacleY(12);
        }

        // collision with bottom
        if(bottom >= screenY){
            BreakoutGame.BreakoutView.getScoreBoard().decrementLife();
            boolean result = BreakoutGame.BreakoutView.checkVictory();
            if(result){
                return;
            }
            BreakoutGame.BreakoutView.getSoundManager().playSound("loseLife");
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

                    _angle = 180.0f - _angle;

                    if(_angle < 0.0f){
                        _angle += 360.0f;
                    }
                    if(_angle > 360.0f){
                        _angle = _angle - 360.0f;
                    }

                    BreakoutGame.BreakoutView.getScoreBoard().incrementScore();
                    BreakoutGame.BreakoutView.getSoundManager().playSound("explode");
                }
            }
        }

        // collision with paddle
        Paddle paddle = (Paddle) BreakoutGame.BreakoutView.getObjectManager().get("Paddle");

        collisionTime += elapsedTime;
        if(paddle != null && collisionTime > minCollisionInterval){
            RectF paddleRect = paddle.getBoundingRect();
            if(RectF.intersects(paddleRect, getBoundingRect())){
                yVelocity = -yVelocity;

                if(paddleRect.top + paddle.getHeight()/2 < getBoundingRect().bottom){
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
                    _angle = _angle - 360.0f;
                }
                clearObstacleY(paddle.getBoundingRect().top - 2);
                /*
                // If ball is inside paddle
                if(getBoundingRect().top + getHeight() > paddle.getBoundingRect().top){
                    setPosition(getPosition().x, paddle.getBoundingRect().top - getWidth()/2 - 1);
                }*/

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
                        _angle = _angle - 360.0f;
                    }
                }
                BreakoutGame.BreakoutView.getSoundManager().playSound("beep1");
                _velocity += 10.0f;
            }
            collisionTime = 0;
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
        _velocity = initialVelocity;
        elapsedTimeSinceStart = 0;

        Random generator = new Random();
        _angle = generator.nextInt(360);
        if(_angle >= 70.0f && _angle <= 110.0f){
            _angle += 40;
        }
        if(_angle >= 250.0f && _angle <= 290.0f){
            _angle -= 40;
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
