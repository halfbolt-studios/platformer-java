package net.minimist.platformer;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import net.minimist.platformer.render.Render;
import net.minimist.platformer.world.World;

public class Platformer extends ApplicationAdapter {
  World world;
  Render render;

  @Override
  public void create() {
    world = new World();
    render = new Render(world);
  }

  @Override
  public void render() {
    world.update();
    render.render();
  }

  @Override
  public void resize(int width, int height) {
    render.resize();
  }
}
