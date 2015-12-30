package io.github.ishankgulati.breakout;

import android.app.Activity;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.content.Context;
import android.view.WindowManager;

import java.util.Iterator;

public class BreakoutGame extends Activity{

    // view of the game
    BreakoutView breakoutView;


    @Override
    protected void onCreate(Bundle savedInstanceState){
        // so that Dalvik VM runs our onCreate in addition to parent class
        super.onCreate(savedInstanceState);

        breakoutView = new BreakoutView(this);
        setContentView(breakoutView);
    }

    private enum gameState{Playing, Paused, ShowingSplash, ShowingMenu, Completed, Exiting}



    public static class BreakoutView extends SurfaceView implements Runnable{

        Thread gameThread = null;

        private static SurfaceHolder ourHolder;
        private static Canvas canvas;
        private static Paint paint;

        private long fps;
        private long timeElapsed;
        private static gameState _gameState;
        public static int screenX, screenY;

        private static Paddle paddle;
        private static Ball ball;
        private static GameObjectManager _gameObjectManager;
        private static SoundManager _soundManager;
        private static ScoreBoard _scoreBoard;
        private static MainMenu _mainMenu;
        private static InGameMenu _inGameMenu;

        Brick[] bricks;
        private static int numBricks;

        // rect for menu buttons
        Rect menuButton;

        private BreakoutView(Context context){
            super(context);

            ourHolder = getHolder();
            paint = new Paint();
            WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
            Display display = wm.getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);
            screenX = size.x;
            screenY = size.y;

            _gameState = gameState.ShowingSplash;

            _gameObjectManager = new GameObjectManager();
            _soundManager = new SoundManager(context);
            _mainMenu = new MainMenu();
            _inGameMenu = new InGameMenu();

            paddle = new Paddle();
            paddle.setInitialPosition(screenX / 2 - 65, screenY - 30);
            _gameObjectManager.add("Paddle", paddle);

            ball = new Ball();
            ball.setInitialPosition(screenX / 2, screenY / 2 + screenY / 15);
            _gameObjectManager.add("Ball", ball);

            bricks = new Brick[200];
            numBricks = 0;

            createBricks();
            _scoreBoard = new ScoreBoard(numBricks);

            menuButton = new Rect();
            fps = 0;
            timeElapsed = 0;
        }


        @Override
        public void run(){
            _soundManager.playMusic();
            while(!isExiting()){
                GameLoop();
            }
        }

        private boolean isExiting(){
            if(_gameState == gameState.Exiting)
                return true;
            else
                return false;
        }

        private void GameLoop(){
            switch(_gameState){
                case ShowingSplash:
                    showSplashScreen();
                    break;
                case ShowingMenu:
                    showMainMenu();
                    break;
                case Paused:
                    showInGameMenu();
                    break;
                case Completed:
                    showEndGame();
                    break;
                case Playing:

                    long startFrameTime = System.currentTimeMillis();

                    _gameObjectManager.updateAll(fps, timeElapsed);
                    _gameObjectManager.drawAll(ourHolder, canvas, paint);

                    timeElapsed = System.currentTimeMillis() - startFrameTime;
                    if(timeElapsed > 1){
                        fps = 1000 / timeElapsed;
                    }
                    break;
                default:
                    break;
            }
        }

        private void pause(){
            _gameState = gameState.Exiting;
            try{
                gameThread.join();
            }
            catch (InterruptedException e){
                Log.e("Error:", "joining thread");
            }
        }

        private void resume(){
            if(_gameState == gameState.Exiting)
                _gameState = gameState.Playing;
            else
                _gameState = gameState.ShowingSplash;

            gameThread = new Thread(this);
            gameThread.start();
        }


