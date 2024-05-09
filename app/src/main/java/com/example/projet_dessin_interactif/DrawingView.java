package com.example.projet_dessin_interactif;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class DrawingView extends View {
    private Path path;
    private Paint paint;

    public DrawingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        path = new Path();
        paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(10f);
        paint.setAntiAlias(true);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawPath(path, paint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                path.moveTo(x, y);
                return true;
            case MotionEvent.ACTION_MOVE:
                path.lineTo(x, y);
                break;
            case MotionEvent.ACTION_UP:
                // Finaliser le trait de dessin
                break;
            default:
                return false;
        }

        invalidate(); // Redessine la vue
        return true;
    }

    public void clearDrawing() {
        path.reset(); // Efface le dessin
        invalidate(); // Redessine la vue
    }
}
