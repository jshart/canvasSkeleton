package games.k452b.canvasskeleton;



import android.graphics.Canvas;
import android.view.SurfaceHolder;

public class GameThread extends Thread {

    private boolean isRunning=false;
    private DrawGameWorldSurface gameSurface;
    private SurfaceHolder surfaceHolder;

    private final static int MAX_FPS = 120; //desired fps
    private final static int FRAME_PERIOD = 1000 / MAX_FPS; // the frame period

    public GameThread(DrawGameWorldSurface gs, SurfaceHolder sh)  {
        gameSurface= gs;
        surfaceHolder= sh;
        setName("GameThread");
    }

    @Override
    public void run()  {
        long startTime;
        int sleepTime;
        int rollingAverage=0;
        int averageCount=0;

        while(isRunning)
        {
            Canvas canvas= null;

            startTime = System.nanoTime();

            // Only attempt to update the canvas if its valid.
            // this stops a few alerts we otherwise get as we're
            // initiating and setting everything up.
            if (surfaceHolder.getSurface().isValid())
            {
                //try {
                    // Get Canvas from Holder and lock it.
                    canvas = surfaceHolder.lockCanvas();

                    if (canvas!=null) {

                        // Synchronized
                        synchronized (canvas) {
                            gameSurface.update();
                            gameSurface.draw(canvas);
                        }
                    }
                //} catch (Exception e) {
                    // Do nothing.
                    //System.out.println("*** failed to do canvas update");
                    //e.printStackTrace();

                //} finally {
                    if (canvas != null) {
                        // Unlock Canvas.
                        surfaceHolder.unlockCanvasAndPost(canvas);
                    }
                //}
            }

            long now = System.nanoTime() ;

            // Interval to redraw game
            // (Change nanoseconds to milliseconds)
            long deltaTime = (now - startTime)/1000000;

            sleepTime = (int) (FRAME_PERIOD - deltaTime);



            rollingAverage+=deltaTime;
            averageCount++;
            if (averageCount==100)
            {
                //System.out.println("*** Average wait time over "+averageCount+" cycles is "+rollingAverage/averageCount);
                rollingAverage=0;
                averageCount=0;
            }

            if (sleepTime >0)
            {
                try {
                    // Sleep.
                    this.sleep(deltaTime);
                } catch (InterruptedException e) {

                }
            }

            // if we've behind on refresh, then skip
            // the draw and just update the item
            // state.
            while (sleepTime < 0) {
                    gameSurface.update();
                sleepTime += FRAME_PERIOD;
            }
        }
    }

    public void setRunning(boolean running) {
        this.isRunning = running;
    }

}