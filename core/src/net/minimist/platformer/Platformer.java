package net.minimist.platformer;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import net.minimist.platformer.render.Render;
import net.minimist.platformer.world.World;

public class Platformer extends ApplicationAdapter {
  World world;
  Render render;

  @Override
  public void create() {
    render = new Render();
    world = render.getWorld();
    Gdx.input.setInputProcessor(new InputHandler(render));
  }

  @Override
  public void render() {
    world.update();
    render.update();
    render.render();
  }

  @Override
  public void resize(int width, int height) {
    render.resize();
  }
}
