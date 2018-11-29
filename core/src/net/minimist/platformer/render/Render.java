package net.minimist.platformer.render;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import net.minimist.platformer.world.World;

public class Render {
  private World w;
  private Box2DDebugRenderer debugRenderer = new Box2DDebugRenderer();
  private OrthographicCamera cam;
  public Render(World w) {
    this.w = w;
    cam = new OrthographicCamera();
    cam.setToOrtho(true, 60, 60 * ((float) Gdx.graphics.getHeight() / Gdx.graphics.getWidth()));

    cam.position.set(cam.viewportWidth / 2f, cam.viewportHeight / 2f, 0);
  }

  public void render() {
    cam.update();

    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    debugRenderer.render(w.getWorld(), cam.combined);
  }

  public void resize() {
    cam.setToOrtho(true, 60, 60 * ((float) Gdx.graphics.getHeight() / Gdx.graphics.getWidth()));
  }
}
