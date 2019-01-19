package net.halfbolt.platformer.render;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import net.halfbolt.platformer.player.controller.Controller;

public class GuiRender {
    private final OrthographicCamera cam;
    private SpriteBatch guiBatch = new SpriteBatch();
    private BitmapFont font = new BitmapFont(true);
    private Controller control;
    private Render render;

    public GuiRender(Render render) {

        cam = new OrthographicCamera();
        resize();
        guiBatch.setProjectionMatrix(cam.combined);

        guiBatch = new SpriteBatch();
        control = new Controller(this);

        this.render = render;
    }

    public Vector2 getTileFromCursor(Vector2 mousePos) {
        Vector3 point = render.getCamera().unproject(new Vector3(mousePos.x, mousePos.y, 0));
        return new Vector2(point.x, point.y);
    }

    public Vector2 getTileFromCursor() {
        return getTileFromCursor(new Vector2(Gdx.input.getX(), Gdx.input.getY()));
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
