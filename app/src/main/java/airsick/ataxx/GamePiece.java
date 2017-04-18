package airsick.ataxx;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.icu.util.Calendar;
import android.provider.Settings;

/**
 * Created by coler_000 on 4/13/2017.
 */

public class GamePiece {
    public static int COLOR_WHITE = 0;
    public static int COLOR_BLACK = 1;

    private static Bitmap blackImage;
    private static Bitmap whiteImage;
    private static Bitmap[] blackToWhiteImage;
    private static Bitmap[] whiteToBlackImage;
    private Bitmap[] animationImage;
    private int animationFrame;
    private long lastAnimationTime;
    public boolean isAnimating = false;

    private Bitmap currentImage;
    private int currentColor;
    private Rect bounds;
    public boolean isPlaced;
    private boolean isSelected;

    public GamePiece (Context context) {
        // This runs on the first piece that's initialized, setting all the images
        if(blackImage == null) {
            blackImage = BitmapFactory.decodeResource(context.getResources(), R.drawable.black1);
            whiteImage = BitmapFactory.decodeResource(context.getResources(), R.drawable.white1);

            blackToWhiteImage = new Bitmap[10];
            blackToWhiteImage[0] = BitmapFactory.decodeResource(context.getResources(), R.drawable.black2);
            blackToWhiteImage[1] = BitmapFactory.decodeResource(context.getResources(), R.drawable.black3);
            blackToWhiteImage[2] = BitmapFactory.decodeResource(context.getResources(), R.drawable.black4);
            blackToWhiteImage[3] = BitmapFactory.decodeResource(context.getResources(), R.drawable.black5);
            blackToWhiteImage[4] = BitmapFactory.decodeResource(context.getResources(), R.drawable.black6);
            blackToWhiteImage[5] = BitmapFactory.decodeResource(context.getResources(), R.drawable.black7);
            blackToWhiteImage[6] = BitmapFactory.decodeResource(context.getResources(), R.drawable.black8);
            blackToWhiteImage[7] = BitmapFactory.decodeResource(context.getResources(), R.drawable.black9);
            blackToWhiteImage[8] = BitmapFactory.decodeResource(context.getResources(), R.drawable.black10);
            blackToWhiteImage[9] = BitmapFactory.decodeResource(context.getResources(), R.drawable.black11);

            whiteToBlackImage = new Bitmap[10];
            whiteToBlackImage[0] = BitmapFactory.decodeResource(context.getResources(), R.drawable.white2);
            whiteToBlackImage[1] = BitmapFactory.decodeResource(context.getResources(), R.drawable.white3);
            whiteToBlackImage[2] = BitmapFactory.decodeResource(context.getResources(), R.drawable.white4);
            whiteToBlackImage[3] = BitmapFactory.decodeResource(context.getResources(), R.drawable.white5);
            whiteToBlackImage[4] = BitmapFactory.decodeResource(context.getResources(), R.drawable.white6);
            whiteToBlackImage[5] = BitmapFactory.decodeResource(context.getResources(), R.drawable.white7);
            whiteToBlackImage[6] = BitmapFactory.decodeResource(context.getResources(), R.drawable.white8);
            whiteToBlackImage[7] = BitmapFactory.decodeResource(context.getResources(), R.drawable.white9);
            whiteToBlackImage[8] = BitmapFactory.decodeResource(context.getResources(), R.drawable.white10);
            whiteToBlackImage[9] = BitmapFactory.decodeResource(context.getResources(), R.drawable.white11);

        }
        currentColor = COLOR_BLACK;
        currentImage = blackImage;
        isPlaced = false;
        bounds = new Rect(0, 0, currentImage.getWidth(), currentImage.getHeight());
    }

    public GamePiece (Context context, int currentColor, Rect bounds, boolean isPlaced) {
        this(context);
        if(currentColor==COLOR_BLACK)
            currentImage = blackImage;
        else
            currentImage = whiteImage;
        setCurrentColor(currentColor);
        this.bounds = bounds;
        this.isPlaced = isPlaced;
    }

    public GamePiece (Context context, int currentColor, int x, int y, boolean isPlaced) {
        this(context);
        if(currentColor==COLOR_BLACK)
            currentImage = blackImage;
        else
            currentImage = whiteImage;
        setCurrentColor(currentColor);
        this.bounds = new Rect(x, y, currentImage.getWidth(), currentImage.getHeight());
    }

    public void draw(Canvas canvas) {
        if(isPlaced)
            canvas.drawBitmap(currentImage, null, bounds, null);
        if(isSelected) {
            Paint paint = new Paint();
            paint.setColor(Color.CYAN);
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(10);
            paint.setAlpha(180);
            canvas.drawRoundRect(new RectF(bounds), bounds.width()/2, bounds.height()/2, paint);
        }
    }

    public void update() {
        if(isAnimating) {
            long currentTime = System.currentTimeMillis();
            if(currentTime-lastAnimationTime>33) {
                // end animation if on last frame
                if(animationFrame==animationImage.length){
                    isAnimating=false;
                    currentImage=(currentColor==COLOR_BLACK)? blackImage : whiteImage;
                }
                // otherwise go to next frame
                else {
                    currentImage = animationImage[animationFrame++];
                    lastAnimationTime = currentTime;
                }
            }
        }
    }

    public void drawSelectedCircle(Canvas canvas) {

        Paint paint = new Paint();
        paint.setColor(Color.CYAN);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(10);
        paint.setAlpha(180);
        canvas.drawRoundRect(new RectF(getBounds()), getBounds().width() / 2, getBounds().height() / 2, paint);
    }

    public void flip() {
        if(currentColor == COLOR_BLACK){
            //flip to white
            currentColor = COLOR_WHITE;
            startAnimation();
        } else {
            //flip to black
            currentColor = COLOR_BLACK;
            startAnimation();
        }
    }

    public void flip(int colorToFlipTo){
        if(colorToFlipTo!=currentColor)
            flip();
    }

    private void startAnimation() {
        animationImage = (currentColor==COLOR_BLACK) ? whiteToBlackImage : blackToWhiteImage;
        animationFrame = 0;
        isAnimating = true;
        lastAnimationTime = System.currentTimeMillis();
    }

    public void place(int color) {
        isPlaced = true;
        currentColor = color;
        if(color==COLOR_BLACK)
            currentImage = blackImage;
        else
            currentImage = whiteImage;
    }

    public int getCurrentColor() {
        return currentColor;
    }

    public void setCurrentColor(int currentColor) {
        this.currentColor = currentColor;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public Rect getBounds() {
        return bounds;
    }

    public void setBounds(Rect r) {
        bounds = r;
    }
}
