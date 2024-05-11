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
    private List<PointF> circlePositions = new ArrayList<>();
    private Path path;
    private boolean isNormalMode = false;
    private boolean isCircleMode = false;
    private boolean isStraightLineMode = false; // Indicateur pour le mode "ligne droite"
    private PointF startPoint; // Point de départ pour la ligne droite
    private PointF endPoint; // Point de fin pour la ligne droite
    private float touchX;
    private float touchY;
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
        isCircleMode = false;
        isNormalMode = true;
        isStraightLineMode = false;
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // Dessiner les cercles si le mode cercle est activé
        if (isCircleMode) {
            paint.setStyle(Paint.Style.FILL); // Remplir les cercles
            for (PointF position : circlePositions) {
                canvas.drawCircle(position.x, position.y, 50f, paint); // Dessiner chaque cercle à partir de la liste
            }
        }

        // Dessiner les lignes droites si le mode ligne droite est activé
        if (isStraightLineMode) {
            if (startPoint != null && endPoint != null) {
                canvas.drawLine(startPoint.x, startPoint.y, endPoint.x, endPoint.y, paint); // Dessiner une ligne droite entre startPoint et endPoint
            }
        }

        // Dessiner les lignes si le mode normal est activé
        if (isNormalMode) {
            canvas.drawPath(path, paint); // Dessiner une ligne
        }
    }



    // Méthode pour activer ou désactiver le mode cercle
    public void setCircleMode() {
        isCircleMode = true;
        isNormalMode = false;
        isStraightLineMode = false;
    }
    public void setNormalMode() {
        isCircleMode = false;
        isNormalMode = true;
        isStraightLineMode = false;
    }
    public void setStraightLineMode() {
        isCircleMode = false;
        isNormalMode = false;
        isStraightLineMode = true;
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (isStraightLineMode) {
                    startPoint = new PointF(x, y); // Enregistrer le point de départ pour la ligne droite
                } else if (!isCircleMode) {
                    path.moveTo(x, y); // Commencer un nouveau chemin si le mode cercle n'est pas activé
                }
                return true;
            case MotionEvent.ACTION_MOVE:
                if (isStraightLineMode) {
                    // Ne rien faire pendant le déplacement pour la ligne droite
                } else if (!isCircleMode) {
                    path.lineTo(x, y); // Dessiner une ligne si le mode cercle n'est pas activé
                }
                break;
            case MotionEvent.ACTION_UP:
                if (isStraightLineMode) {
                    endPoint = new PointF(x, y); // Enregistrer le point de fin pour la ligne droite
                } else if (isCircleMode) {
                    circlePositions.add(new PointF(x, y)); // Ajouter les coordonnées du cercle actuellement dessiné à la liste
                }
                break;
            default:
                return false;
        }

        invalidate(); // Redessiner la vue pour afficher la ligne

        return true;
    }





    public void clearDrawing() {
        path.reset(); // Efface le dessin
        invalidate(); // Redessine la vue
    }
}
