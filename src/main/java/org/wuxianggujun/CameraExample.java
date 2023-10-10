package org.wuxianggujun;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWCursorPosCallback;
import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import org.lwjgl.system.MemoryStack;

import java.nio.DoubleBuffer;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.system.MemoryStack.stackPush;
import static org.lwjgl.system.MemoryUtil.NULL;

public class CameraExample {
    private long window;
    private float cameraX, cameraY, cameraZ;
    private float cameraPitch, cameraYaw;
    private final float CAMERA_SPEED = 0.05f;
    private final float MOUSE_SENSITIVITY = 0.1f;
    private double lastMouseX, lastMouseY;
    private boolean firstMouse = true;

    public void run() {
        init();
        loop();

        // Cleanup
        GLFW.glfwDestroyWindow(window);

        GLFW.glfwTerminate();
    }

    private void init() {
        if (!GLFW.glfwInit()) {
            throw new IllegalStateException("Unable to initialize GLFW");
        }

        GLFW.glfwDefaultWindowHints();
        GLFW.glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
        GLFW.glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 3);
        GLFW.glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);

        window = GLFW.glfwCreateWindow(800, 600, "Camera Example", NULL, NULL);
        if (window == NULL) {
            throw new RuntimeException("Failed to create the GLFW window");
        }

        GLFW.glfwMakeContextCurrent(window);
        GLFW.glfwSwapInterval(1);
        GLFW.glfwShowWindow(window);

        GL.createCapabilities();

        GLFW.glfwSetKeyCallback(window, new GLFWKeyCallback() {
            @Override
            public void invoke(long window, int key, int scancode, int action, int mods) {
                if (key == GLFW_KEY_ESCAPE && action == GLFW_PRESS) {
                    GLFW.glfwSetWindowShouldClose(window, true);
                }
            }
        });

        GLFW.glfwSetCursorPosCallback(window, new GLFWCursorPosCallback() {
            @Override
            public void invoke(long window, double xpos, double ypos) {
                if (firstMouse) {
                    lastMouseX = xpos;
                    lastMouseY = ypos;
                    firstMouse = false;
                }

                float xOffset = (float) (xpos - lastMouseX) * MOUSE_SENSITIVITY;
                float yOffset = (float) (lastMouseY - ypos) * MOUSE_SENSITIVITY;

                lastMouseX = xpos;
                lastMouseY = ypos;

                cameraYaw += xOffset;
                cameraPitch += yOffset;

                if (cameraPitch > 89.0f) {
                    cameraPitch = 89.0f;
                }
                if (cameraPitch < -89.0f) {
                    cameraPitch = -89.0f;
                }
            }
        });
    }

    private void loop() {
        GL11.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);

        while (!GLFW.glfwWindowShouldClose(window)) {
            processInput();
            updateCamera();

            GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);

            // Set up your game rendering here
            // ...

            GLFW.glfwSwapBuffers(window);
            GLFW.glfwPollEvents();
        }
    }

    private void processInput() {
        if (GLFW.glfwGetKey(window, GLFW_KEY_W) == GLFW_PRESS) {
            cameraX += CAMERA_SPEED * Math.sin(Math.toRadians(cameraYaw));
            cameraZ -= CAMERA_SPEED * Math.cos(Math.toRadians(cameraYaw));
        }

        if (GLFW.glfwGetKey(window, GLFW_KEY_S) == GLFW_PRESS) {
            cameraX -= CAMERA_SPEED * Math.sin(Math.toRadians(cameraYaw));
            cameraZ += CAMERA_SPEED * Math.cos(Math.toRadians(cameraYaw));
        }

        if (GLFW.glfwGetKey(window, GLFW_KEY_A) == GLFW_PRESS) {
            cameraX += CAMERA_SPEED * Math.sin(Math.toRadians(cameraYaw - 90));
            cameraZ -= CAMERA_SPEED * Math.cos(Math.toRadians(cameraYaw - 90));
        }

        if (GLFW.glfwGetKey(window, GLFW_KEY_D) == GLFW_PRESS) {
            cameraX += CAMERA_SPEED * Math.sin(Math.toRadians(cameraYaw + 90));
            cameraZ -= CAMERA_SPEED * Math.cos(Math.toRadians(cameraYaw + 90));
        }

        if (GLFW.glfwGetKey(window, GLFW_KEY_SPACE) == GLFW_PRESS) {
            cameraY += CAMERA_SPEED;
        }

        if (GLFW.glfwGetKey(window, GLFW_KEY_LEFT_SHIFT) == GLFW_PRESS) {
            cameraY -= CAMERA_SPEED;
        }
    }

    private void updateCamera() {
        // Set up your camera transformation here
        // You can use OpenGL matrices or custom matrix transformations
        // ...
    }

    public static void main(String[] args) {
        new CameraExample().run();
    }
}
