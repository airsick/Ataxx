package airsick.ataxx;

import android.graphics.Canvas;
import android.util.Log;
import android.view.SurfaceHolder;

/**
 * Game Loop Class
 */

public class MainThread extends Thread {

    private static final String TAG = MainThread.class.getSimpleName();

    // flag to hold gamestate
    private boolean running;
    public void setRunning(boolean running) {
        this.running = running;
    }
    private SurfaceHolder surfaceHolder;
    private MainGamePanel gamePanel;

    public MainThread(SurfaceHolder surfaceHolder, MainGamePanel gamePanel) {
        super();
        this.surfaceHolder = surfaceHolder;
        this.gamePanel = gamePanel;
    }

    @Override
    public void run() {
        Canvas canvas;
        Log.d(TAG, "Starting game loop");
        while (running) {
            canvas = null;
            // try locking the canvas for exclusive pixel editing on the surface
            try {
                canvas = this.surfaceHolder.lockCanvas();
                synchronized (surfaceHolder) {
                    // update game state
                    // draws the canvas on the panel
                    if(canvas != null)
                        this.gamePanel.onDraw(canvas);
                }
            } finally {
                // in case of an exception the surface is not left in an inconsistent state
                if(canvas != null) {
                    surfaceHolder.unlockCanvasAndPost(canvas);
                }
            }
        }
    }
}
