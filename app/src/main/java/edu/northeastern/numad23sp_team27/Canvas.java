package edu.northeastern.numad23sp_team27;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.view.View;
import android.widget.ImageView;

public class Canvas {
    android.graphics.Canvas drawingCanvas;
    Bitmap drawingBitmap;
    ImageView drawingImageView;
    Paint paint;
    View v;

    Canvas(View v){
        int vWidth = v.getWidth();
        int vHeight = v.getHeight();
        drawingBitmap = Bitmap.createBitmap(vWidth, vHeight, Bitmap.Config.ARGB_8888);
        drawingImageView.setImageBitmap(drawingBitmap);
        drawingCanvas = new android.graphics.Canvas(drawingBitmap);
        Paint backgroundPaint = new Paint();
        backgroundPaint.setColor(Color.WHITE);
        drawingCanvas.drawPaint(backgroundPaint);
    }

    public void drawRectangle(int x, int y, String textToDraw){
        //Draw rectangle
        Rect r = new Rect(x-200, y+100, x+200, y-100);
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
        Rect r = new Rect(x-200, y+70, x+200, y-70);
        RectF rectF = new RectF(x-250, y+120, x+250, y-120);

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
        int maxRows = 2;
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
        Rect r = new Rect(x-150, y+40, x+150, y-40);
        int width = r.width();
        int height = r.height();
        Path path = new Path();
        path.moveTo(x, y + height*2); // Top
        path.lineTo(x - width, y - height/2); // Bottom left
        path.lineTo(x + width, y - height/2); // Bottom right
        path.lineTo(x, y + height*2); // Back to Top
        path.close();
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(5);
        drawingCanvas.drawPath(path, paint);

        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(5);
        drawingCanvas.drawRect(r,paint);

        //Set Paint object for text
        paint.setTextSize(70);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        paint.setTextAlign(Paint.Align.CENTER);
        int start = 0;
        //Based on size of text box and font size
        int maxRows = 1;
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

    public void drawArrow(int x, int y, String direction){
        //Draw arrow
        Rect r = new Rect(x-60, y+60, x+60, y-60);
        int width = r.width();
        int height = r.height();
        Path path = new Path();
        if (direction.equals("down")) {
            path.moveTo(x, y - height/2); // Center Top
            path.lineTo(x + width/2, y); // Center Right
            path.lineTo(x + width/4, y); // Center Inner Right
            path.lineTo(x + width/4, y + height/2); // Bottom Inner Right
            path.lineTo(x - width/4, y + height/2); // Bottom Inner Left
            path.lineTo(x - width/4, y); // Center Inner Left
            path.lineTo(x - width/2, y); // Center Left
            path.lineTo(x, y - height/2); // Center Top
        } else if (direction.equals("up")) {
            path.moveTo(x, y + height/2); // Center Bottom
            path.lineTo(x + width/2, y); // Center Right
            path.lineTo(x + width/4, y); // Center Inner Right
            path.lineTo(x + width/4, y - height/2); // Top Inner Right
            path.lineTo(x - width/4, y - height/2); // Top Inner Left
            path.lineTo(x - width/4, y); // Center Inner Left
            path.lineTo(x - width/2, y); // Center Left
            path.lineTo(x, y + height/2); // Center Bottom
        } else if (direction.equals("right")) {
            path.moveTo(x + width/2, y); // Center Right
            path.lineTo(x, y - height/2); // Bottom Center
            path.lineTo(x, y - height/4); // Inner Bottom Center
            path.lineTo(x - width/2, y - height/4); // Inner Bottom Left
            path.lineTo(x - width/2, y + height/4); // Inner Top Left
            path.lineTo(x, y + height/4); // Inner Top Center
            path.lineTo(x, y + height/2); // Top Center
            path.lineTo(x + width/2, y); // Center Right
        } else if (direction.equals("left")) {
            path.moveTo(x - width/2, y); // Center Left
            path.lineTo(x, y - height/2); // Bottom Center
            path.lineTo(x, y - height/4); // Inner Bottom Center
            path.lineTo(x + width/2, y - height/4); // Inner Bottom Right
            path.lineTo(x + width/2, y + height/4); // Inner Top Right
            path.lineTo(x, y + height/4); // Inner Top Center
            path.lineTo(x, y + height/2); // Top Center
            path.lineTo(x - width/2, y); // Center Left
        }
        path.close();
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(5);
        drawingCanvas.drawPath(path, paint);
        v.invalidate();
    }

}
