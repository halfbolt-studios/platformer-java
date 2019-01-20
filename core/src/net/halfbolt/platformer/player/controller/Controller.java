package net.halfbolt.platformer.player.controller;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import net.halfbolt.platformer.render.GuiRender;

public class Controller {
    private Touchpad moveTouchpad;
    private Button bowButton;
    private SpriteBatch batch;
    private GuiRender gui;
    private Vector2 lanternTarget;

    public Controller(GuiRender gui) {
        this.gui = gui;
        batch = gui.getBatch();

        if (Gdx.app.getType() == Application.ApplicationType.Android || Gdx.app.getType() == Application.ApplicationType.iOS) {
            moveTouchpad = new Touchpad(batch,
                    "badlogic.jpg",
                    "badlogic.jpg",
                    new Vector2(Gdx.graphics.getWidth() / 8f, Gdx.graphics.getHeight() * 0.8f),
                    Gdx.graphics.getWidth() / 6f,
                    Gdx.graphics.getWidth() / 40f,
                    new Rectangle(0, Gdx.graphics.getHeight() / 2f, Gdx.graphics.getWidth() / 2f, Gdx.graphics.getHeight()));
            ImageButton.ImageButtonStyle style = new ImageButton.ImageButtonStyle();
            style.up = new TextureRegionDrawable(new Texture("badlogic.jpg"));
            style.down = new TextureRegionDrawable(new Texture("badlogic.jpg"));
            style.checked = new TextureRegionDrawable(new Texture("badlogic.jpg"));
            bowButton = new Button(style);
        }
    }

    public void render() {
        if (Gdx.app.getType() == Application.ApplicationType.Android || Gdx.app.getType() == Application.ApplicationType.iOS) {
            moveTouchpad.render();
            batch.begin();
            bowButton.draw(batch, 1);
            batch.end();
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
        } else {
            // Desktop
            if (Gdx.input.isButtonPressed(1)) {
                lanternTarget = gui.getTileFromCursor();
            }
        }
        if (lanternTarget == null) {
            return new Vector2();
        }
        delta = lanternTarget.cpy().sub(pos);
        delta.setLength(delta.len() * 20f);
        if (delta.len() < 40) {
            return delta;
        } else {
            return delta.setLength(40f);
        }
    }

    public void touchDragged(int x, int y, int cursor) {
        if (Gdx.app.getType() == Application.ApplicationType.Android || Gdx.app.getType() == Application.ApplicationType.iOS) {
            moveTouchpad.touchDragged(x, y, cursor);
        }
    }

    public void touchDown(int x, int y, int cursor) {
        if (Gdx.app.getType() == Application.ApplicationType.Android || Gdx.app.getType() == Application.ApplicationType.iOS) {
            moveTouchpad.touchDown(x, y, cursor);
            if (moveTouchpad.getCursor() != cursor) {
                lanternTarget = gui.getTileFromCursor(cursor);
            }
        }

    }

    public void touchUp(int x, int y, int cursor) {
        if (Gdx.app.getType() == Application.ApplicationType.Android || Gdx.app.getType() == Application.ApplicationType.iOS) {
            moveTouchpad.touchUp(x, y, cursor);
        }
    }

    public void dispose() {
        if (Gdx.app.getType() == Application.ApplicationType.Android || Gdx.app.getType() == Application.ApplicationType.iOS) {
            moveTouchpad.dispose();
        }
    }
}
