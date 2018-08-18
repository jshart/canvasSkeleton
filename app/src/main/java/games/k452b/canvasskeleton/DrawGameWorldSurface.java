package games.k452b.canvasskeleton;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import static android.graphics.Color.RED;

/**
 * Created by jhart on 09/07/2018.
 * template: https://o7planning.org/en/10521/android-2d-game-tutorial-for-beginners
 */

public class DrawGameWorldSurface extends SurfaceView implements SurfaceHolder.Callback {

    int mStartTouchX;
    int mStartTouchY;
    int mEndTouchX;
    int mEndTouchY;

    private Context mContext;
    private SurfaceHolder mSurfaceHolder;

    private GameThread gameThread;

    Bitmap mBackdropCanvasBitmap;

    // TODO - move bDrop into gw object?
    public DrawGameWorldSurface(Context context, Bitmap bDrop) {
        super(context);

        mContext = context;
        mSurfaceHolder = getHolder();

        mBackdropCanvasBitmap=bDrop;


        // Make Game Surface focusable so it can handle events. .
        this.setFocusable(true);

        // Sét callback.
        this.getHolder().addCallback(this);
    }


    @Override
    public void draw(Canvas canvas)  {
        super.draw(canvas);

        Paint p = new Paint();
        canvas.drawBitmap(mBackdropCanvasBitmap, 0, 0, p);

        Paint demo = new Paint();
        demo.setColor(RED);
        demo.setStyle(Paint.Style.FILL);


        canvas.drawRect(10,10,100,100,demo);
    }


    public void update()  {
    }


    // Implements method of SurfaceHolder.Callback
    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        System.out.println("*-*-* Surface created");

        if (gameThread==null)
        {
            System.out.println("*-*-* Creating new GameThread");
            gameThread = new GameThread(this,holder);
            gameThread.setRunning(true);
            gameThread.start();
        }
        else
        {
            System.out.println("*-*-* Skipping GameThread creation, as we already have one");
        }
    }

    // Implements method of SurfaceHolder.Callback
    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        System.out.println("*** Surface changed");

    }

    /**
     * Start or resume the game.
     */
    public void resume() {
        System.out.println("*** Resume called, checking thread");
        if (gameThread!=null)
        {
            if (gameThread.isAlive())
            {
                System.out.println("Thread running no need to start a new therad");
                return;
            }
        }

        gameThread = new GameThread(this, mSurfaceHolder);

        System.out.println("*** mSurfaceHolder is:" + mSurfaceHolder);
        System.out.println("*** setting running");
        gameThread.setRunning(true);

        System.out.println("*** started");
        gameThread.start();
    }

    /**
     * Pause the game loop
     */
    public void pause() {
        gameThread.setRunning(false);
        boolean retry = true;

        System.out.println("*** pause called, closing GameThread");


        while (retry)
        {
            try {
                System.out.println("*** pause called, thread join started");

                gameThread.join();
                System.out.println("*** pause called, thread join ended");
                retry = false;

            } catch (InterruptedException e) {
                // try again shutting down the thread
                System.out.println("*** Failed to close thread, retrying");
            }
        }
    }

    // Implements method of SurfaceHolder.Callback
    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        System.out.println("*** Surface destroyed");

   /*     boolean retry= true;
        while(retry)
        {
            try {
                this.gameThread.setRunning(false);

                // Parent thread must wait until the end of GameThread.
                this.gameThread.join();
            }catch(InterruptedException e)  {
                e.printStackTrace();
            }
            retry= true;
        }*/
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int ex,ey;

        switch (event.getAction())
        {
            case MotionEvent.ACTION_DOWN:

                mStartTouchX = (int) event.getX();
                mStartTouchY = (int) event.getY();


                return true;

            case MotionEvent.ACTION_MOVE:

                ex=(int) event.getX();
                ey=(int) event.getY();


                return true;

            case MotionEvent.ACTION_UP:
                mEndTouchX = (int) event.getX();
                mEndTouchY = (int) event.getY();

                int deltaX = mEndTouchX - mStartTouchX;
                int deltaY = mEndTouchY - mStartTouchY;



                System.out.println("*** Swipe start:"+mStartTouchX+","+mStartTouchY+" ended:"+mEndTouchX+","+mEndTouchY+" Delta:"+deltaX+","+deltaY);


                return true;
        }
        return false;
    }
}