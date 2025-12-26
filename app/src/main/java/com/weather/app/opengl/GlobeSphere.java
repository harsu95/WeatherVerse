package com.weather.app.opengl;

import android.opengl.GLES30;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

/**
 * 3D Sphere for the weather globe
 * Uses procedural generation with latitude/longitude
 */
public class GlobeSphere {

    private final FloatBuffer vertexBuffer;
    private final ShortBuffer indexBuffer;
    private final int indexCount;
    private final int program;

    private static final String VERTEX_SHADER =
        "#version 300 es\n" +
        "uniform mat4 uMVPMatrix;\n" +
        "in vec4 aPosition;\n" +
        "in vec3 aNormal;\n" +
        "out vec3 vNormal;\n" +
        "out vec3 vPosition;\n" +
        "void main() {\n" +
        "    gl_Position = uMVPMatrix * aPosition;\n" +
        "    vNormal = aNormal;\n" +
        "    vPosition = aPosition.xyz;\n" +
        "}\n";

    private static final String FRAGMENT_SHADER =
        "#version 300 es\n" +
        "precision mediump float;\n" +
        "uniform vec4 uColor;\n" +
        "in vec3 vNormal;\n" +
        "in vec3 vPosition;\n" +
        "out vec4 fragColor;\n" +
        "void main() {\n" +
        "    vec3 lightDir = normalize(vec3(1.0, 1.0, 1.0));\n" +
        "    float diff = max(dot(normalize(vNormal), lightDir), 0.2);\n" +
        "    vec3 ambient = uColor.rgb * 0.3;\n" +
        "    vec3 diffuse = uColor.rgb * diff * 0.7;\n" +
        "    // Add grid lines\n" +
        "    float lat = asin(vPosition.y) * 12.0;\n" +
        "    float lon = atan(vPosition.z, vPosition.x) * 12.0;\n" +
        "    float grid = step(0.95, fract(lat)) + step(0.95, fract(lon));\n" +
        "    vec3 gridColor = mix(ambient + diffuse, vec3(0.5, 0.8, 1.0), grid * 0.3);\n" +
        "    fragColor = vec4(gridColor, uColor.a);\n" +
        "}\n";

    public GlobeSphere(float radius, int latSegments, int lonSegments) {
        // Generate sphere vertices
        float[] vertices = new float[(latSegments + 1) * (lonSegments + 1) * 6]; // position + normal
        short[] indices = new short[latSegments * lonSegments * 6];
        
        int vIndex = 0;
        for (int lat = 0; lat <= latSegments; lat++) {
            float theta = (float) (lat * Math.PI / latSegments);
            float sinTheta = (float) Math.sin(theta);
            float cosTheta = (float) Math.cos(theta);
            
            for (int lon = 0; lon <= lonSegments; lon++) {
                float phi = (float) (lon * 2 * Math.PI / lonSegments);
                float sinPhi = (float) Math.sin(phi);
                float cosPhi = (float) Math.cos(phi);
                
                float x = cosPhi * sinTheta;
                float y = cosTheta;
                float z = sinPhi * sinTheta;
                
                // Position
                vertices[vIndex++] = radius * x;
                vertices[vIndex++] = radius * y;
                vertices[vIndex++] = radius * z;
                
                // Normal
                vertices[vIndex++] = x;
                vertices[vIndex++] = y;
                vertices[vIndex++] = z;
            }
        }
        
        int iIndex = 0;
        for (int lat = 0; lat < latSegments; lat++) {
            for (int lon = 0; lon < lonSegments; lon++) {
                int first = lat * (lonSegments + 1) + lon;
                int second = first + lonSegments + 1;
                
                indices[iIndex++] = (short) first;
                indices[iIndex++] = (short) second;
                indices[iIndex++] = (short) (first + 1);
                
                indices[iIndex++] = (short) second;
                indices[iIndex++] = (short) (second + 1);
                indices[iIndex++] = (short) (first + 1);
            }
        }
        
        indexCount = indices.length;
        
        // Create vertex buffer
        ByteBuffer bb = ByteBuffer.allocateDirect(vertices.length * 4);
        bb.order(ByteOrder.nativeOrder());
        vertexBuffer = bb.asFloatBuffer();
        vertexBuffer.put(vertices);
        vertexBuffer.position(0);
        
        // Create index buffer
        ByteBuffer ib = ByteBuffer.allocateDirect(indices.length * 2);
        ib.order(ByteOrder.nativeOrder());
        indexBuffer = ib.asShortBuffer();
        indexBuffer.put(indices);
        indexBuffer.position(0);
        
        // Create shader program
        int vertexShader = WeatherGlobeRenderer.loadShader(GLES30.GL_VERTEX_SHADER, VERTEX_SHADER);
        int fragmentShader = WeatherGlobeRenderer.loadShader(GLES30.GL_FRAGMENT_SHADER, FRAGMENT_SHADER);
        
        program = GLES30.glCreateProgram();
        GLES30.glAttachShader(program, vertexShader);
        GLES30.glAttachShader(program, fragmentShader);
        GLES30.glLinkProgram(program);
    }

    public void draw(float[] mvpMatrix, float[] color) {
        GLES30.glUseProgram(program);
        
        // Get handles
        int mvpHandle = GLES30.glGetUniformLocation(program, "uMVPMatrix");
        int colorHandle = GLES30.glGetUniformLocation(program, "uColor");
        int posHandle = GLES30.glGetAttribLocation(program, "aPosition");
        int normalHandle = GLES30.glGetAttribLocation(program, "aNormal");
        
        // Set MVP matrix
        GLES30.glUniformMatrix4fv(mvpHandle, 1, false, mvpMatrix, 0);
        
        // Set color
        GLES30.glUniform4fv(colorHandle, 1, color, 0);
        
        // Enable vertex arrays
        GLES30.glEnableVertexAttribArray(posHandle);
        GLES30.glEnableVertexAttribArray(normalHandle);
        
        // Set vertex data
        vertexBuffer.position(0);
        GLES30.glVertexAttribPointer(posHandle, 3, GLES30.GL_FLOAT, false, 24, vertexBuffer);
        
        vertexBuffer.position(3);
        GLES30.glVertexAttribPointer(normalHandle, 3, GLES30.GL_FLOAT, false, 24, vertexBuffer);
        
        // Draw
        GLES30.glDrawElements(GLES30.GL_TRIANGLES, indexCount, GLES30.GL_UNSIGNED_SHORT, indexBuffer);
        
        // Disable vertex arrays
        GLES30.glDisableVertexAttribArray(posHandle);
        GLES30.glDisableVertexAttribArray(normalHandle);
    }
}