        @Override
        public boolean onTouchEvent(MotionEvent motionEvent){
            switch(motionEvent.getAction() & MotionEvent.ACTION_MASK){

                // player has touched screen
                case MotionEvent.ACTION_DOWN:
                    if(_gameState == gameState.Playing) {
                        if (motionEvent.getX() > screenX / 2) {
                            paddle.setMovementState(Paddle.MovementState.Right);
                        } else {
                            paddle.setMovementState(Paddle.MovementState.Left);
                        }
                    }
                    else if(_gameState == gameState.ShowingSplash){
                        _gameState = gameState.ShowingMenu;
                    }
                    else if(_gameState == gameState.ShowingMenu){
                        float xPos = motionEvent.getX();
                        float yPos = motionEvent.getY();

                        Iterator itr = _mainMenu.getMenuItems().iterator();
                        while(itr.hasNext()){
                            MainMenu.MenuItem button = (MainMenu.MenuItem)itr.next();
                            menuButton = button.rect;
                            if(xPos > menuButton.left && xPos < menuButton.right &&
                                    yPos > menuButton.top && yPos < menuButton.bottom){
                                switch (button.action){
                                    //remove exit from menu
                                    /*
                                    case Exit:
                                        _gameState = gameState.Exiting;
                                        break; */
                                    case Play:
                                        _gameState = gameState.Playing;
                                        _soundManager.stopAllSounds();
                                        break;
                                }
                            }
                        }
                    }
                    else if(_gameState == gameState.Paused){
                        float xPos = motionEvent.getX();
                        float yPos = motionEvent.getY();

                        Iterator itr = _inGameMenu.getMenuItems().iterator();
                        while(itr.hasNext()){
                            InGameMenu.MenuItem button = (InGameMenu.MenuItem)itr.next();
                            menuButton = button.rect;
                            if(xPos > menuButton.left && xPos < menuButton.right &&
                                    yPos > menuButton.top && yPos < menuButton.bottom){
                                switch (button.action){
                                    case Resume:
                                        _gameState = gameState.Playing;
                                        _soundManager.stopAllSounds();
                                        break;
                                    case Restart:
                                        resetGame();
                                        _gameState = gameState.Playing;
                                        _soundManager.stopAllSounds();
                                        break;
                                }
                            }
                        }
                    }
                    else if(_gameState == gameState.Completed){
                        resetGame();
                        _soundManager.stopAllSounds();
                        _gameState = gameState.ShowingMenu;
                        _soundManager.playMusic();
                    }
                    break;

                // player removed finger from screen
                case MotionEvent.ACTION_UP:
                    if(_gameState == gameState.Playing) {
                        paddle.setMovementState(Paddle.MovementState.Stopped);
                    }
                    break;
            }
            return true;
        }

        private void createBricks(){
            int brickWidth = screenX/8;
            int brickHeight = screenY/10;
            float x, y;
            int padding = 1;
            numBricks = 0;
            int offset = 30;
            for(int row = 0; row < 3; row++){
                for(int column = 0; column < 8; column++){
                    bricks[numBricks] = new Brick();
                    x = column * brickWidth + padding;
                    y = offset + (row * brickHeight) + padding;
                    bricks[numBricks].setSize(brickWidth - padding, brickHeight - padding);
                    bricks[numBricks].setInitialPosition(x, y);
                    _gameObjectManager.add("Brick" + Integer.toString(numBricks),
                            bricks[numBricks]);
                    numBricks++;
                }
            }
        }

        public static void drawScoreBoard(Canvas c, Paint p){
            _scoreBoard.draw(c, p);
        }

        public static void showSplashScreen(){
            SplashScreen splashScreen = new SplashScreen();
            splashScreen.show(ourHolder, canvas, paint);
        }

        public static void showMainMenu(){
            _mainMenu.show(ourHolder, canvas, paint);
        }

        public static void showInGameMenu(){
            _soundManager.playMusic();
            _inGameMenu.show(ourHolder, canvas, paint);
        }

        public static void showEndGame(){
            if(ourHolder.getSurface().isValid()) {
                canvas = ourHolder.lockCanvas();
                canvas.drawColor(Color.argb(255, 26, 128, 182));
                drawScoreBoard(canvas, paint);
                ourHolder.unlockCanvasAndPost(canvas);
            }
        }

        public static GameObjectManager getObjectManager(){
            return _gameObjectManager;
        }

        public static ScoreBoard getScoreBoard(){
            return _scoreBoard;
        }

        public static SoundManager getSoundManager(){
            return _soundManager;
        }

        public static boolean checkVictory(){
            if(_scoreBoard.getGameResult() != ScoreBoard.GameResult.Playing) {
                _gameState = gameState.Completed;
                return true;
            }
            return false;
        }

        public static int getNumberOfBricks(){
            return numBricks;
        }

        public void resetGame(){
            _scoreBoard.resetScore();
            _gameObjectManager.resetAll();
        }

        public void stop(){
            _soundManager.player.release();
            _soundManager.soundPool.release();
        }

    }

    @Override
    protected void onResume(){
        super.onResume();
        breakoutView.resume();
    }

    @Override
    protected void onPause(){
        super.onPause();
        breakoutView.pause();
    }

    @Override
    protected void onStop(){
        super.onStop();
        breakoutView.stop();
    }
}

