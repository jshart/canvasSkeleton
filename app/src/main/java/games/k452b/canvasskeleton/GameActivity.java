package games.k452b.canvasskeleton;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

// Good source of basic game loop:
// https://google-developer-training.gitbooks.io/android-developer-advanced-course-practicals/unit-5-advanced-graphics-and-views/lesson-11-canvas/11-2-p-create-a-surfaceview/11-2-p-create-a-surfaceview.html


public class GameActivity extends AppCompatActivity {


    DrawGameWorldSurface mDrawGameViewSurface;

    Bitmap mBackdropCanvasBitmap;
    Canvas mBackdropCanvas;

    int mScreenSizeX;
    int mScreenSizeY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);


        //requestWindowFeature(Window.FEATURE_NO_TITLE);


        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                        | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);


        Point outPoint = new Point();
        getWindowManager().getDefaultDisplay().getRealSize(outPoint);

        mScreenSizeX = outPoint.x;
        mScreenSizeY = outPoint.y;
        System.out.println("screen dimensions:" + mScreenSizeX + "," + mScreenSizeY);


        System.out.println("*** backgrop canvas:"+mScreenSizeX+","+mScreenSizeY);
        mBackdropCanvasBitmap = Bitmap.createBitmap(mScreenSizeX, mScreenSizeY, Bitmap.Config.ARGB_8888);
        System.out.println("*** Created canvas");
        mBackdropCanvas = new Canvas(mBackdropCanvasBitmap);


    }

    /**
     * Pauses game when activity is paused.
     */
    @Override
    protected void onPause() {
        super.onPause();

        System.out.println("*** Activity onPause called");

        if (mDrawGameViewSurface !=null)
        {
            System.out.println("*** Calling mGameView.pause");
            mDrawGameViewSurface.pause();
        }
        else
        {
            System.out.println("*** mGameView is NULL");
        }
    }

    /**
     * Resumes game when activity is resumed.
     */
    @Override
    protected void onResume() {
        super.onResume();

        System.out.println("*** Activity onResume called");
        // Create the surface view thread which will manage the display
        mDrawGameViewSurface = new DrawGameWorldSurface(this, mBackdropCanvasBitmap);

        // Add the surfaceview to this activity
        setContentView(mDrawGameViewSurface);

        if (mDrawGameViewSurface !=null)
        {
            System.out.println("*** Calling mGameView.resume");
            mDrawGameViewSurface.resume();
        }
        else
        {
            System.out.println("*** mGameView is NULL");
        }
    }

    protected void onRestart()
    {
        super.onRestart();
        System.out.println("*** onRestart called");
    }


    protected void onStart()
    {
        super.onStart();
        System.out.println("*** onStart called");
    }
}
