package net.halfbolt.platformer.player;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import net.halfbolt.platformer.helper.Point;
import net.halfbolt.platformer.render.GuiRender;

public class Controller {
    private Touchpad moveTouchpad;
    private Touchpad lanternTouchpad;
    private OrthographicCamera cam;
    private SpriteBatch batch;
    private GuiRender gui;

    public Controller(GuiRender gui) {
        this.gui = gui;
        batch = gui.getBatch();
        cam = gui.getCam();

        if (Gdx.app.getType() == Application.ApplicationType.Android || Gdx.app.getType() == Application.ApplicationType.iOS) {
            moveTouchpad = new Touchpad(batch,
                    "badlogic.jpg",
                    "badlogic.jpg",
                    new Vector2(Gdx.graphics.getWidth() / 8f, Gdx.graphics.getHeight() * 0.8f),
                    Gdx.graphics.getWidth() / 6f,
                    Gdx.graphics.getWidth() / 40f,
                    new Rectangle(0, 0, Gdx.graphics.getWidth() / 2f, Gdx.graphics.getHeight()));
            lanternTouchpad = new Touchpad(batch,
                    "badlogic.jpg",
                    "badlogic.jpg",
                    new Vector2(Gdx.graphics.getWidth() - Gdx.graphics.getWidth() / 8f, Gdx.graphics.getHeight() * 0.8f),
                    Gdx.graphics.getWidth() / 6f,
                    Gdx.graphics.getWidth() / 40f,
                    new Rectangle(Gdx.graphics.getWidth() / 2f, 0, Gdx.graphics.getWidth() / 2f, Gdx.graphics.getHeight()));
        }
    }

    public void render() {
        if (Gdx.app.getType() == Application.ApplicationType.Android || Gdx.app.getType() == Application.ApplicationType.iOS) {
            moveTouchpad.render();
            lanternTouchpad.render();
        }
    }

    public Vector2 getMovementDelta() {
        float speed = 0.6f;
        if (Gdx.app.getType() == Application.ApplicationType.Android || Gdx.app.getType() == Application.ApplicationType.iOS) {
            // Mobile
            return new Vector2((float) moveTouchpad.getX() * speed, (float) moveTouchpad.getY() * speed);
        } else {
            // Desktop
            int x = 0;
            int y = 0;
            if (Gdx.input.isKeyPressed(Input.Keys.W)) {
                y -= 1;
            }
            if (Gdx.input.isKeyPressed(Input.Keys.A)) {
                x -= 1;
            }
            if (Gdx.input.isKeyPressed(Input.Keys.S)) {
                y += 1;
            }
            if (Gdx.input.isKeyPressed(Input.Keys.D)) {
                x += 1;
            }
            return new Vector2(x, y).setLength(speed);
        }
    }

    public Vector2 getLanternDelta(Vector2 pos) {
        Vector2 delta;
        if (Gdx.app.getType() == Application.ApplicationType.Android || Gdx.app.getType() == Application.ApplicationType.iOS) {
            // Mobile
            delta = new Vector2((float) lanternTouchpad.getX(), (float) lanternTouchpad.getY());
            delta.setLength(delta.len() * 80f);
        } else {
            // Desktop
            Point cursorPos = gui.getTileFromCursor();
            delta = cursorPos.toVec().sub(pos);
            delta.setLength(delta.len() * 20f);
        }
        if (delta.len() < 200) {
            return delta;
        } else {
            return delta.setLength(200f);
        }
    }

    public void touchDragged(int x, int y) {
        if (Gdx.app.getType() == Application.ApplicationType.Android || Gdx.app.getType() == Application.ApplicationType.iOS) {
            moveTouchpad.touchDragged(x, y);
            lanternTouchpad.touchDragged(x, y);
        }
    }

    public void touchDown(int x, int y) {
        if (Gdx.app.getType() == Application.ApplicationType.Android || Gdx.app.getType() == Application.ApplicationType.iOS) {
            moveTouchpad.touchDown(x, y);
            lanternTouchpad.touchDown(x, y);
        }
    }

    public void touchUp(int x, int y) {
        if (Gdx.app.getType() == Application.ApplicationType.Android || Gdx.app.getType() == Application.ApplicationType.iOS) {
            moveTouchpad.touchUp(x, y);
            lanternTouchpad.touchUp(x, y);
        }
    }

    public void dispose() {
        if (Gdx.app.getType() == Application.ApplicationType.Android || Gdx.app.getType() == Application.ApplicationType.iOS) {
            moveTouchpad.dispose();
            lanternTouchpad.dispose();
        }
    }
}
