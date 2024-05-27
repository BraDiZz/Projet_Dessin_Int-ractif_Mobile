package com.example.projet_dessin_interactif;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.graphics.PointF;
import java.util.ArrayList;
import java.util.List;
import android.view.View;

public class DrawingView extends View {
    private List<Circle> circles = new ArrayList<>();
    private List<Line> lines = new ArrayList<>();
    private List<PathWithPaint> paths = new ArrayList<>();


    private Path currentPath;
    private Paint currentPaint;
    private float currentStrokeWidth = 10f;

    private boolean isNormalMode = false;
    private boolean isCircleMode = false;
    private boolean isStraightLineMode = false; // Indicateur pour le mode "ligne droite"
    private PointF startPoint; // Point de départ pour la ligne droite
    private PointF endPoint; // Point de fin pour la ligne droite

    public DrawingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        currentPath = new Path();
        currentPaint = new Paint();
        currentPaint.setColor(Color.BLACK);
        currentPaint.setStyle(Paint.Style.STROKE);
        setBackgroundColor(Color.WHITE);
        currentPaint.setStrokeWidth(currentStrokeWidth);
        currentPaint.setAntiAlias(true);
        isCircleMode = false;
        isNormalMode = true;
        isStraightLineMode = false;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        for (Circle circle : circles) {
            circle.paint.setStyle(Paint.Style.FILL);
            canvas.drawCircle(circle.position.x, circle.position.y, circle.radius, circle.paint);
        }

        for (Line line : lines) {
            line.paint.setStyle(Paint.Style.STROKE);
            canvas.drawLine(line.startPoint.x, line.startPoint.y, line.endPoint.x, line.endPoint.y, line.paint);
        }

        for (PathWithPaint pathWithPaint : paths) {
            pathWithPaint.paint.setStyle(Paint.Style.STROKE);
            canvas.drawPath(pathWithPaint.path, pathWithPaint.paint);
        }

        if (isStraightLineMode && startPoint != null && endPoint != null) {
            currentPaint.setStyle(Paint.Style.STROKE);
            canvas.drawLine(startPoint.x, startPoint.y, endPoint.x, endPoint.y, currentPaint);
        }

        if (isNormalMode) {
            currentPaint.setStyle(Paint.Style.STROKE);
            canvas.drawPath(currentPath, currentPaint);
        }
    }

    // Méthode pour activer le mode cercle
    public void setCircleMode() {
        isCircleMode = true;
        isNormalMode = false;
        isStraightLineMode = false;
    }

    // Méthode pour activer le mode normal
    public void setNormalMode() {
        isCircleMode = false;
        isNormalMode = true;
        isStraightLineMode = false;
    }

    // Méthode pour activer le mode ligne droite
    public void setStraightLineMode() {
        isCircleMode = false;
        isNormalMode = false;
        isStraightLineMode = true;
    }

    public void setStrokeWidth(float strokeWidth) {
        currentStrokeWidth = strokeWidth;
        currentPaint.setStrokeWidth(strokeWidth);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (isStraightLineMode) {
                    startPoint = new PointF(x, y);
                    endPoint = startPoint;
                } else if (isCircleMode) {
                    circles.add(new Circle(new PointF(x, y), currentStrokeWidth+10, new Paint(currentPaint)));
                } else {
                    currentPath.moveTo(x, y);
                }
                return true;
            case MotionEvent.ACTION_MOVE:
                if (isStraightLineMode) {
                    endPoint = new PointF(x, y);
                } else if (!isCircleMode) {
                    currentPath.lineTo(x, y);
                }
                break;
            case MotionEvent.ACTION_UP:
                if (isStraightLineMode) {
                    endPoint = new PointF(x, y);
                    lines.add(new Line(startPoint, endPoint, new Paint(currentPaint)));
                } else if (isCircleMode) {
                    // Cercle déjà ajouté dans ACTION_DOWN
                } else {
                    paths.add(new PathWithPaint(new Path(currentPath), new Paint(currentPaint)));
                    currentPath.reset();
                }
                break;
            default:
                return false;
        }

        invalidate();
        return true;
    }

    public void clearDrawing() {
        circles.clear();
        lines.clear();
        paths.clear();
        startPoint = null;
        endPoint = null;
        invalidate();
    }

    private static class Line {
        PointF startPoint;
        PointF endPoint;
        Paint paint;

        Line(PointF startPoint, PointF endPoint, Paint paint) {
            this.startPoint = startPoint;
            this.endPoint = endPoint;
            this.paint = paint;
        }
    }

    private static class PathWithPaint {
        Path path;
        Paint paint;

        PathWithPaint(Path path, Paint paint) {
            this.path = path;
            this.paint = paint;
        }
    }

    private static class Circle {
        PointF position;
        float radius;
        Paint paint;

        Circle(PointF position, float radius, Paint paint) {
            this.position = position;
            this.radius = radius;
            this.paint = paint;
        }
    }
}