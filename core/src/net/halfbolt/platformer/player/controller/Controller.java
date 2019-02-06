package net.halfbolt.platformer.player.controller;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import net.halfbolt.platformer.enemy.Enemy;
import net.halfbolt.platformer.player.Player;
import net.halfbolt.platformer.render.GuiRender;
import net.halfbolt.platformer.world.LevelManager;

public class Controller {
    private final LevelManager manager;
    private Touchpad moveTouchpad;
    private Touchpad bowButton;
    private SpriteBatch batch;
    private GuiRender gui;
    private Vector2 lanternTarget;
    public Vector2 bowProjection;

    public Controller(LevelManager manager, GuiRender gui) {
        this.gui = gui;
        this.manager = manager;
        batch = gui.getBatch();
        bowProjection = new Vector2();

        if (Gdx.app.getType() == Application.ApplicationType.Android || Gdx.app.getType() == Application.ApplicationType.iOS) {
            moveTouchpad = new Touchpad(batch,
                    "badlogic.jpg",
                    "badlogic.jpg",
                    new Vector2(Gdx.graphics.getWidth() / 8f, Gdx.graphics.getHeight() * 0.8f),
                    Gdx.graphics.getWidth() / 6f,
                    Gdx.graphics.getWidth() / 40f,
                    new Rectangle(0, Gdx.graphics.getHeight() / 2f, Gdx.graphics.getWidth() / 4f, Gdx.graphics.getHeight()));
            /*ImageButton.ImageButtonStyle style = new ImageButton.ImageButtonStyle();
            style.up = new TextureRegionDrawable(new Texture("badlogic.jpg"));
            style.down = new TextureRegionDrawable(new Texture("badlogic.jpg"));
            style.checked = new TextureRegionDrawable(new Texture("badlogic.jpg"));
            bowButton = new Button(style);
            bowButton.setWidth(Gdx.graphics.getWidth() / 8f);
            bowButton.setHeight(Gdx.graphics.getWidth() / 8f);
            bowButton.setPosition(Gdx.graphics.getWidth() - Gdx.graphics.getWidth() * (3f / 16), Gdx.graphics.getHeight() - Gdx.graphics.getWidth() * (3f / 16));
            */
            //bowButton = new Button(new Vector2(Gdx.graphics.getWidth() - Gdx.graphics.getWidth() * (3f / 16), Gdx.graphics.getHeight() - Gdx.graphics.getWidth() * (3f / 16)), Gdx.graphics.getWidth() / 6f, Gdx.graphics.getWidth() / 6f, "badlogic.jpg");
            bowButton = new Touchpad(batch,
                    "badlogic.jpg",
                    "badlogic.jpg",
                    new Vector2(Gdx.graphics.getWidth() / 1.3f, Gdx.graphics.getHeight() * 0.8f),
                    Gdx.graphics.getWidth() / 6f,
                    Gdx.graphics.getWidth() / 40f,
                    new Rectangle(Gdx.graphics.getWidth() / 1.5f, Gdx.graphics.getHeight() / 2f, Gdx.graphics.getWidth() / 2f, Gdx.graphics.getHeight()));
        }
    }

    public void render() {
        if (Gdx.app.getType() == Application.ApplicationType.Android || Gdx.app.getType() == Application.ApplicationType.iOS) {
            moveTouchpad.render();
            bowButton.render();
            //get velocity of manual aim
            Player p = manager.getPlayer(0);
            Enemy closest = manager.getClosetEnemy(p.getPos());
            if ((getBowPressed() && !bowButton.autoAim()) || closest == null) {
                bowProjection.set(new Vector2((float) bowButton.getX(), (float) bowButton.getY()));
            } else {
                bowProjection.set(new Vector2((closest.getPos().x - p.getPos().x) / 7f, (closest.getPos().y - p.getPos().y) / 7f));
            }
        }
    }

    public boolean getBowPressed() {
        if (Gdx.app.getType() == Application.ApplicationType.Android || Gdx.app.getType() == Application.ApplicationType.iOS) {
            return bowButton.isPressed();
        } else {
            return Gdx.input.isButtonPressed(0);
        }
    }

    public Vector2 getBowTarget(Player p) {
        if (Gdx.app.getType() == Application.ApplicationType.Android || Gdx.app.getType() == Application.ApplicationType.iOS) {
            Enemy closest = manager.getClosetEnemy(p.getPos());
                //auto-aim
                if (closest != null && bowButton.autoAim() && bowProjection.equals(new Vector2())) {
                    return new Vector2((closest.getPos().x - p.getPos().x) / 7f, (closest.getPos().y - p.getPos().y) / 7f);
                }
                //manual aim
                return new Vector2(bowProjection);
        } else {
            return manager.getRender().getGui().getTileFromCursor();
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

    public void touchDown(int x, int y, int cursor) {
        if (Gdx.app.getType() == Application.ApplicationType.Android || Gdx.app.getType() == Application.ApplicationType.iOS) {
            moveTouchpad.touchDown(x, y, cursor);
            //bowButton.getClickListener().touchDown(new InputEvent(), x, y, cursor, 0);
            bowButton.touchDown(x, y, cursor);
            if (moveTouchpad.getCursor() != cursor) {
                lanternTarget = gui.getTileFromCursor(cursor);
            }
        }

    }

    public void touchDragged(int x, int y, int cursor) {
        if (Gdx.app.getType() == Application.ApplicationType.Android || Gdx.app.getType() == Application.ApplicationType.iOS) {
            moveTouchpad.touchDragged(x, y, cursor);
            //bowButton.getClickListener().touchDragged(new InputEvent(), x, y, cursor);
            bowButton.touchDragged(x, y, cursor);
        }
    }

    public void touchUp(int x, int y, int cursor) {
        if (Gdx.app.getType() == Application.ApplicationType.Android || Gdx.app.getType() == Application.ApplicationType.iOS) {
            moveTouchpad.touchUp(x, y, cursor);
            //bowButton.getClickListener().touchUp(new InputEvent(), x, y, cursor, 0);
            bowButton.touchUp(x, y, cursor);
        }
    }

    public void dispose() {
        if (Gdx.app.getType() == Application.ApplicationType.Android || Gdx.app.getType() == Application.ApplicationType.iOS) {
            moveTouchpad.dispose();
        }
    }
}
