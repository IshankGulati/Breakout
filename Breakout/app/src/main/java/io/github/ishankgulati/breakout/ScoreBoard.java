package io.github.ishankgulati.breakout;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

/**
 * Created by Hackbook on 12/26/2015.
 */
public class ScoreBoard {
    private int score;
    private int lives;
    private int maxScore;
    private int screenY, screenX;
    enum GameResult{Win, Lose, Playing}
    private GameResult result;

    ScoreBoard(int numBricks){
        score = 0;
        lives = 3;
        maxScore = numBricks * 10;

        screenX = BreakoutGame.screenX;
        screenY = BreakoutGame.screenY;
        result = GameResult.Playing;
    }

    public void draw(Canvas canvas, Paint paint){
        paint.setColor(Color.argb(255, 255, 255, 255));
        paint.setTextSize(40);
        canvas.drawText("Score: " + score + "  Lives: " + lives, 10, 50, paint);

        if(result == GameResult.Win){
            paint.setTextSize(90);
            int yPos = (int)((canvas.getHeight() * 3 / 4) - ((paint.descent() + paint.ascent())/2));
            canvas.drawText("YOU HAVE WON!", screenX / 2, screenY / 2, paint);
            canvas.drawText("Press any key to continue", screenX / 2, yPos, paint);
        }

        if(result == GameResult.Lose){
            paint.setTextSize(90);
            int yPos = (int)((canvas.getHeight() * 3 / 4) - ((paint.descent() + paint.ascent())/2));
            canvas.drawText("YOU HAVE LOST!", screenX / 2,screenY / 2, paint);
            canvas.drawText("Press any key to continue", screenX / 2, yPos, paint);
        }
    }

    public GameResult getGameResult(){
        return result;
    }

    public void incrementScore(){
        score += 10;
        if(score == maxScore){
            result = GameResult.Win;
        }
    }

    public void decrementLife(){
        lives -= 1;
        if(lives <= 0){
            result = GameResult.Lose;
        }
    }

    public void resetScore(){
        score = 0;
    }
}
