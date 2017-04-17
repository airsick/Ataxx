package airsick.ataxx;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.view.MotionEvent;

/**
 * Created by coler_000 on 4/13/2017.
 */

public class GameManager {

    private Bitmap board;
    private int boardX, boardY;
    private GamePiece[][] boardPieces;
    private GamePiece selectedPiece;
    private int selectedX, selectedY;
    private GamePiece blackTurnMarkerPiece, whiteTurnMarkerPiece;
    private Context context;

    private int activePlayer;
    private boolean centered=false;


    public GameManager(Context context) {
        // Save the context
        this.context = context;

        // Asset Loading
        board = BitmapFactory.decodeResource(context.getResources(), R.drawable.board);
        boardX = 10;
        boardY = board.getHeight()/7+20;
        boardPieces = new GamePiece[7][7];
        Rect pieceBounds;
        for(int i=0; i<7; i++) {
            for(int j=0; j<7; j++) {
                pieceBounds = new Rect((board.getWidth()/7)*i+boardX,
                        (board.getHeight()/7)*j+boardY,
                        (board.getWidth()/7)*(i+1)+boardX,
                        (board.getHeight()/7)*(j+1)+boardY);
                boardPieces[i][j] = new GamePiece(context, GamePiece.COLOR_BLACK, pieceBounds, false);
            }
        }
        // TurnMakerPieces initialization
        pieceBounds = new Rect((int) ((board.getWidth()/7)*1.5)+boardX,
                10,
                (int) ((board.getWidth()/7)*2.5+boardX),
                (int) (board.getHeight()/7)+10);
        blackTurnMarkerPiece = new GamePiece(context, GamePiece.COLOR_BLACK, pieceBounds, false);
        blackTurnMarkerPiece.place(GamePiece.COLOR_BLACK);

        pieceBounds = new Rect((int) ((board.getWidth()/7)*4.5+boardX),
                10,
                (int) ((board.getWidth()/7)*(5.5)+boardX),
                (int) (board.getHeight()/7)+10);
        whiteTurnMarkerPiece = new GamePiece(context, GamePiece.COLOR_BLACK, pieceBounds, false);
        whiteTurnMarkerPiece.place(GamePiece.COLOR_WHITE);

        // Set starting player
        activePlayer = GamePiece.COLOR_BLACK;

        // Initial board positions
        boardPieces[0][0].place(GamePiece.COLOR_BLACK);
        boardPieces[6][6].place(GamePiece.COLOR_BLACK);

        boardPieces[0][6].place(GamePiece.COLOR_WHITE);
        boardPieces[6][0].place(GamePiece.COLOR_WHITE);
    }

    private void expand(int x, int y) {
        // Place the new piece
        boardPieces[x][y].place(activePlayer);

        // Flip surrounding pieces
        for(int i = (x-1<0)?0:x-1; i <= ((x+1>boardPieces.length-1) ? boardPieces.length-1:x+1); i++) {
            for(int j = (y-1<0)?0:y-1; j <= ((y+1>boardPieces[i].length-1) ? boardPieces[i].length-1:y+1); j++) {
                boardPieces[i][j].flip(activePlayer);
            }
        }

        // Deselect the piece
        selectedPiece = null;

        checkForWin();
        // Change active player
        if(activePlayer==GamePiece.COLOR_BLACK)
            activePlayer=GamePiece.COLOR_WHITE;
        else
            activePlayer=GamePiece.COLOR_BLACK;
    }

    private void move(int x, int y) {
        // Remove the selected piece
        selectedPiece.isPlaced = false;

        // Place the new piece
        boardPieces[x][y].place(activePlayer);

        // Flip surrounding pieces
        for(int i = (x-1<0)?0:x-1; i <= ((x+1>boardPieces.length-1)?boardPieces.length-1:x+1); i++) {
            for(int j = (y-1<0)?0:y-1; j <= ((y+1>boardPieces[i].length-1)?boardPieces[i].length-1:y+1); j++) {
                boardPieces[i][j].flip(activePlayer);
            }
        }

        // Deselect the piece
        selectedPiece = null;

        checkForWin();

        // Change active player
        if(activePlayer==GamePiece.COLOR_BLACK)
            activePlayer=GamePiece.COLOR_WHITE;
        else
            activePlayer=GamePiece.COLOR_BLACK;
    }

    private void checkForWin() {
        // Count Pieces
        int blackPieceCount = 0, whitePieceCount = 0;

        for(int i=0; i<7; i++) {
            for(int j=0; j<7; j++) {
                if (boardPieces[i][j].isPlaced) {
                    if(boardPieces[i][j].getCurrentColor()==GamePiece.COLOR_BLACK)
                        blackPieceCount++;
                    else
                        whitePieceCount++;
                }
            }
        }

        if(blackPieceCount+whitePieceCount==49) {
            String winner = (blackPieceCount > whitePieceCount)?"Black":"White";
            new AlertDialog.Builder(context)
                .setTitle("Winner")
                .setMessage(winner + " wins!")
                .show();
        }
    }

