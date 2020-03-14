package com.mozarellabytes.kroy.Utilities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.mozarellabytes.kroy.Entities.Fortress;
import com.mozarellabytes.kroy.Entities.Patrol;
import com.mozarellabytes.kroy.GUI.GUI;
import com.mozarellabytes.kroy.Screens.GameScreen;

/**
 * Manages inputs for GameScreen. Controls many aspects such as:
 * - Clicking buttons in top left
 * - Clicking on entities
 * - Drawing truck path
 * - Toggling auto attack
 * - Key binds for buttons
 * - Freezing game
 * - Undo/Redo segments of truck path
 */
public class GameInputHandler implements InputProcessor {

    /** The game screen that this input handler controls */
    private final GameScreen gameScreen;

    /** The graphical user interface - contains the buttons */
    private final GUI gui;

    /** Constructs the GameInputHandler
     *
     * @param gameScreen The game screen that this input handler controls
     * @param gui The graphical user interface - contains the buttons
     */
    public GameInputHandler(GameScreen gameScreen, GUI gui) {
        this.gameScreen = gameScreen;
        this.gui = gui;
    }

    /** Called when a key was pressed
     *
     * This handles toggling sound, the control screen, the pause
     * screen and makes the fire trucks attack a fortress that is
     * within it's range
     *
     * @param keycode one of the constants in {@link Input.Keys}
     * @return whether the input was processed */
    @Override
    public boolean keyDown(int keycode) {
        switch (keycode) {
            case Input.Keys.ESCAPE:
                gui.clickedPauseButton();
                gameScreen.changeState(GameScreen.PlayState.PAUSE);
                break;
            case Input.Keys.C:
                gameScreen.toControlScreen();
                break;
            case Input.Keys.M:
                gui.clickedSoundButton();
                gui.changeSound();
                gui.idleSoundButton();
                break;
            case Input.Keys.S:
                gameScreen.saveGameState();
                break;
            case Input.Keys.SPACE:
                if (gameScreen.isNotPaused()) gameScreen.changeState(GameScreen.PlayState.FREEZE);
                break;
            case Input.Keys.A:
                if (gameScreen.isNotPaused()) gameScreen.toggleTruckAttack();
                break;
            case Input.Keys.LEFT:
                if (gameScreen.selectedTruck != null) gameScreen.selectedTruck.undoSegment();
                break;
            case Input.Keys.RIGHT:
                if (gameScreen.selectedTruck != null) gameScreen.selectedTruck.redoSegment();
        }
        return true;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    /** Checks whether the user clicks on a firetruck, fortress, button or the end
     * of a firetrucks path
     * @param screenX The x coordinate, origin is in the upper left corner
     * @param screenY The y coordinate, origin is in the upper left corner
     * @param pointer the pointer for the event.
     * @param button the button
     * @return whether the input was processed */
    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        Vector2 clickCoordinates = generateClickCoordinates(screenX, screenY);
        if (gameScreen.isNotPaused()) {
            if (gameScreen.checkClick(clickCoordinates)) {
                gameScreen.selectedTruck.resetPath();
                gameScreen.selectedTruck.addTileToPathSegment(clickCoordinates);
            } else if (!gameScreen.checkTrailClick(clickCoordinates) &&
                    !checkFortressClick(clickCoordinates) &&
                    !checkPatrolClick(clickCoordinates) &&
                    !checkFireStationClick(clickCoordinates)) {
                gameScreen.selectedTruck = null;
                gameScreen.setSelectedEntity(null);
            }
        } else {
            checkFortressClick(clickCoordinates);
            checkPatrolClick(clickCoordinates);
            checkFireStationClick(clickCoordinates);
        }
        checkButtonClick(new Vector2(screenX, Gdx.graphics.getHeight() - screenY));
        return true;
    }

