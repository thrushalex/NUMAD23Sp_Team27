package edu.northeastern.numad23sp_team27;

import static android.graphics.Path.Direction.CW;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
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
                int x = (int)event.getX();
                int y = (int)event.getY();
                drawRectangle(x,y, "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.");
            }
        }
        return true;
    }

    public void drawRectangle(int x, int y, String textToDraw){
        View v = this.findViewById(R.id.imageView);
        //offsets used to account for vertical and horizontal scrolling
        x = x - xOffset;
        y = y - yOffset;
        Paint tempPaint = new Paint();
        tempPaint.setStyle(Paint.Style.STROKE);
        tempPaint.setStrokeWidth(5);
        tempPaint.setColor(Color.rgb(88, 50, 168));
        mCanvas.drawRect(x - 300,y + 200,x + 300,y - 200,tempPaint);
        tempPaint.setTextSize(50);
        tempPaint.setStrokeWidth(3);
        Path path = new Path();
        path.addRect((x-280), (y+180), (x+280), (y-180), CW);
        int lineCount = textToDraw.length() / 20;
        int mod = lineCount % 20;
        int start = 0;
        int end = 20;
        if (textToDraw.length() > 0) {
            if (mod > 0 | textToDraw.length() <= 20) {
                lineCount += 1;
                if (textToDraw.length() <= 20) {
                    end = textToDraw.length();
                }
            }
            int initialVerticalAdjustment = 150;
            for (int i = 0; i < lineCount; i++) {
                if (i <= 6) {
                    mCanvas.drawText(textToDraw.substring(start,end),x-280,y-initialVerticalAdjustment, tempPaint);
                    initialVerticalAdjustment = initialVerticalAdjustment - 50;
                    if(end + 1 < textToDraw.length()){
                        start = end + 1;
                        if (textToDraw.substring(end).length() <= 20) {
                            end = textToDraw.length();
                        } else {
                            end = start + 20;
                        }

                    }
                }
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