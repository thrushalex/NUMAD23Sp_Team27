package edu.northeastern.numad23sp_team27;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
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

import java.lang.reflect.Array;
import java.util.ArrayList;
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
    private Button undoButton;
    private Button redoButton;
    private String shapeName;
    private String color;
    private String shapeText;
    private int xCoordinate;
    private int yCoordinate;
    private ArrayList<String> undoStack;
    private ArrayList<String> redoStack;
    private ArrayList<String> drawCommandsLog;
    private static final String preferences = "projTalkPreferences";
    private SharedPreferences sharedpreferences;
    private final int undoRedoMaxSize = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawing);

        shapeColorButton = findViewById(R.id.shapeColorSelectButton);
        undoButton = findViewById(R.id.undoButton);
        redoButton = findViewById(R.id.redoButton);
        drawingImageView = findViewById(R.id.imageView);
        constraintLayout = findViewById(R.id.constraintLayout);
        paint = new Paint();
        navigateMode = false;
        drawMode = true;
        canvasMade = false;
        shapeName = "rectangle";
        color = "black";
        shapeText = "";
        drawCommandsLog = new ArrayList<>();
        undoStack = new ArrayList<>();
        redoStack = new ArrayList<>();
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
        undoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                redoStack.add(undoStack.get(0));
                undoStack.remove(0);
                drawCommandsLog.remove(drawCommandsLog.size() - 1);
                makeCanvas();
                for (int i = 0; i < drawCommandsLog.size(); i++) {
                    deserializeDraw(drawCommandsLog.get(i));
                    drawShape(xCoordinate, yCoordinate, shapeName,shapeText);
                }

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
            shapeName = tempShape;
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
                xCoordinate = (int)event.getX() - xOffset;
                yCoordinate = (int)event.getY() - yOffset;
                drawShape(xCoordinate, yCoordinate,shapeName,shapeText);
                String serializedDrawCommand = serializeDraw(xCoordinate, yCoordinate,shapeName,shapeText);
                undoStack.add(0,serializedDrawCommand);
                drawCommandsLog.add(serializedDrawCommand);
            }
        }
        return true;
    }

    public void drawShape(int x, int y, String shapeName, String textToDraw){
        if(shapeName.equals("rectangle")){
            drawRectangle(x,y,textToDraw);
        }
        if(shapeName.equals("oval")){
            drawOval(x,y,textToDraw);
        }
        if(shapeName.equals("triangle")){
            drawTriangle(x,y,textToDraw);
        }
    }

    public String serializeDraw(int xCoordinate, int yCoordinate, String shapeName, String shapeText) {
        String drawCommandString = Integer.toString(xCoordinate);
        drawCommandString += ",";
        drawCommandString += Integer.toString(yCoordinate);
        drawCommandString += ",";
        drawCommandString += shapeName;
        if (!shapeText.equals("")) {
            drawCommandString += ",";
            drawCommandString += shapeText;
        }
        return drawCommandString;
    }

    public void deserializeDraw(String serializedDrawString){
        int groupNumber = 1;
        //default to empty string in case text not provided
        shapeText = "";

        String subString = "";
        for (int i = 0; i < serializedDrawString.length(); i++) {
            if (!(serializedDrawString.charAt(i) == ",".charAt(0))) {
                subString += serializedDrawString.charAt(i);
                if (groupNumber == 1) {
                    xCoordinate =  Integer.parseInt(subString);
                } else if (groupNumber == 2) {
                    yCoordinate =  Integer.parseInt(subString);
                } else if (groupNumber == 3) {
                    shapeName =  subString;
                } else if (groupNumber == 4) {
                    shapeText =  subString;
                }
            } else {
                groupNumber += 1;
                subString = "";
            }
        }

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

    public void drawOval(int x, int y, String textToDraw){
        //Draw oval
        Rect r = new Rect(x-200, y+100, x+200, y-100);
        RectF rectF = new RectF(x-250, y+180, x+250, y-180);

        View v = this.findViewById(R.id.imageView);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(5);
        drawingCanvas.drawOval(rectF, paint);

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
        //Loop to draw text within oval dimensions
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

    public void drawTriangle(int x, int y, String textToDraw){
        //Draw triangle
        Rect r = new Rect(x-200, y+100, x+200, y-100);
        int width = r.width();
        Path path = new Path();
        path.moveTo(x, y - width); // Top
        path.lineTo(x - width, y + width/2); // Bottom left
        path.lineTo(x + width, y + width/2); // Bottom right
        path.lineTo(x, y - width); // Back to Top
        path.close();
        View v = this.findViewById(R.id.imageView);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(5);
        drawingCanvas.drawPath(path, paint);

        //Set Paint object for text
        paint.setTextSize(70);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        paint.setTextAlign(Paint.Align.CENTER);
        int start = 0;
        //Based on size of text box and font size
        int maxRows = 3;
        int verticalOffset = 60;
        int offSetSize = 60;
        String remainingText = textToDraw;
        //Loop to draw text within triangle dimensions
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