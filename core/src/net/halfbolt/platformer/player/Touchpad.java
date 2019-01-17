package net.halfbolt.platformer.player;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public class Touchpad {
    private SpriteBatch batch;
    private Texture background;
    private Texture knob;
    private Vector2 knobPos;
    private Vector2 pos;
    private final Vector2 startingPos;
    private double size;
    private float knobSize;
    private final boolean fixedPos = false;

    public Touchpad(SpriteBatch batch, String backgroundPath, String knobPath, Vector2 pos, double size, float knobSize) {
        this.batch = batch;
        this.background = new Texture(backgroundPath);
        this.knob = new Texture(knobPath);
        this.pos = pos.cpy();
        this.knobPos = pos.cpy();
        this.startingPos = pos.cpy();
        this.size = size;
        this.knobSize = knobSize;
    }

    public void render() {
        batch.begin();
        batch.draw(background,
                (float) (pos.x - size / 2), (float) (pos.y - size / 2),
                (float) size, (float) size,
                0, 0,
                background.getWidth(), background.getHeight(),
                false, true);
        batch.draw(knob,
                knobPos.x, knobPos.y,
                knobSize, knobSize,
                0, 0,
                knob.getWidth(), knob.getHeight(),
                false, true);
        batch.end();
    }

    public double getX() {
        return (knobPos.x - pos.x) / size * 2;
    }

    public double getY() {
        return (knobPos.y - pos.y) / size * 2;
    }

    public void touchDragged(int x, int y) {
        if (x > Gdx.graphics.getWidth() / 2) {
            return;
        }
        double angle = Math.atan((x - pos.x) / (y - pos.y));
        double dist = Math.sqrt(Math.pow(x - pos.x, 2) + Math.pow(y - pos.y, 2));
        if (dist < size / 2) {
            knobPos.x = x;
            knobPos.y = y;
        } else {
            if (y < pos.y) {
                knobPos.x = (float) (Math.sin(angle + Math.PI) * size / 2 + pos.x);
                knobPos.y = (float) (Math.cos(angle + Math.PI) * size / 2 + pos.y);
            } else {
                knobPos.x = (float) (Math.sin(angle) * size / 2 + pos.x);
                knobPos.y = (float) (Math.cos(angle) * size / 2 + pos.y);
            }
        }
    }

    public void touchDown(int x, int y) {
        if (x > Gdx.graphics.getWidth() / 2) {
            return;
        }
        if (!fixedPos) {
            pos.x = x;
            pos.y = y;
        }
    }

    public void touchUp(int x, int y) {
        if (x > Gdx.graphics.getWidth() / 2) {
            return;
        }
        pos = startingPos.cpy();
        knobPos = pos.cpy();
    }

    public void dispose() {
        batch.dispose();
        background.dispose();
        knob.dispose();
    }
}
