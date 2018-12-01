package net.minimist.platformer.world;

import com.badlogic.gdx.math.Vector2;
import net.minimist.platformer.player.Controller;
import net.minimist.platformer.player.Player;
import net.minimist.platformer.world.tile.Tile;

public class World {
  private com.badlogic.gdx.physics.box2d.World w;
  private Player p;
  public World(Controller control) {
    w = new com.badlogic.gdx.physics.box2d.World(new Vector2(0, 0), true);
    Tile t = new Tile(this, 0, 0);
    p = new Player(this, control);
  }

  public void update() {
    w.step(1/60f, 6, 2);
    p.update();
  }

  public com.badlogic.gdx.physics.box2d.World getWorld() {
    return w;
  }
  public Player getPlayer() { return p; }
}
