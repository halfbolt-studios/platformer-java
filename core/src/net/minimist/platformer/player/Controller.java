package net.minimist.platformer.player;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public class Controller {
  private Touchpad touchpad;
  private OrthographicCamera cam;
  private SpriteBatch batch;
  public Controller() {
    cam = new OrthographicCamera();
    cam.setToOrtho(true, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

    batch = new SpriteBatch();
    batch.setProjectionMatrix(cam.combined);

    if (Gdx.app.getType() == Application.ApplicationType.Android) {
      touchpad = new Touchpad(batch,
              "badlogic.jpg",
              "badlogic.jpg",
              new Vector2(Gdx.graphics.getWidth() / 8f, Gdx.graphics.getHeight() - Gdx.graphics.getHeight() / 4f),
              Gdx.graphics.getWidth() / 4f,
              new Vector2(Gdx.graphics.getWidth() / 40f, Gdx.graphics.getWidth() / 40f));
    }
  }

  public void render() {
    touchpad.render();
  }

  public Vector2 getMovementDelta() {
    float speed = 5f;
    if (Gdx.app.getType() == Application.ApplicationType.Android || Gdx.app.getType() == Application.ApplicationType.iOS) {
      // Mobile
      return new Vector2((float) touchpad.getX() * speed, (float) touchpad.getY() * speed);
    } else {
      // Desktop
      return new Vector2(0, 0);
    }
  }

  public void touchDragged(int x, int y) {
    touchpad.touchDragged(x, y);
  }

  public void touchDown(int x, int y) {
    touchpad.touchDown(x, y);
  }

  public void touchUp(int x, int y) {
    touchpad.touchUp(x, y);
  }
}
