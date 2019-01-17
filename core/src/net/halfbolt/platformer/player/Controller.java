package net.halfbolt.platformer.player;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public class Controller {
    private Touchpad moveTouchpad;
    private Touchpad lanternTouchpad;
    private OrthographicCamera cam;
    private SpriteBatch batch;

    public Controller() {
        cam = new OrthographicCamera();
        cam.setToOrtho(true, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        batch = new SpriteBatch();
        batch.setProjectionMatrix(cam.combined);

        if (Gdx.app.getType() == Application.ApplicationType.Android || Gdx.app.getType() == Application.ApplicationType.iOS) {
            moveTouchpad = new Touchpad(batch,
                    "badlogic.jpg",
                    "badlogic.jpg",
                    new Vector2(Gdx.graphics.getWidth() / 8f, Gdx.graphics.getHeight() * 0.8f),
                    Gdx.graphics.getWidth() / 6f,
                    Gdx.graphics.getWidth() / 40f);
            lanternTouchpad = new Touchpad(batch,
                    "badlogic.jpg",
                    "badlogic.jpg",
                    new Vector2(Gdx.graphics.getWidth() - Gdx.graphics.getWidth() / 8f, Gdx.graphics.getHeight() * 0.8f),
                    Gdx.graphics.getWidth() / 6f,
                    Gdx.graphics.getWidth() / 40f);
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
        float speed = 0.6f;
        if (Gdx.app.getType() == Application.ApplicationType.Android || Gdx.app.getType() == Application.ApplicationType.iOS) {
            // Mobile
            return new Vector2((float) lanternTouchpad.getX() * speed, (float) lanternTouchpad.getY() * speed);
        } else {
            // Desktop
            int x = 0;
            int y = 0;
            if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
                y -= 1;
            }
            if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
                x -= 1;
            }
            if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
                y += 1;
            }
            if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
                x += 1;
            }
            return new Vector2(x, y).setLength(speed);
        }
    }

    public void touchDragged(int x, int y) {
        if (Gdx.app.getType() == Application.ApplicationType.Android || Gdx.app.getType() == Application.ApplicationType.iOS) {
            moveTouchpad.touchDragged(x, y);
        }
    }

    public void touchDown(int x, int y) {
        if (Gdx.app.getType() == Application.ApplicationType.Android || Gdx.app.getType() == Application.ApplicationType.iOS) {
            moveTouchpad.touchDown(x, y);
        }
    }

    public void touchUp(int x, int y) {
        if (Gdx.app.getType() == Application.ApplicationType.Android || Gdx.app.getType() == Application.ApplicationType.iOS) {
            moveTouchpad.touchUp(x, y);
        }
    }

    public void dispose() {
        if (Gdx.app.getType() == Application.ApplicationType.Android || Gdx.app.getType() == Application.ApplicationType.iOS) {
            moveTouchpad.dispose();
            lanternTouchpad.dispose();
        }
    }
}
