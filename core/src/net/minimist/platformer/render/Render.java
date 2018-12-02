package net.minimist.platformer.render;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import net.minimist.platformer.player.Controller;
import net.minimist.platformer.world.World;
import org.jetbrains.annotations.NotNull;

public class Render {
  private World w;
  private Box2DDebugRenderer debugRenderer = new Box2DDebugRenderer();
  private OrthographicCamera cam;
  private SpriteBatch batch;
  private Controller control;
  public Render() {
    cam = new OrthographicCamera();
    cam.setToOrtho(true, 20, 20 * ((float) Gdx.graphics.getHeight() / Gdx.graphics.getWidth()));
    cam.position.set(cam.viewportWidth / 2f, cam.viewportHeight / 2f, 0);

    batch = new SpriteBatch();
    batch.setProjectionMatrix(cam.combined);

    control = new Controller();
    this.w = new World(control);
  }

  public void render() {
    cam.update();
    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

    // For debugging box2d
    debugRenderer.render(w.getWorld(), cam.combined);

    w.render();
    w.getPlayer().render();
    control.render();
  }

  public void update() {
    float borderX = (float) Gdx.graphics.getWidth() / 2.5f;
    float borderY = (float) Gdx.graphics.getHeight() / 2.5f;
    Vector3 pos = cam.project(new Vector3(w.getPlayer().getPos().x, w.getPlayer().getPos().y, 0));
    float div = 200f;
    if (pos.x < borderX) {
      cam.position.set(cam.position.x + (pos.x - borderX) / div, cam.position.y, 0);
    }
    if (pos.x > Gdx.graphics.getWidth() - borderX) {
      cam.position.set(cam.position.x + (pos.x - Gdx.graphics.getWidth() + borderX) / div, cam.position.y, 0);
    }
    if (pos.y < borderY) {
      cam.position.set(cam.position.x, cam.position.y - (pos.y - borderY) / div, 0);
    }
    if (pos.y > Gdx.graphics.getHeight() - borderY) {
      cam.position.set(cam.position.x, cam.position.y - (pos.y - Gdx.graphics.getHeight() + borderY) / div, 0);
    }
  }

  public void resize() {
    cam.setToOrtho(true, 100, 100 * ((float) Gdx.graphics.getHeight() / Gdx.graphics.getWidth()));
    batch.setProjectionMatrix(cam.combined);
  }

  public World getWorld() {
    return w;
  }

  private void drawTex(@NotNull SpriteBatch batch, Texture tex, @NotNull Vector2 pos, @NotNull Vector2 size) {
    batch.draw(tex, pos.x, pos.y, size.x, size.y, 0, 0, tex.getWidth(), tex.getHeight(), false, true);
  }

  public void touchDragged(int x, int y) {
    control.touchDragged(x, y);
  }

  public void touchDown(int x, int y) {
    control.touchDown(x, y);
  }

  public void touchUp(int x, int y) {
    control.touchUp(x, y);
  }
}
