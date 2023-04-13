package edu.northeastern.numad23sp_team27;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import java.util.Objects;

public class DrawingActivity extends AppCompatActivity implements View.OnTouchListener {

    private Canvas drawingCanvas;
    private Bitmap drawingBitmap;
    private ImageView drawingImageView;

    private boolean navigateMode;
    private boolean drawMode;
    private boolean canvasMade;
    private int xOffset;
    private int yOffset;
    private ConstraintLayout constraintLayout;

    private GestureDetector gestureDetector;
    private Paint paint;
    private Button shapeColorButton;
    private String shape;
    private String color;
    private String shapeText;
    private static final String preferences = "projTalkPreferences";
    SharedPreferences sharedpreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawing);

        shapeColorButton = findViewById(R.id.shapeColorSelectButton);
        drawingImageView = findViewById(R.id.imageView);
        constraintLayout = findViewById(R.id.constraintLayout);
        paint = new Paint();
        navigateMode = false;
        drawMode = true;
        canvasMade = false;
        shape = "rectangle";
        color = "black";
        shapeText = "";
        sharedpreferences = getSharedPreferences(preferences, Context.MODE_PRIVATE);
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
        shapeColorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startChooseShapeActivity();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadShapeColor();
    }

    public void loadShapeColor(){
        String tempColor = sharedpreferences.getString("color", "DEFAULT");
        String tempShape = sharedpreferences.getString("shape", "DEFAULT");
        String tempShapeText = sharedpreferences.getString("shapeText", "DEFAULT");
        if (!tempColor.equals("DEFAULT")) {
            color = tempColor;
            setColor(color);
        }
        if (!tempShape.equals("DEFAULT")) {
            shape = tempShape;
        }
        if (!tempShapeText.equals("DEFAULT")) {
            shapeText = tempShapeText;
        }
    }

    public void scroll(String direction) {
        if (Objects.equals(direction, "right")) {
            drawingImageView.scrollBy(-100,0);
            xOffset = xOffset + 100;
        } else if (Objects.equals(direction, "left")) {
            drawingImageView.scrollBy(100,0);
            xOffset = xOffset - 100;
        } else if (Objects.equals(direction, "up")) {
            drawingImageView.scrollBy(0,100);
            yOffset = yOffset - 100;
        } else if (Objects.equals(direction, "down")) {
            drawingImageView.scrollBy(0,-100);
            yOffset = yOffset + 100;
        }
        View v = this.findViewById(android.R.id.content);
        v.invalidate();
    }

    public void setColor(String color){
        if(color.equals("blue")){
            paint.setColor(getResources().getColor(R.color.blue));
        }
        if(color.equals("black")){
            paint.setColor(getResources().getColor(R.color.black));
        }
        if(color.equals("red")){
            paint.setColor(getResources().getColor(R.color.red));
        }
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
                drawRectangle(x,y,shapeText);
            }
        }
        return true;
    }

    public void drawRectangle(int x, int y, String textToDraw){
        //Draw rectangle
        Rect r = new Rect(x-200, y+100, x+200, y-100);
        View v = this.findViewById(R.id.imageView);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(5);
        drawingCanvas.drawRect(r,paint);

        //Set Paint object for text
        paint.setTextSize(70);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        paint.setTextAlign(Paint.Align.CENTER);
        int width = r.width();
        int start = 0;
        //Based on size of text box and font size
        int maxRows = 3;
        int verticalOffset = 60;
        int offSetSize = 60;
        String remainingText = textToDraw;
        //Loop to draw text within rectangle dimentions
        for (int i = 0; i < maxRows; i++) {
            int numOfChars = paint.breakText(remainingText,true,width,null);
            drawingCanvas.drawText(remainingText,start,start+numOfChars,r.exactCenterX(),r.bottom + verticalOffset,paint);
            verticalOffset = verticalOffset + offSetSize;
            if (remainingText.length()-numOfChars > 0) {
                remainingText = remainingText.substring(numOfChars);
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
        drawingBitmap = Bitmap.createBitmap(vWidth, vHeight, Bitmap.Config.ARGB_8888);
        drawingImageView.setImageBitmap(drawingBitmap);
        drawingCanvas = new Canvas(drawingBitmap);
    }

    public void startChooseShapeActivity() {
        Intent intent = new Intent(this, ChooseShape.class);
        startActivity(intent);
    }
}