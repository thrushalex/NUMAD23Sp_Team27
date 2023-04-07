package edu.northeastern.numad23sp_team27;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.res.ResourcesCompat;

public class DrawingActivity extends AppCompatActivity implements View.OnTouchListener {

    private Canvas mCanvas;
    private Paint mPaint = new Paint();
    private Paint mPaintText = new Paint(Paint.UNDERLINE_TEXT_FLAG);
    private Bitmap mBitmap;
    private ImageView mImageView;
    private Rect mRect = new Rect();
    private Rect mBounds = new Rect();
    private static final int OFFSET = 120;
    private int mOffset = OFFSET;
    private int mColorBackground;
    private int mColorRectangle;
    private int mColorAccent;

    //Used to tell whether user is aiming to scroll along image view,
    // or if they are indenting to add visual elements
    private boolean navigateMode;
    private boolean drawMode;
    private boolean canvasMade;
    private ConstraintLayout constraintLayout;

    private GestureDetector gestureDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawing);

        mColorBackground = ResourcesCompat.getColor(getResources(),
                R.color.colorBackground, null);
        mColorRectangle = ResourcesCompat.getColor(getResources(),
                R.color.colorRectangle, null);
        mColorAccent = ResourcesCompat.getColor(getResources(),
                R.color.colorAccent, null);
        mPaint.setColor(mColorBackground);
        mPaintText.setColor(
                ResourcesCompat.getColor(getResources(),
                        R.color.colorPrimaryDark, null)
        );
        mPaintText.setTextSize(70);
        mImageView = (ImageView) findViewById(R.id.imageView);
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
        Switch onOffSwitch = (Switch)  findViewById(R.id.drawNavigateSwitch);
        onOffSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    Toast.makeText(getApplicationContext(), "Switch on", Toast.LENGTH_SHORT).show();
                    navigateMode = false;
                    drawMode = true;
                } else {
                    Toast.makeText(getApplicationContext(), "Switch off", Toast.LENGTH_SHORT).show();
                    navigateMode = true;
                    drawMode = false;
                }
            }
        });
        }

    public void drawSomething(View view) {
        int vWidth = view.getWidth();
        int vHeight = view.getHeight();
        mBitmap = Bitmap.createBitmap(vWidth, vHeight, Bitmap.Config.ARGB_8888);
        mImageView.setImageBitmap(mBitmap);
        mCanvas = new Canvas(mBitmap);
        mCanvas.drawColor(mColorBackground);
        mCanvas.drawText(getString(R.string.keep_tapping), 100, 100, mPaintText);
        view.invalidate();
    }

    public void scroll(String direction) {
        if (direction == "right") {
            mImageView.scrollBy(-100,0);
        } else if (direction == "left") {
            mImageView.scrollBy(100,0);
        } else if (direction == "up") {
            mImageView.scrollBy(0,100);
        } else if (direction == "down") {
            mImageView.scrollBy(0,-100);
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
                View v = this.findViewById(R.id.imageView);
                int x = (int)event.getX();
                x = (int) (x - v.getX());
                int y = (int)event.getY();
                y = (int) (y - v.getY());
                Paint tempPaint = new Paint();
                tempPaint.setStyle(Paint.Style.STROKE);
                tempPaint.setStrokeWidth(5);
                tempPaint.setColor(Color.rgb(88, 50, 168));
                mCanvas.drawRect(x - 100,y + 100,x + 100,y - 100,tempPaint);
                mCanvas.drawRect(x - 80,y + 80,x + 80,y - 80,tempPaint);
                v.invalidate();
            }
        }
        return true;
    }

    public void makeCanvas(){
        Toast.makeText(getApplicationContext(), "Making new canvas", Toast.LENGTH_SHORT).show();
        View v = this.findViewById(R.id.imageView);
        int vWidth = v.getWidth();
        int vHeight = v.getHeight();
        mBitmap = Bitmap.createBitmap(vWidth, vHeight, Bitmap.Config.ARGB_8888);
        mImageView.setImageBitmap(mBitmap);
        mCanvas = new Canvas(mBitmap);
    }
}