package com.weather.app.opengl;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * 3D Weather Globe Surface View
 * Uses OpenGL ES 3.0 for stunning weather visualization
 */
public class WeatherGlobeView extends GLSurfaceView {

    private WeatherGlobeRenderer renderer;
    private float previousX;
    private float previousY;
    private static final float TOUCH_SCALE_FACTOR = 0.5f;

    public WeatherGlobeView(Context context) {
        super(context);
        init(context);
    }

    public WeatherGlobeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        // Use OpenGL ES 3.0
        setEGLContextClientVersion(3);
        
        // Enable transparency
        setEGLConfigChooser(8, 8, 8, 8, 16, 0);
        getHolder().setFormat(android.graphics.PixelFormat.TRANSLUCENT);
        setZOrderOnTop(true);
        
        // Create renderer
        renderer = new WeatherGlobeRenderer(context);
        setRenderer(renderer);
        
        // Render only when dirty (for battery efficiency)
        setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        float x = e.getX();
        float y = e.getY();

        switch (e.getAction()) {
            case MotionEvent.ACTION_MOVE:
                float dx = x - previousX;
                float dy = y - previousY;
                
                // Rotate the globe
                renderer.setRotation(
                    renderer.getRotationY() + dx * TOUCH_SCALE_FACTOR,
                    renderer.getRotationX() + dy * TOUCH_SCALE_FACTOR
                );
                requestRender();
                break;
        }

        previousX = x;
        previousY = y;
        return true;
    }

    /**
     * Set the current weather condition for visualization
     */
    public void setWeatherCondition(String condition) {
        if (renderer != null) {
            renderer.setWeatherCondition(condition);
        }
    }

    /**
     * Set temperature for color gradient
     */
    public void setTemperature(float temperature) {
        if (renderer != null) {
            renderer.setTemperature(temperature);
        }
    }

    /**
     * Set cloud coverage percentage
     */
    public void setCloudCoverage(float coverage) {
        if (renderer != null) {
            renderer.setCloudCoverage(coverage);
        }
    }
}
