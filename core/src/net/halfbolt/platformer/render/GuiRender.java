package net.halfbolt.platformer.render;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import net.halfbolt.platformer.player.Controller;

public class GuiRender {
    private final OrthographicCamera cam;
    private SpriteBatch guiBatch = new SpriteBatch();
    private BitmapFont font = new BitmapFont(true);
    private Controller control;

    public GuiRender(Render render) {

        cam = new OrthographicCamera();
        resize();
        guiBatch.setProjectionMatrix(cam.combined);

        guiBatch = new SpriteBatch();
        control = new Controller(this);
    }

    public void render() {
        guiBatch.setProjectionMatrix(cam.combined);
        guiBatch.begin();
        font.draw(guiBatch, "" + Gdx.graphics.getFramesPerSecond(), Gdx.graphics.getWidth() - 20, 20);
        guiBatch.end();

        control.render();
    }

    public void dispose() {
        guiBatch.dispose();
        control.dispose();
    }

    public void touchUp(int x, int y) {
        control.touchUp(x, y);
    }

    public void touchDown(int x, int y) {
        control.touchDown(x, y);
    }

    public void touchDragged(int x, int y) {
        control.touchDragged(x, y);
    }

    public Controller getControl() {
        return control;
    }

    public SpriteBatch getBatch() {
        return guiBatch;
    }

    public OrthographicCamera getCam() {
        return cam;
    }

    public void resize() {
        cam.setToOrtho(true, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    }
}
