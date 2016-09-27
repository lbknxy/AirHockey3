package com.arcsoft.firstopenglproject.util;

import android.support.v4.util.LogWriter;
import android.util.Log;

import static android.opengl.GLES20.*;

/**
 * Created by Administrator on 2016/9/27.
 */
public class Shaderhelper {

    private static final String TAG = "Shaderhelper";

    public static int compileVertexShader(String shaderCode){
        return compileShader(GL_VERTEX_SHADER,shaderCode);
    }

    public static int compileFragmentShader(String shaderCode){
        return compileShader(GL_FRAGMENT_SHADER,shaderCode);
    }

    private static int compileShader(int type,String shaderCode){

        final int shaderObjectId = glCreateShader(type);

        if(shaderObjectId==0){
            LoggerConfig.w(TAG,"create sahder failed.");

            return 0;
        }

        glShaderSource(shaderObjectId,shaderCode);

        glCompileShader(shaderObjectId);

        final int[] compileStatue = new int[1];
        glGetShaderiv(shaderObjectId,GL_COMPILE_STATUS,compileStatue,0);

        LoggerConfig.i(TAG,"Results of compile resource : "+ type + " \n" +
                "  " + glGetShaderInfoLog(shaderObjectId)
        );


        if(compileStatue[0]==0){
            glDeleteShader(shaderObjectId);

            LoggerConfig.w(TAG,"compilation of shader failed");

            return 0;
        }

        return shaderObjectId;
    }

    public static int linkProgram(int vertexShaderId, int fragmentShaderId){

        final int programeObjectId = glCreateProgram();

        if(programeObjectId==0){
            LoggerConfig.w(TAG,"Could not create new Program");

            return 0;
        }

        glAttachShader(programeObjectId,vertexShaderId);
        glAttachShader(programeObjectId,fragmentShaderId);

        glLinkProgram(programeObjectId);

        final int[] linkStatus= new int[1];

        glGetProgramiv(programeObjectId,GL_LINK_STATUS,linkStatus,0);

        if(linkStatus[0]==0){
            glDeleteProgram(programeObjectId);

            LoggerConfig.w(TAG,"linking of program failed");

            return 0;
        }

        return programeObjectId;
    }

    public static boolean validateProgram(int programId){

        glValidateProgram(programId);

        final int[] validateStatus = new int[1];

        glGetProgramiv(programId,GL_VALIDATE_STATUS,validateStatus,0);

        LoggerConfig.i(TAG,"Results of validating program : " + validateStatus[0] +"\n"+
            "and Log : " + glGetProgramInfoLog(programId)
        );

        return validateStatus[0]!=0;
    }
}
