package org.wuxianggujun.jade;

import java.awt.event.KeyEvent;

public class LevelEditorScene extends Scene {

    private boolean changingscene = false;
    private float timeToChangeScene = 2.0f;

    public LevelEditorScene() {
        System.out.println("LevelEditorScene");
    }


    @Override
    public void update(float deltaTime) {
        System.out.println("update: " + (1.0f / deltaTime)+"FPS");
        if (!changingscene && KeyListener.isKeyPressed(KeyEvent.VK_SPACE)) {
            changingscene = true;
        }

        if (changingscene && timeToChangeScene > 0) {
            timeToChangeScene -= deltaTime;
            Window.get().r -= deltaTime;
            Window.get().g -= deltaTime * 5.0f;
            Window.get().b -= deltaTime * 5.0f;

        } else if (changingscene) {
            Window.changeScene(1);
        }

    }


}
