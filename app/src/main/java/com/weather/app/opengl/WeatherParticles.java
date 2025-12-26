package com.weather.app.opengl;

import android.opengl.GLES30;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.Random;

/**
 * Weather particle system for rain and snow effects
 */
public class WeatherParticles {

    public static final int TYPE_RAIN = 0;
    public static final int TYPE_SNOW = 1;

    private final FloatBuffer vertexBuffer;
    private final int particleCount;
    private final int program;
    private final float[] positions;
    private final float[] velocities;
    private int particleType = TYPE_RAIN;
    private final Random random = new Random();

    private static final String VERTEX_SHADER =
        "#version 300 es\n" +
        "uniform mat4 uMVPMatrix;\n" +
        "uniform float uPointSize;\n" +
        "in vec4 aPosition;\n" +
        "out float vAlpha;\n" +
        "void main() {\n" +
        "    gl_Position = uMVPMatrix * aPosition;\n" +
        "    gl_PointSize = uPointSize;\n" +
        "    // Fade based on Y position\n" +
        "    vAlpha = smoothstep(-1.5, 1.5, aPosition.y);\n" +
        "}\n";

    private static final String FRAGMENT_SHADER_RAIN =
        "#version 300 es\n" +
        "precision mediump float;\n" +
        "in float vAlpha;\n" +
        "out vec4 fragColor;\n" +
        "void main() {\n" +
        "    vec2 coord = gl_PointCoord - vec2(0.5);\n" +
        "    // Rain drop shape (elongated)\n" +
        "    float dist = length(coord * vec2(1.0, 0.3));\n" +
        "    float alpha = 1.0 - smoothstep(0.0, 0.5, dist);\n" +
        "    fragColor = vec4(0.6, 0.8, 1.0, alpha * vAlpha * 0.7);\n" +
        "}\n";

    private static final String FRAGMENT_SHADER_SNOW =
        "#version 300 es\n" +
        "precision mediump float;\n" +
        "in float vAlpha;\n" +
        "out vec4 fragColor;\n" +
        "void main() {\n" +
        "    vec2 coord = gl_PointCoord - vec2(0.5);\n" +
        "    // Snowflake shape (circular with soft edge)\n" +
        "    float dist = length(coord);\n" +
        "    float alpha = 1.0 - smoothstep(0.2, 0.5, dist);\n" +
        "    fragColor = vec4(1.0, 1.0, 1.0, alpha * vAlpha * 0.8);\n" +
        "}\n";

    private int programRain;
    private int programSnow;

    public WeatherParticles(int count) {
        this.particleCount = count;
        this.positions = new float[count * 3];
        this.velocities = new float[count * 3];
        
        // Initialize particles in random positions
        for (int i = 0; i < count; i++) {
            resetParticle(i);
            positions[i * 3 + 1] = random.nextFloat() * 3f - 1.5f; // Random Y start
        }
        
        ByteBuffer bb = ByteBuffer.allocateDirect(positions.length * 4);
        bb.order(ByteOrder.nativeOrder());
        vertexBuffer = bb.asFloatBuffer();
        
        // Create rain program
        int vertexShader = WeatherGlobeRenderer.loadShader(GLES30.GL_VERTEX_SHADER, VERTEX_SHADER);
        int fragmentShaderRain = WeatherGlobeRenderer.loadShader(GLES30.GL_FRAGMENT_SHADER, FRAGMENT_SHADER_RAIN);
        int fragmentShaderSnow = WeatherGlobeRenderer.loadShader(GLES30.GL_FRAGMENT_SHADER, FRAGMENT_SHADER_SNOW);
        
        programRain = GLES30.glCreateProgram();
        GLES30.glAttachShader(programRain, vertexShader);
        GLES30.glAttachShader(programRain, fragmentShaderRain);
        GLES30.glLinkProgram(programRain);
        
        programSnow = GLES30.glCreateProgram();
        GLES30.glAttachShader(programSnow, vertexShader);
        GLES30.glAttachShader(programSnow, fragmentShaderSnow);
        GLES30.glLinkProgram(programSnow);
        
        program = programRain;
    }

    private void resetParticle(int i) {
        // Random position in a sphere around the globe
        float angle = random.nextFloat() * (float) (2 * Math.PI);
        float radius = 1.2f + random.nextFloat() * 0.3f;
        
        positions[i * 3] = (float) Math.cos(angle) * radius + random.nextFloat() * 0.2f - 0.1f;
        positions[i * 3 + 1] = 1.5f + random.nextFloat() * 0.5f; // Start above
        positions[i * 3 + 2] = (float) Math.sin(angle) * radius + random.nextFloat() * 0.2f - 0.1f;
        
        // Velocity (falling down)
        if (particleType == TYPE_RAIN) {
            velocities[i * 3] = random.nextFloat() * 0.01f - 0.005f;
            velocities[i * 3 + 1] = -0.05f - random.nextFloat() * 0.03f; // Fast fall
            velocities[i * 3 + 2] = random.nextFloat() * 0.01f - 0.005f;
        } else {
            velocities[i * 3] = random.nextFloat() * 0.02f - 0.01f;
            velocities[i * 3 + 1] = -0.01f - random.nextFloat() * 0.01f; // Slow fall
            velocities[i * 3 + 2] = random.nextFloat() * 0.02f - 0.01f;
        }
    }

    public void setType(int type) {
        if (this.particleType != type) {
            this.particleType = type;
            // Reset all particles for new type
            for (int i = 0; i < particleCount; i++) {
                resetParticle(i);
            }
        }
    }

    public void draw(float[] mvpMatrix) {
        // Update particle positions
        for (int i = 0; i < particleCount; i++) {
            positions[i * 3] += velocities[i * 3];
            positions[i * 3 + 1] += velocities[i * 3 + 1];
            positions[i * 3 + 2] += velocities[i * 3 + 2];
            
            // Reset if fallen below
            if (positions[i * 3 + 1] < -1.5f) {
                resetParticle(i);
            }
            
            // Add some wind sway for snow
            if (particleType == TYPE_SNOW) {
                velocities[i * 3] += (random.nextFloat() - 0.5f) * 0.002f;
                velocities[i * 3 + 2] += (random.nextFloat() - 0.5f) * 0.002f;
            }
        }
        
        vertexBuffer.put(positions);
        vertexBuffer.position(0);
        
        int prog = particleType == TYPE_RAIN ? programRain : programSnow;
        GLES30.glUseProgram(prog);
        
        int mvpHandle = GLES30.glGetUniformLocation(prog, "uMVPMatrix");
        int sizeHandle = GLES30.glGetUniformLocation(prog, "uPointSize");
        int posHandle = GLES30.glGetAttribLocation(prog, "aPosition");
        
        GLES30.glUniformMatrix4fv(mvpHandle, 1, false, mvpMatrix, 0);
        GLES30.glUniform1f(sizeHandle, particleType == TYPE_RAIN ? 8.0f : 12.0f);
        
        GLES30.glEnableVertexAttribArray(posHandle);
        GLES30.glVertexAttribPointer(posHandle, 3, GLES30.GL_FLOAT, false, 0, vertexBuffer);
        
        GLES30.glDrawArrays(GLES30.GL_POINTS, 0, particleCount);
        
        GLES30.glDisableVertexAttribArray(posHandle);
    }
}
