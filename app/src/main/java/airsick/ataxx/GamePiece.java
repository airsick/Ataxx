package airsick.ataxx;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;

/**
 * Created by coler_000 on 4/13/2017.
 */

public class GamePiece {
    public static int COLOR_WHITE = 0;
    public static int COLOR_BLACK = 1;

    private static Bitmap blackImage;
    private static Bitmap whiteImage;
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
            currentImage = whiteImage;
        } else {
            //flip to black
            currentColor = COLOR_BLACK;
            currentImage = blackImage;
        }
    }

    public void flip(int colorToFlipTo){
        if(colorToFlipTo!=currentColor)
            flip();
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
