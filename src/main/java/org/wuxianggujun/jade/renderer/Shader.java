package org.wuxianggujun.jade.renderer;

import org.joml.*;
import org.lwjgl.BufferUtils;

import java.io.IOException;
import java.nio.FloatBuffer;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.lwjgl.opengl.GL11.GL_FALSE;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL20.glGetShaderInfoLog;

public class Shader {

    private int shaderProgramID;
    private boolean beingUsed = false;

    private String vertexSource;
    private String fragmentSource;
    private String filepath;


    public Shader(String filepath) {
        this.filepath = filepath;
        try {
            String source = new String(Files.readAllBytes(Paths.get(filepath)));
            String[] splitString = source.split("(#type)( )+([a-zA-Z]+)");

            // Find the first pattern after #type 'vertex'
            int index = source.indexOf("#type") + 6;
            int eol = source.indexOf("\r\n", index);
            String firstPattern = source.substring(index, eol).trim();


            index = source.indexOf("#type", eol) + 6;
            eol = source.indexOf("\r\n", index);
            String secondPattern = source.substring(index, eol).trim();

            if (firstPattern.equals("vertex")) {
                vertexSource = splitString[1];

            } else if (firstPattern.equals("fragment")) {
                fragmentSource = splitString[1];
            } else {
                throw new IOException("Unknown shader type '" + firstPattern + "'");
            }


            if (secondPattern.equals("vertex")) {
                vertexSource = splitString[2];

            } else if (secondPattern.equals("fragment")) {
                fragmentSource = splitString[2];
            } else {
                throw new IOException("Unknown shader type '" + secondPattern + "'");
            }


        } catch (IOException e) {
            e.printStackTrace();
            assert false : "Error: Could not file for  shader: '" + filepath + "'";
        }
    }

    public void compile() {
        int vertexID, fragmentID;

        // Compile and link shaders

        // First load and compile the vertex shader
        vertexID = glCreateShader(GL_VERTEX_SHADER);
        // Pass the shader source to the GPU
        glShaderSource(vertexID, vertexSource);
        glCompileShader(vertexID);

        // Check for errors in compilation
        int success = glGetShaderi(vertexID, GL_COMPILE_STATUS);
        if (success == GL_FALSE) {
            int length = glGetShaderi(vertexID, GL_INFO_LOG_LENGTH);
            System.out.println("ERROR::SHADER::VERTEX::COMPILATION_FAILED\n" + glGetShaderInfoLog(vertexID, length));
            assert false : "Failed to compile vertex shader";
        }

        // First load and compile the vertex shader
        fragmentID = glCreateShader(GL_FRAGMENT_SHADER);
        // Pass the shader source to the GPU
        glShaderSource(fragmentID, fragmentSource);
        glCompileShader(fragmentID);

        // Check for errors in compilation
        success = glGetShaderi(fragmentID, GL_COMPILE_STATUS);
        if (success == GL_FALSE) {
            int length = glGetShaderi(fragmentID, GL_INFO_LOG_LENGTH);
            System.out.println("ERROR:" + filepath + glGetShaderInfoLog(fragmentID, length));
            assert false : "Failed to compile fragment shader";
        }


        // link shaders and check for errors
        shaderProgramID = glCreateProgram();
        glAttachShader(shaderProgramID, vertexID);
        glAttachShader(shaderProgramID, fragmentID);
        glLinkProgram(shaderProgramID);

        // Check for linking errors
        success = glGetProgrami(shaderProgramID, GL_LINK_STATUS);
        if (success == GL_FALSE) {
            int length = glGetProgrami(shaderProgramID, GL_INFO_LOG_LENGTH);
            System.out.println("ERROR:" + filepath + glGetProgramInfoLog(shaderProgramID, length));
            assert false : "Failed to link shader program";
        }

    }

    public void use() {
        if (!beingUsed) {
            glUseProgram(shaderProgramID);
            beingUsed = true;
        }


    }

    public void detach() {
        glUseProgram(0);
        beingUsed = false;
    }


    public void uploadMatrix4f(String name, Matrix4f matrix) {
        int varLocation = glGetUniformLocation(shaderProgramID, name);
        use();
        FloatBuffer matrixBuffer = BufferUtils.createFloatBuffer(16);
        matrix.get(matrixBuffer);
        glUniformMatrix4fv(varLocation, false, matrixBuffer);
    }

    public void uploadMatrix3f(String name, Matrix3f matrix) {
        int varLocation = glGetUniformLocation(shaderProgramID, name);
        use();
        FloatBuffer matrixBuffer = BufferUtils.createFloatBuffer(9);
        matrix.get(matrixBuffer);
        glUniformMatrix3fv(varLocation, false, matrixBuffer);
    }


    public void uploadVector4f(String varName, Vector4f vector) {
        int varLocation = glGetUniformLocation(shaderProgramID, varName);
        use();
        glUniform4f(varLocation, vector.x, vector.y, vector.z, vector.w);
    }

    public void uploadVector3f(String varName, Vector3f vector) {
        int varLocation = glGetUniformLocation(shaderProgramID, varName);
        use();
        glUniform3f(varLocation, vector.x, vector.y, vector.z);
    }

    public void uploadVector2f(String varName, Vector2f vector) {
        int varLocation = glGetUniformLocation(shaderProgramID, varName);
        use();
        glUniform2f(varLocation, vector.x, vector.y);
    }

    public void uploadFloat(String varName, float value) {
        int varLocation = glGetUniformLocation(shaderProgramID, varName);
        use();
        glUniform1f(varLocation, value);
    }

    public void uploadInt(String varName, int value) {
        int varLocation = glGetUniformLocation(shaderProgramID, varName);
        use();
        glUniform1i(varLocation, value);
    }


}