    public void draw(Canvas canvas) {
        // If this is the first frame we need to center the board
        if(!centered) {
            boardX = canvas.getWidth()/2-board.getWidth()/2;

            Rect pieceBounds;
            for(int i=0; i<7; i++) {
                for(int j=0; j<7; j++) {
                    pieceBounds = new Rect((board.getWidth()/7)*i+boardX,
                            (board.getHeight()/7)*j+boardY,
                            (board.getWidth()/7)*(i+1)+boardX,
                            (board.getHeight()/7)*(j+1)+boardY);
                    boardPieces[i][j].setBounds(pieceBounds);
                }
            }
            // TurnMakerPieces initialization
            pieceBounds = new Rect((int) ((board.getWidth()/7)*1.5)+boardX,
                    10,
                    (int) ((board.getWidth()/7)*2.5+boardX),
                    (int) (board.getHeight()/7)+10);
            blackTurnMarkerPiece.setBounds(pieceBounds);

            pieceBounds = new Rect((int) ((board.getWidth()/7)*4.5+boardX),
                    10,
                    (int) ((board.getWidth()/7)*(5.5)+boardX),
                    (int) (board.getHeight()/7)+10);
            whiteTurnMarkerPiece.setBounds(pieceBounds);

            centered = true;
        }




        // Draw background and board
        canvas.drawRGB(27, 94, 32);
        canvas.drawBitmap(board, boardX, boardY, null);
        // Draw Turn Marker Pieces
        blackTurnMarkerPiece.draw(canvas);
        whiteTurnMarkerPiece.draw(canvas);
        // Mark the active player
        if(activePlayer==GamePiece.COLOR_BLACK)
            blackTurnMarkerPiece.drawSelectedCircle(canvas);
        else
            whiteTurnMarkerPiece.drawSelectedCircle(canvas);

        for(int i = 0; i<boardPieces.length; i++) {
            for (int j = 0; j < boardPieces[i].length; j++) {
                boardPieces[i][j].draw(canvas);
            }
        }
        if(selectedPiece != null) {
            // Draw circle on selected piece
            selectedPiece.drawSelectedCircle(canvas);

            // Draw markers for valid moves

            for(int i = (selectedX-2<0)?0:selectedX-2; i <= ((selectedX+2>boardPieces.length-1) ? boardPieces.length-1:selectedX+2); i++) {
                for(int j = (selectedY-2<0)?0:selectedY-2; j <= ((selectedY+2>boardPieces[i].length-1) ? boardPieces[i].length-1:selectedY+2); j++) {
                    // Make sure the square is empty
                    if(!boardPieces[i][j].isPlaced) {
                        // If it's 2 squares away draw a red circle
                        Paint paint = new Paint();
                        paint.setStyle(Paint.Style.STROKE);
                        paint.setStrokeWidth(10);
                        paint.setAlpha(180);
                        if (Math.abs(selectedX - i) == 2 || Math.abs(selectedY - j) == 2) {
                            paint.setColor(Color.RED);
                            canvas.drawCircle(boardPieces[i][j].getBounds().centerX(), boardPieces[i][j].getBounds().centerY(), boardPieces[i][j].getBounds().width() / 4, paint);
                        }

                        // If it's 1 square away draw a yellow circle
                        else if (Math.abs(selectedX - i) == 1 || Math.abs(selectedY - j) == 1) {
                            paint.setColor(Color.YELLOW);
                            canvas.drawCircle(boardPieces[i][j].getBounds().centerX(), boardPieces[i][j].getBounds().centerY(), boardPieces[i][j].getBounds().width() / 3, paint);
                        }
                    }
                }
            }
        }
    }

    public void touchEvent(MotionEvent event) {
        // Check if user is tapping a board position
        for(int i = 0; i<boardPieces.length; i++) {
            for (int j = 0; j < boardPieces[i].length; j++) {
                if(boardPieces[i][j].getBounds().contains((int) event.getX(), (int) event.getY())) {
                    // A board position was tapped
                    // See if user is tapping a piece or an empty square
                    if(boardPieces[i][j].isPlaced) {
                        if(activePlayer==boardPieces[i][j].getCurrentColor()) {
                            // If user is tapping a piece, select it or deselect it if it's already selected
                            if (selectedPiece == boardPieces[i][j])
                                selectedPiece = null;
                            else {
                                selectedPiece = boardPieces[i][j];
                                selectedX = i;
                                selectedY = j;
                            }
                        } else {
                            // user tapped the opposite color piece, so we'll deselect his piece
                            selectedPiece = null;
                        }
                    } else {
                        // Board position currently has no piece
                        if(selectedPiece!=null) {
                            // Deselect if you click the selected piece
                            if(selectedX==i && selectedY==j) {
                                selectedPiece = null;
                            }
                            // If the selected location is within 1 square of the selected piece, expand there
                            else if (Math.abs(selectedX-i)<=1 && Math.abs(selectedY-j)<=1) {
                                expand(i, j);
                            }
                            // If the selected location is within 2 squares of the selected piece, move there
                            else if (Math.abs(selectedX-i)<=2 && Math.abs(selectedY-j)<=2) {
                                move(i, j);
                            }else {
                                // Otherwise just deselect the piece
                                selectedPiece = null;
                            }
                        }
                    }
                }
            }
        }
    }
}
