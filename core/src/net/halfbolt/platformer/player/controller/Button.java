package net.halfbolt.platformer.player.controller;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

//This is a very basic button class, we don't have to use it, but I think that it might be useful for later buttons

public class Button {
    private Vector2 position;
    private float width;
    private float height;
    private Texture backgroundTex;
    private String status;

    public Button (Vector2 position, float width, float height, String backgroundPath) {
        this.position = position;
        this.width = width;
        this.height = height;
        this.backgroundTex = new Texture(backgroundPath);
        status = "NONE";
    }

    public void render (SpriteBatch batch) {
        batch.begin();
        batch.draw(backgroundTex,
                (float) (position.x - width / 2), (float) (position.y - height / 2),
                (float) width, (float) height,
                0, 0,
                backgroundTex.getWidth(), backgroundTex.getHeight(),
                false, true);
        batch.end();
    }

    public boolean isPressed () {
        return (status.equals("DOWN") || status.equals("DRAGGED"));
    }

    public void touchDown(int x, int y, int cursor) {
        if (inBounds(x, y)) {
            status = "DOWN";
        }
    }

    public void touchDragged(int x, int y, int cursor) {
        if (inBounds(x, y)) {
            status = "DRAGGED";
        }
    }

    public void touchUp(int x, int y, int cursor) {
        status = "UP";
    }

    private boolean inBounds (int x, int y) {
        return (x >= position.x && x <= position.x + width && y >= position.y && y <= position.y + height);
    }
}
