package com.weather.app.opengl;

import android.opengl.GLES30;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

/**
 * Cloud layer for the 3D weather globe
 * Semi-transparent sphere with noise-based opacity
 */
public class CloudLayer {

    private final FloatBuffer vertexBuffer;
    private final int vertexCount;
    private final int program;

    private static final String VERTEX_SHADER =
        "#version 300 es\n" +
        "uniform mat4 uMVPMatrix;\n" +
        "uniform float uTime;\n" +
        "in vec4 aPosition;\n" +
        "out vec3 vPosition;\n" +
        "void main() {\n" +
        "    vec4 pos = aPosition;\n" +
        "    // Animate clouds slightly\n" +
        "    pos.x += sin(uTime * 0.5 + pos.y * 2.0) * 0.02;\n" +
        "    gl_Position = uMVPMatrix * pos;\n" +
        "    vPosition = aPosition.xyz;\n" +
        "}\n";

    private static final String FRAGMENT_SHADER =
        "#version 300 es\n" +
        "precision mediump float;\n" +
        "uniform float uOpacity;\n" +
        "in vec3 vPosition;\n" +
        "out vec4 fragColor;\n" +
        "float noise(vec3 p) {\n" +
        "    return fract(sin(dot(p, vec3(12.9898, 78.233, 45.543))) * 43758.5453);\n" +
        "}\n" +
        "void main() {\n" +
        "    // Create cloud-like pattern\n" +
        "    float n1 = noise(vPosition * 5.0);\n" +
        "    float n2 = noise(vPosition * 10.0) * 0.5;\n" +
        "    float n3 = noise(vPosition * 20.0) * 0.25;\n" +
        "    float cloudDensity = (n1 + n2 + n3) / 1.75;\n" +
        "    cloudDensity = smoothstep(0.3, 0.7, cloudDensity);\n" +
        "    vec3 cloudColor = vec3(1.0, 1.0, 1.0);\n" +
        "    fragColor = vec4(cloudColor, cloudDensity * uOpacity * 0.6);\n" +
        "}\n";

    private float time = 0f;

    public CloudLayer(float radius, int segments, int rings) {
        // Generate sphere vertices for cloud layer
        float[] vertices = new float[(segments + 1) * (rings + 1) * 3];
        
        int index = 0;
        for (int ring = 0; ring <= rings; ring++) {
            float theta = (float) (ring * Math.PI / rings);
            float sinTheta = (float) Math.sin(theta);
            float cosTheta = (float) Math.cos(theta);
            
            for (int seg = 0; seg <= segments; seg++) {
                float phi = (float) (seg * 2 * Math.PI / segments);
                float sinPhi = (float) Math.sin(phi);
                float cosPhi = (float) Math.cos(phi);
                
                vertices[index++] = radius * cosPhi * sinTheta;
                vertices[index++] = radius * cosTheta;
                vertices[index++] = radius * sinPhi * sinTheta;
            }
        }
        
        vertexCount = vertices.length / 3;
        
        ByteBuffer bb = ByteBuffer.allocateDirect(vertices.length * 4);
        bb.order(ByteOrder.nativeOrder());
        vertexBuffer = bb.asFloatBuffer();
        vertexBuffer.put(vertices);
        vertexBuffer.position(0);
        
        // Create shader program
        int vertexShader = WeatherGlobeRenderer.loadShader(GLES30.GL_VERTEX_SHADER, VERTEX_SHADER);
        int fragmentShader = WeatherGlobeRenderer.loadShader(GLES30.GL_FRAGMENT_SHADER, FRAGMENT_SHADER);
        
        program = GLES30.glCreateProgram();
        GLES30.glAttachShader(program, vertexShader);
        GLES30.glAttachShader(program, fragmentShader);
        GLES30.glLinkProgram(program);
    }

    public void draw(float[] mvpMatrix, float opacity) {
        time += 0.016f; // ~60fps
        
        GLES30.glUseProgram(program);
        
        int mvpHandle = GLES30.glGetUniformLocation(program, "uMVPMatrix");
        int opacityHandle = GLES30.glGetUniformLocation(program, "uOpacity");
        int timeHandle = GLES30.glGetUniformLocation(program, "uTime");
        int posHandle = GLES30.glGetAttribLocation(program, "aPosition");
        
        GLES30.glUniformMatrix4fv(mvpHandle, 1, false, mvpMatrix, 0);
        GLES30.glUniform1f(opacityHandle, opacity);
        GLES30.glUniform1f(timeHandle, time);
        
        GLES30.glEnableVertexAttribArray(posHandle);
        GLES30.glVertexAttribPointer(posHandle, 3, GLES30.GL_FLOAT, false, 0, vertexBuffer);
        
        GLES30.glDrawArrays(GLES30.GL_POINTS, 0, vertexCount);
        
        GLES30.glDisableVertexAttribArray(posHandle);
    }
}
