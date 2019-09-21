package kr.devs.geniusbean.utils;

    import java.util.ArrayList;
    import android.content.Context;
    import android.graphics.Bitmap;
    import android.graphics.BitmapFactory;
    import android.graphics.Canvas;
    import android.graphics.Color;
    import android.graphics.Matrix;
    import android.graphics.Paint;
    import android.view.View;
    import com.nineoldandroids.animation.ValueAnimator;
    import com.nineoldandroids.animation.ValueAnimator.AnimatorUpdateListener;

    import kr.devs.geniusbean.R;

public class FlakeView extends View {

    Bitmap droid;
    int numFlakes = 16;
    ArrayList<Flake> flakes = new ArrayList<Flake>();

    ValueAnimator animator = ValueAnimator.ofFloat(0, 1);
    long startTime, prevTime;
    int frames = 0;
    Paint textPaint;
    float fps = 0;
    Matrix m = new Matrix();
    String fpsString = "";
    String numFlakesString = "";

    public FlakeView(Context context) {
        super(context);
        droid = BitmapFactory.decodeResource(getResources(), R.drawable.minibean);
        textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setColor(Color.WHITE);
        textPaint.setTextSize(24);

        animator.addUpdateListener(new AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator arg0) {
                long nowTime = System.currentTimeMillis();
                float secs = (float)(nowTime - prevTime) / 1000f;
                prevTime = nowTime;
                for (int i = 0; i < numFlakes; ++i) {
                    Flake flake = flakes.get(i);
                    flake.y += (flake.speed * secs);
                    if (flake.y > getHeight()) {
                        flake.width = (int)(10 + Math.random() * 35);
                        float hwRatio = (float)droid.getHeight() / (float)droid.getWidth();
                        flake.height = (int)(flake.width * hwRatio);
                        flake.y = 0 - flake.height;
                        flake.x =  (float)Math.random() * (getWidth() - flake.width);
                        flake.speed = 35 + (float) Math.random() * 170;
                    }
                    flake.rotation = flake.rotation + (flake.rotationSpeed * secs);
                }
                invalidate();
            }
        });
        animator.setRepeatCount(ValueAnimator.INFINITE);
        animator.setDuration(3000);
    }

    int getNumFlakes() {
        return numFlakes;
    }

    private void setNumFlakes(int quantity) {
        numFlakes = quantity;
        numFlakesString = "numFlakes: " + numFlakes;
    }

    void addFlakes(int quantity) {
        for (int i = 0; i < quantity; ++i) {
            flakes.add(Flake.createFlake(getWidth(), droid));
        }
        setNumFlakes(numFlakes + quantity);
    }

    void subtractFlakes(int quantity) {
        for (int i = 0; i < quantity; ++i) {
            int index = numFlakes - i - 1;
            flakes.remove(index);
        }
        setNumFlakes(numFlakes - quantity);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        flakes.clear();
        numFlakes = 0;
        addFlakes(16);
        animator.cancel();
        startTime = System.currentTimeMillis();
        prevTime = startTime;
        frames = 0;
        animator.start();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawColor(Color.TRANSPARENT);
        for (int i = 0; i < numFlakes; ++i) {
            Flake flake = flakes.get(i);
            m.setTranslate(-flake.width/2, -flake.height/2);
            m.postRotate(flake.rotation);
            m.postTranslate(flake.width/2 + flake.x, flake.height/2 + flake.y);
            canvas.drawBitmap(flake.bitmap, m, null);
        }
        ++frames;
        long nowTime = System.currentTimeMillis();
        long deltaTime = nowTime - startTime;
        if (deltaTime > 1000) {
            float secs = (float) deltaTime / 1000f;
            fps = (float) frames / secs;
            fpsString = "fps: " + fps;
            startTime = nowTime;
            frames = 0;
        }
    }

    public void pause() {
        animator.cancel();
    }

    public void resume() {
        animator.start();
    }

}
