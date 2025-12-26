package com.weather.app.opengl;

import android.content.Context;
import android.opengl.GLES30;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * OpenGL ES 3.0 Renderer for 3D Weather Globe
 * Features:
 * - Rotating Earth-like sphere
 * - Dynamic weather effects (clouds, rain, sun glow)
 * - Temperature-based color gradients
 * - Particle systems for rain/snow
 */
public class WeatherGlobeRenderer implements GLSurfaceView.Renderer {

    private final Context context;
    
    // Matrices
    private final float[] mvpMatrix = new float[16];
    private final float[] projectionMatrix = new float[16];
    private final float[] viewMatrix = new float[16];
    private final float[] modelMatrix = new float[16];
    
    // Rotation
    private float rotationX = 0f;
    private float rotationY = 0f;
    private float autoRotation = 0f;
    
    // Weather state
    private String weatherCondition = "Clear";
    private float temperature = 20f;
    private float cloudCoverage = 0f;
    
    // 3D Objects
    private GlobeSphere globe;
    private CloudLayer clouds;
    private WeatherParticles particles;
    private SunGlow sunGlow;
    
    public WeatherGlobeRenderer(Context context) {
        this.context = context;
    }

    @Override
    public void onSurfaceCreated(GL10 unused, EGLConfig config) {
        // Set clear color (transparent)
        GLES30.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        
        // Enable depth testing
        GLES30.glEnable(GLES30.GL_DEPTH_TEST);
        GLES30.glEnable(GLES30.GL_BLEND);
        GLES30.glBlendFunc(GLES30.GL_SRC_ALPHA, GLES30.GL_ONE_MINUS_SRC_ALPHA);
        
        // Initialize 3D objects
        globe = new GlobeSphere(1.0f, 48, 48);
        clouds = new CloudLayer(1.05f, 32, 32);
        particles = new WeatherParticles(500);
        sunGlow = new SunGlow();
    }

    @Override
    public void onSurfaceChanged(GL10 unused, int width, int height) {
        GLES30.glViewport(0, 0, width, height);
        
        float ratio = (float) width / height;
        Matrix.frustumM(projectionMatrix, 0, -ratio, ratio, -1, 1, 2, 10);
    }

    @Override
    public void onDrawFrame(GL10 unused) {
        // Clear the screen
        GLES30.glClear(GLES30.GL_COLOR_BUFFER_BIT | GLES30.GL_DEPTH_BUFFER_BIT);
        
        // Set camera position
        Matrix.setLookAtM(viewMatrix, 0, 
            0, 0, 4f,   // Eye position
            0, 0, 0,    // Look at center
            0, 1, 0);   // Up vector
        
        // Auto-rotate slowly
        autoRotation += 0.3f;
        
        // Apply rotations
        Matrix.setIdentityM(modelMatrix, 0);
        Matrix.rotateM(modelMatrix, 0, rotationX + autoRotation * 0.2f, 1, 0, 0);
        Matrix.rotateM(modelMatrix, 0, rotationY + autoRotation, 0, 1, 0);
        
        // Calculate MVP matrix
        float[] tempMatrix = new float[16];
        Matrix.multiplyMM(tempMatrix, 0, viewMatrix, 0, modelMatrix, 0);
        Matrix.multiplyMM(mvpMatrix, 0, projectionMatrix, 0, tempMatrix, 0);
        
        // Draw sun glow (for clear weather)
        if (weatherCondition.contains("Clear") || weatherCondition.contains("Sunny")) {
            sunGlow.draw(mvpMatrix);
        }
        
        // Draw the globe with temperature colors
        float[] globeColor = getTemperatureColor(temperature);
        globe.draw(mvpMatrix, globeColor);
        
        // Draw clouds
        if (cloudCoverage > 0 || weatherCondition.contains("Cloud")) {
            float[] cloudMatrix = new float[16];
            Matrix.setIdentityM(cloudMatrix, 0);
            Matrix.rotateM(cloudMatrix, 0, autoRotation * 0.5f, 0, 1, 0);
            
            float[] cloudMvp = new float[16];
            Matrix.multiplyMM(tempMatrix, 0, viewMatrix, 0, cloudMatrix, 0);
            Matrix.multiplyMM(cloudMvp, 0, projectionMatrix, 0, tempMatrix, 0);
            
            clouds.draw(cloudMvp, Math.max(cloudCoverage / 100f, 0.5f));
        }
        
        // Draw weather particles
        if (weatherCondition.contains("Rain") || weatherCondition.contains("Shower")) {
            particles.setType(WeatherParticles.TYPE_RAIN);
            particles.draw(mvpMatrix);
        } else if (weatherCondition.contains("Snow")) {
            particles.setType(WeatherParticles.TYPE_SNOW);
            particles.draw(mvpMatrix);
        }
    }
    
    private float[] getTemperatureColor(float temp) {
        // Temperature-based color gradient
        // Hot (>30°C) = Orange/Red
        // Warm (20-30°C) = Yellow/Green  
        // Cold (<10°C) = Blue
        
        float r, g, b;
        
        if (temp > 30) {
            r = 1.0f;
            g = 0.4f;
            b = 0.2f;
        } else if (temp > 20) {
            float t = (temp - 20) / 10f;
            r = 0.3f + t * 0.7f;
            g = 0.7f - t * 0.3f;
            b = 0.4f - t * 0.2f;
        } else if (temp > 10) {
            float t = (temp - 10) / 10f;
            r = 0.2f + t * 0.1f;
            g = 0.5f + t * 0.2f;
            b = 0.7f - t * 0.3f;
        } else {
            r = 0.2f;
            g = 0.5f;
            b = 0.9f;
        }
        
        return new float[]{r, g, b, 1.0f};
    }

    // Getters and setters
    public float getRotationX() { return rotationX; }
    public float getRotationY() { return rotationY; }
    
    public void setRotation(float rotY, float rotX) {
        this.rotationY = rotY;
        this.rotationX = Math.max(-30, Math.min(30, rotX)); // Limit vertical rotation
    }
    
    public void setWeatherCondition(String condition) {
        this.weatherCondition = condition;
    }
    
    public void setTemperature(float temp) {
        this.temperature = temp;
    }
    
    public void setCloudCoverage(float coverage) {
        this.cloudCoverage = coverage;
    }
    
    // Utility method for compiling shaders
    public static int loadShader(int type, String shaderCode) {
        int shader = GLES30.glCreateShader(type);
        GLES30.glShaderSource(shader, shaderCode);
        GLES30.glCompileShader(shader);
        return shader;
    }
}