    /** The user draws a path for the fire truck, if the path is valid the coordinate
     * positions are added to the trucks path
     * @param pointer the pointer for the event.
     * @return whether the input was processed */
    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        if (gameScreen.isNotPaused()) {
            if (gameScreen.selectedTruck != null) {
                Vector2 clickCoordinates = generateClickCoordinates(screenX, screenY);
                gameScreen.selectedTruck.addTileToPathSegment(clickCoordinates);
            }
        }
        return true;
    }

    /** Check if a user clicks up on a button or if the user draws multiple
     * trucks to end on the same tile
     *
     * @param pointer the pointer for the event.
     * @param button the button
     * @return whether the input was processed */
    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        if (gameScreen.isNotPaused()) {
            if (gameScreen.selectedTruck != null) {
                if (!gameScreen.selectedTruck.pathSegment.isEmpty()) {
                    if (gameScreen.getStation().doTrucksHaveSameLastTile()) {
                        gameScreen.shortenActiveSegment();
                    }
                    if (gameScreen.selectedTruck.canPathSegmentBeAddedToRoute()) {
                        gameScreen.selectedTruck.addPathSegmentToRoute();
                        if (gameScreen.getState().equals(GameScreen.PlayState.PLAY))
                            gameScreen.selectedTruck.generatePathFromLastSegments();
                    }
                }
            }
        }
        checkButtonUnclick(screenX, screenY);
        return true;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }

    /** Maps the position of where the user clicked on the screen to the tile that they clicked on
     *
     * @param screenX The x coordinate, origin is in the upper left corner
     * @param screenY The y coordinate, origin is in the upper left corner
     * @return a Vector2 containing the tile that the user clicked on
     */
    private Vector2 generateClickCoordinates(int screenX, int screenY){
        Vector2 clickCoordinates = new Vector2(screenX, screenY);
        Vector3 position = gameScreen.getCamera().unproject(new Vector3(clickCoordinates.x, clickCoordinates.y, 0));
        return new Vector2((int) position.x, (int) position.y);
    }

    /** Checks if the user clicked on the home, pause or sound button
     * and changes the sprite accordingly
     * @param position2d The tile that was clicked
     */
    private void checkButtonClick(Vector2 position2d){
        if (gui.getHomeButton().contains(position2d)) gui.clickedHomeButton();
        else if (gui.getPauseButton().contains(position2d)) gui.clickedPauseButton();
        else if (gui.getSoundButton().contains(position2d)) gui.clickedSoundButton();
        else if (gui.getInfoButton().contains(position2d)) gui.clickedInfoButton();
        else if (gui.getSaveButton().contains(position2d)) gui.clickedSaveButton();
    }

    /** Checks if user clicked on a fortress, if it did this fortress
     * becomes the selected entity meaning its stats will be rendered
     * in the top left hand corner
     *
     * @param position2d the tile that was clicked
     * @return <code> true </code> If a fortress has been clicked on
     *         <code> false </code> Otherwise
     */
    private boolean checkFortressClick(Vector2 position2d) {
        for (Fortress fortress : gameScreen.getFortresses()) {
            if (fortress.getArea().contains(position2d)) {
                gameScreen.setSelectedEntity(fortress);
                return true;
            }
        }
        gameScreen.selectedTruck = null;
        gameScreen.setSelectedEntity(null);
        return false;
    }

    /** Checks if user clicked on the fire station, if it did it
     * becomes the selected entity meaning its stats will be rendered
     * in the top left hand corner
     *
     * @param position2d the tile that was clicked
     * @return <code> true </code> If the fire station has been clicked on
     *         <code> false </code> Otherwise
     */
    private boolean checkFireStationClick(Vector2 position2d) {
        if (gameScreen.getStation().getArea().contains(position2d)) {
            gameScreen.setSelectedEntity(gameScreen.getStation());
            return true;
        }
        return false;
    }

    /** Checks if user clicked on a patrol, if it did this patrol
     * becomes the selected entity meaning its stats will be rendered
     * in the top left hand corner
     *
     * @param position2d the tile that was clicked
     * @return <code> true </code> If a fortress has been clicked on
     *         <code> false </code> Otherwise
     */
    private boolean checkPatrolClick(Vector2 position2d) {
        if (gameScreen.getBossPatrol() != null && gameScreen.getBossPatrol().getRoundedPosition().dst(position2d) < 1) {
            gameScreen.setSelectedEntity(gameScreen.getBossPatrol());
            return true;
        }
        for (Patrol patrol : gameScreen.getPatrols()) {
            if (patrol.getPosition().dst(position2d) < 1) {
                gameScreen.setSelectedEntity(patrol);
                return true;
            }
        }
        gameScreen.selectedTruck = null;
        gameScreen.setSelectedEntity(null);
        return false;
    }

    /** Checks if the user has lifted the mouse over a button and triggers the
     * appropriate action
     * @param screenX The x coordinate, origin is in the upper left corner
     * @param screenY The y coordinate, origin is in the upper left corner
     */
    private void checkButtonUnclick(int screenX, int screenY){
        Vector2 screenCoords = new Vector2(screenX, Gdx.graphics.getHeight() - screenY);

        gui.idleHomeButton();
        gui.idleSoundButton();
        gui.idlePauseButton();
        gui.idleInfoButton();
        gui.idleSaveButton();

        if (gui.getHomeButton().contains(screenCoords)) gameScreen.toHomeScreen();
        if (gui.getSoundButton().contains(screenCoords)) gui.changeSound();
        if (gui.getPauseButton().contains(screenCoords)) gameScreen.changeState(GameScreen.PlayState.PAUSE);
        if (gui.getSaveButton().contains(screenCoords)) gameScreen.saveGameState();
        if (gui.getInfoButton().contains(screenCoords)) gameScreen.toControlScreen();

    }
}