package com.example.sonu88.spaceimpact;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;

public class DrawView extends View {

    private int width, height;

    Context mContext;

    // We can have multiple bullets and explosions
    // keep track of them in ArrayList
    ArrayList<Bullet> bullets;
    ArrayList<Explosion> explosions;
    Canon canon;
    AndroidGuy androidGuy;
    Score score;


    public DrawView(Context context, AttributeSet attrs) {
        super(context, attrs);

        mContext = context;

        // create a cannon object
        canon = new Canon(Color.BLUE,mContext);

        // create arraylists to keep track of bullets and explosions
        bullets = new ArrayList<Bullet> ();
        explosions = new ArrayList<Explosion>();

        // create the falling Android Guy
        androidGuy = new AndroidGuy(Color.RED, mContext);

        score = new Score(Color.BLACK);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        drawGameBoard(canvas);
        try {
            Thread.sleep(30);
        } catch (InterruptedException e) {
        }

        invalidate();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        width = w;
        height = h;

        canon.setBounds(0,0,width,height);
        androidGuy.setBounds(0,0,width,height);
        for (int i = 0; i < bullets.size(); i++ ) {
            bullets.get(i).setBounds(0,0,width,height);
        }

    }

    public void drawGameBoard(Canvas canvas) {
        canvas.drawColor(Color.WHITE);     //if you want another background color
        // Draw the cannon
        canon.draw(canvas);

        // Draw all the bullets
        for (int i = 0; i < bullets.size(); i++ ) {
            if (bullets.get(i) != null) {
                bullets.get(i).draw(canvas);

                if (bullets.get(i).move() == false) {
                    bullets.remove(i);
                }
            }
        }

        // Draw all the explosions, at those locations where the bullet
        // hits the Android Guy
        for (int i = 0; i < explosions.size(); i++ ) {
            if (explosions.get(i) != null) {
                if (explosions.get(i).draw(canvas) == false) {
                    explosions.remove(i);
                }
            }
        }

        // If the Android Guy is falling, check to see if any of the bullets
        // hit the Guy. Draw the Android Guy
        if (androidGuy != null) {
            androidGuy.draw(canvas);

            RectF guyRect = androidGuy.getRect();

            for (int i = 0; i < bullets.size(); i++ ) {
                if (RectF.intersects(guyRect, bullets.get(i).getRect())) {
                    explosions.add(new Explosion(Color.RED,mContext, androidGuy.getX(), androidGuy.getY()));
                    androidGuy.reset();
                    bullets.remove(i);

                    score.incrementScore();
                    break;
                }

            }

            if (androidGuy.move() == false) {
                androidGuy = null;
            }
        }

        score.getScore();
        score.draw(canvas);

    }

    // Move the canon left or right
    public void moveCannonLeft() {
        canon.moveLeft();
    }

    public void moveCannonRight() {
        canon.moveRight();
    }

    // Whenever the user shoots a bullet, create a new bullet moving upwards
    public void shootCannon() {

        bullets.add(new Bullet(Color.RED, mContext, canon.getPosition(), (float) (height-40)));

    }
}


