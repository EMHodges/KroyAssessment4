package com.mozarellabytes.kroy.InputHandlers;

/**********************************************************************************
                            Added for assessment 4
 **********************************************************************************/

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.mozarellabytes.kroy.Screens.DifficultyScreen;
import com.mozarellabytes.kroy.Utilities.DifficultyLevel;

/** Manages the inputs for the difficulty screen, such as button presses
 * for selecting a difficulty and returning to the main menu
 */
public class DifficultyScreenInputHandler implements InputProcessor {

    /**
     * Screen managing inputs for
     */
    final DifficultyScreen difficultyScreen;

    /**
     * Constructor for handler
     *
     * @param difficultyScreen  screen managing inputs for
     */
    public DifficultyScreenInputHandler(DifficultyScreen difficultyScreen) {
        this.difficultyScreen = difficultyScreen;
    }

    @Override
    public boolean keyDown(int keycode) {
        switch (keycode) {
            case Input.Keys.ESCAPE:
                Gdx.app.exit();
                System.exit(1);
                break;
            case Input.Keys.M:
                difficultyScreen.clickedSoundButton();
                difficultyScreen.changeSound();
                break;
        }
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        Vector2 clickCoordinates = new Vector2(screenX, screenY);
        Vector3 position = difficultyScreen.camera.unproject(new Vector3(clickCoordinates.x, clickCoordinates.y, 0));
        if (difficultyScreen.getEasyButton().contains(position.x, position.y))
            difficultyScreen.clickedEasyButton();
        else if (difficultyScreen.getMediumButton().contains(position.x, position.y))
            difficultyScreen.clickedMediumButton();
        else if (difficultyScreen.getHardButton().contains(position.x, position.y))
            difficultyScreen.clickedHardButton();
        else if (difficultyScreen.getReturnButton().contains(position.x, position.y))
            difficultyScreen.clickedReturnButton();
        else if (difficultyScreen.getSoundButton().contains(position.x, position.y))
            difficultyScreen.clickedSoundButton();

        return true;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        Vector2 clickCoordinates = new Vector2(screenX, screenY);
        Vector3 position = difficultyScreen.camera.unproject(new Vector3(clickCoordinates.x, clickCoordinates.y, 0));

        difficultyScreen.idleEasyButton();
        difficultyScreen.idleMediumButton();
        difficultyScreen.idleHardButton();
        difficultyScreen.idleReturnButton();
        difficultyScreen.idleSoundButton();

        if (difficultyScreen.getEasyButton().contains(position.x, position.y))
            difficultyScreen.toGameScreen(DifficultyLevel.Easy);
        else if (difficultyScreen.getMediumButton().contains(position.x, position.y))
            difficultyScreen.toGameScreen(DifficultyLevel.Medium);
        else if (difficultyScreen.getHardButton().contains(position.x, position.y))
            difficultyScreen.toGameScreen(DifficultyLevel.Hard);
        else if (difficultyScreen.getReturnButton().contains(position.x, position.y))
            difficultyScreen.toMenu();
        else if (difficultyScreen.getSoundButton().contains(position.x, position.y))
            difficultyScreen.changeSound();

        return true;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }
}
