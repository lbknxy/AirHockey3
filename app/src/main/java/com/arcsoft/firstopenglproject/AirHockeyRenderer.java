package com.arcsoft.firstopenglproject;

import android.content.Context;
import android.opengl.GLSurfaceView;

import com.arcsoft.firstopenglproject.util.Shaderhelper;
import com.arcsoft.firstopenglproject.util.TextResourceReader;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import android.opengl.Matrix;
import android.opengl.Matrix.*;
import static android.opengl.GLES20.GL_COLOR_BUFFER_BIT;
import static android.opengl.GLES20.GL_FLOAT;
import static android.opengl.GLES20.GL_LINES;
import static android.opengl.GLES20.GL_LINE_LOOP;
import static android.opengl.GLES20.GL_POINTS;
import static android.opengl.GLES20.GL_TRIANGLES;
import static android.opengl.GLES20.GL_TRIANGLE_FAN;
import static android.opengl.GLES20.glClear;
import static android.opengl.GLES20.glClearColor;
import static android.opengl.GLES20.glDrawArrays;
import static android.opengl.GLES20.glEnableVertexAttribArray;
import static android.opengl.GLES20.glGetAttribLocation;
import static android.opengl.GLES20.glGetUniformLocation;
import static android.opengl.GLES20.glUniform4f;
import static android.opengl.GLES20.glUniformMatrix4fv;
import static android.opengl.GLES20.glUseProgram;
import static android.opengl.GLES20.glVertexAttribPointer;
import static android.opengl.GLES20.glViewport;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created by Administrator on 2016/9/26.
 */
public class AirHockeyRenderer implements GLSurfaceView.Renderer {

    private static final int POSITION_COMPONENT_COUNT = 4;

    private static final int BYTE_PER_FLOAT = 4;

    private FloatBuffer vertexData;

    private Context context;

    private int program;
    private static final String U_COLOR = "u_Color";
    private int uColorLocation;

    private static final String A_POSITION = "a_Position";
    private static final String A_COLOR = "a_Color";

    private static final String U_MATRIX = "u_Matrix";

    private final float[] projectionMatrix = new float[16];

    private static int uMatrixLocation;


    private static final int COLOR_COMPONENT_COUNT = 3;
    private static final int STRIDE = (POSITION_COMPONENT_COUNT + COLOR_COMPONENT_COUNT) *BYTE_PER_FLOAT;

    private int aPositionLocation;
    private int aColorLocation;

    public AirHockeyRenderer(Context context) {

        this.context = context;

        float[] tableVertices = {

                // Triangle Fan
                0f,    0f, 0f, 1.5f,   1f,   1f,   1f,
                -0.5f, -0.8f, 0f,   1f, 0.7f, 0.7f, 0.7f,
                0.5f, -0.8f, 0f,   1f, 0.7f, 0.7f, 0.7f,
                0.5f,  0.8f, 0f,   2f, 0.7f, 0.7f, 0.7f,
                -0.5f,  0.8f, 0f,   2f, 0.7f, 0.7f, 0.7f,
                -0.5f, -0.8f, 0f,   1f, 0.7f, 0.7f, 0.7f,

                // Line 1
                -0.5f, 0f, 0f, 1.5f, 1f, 0f, 0f,
                0.5f, 0f, 0f, 1.5f, 1f, 0f, 0f,

                // Mallets
                0f, -0.4f, 0f, 1.25f, 0f, 0f, 1f,
                0f,  0.4f, 0f, 1.75f, 1f, 0f, 0f

        };

        vertexData = ByteBuffer.allocateDirect(tableVertices.length * BYTE_PER_FLOAT).
                order(ByteOrder.nativeOrder()).asFloatBuffer();

        vertexData.put(tableVertices);
    }


    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        glClearColor(0.0f, 0.0f, 0.0f, 0.0f);

        String vertexShaderSource = TextResourceReader.readTextFileFromResource(context, R.raw.sample_vertex_shader);
        String fragmentShaderSource = TextResourceReader.readTextFileFromResource(context, R.raw.sample_fragment_shader);

        int vertexShader = Shaderhelper.compileVertexShader(vertexShaderSource);
        int fragmentShader = Shaderhelper.compileFragmentShader(fragmentShaderSource);

        program = Shaderhelper.linkProgram(vertexShader,fragmentShader);

        Shaderhelper.validateProgram(program);


        glUseProgram(program);

        aPositionLocation = glGetAttribLocation(program,A_POSITION);
        aColorLocation = glGetAttribLocation(program,A_COLOR);

        uMatrixLocation = glGetUniformLocation(program,U_MATRIX);

        vertexData.position(0);
        glVertexAttribPointer(aPositionLocation,POSITION_COMPONENT_COUNT,GL_FLOAT,false,STRIDE,vertexData);
        glEnableVertexAttribArray(aPositionLocation);

        vertexData.position(POSITION_COMPONENT_COUNT);
        glVertexAttribPointer(aColorLocation,COLOR_COMPONENT_COUNT,GL_FLOAT,false,STRIDE,vertexData);
        glEnableVertexAttribArray(aColorLocation);

    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        glViewport(0, 0, width, height);

        final float aspectRatio = width > height ?
                (float) width / (float) height :
                (float) height / (float) width;

        if(width>height){
            Matrix.orthoM(projectionMatrix,0,-aspectRatio,aspectRatio,-1f,1f,-1f,1f);
        }else{
            Matrix.orthoM(projectionMatrix,0,-1f,1f,-aspectRatio,aspectRatio,-1f,1f);
        }
    }

    @Override
    public void onDrawFrame(GL10 gl) {

        glUniformMatrix4fv(uMatrixLocation,1,false,projectionMatrix,0);

        // Draw the table.
        glDrawArrays(GL_TRIANGLE_FAN, 0, 6);

        // Draw the center dividing line.
        glDrawArrays(GL_LINES, 6, 2);

        // Draw the first mallet.
        glDrawArrays(GL_POINTS, 8, 1);

        // Draw the second mallet.
        glDrawArrays(GL_POINTS, 9, 1);

    }
}
