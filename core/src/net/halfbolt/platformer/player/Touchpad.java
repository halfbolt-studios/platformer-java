package net.halfbolt.platformer.player;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public class Touchpad {
    private SpriteBatch batch;
    private Texture background;
    private Texture knob;
    private Vector2 knobPos;
    private Vector2 fixedKnobPos;
    private Vector2 pos;
    private Vector2 originalPos;
    private double size;
    private Vector2 knobSize;

    public Touchpad(SpriteBatch batch, String backgroundPath, String knobPath, Vector2 pos, double size, Vector2 knobSize) {
        this.batch = batch;
        this.background = new Texture(backgroundPath);
        this.knob = new Texture(knobPath);
        this.pos = pos.cpy();
        this.originalPos = pos.cpy();
        this.knobPos = pos.cpy();
        this.fixedKnobPos = pos.cpy();
        this.size = size;
        this.knobSize = knobSize.cpy();
    }

    public void render() {
        batch.begin();
        batch.draw(background,
                (float) (originalPos.x - size / 2), (float) (originalPos.y - size / 2),
                (float) size, (float) size,
                0, 0,
                background.getWidth(), background.getHeight(),
                false, true);
        batch.draw(knob,
                fixedKnobPos.x, fixedKnobPos.y,
                knobSize.x, knobSize.y,
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
        double angle = Math.atan((x - pos.x) / (y - pos.y));
        double dist = Math.sqrt(Math.pow(x - pos.x, 2) + Math.pow(y - pos.y, 2));
        if (dist < size / 2) {
            knobPos.x = x;
            knobPos.y = y;
            fixedKnobPos.x = originalPos.x + (x - pos.x);
            fixedKnobPos.y = originalPos.y + (y - pos.y);
        } else {
            if (y < pos.y) {
                knobPos.x = (float) (Math.sin(angle + Math.PI) * size / 2 + pos.x);
                knobPos.y = (float) (Math.cos(angle + Math.PI) * size / 2 + pos.y);
                fixedKnobPos.x = (float) (Math.sin(angle + Math.PI) * size / 2 + originalPos.x);
                fixedKnobPos.y = (float) (Math.cos(angle + Math.PI) * size / 2 + originalPos.y);
            } else {
                knobPos.x = (float) (Math.sin(angle) * size / 2 + pos.x);
                knobPos.y = (float) (Math.cos(angle) * size / 2 + pos.y);
                fixedKnobPos.x = (float) (Math.sin(angle) * size / 2 + originalPos.x);
                fixedKnobPos.y = (float) (Math.cos(angle) * size / 2 + originalPos.y);
            }
        }
    }

    public void touchDown(int x, int y) {
        touchDragged(x, y);
        pos.x = x;
        pos.y = y;
        knobPos.x = x;
        knobPos.y = y;
        fixedKnobPos.x = originalPos.x + (x - pos.x);
        fixedKnobPos.y = originalPos.y + (y - pos.y);
    }

    public void touchUp(int x, int y) {
        pos = originalPos.cpy();
        knobPos = originalPos.cpy();
        fixedKnobPos = originalPos.cpy();
    }
}
