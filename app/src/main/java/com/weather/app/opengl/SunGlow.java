package com.weather.app.opengl;

import android.opengl.GLES30;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

/**
 * Sun glow effect for clear weather
 * Creates a pulsating light source behind the globe
 */
public class SunGlow {

    private final FloatBuffer vertexBuffer;
    private final int program;
    private float time = 0f;

    private static final float[] QUAD_VERTICES = {
        -2f, -2f, 0f,
         2f, -2f, 0f,
        -2f,  2f, 0f,
         2f,  2f, 0f,
    };

    private static final String VERTEX_SHADER =
        "#version 300 es\n" +
        "uniform mat4 uMVPMatrix;\n" +
        "in vec4 aPosition;\n" +
        "out vec2 vCoord;\n" +
        "void main() {\n" +
        "    gl_Position = uMVPMatrix * aPosition;\n" +
        "    vCoord = aPosition.xy * 0.5 + 0.5;\n" +
        "}\n";

    private static final String FRAGMENT_SHADER =
        "#version 300 es\n" +
        "precision mediump float;\n" +
        "uniform float uTime;\n" +
        "in vec2 vCoord;\n" +
        "out vec4 fragColor;\n" +
        "void main() {\n" +
        "    vec2 center = vec2(0.5, 0.5);\n" +
        "    float dist = distance(vCoord, center);\n" +
        "    // Pulsating effect\n" +
        "    float pulse = 1.0 + sin(uTime * 2.0) * 0.1;\n" +
        "    // Sun glow gradient\n" +
        "    float glow = 1.0 - smoothstep(0.0, 0.5 * pulse, dist);\n" +
        "    glow = pow(glow, 2.0);\n" +
        "    // Sun colors (yellow to orange)\n" +
        "    vec3 sunColor = mix(\n" +
        "        vec3(1.0, 0.9, 0.3),\n" +
        "        vec3(1.0, 0.6, 0.2),\n" +
        "        dist * 2.0\n" +
        "    );\n" +
        "    fragColor = vec4(sunColor, glow * 0.5);\n" +
        "}\n";

    public SunGlow() {
        ByteBuffer bb = ByteBuffer.allocateDirect(QUAD_VERTICES.length * 4);
        bb.order(ByteOrder.nativeOrder());
        vertexBuffer = bb.asFloatBuffer();
        vertexBuffer.put(QUAD_VERTICES);
        vertexBuffer.position(0);
        
        int vertexShader = WeatherGlobeRenderer.loadShader(GLES30.GL_VERTEX_SHADER, VERTEX_SHADER);
        int fragmentShader = WeatherGlobeRenderer.loadShader(GLES30.GL_FRAGMENT_SHADER, FRAGMENT_SHADER);
        
        program = GLES30.glCreateProgram();
        GLES30.glAttachShader(program, vertexShader);
        GLES30.glAttachShader(program, fragmentShader);
        GLES30.glLinkProgram(program);
    }

    public void draw(float[] mvpMatrix) {
        time += 0.016f;
        
        GLES30.glUseProgram(program);
        
        int mvpHandle = GLES30.glGetUniformLocation(program, "uMVPMatrix");
        int timeHandle = GLES30.glGetUniformLocation(program, "uTime");
        int posHandle = GLES30.glGetAttribLocation(program, "aPosition");
        
        GLES30.glUniformMatrix4fv(mvpHandle, 1, false, mvpMatrix, 0);
        GLES30.glUniform1f(timeHandle, time);
        
        GLES30.glEnableVertexAttribArray(posHandle);
        GLES30.glVertexAttribPointer(posHandle, 3, GLES30.GL_FLOAT, false, 0, vertexBuffer);
        
        GLES30.glDrawArrays(GLES30.GL_TRIANGLE_STRIP, 0, 4);
        
        GLES30.glDisableVertexAttribArray(posHandle);
    }
}
