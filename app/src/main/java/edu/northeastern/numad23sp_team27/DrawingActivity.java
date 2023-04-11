package edu.northeastern.numad23sp_team27;

import static android.graphics.Path.Direction.CW;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.res.ResourcesCompat;

import java.util.Objects;

public class DrawingActivity extends AppCompatActivity implements View.OnTouchListener {

    private Canvas mCanvas;
    private final Paint mPaint = new Paint();
    private final Paint mPaintText = new Paint(Paint.UNDERLINE_TEXT_FLAG);
    private Bitmap mBitmap;
    private ImageView mImageView;
    private int mColorBackground;

    //Used to tell whether user is aiming to scroll along image view,
    // or if they are indenting to add visual elements
    private boolean navigateMode;
    private boolean drawMode;
    private boolean canvasMade;
    private int xOffset;
    private int yOffset;
    private ConstraintLayout constraintLayout;

    private GestureDetector gestureDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawing);

        mColorBackground = ResourcesCompat.getColor(getResources(),
                R.color.colorBackground, null);
        mPaint.setColor(mColorBackground);
        mPaintText.setColor(
                ResourcesCompat.getColor(getResources(),
                        R.color.colorPrimaryDark, null)
        );
        mPaintText.setTextSize(70);
        mImageView = findViewById(R.id.imageView);
        constraintLayout = findViewById(R.id.constraintLayout);
        navigateMode = false;
        drawMode = true;
        canvasMade = false;
        gestureDetector = new GestureDetector(this,new OnSwipeListener(){

            @Override
            public boolean onSwipe(Direction direction) {
                if (direction==Direction.up){
                    Toast.makeText(getApplicationContext(), "Swipe Up", Toast.LENGTH_SHORT).show();
                    scroll("up");

                }

                if (direction==Direction.down){
                    Toast.makeText(getApplicationContext(), "Swipe Down", Toast.LENGTH_SHORT).show();
                    scroll("down");
                }

                if (direction==Direction.left){
                    Toast.makeText(getApplicationContext(), "Swipe Left", Toast.LENGTH_SHORT).show();
                    scroll("left");
                }

                if (direction==Direction.right){
                    Toast.makeText(getApplicationContext(), "Swipe Right", Toast.LENGTH_SHORT).show();
                    scroll("right");
                }
                return true;
            }
        });
        constraintLayout.setOnTouchListener(this);
        Switch onOffSwitch = findViewById(R.id.drawNavigateSwitch);
        onOffSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                Toast.makeText(getApplicationContext(), "Switch on", Toast.LENGTH_SHORT).show();
                navigateMode = false;
                drawMode = true;
            } else {
                Toast.makeText(getApplicationContext(), "Switch off", Toast.LENGTH_SHORT).show();
                navigateMode = true;
                drawMode = false;
            }
        });
        }


    public void scroll(String direction) {
        if (Objects.equals(direction, "right")) {
            mImageView.scrollBy(-100,0);
            xOffset = xOffset + 100;
        } else if (Objects.equals(direction, "left")) {
            mImageView.scrollBy(100,0);
            xOffset = xOffset - 100;
        } else if (Objects.equals(direction, "up")) {
            mImageView.scrollBy(0,100);
            yOffset = yOffset - 100;
        } else if (Objects.equals(direction, "down")) {
            mImageView.scrollBy(0,-100);
            yOffset = yOffset + 100;
        }
        View v = this.findViewById(android.R.id.content);
        v.invalidate();
    }

    @Override
    public boolean onTouch(View view, MotionEvent event) {
        if(!canvasMade) {
            makeCanvas();
            canvasMade = true;
        }
        if(navigateMode){
            gestureDetector.onTouchEvent(event);
        } else if(drawMode) {
            if (event.getAction() == 0) {
                int x = (int)event.getX() - xOffset;
                int y = (int)event.getY() - yOffset;
                drawRectangle(x,y,"Wireless Access Point Bleep Bloop Blorp");
            }
        }
        return true;
    }

    public void drawRectangle(int x, int y, String textToDraw){
        //Draw rectangle
        Rect r = new Rect(x-200, y+100, x+200, y-100);
        View v = this.findViewById(R.id.imageView);
        Paint textPaint = new Paint();
        textPaint.setStyle(Paint.Style.STROKE);
        textPaint.setStrokeWidth(5);
        mCanvas.drawRect(r,textPaint);

        //Set Paint object for text
        textPaint.setTextSize(70);
        textPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        textPaint.setTextAlign(Paint.Align.CENTER);
        int width = r.width();
        int start = 0;
        //Based on size of text box and font size
        int maxRows = 3;
        int verticalOffset = 60;
        int offSetSize = 60;
        String remainingText = textToDraw;
        //Loop to draw text within rectangle dimentions
        for (int i = 0; i < maxRows; i++) {
            int numOfChars = textPaint.breakText(remainingText,true,width,null);
            mCanvas.drawText(remainingText,start,start+numOfChars,r.exactCenterX(),r.bottom + verticalOffset,textPaint);
            verticalOffset = verticalOffset + offSetSize;
            if (remainingText.length()-numOfChars > 0) {
                remainingText = remainingText.substring(numOfChars+1);
            } else {
                break;
            }
        }
        v.invalidate();
    }

    public void makeCanvas(){
        Toast.makeText(getApplicationContext(), "Making new canvas", Toast.LENGTH_SHORT).show();
        View v = this.findViewById(R.id.imageView);
        xOffset = (int) v.getX();
        yOffset = (int) v.getY();
        int vWidth = v.getWidth();
        int vHeight = v.getHeight();
        mBitmap = Bitmap.createBitmap(vWidth, vHeight, Bitmap.Config.ARGB_8888);
        mImageView.setImageBitmap(mBitmap);
        mCanvas = new Canvas(mBitmap);
    }
}