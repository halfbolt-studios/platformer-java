package net.minimist.platformer.world.tile;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import net.minimist.platformer.world.World;

public class Tile {
  private Body body;
  public Tile(World w, Vector2 pos) {
    BodyDef groundBodyDef = new BodyDef();

    body = w.getWorld().createBody(groundBodyDef);
    body.setTransform(pos, 0);

    PolygonShape groundBox = new PolygonShape();
    groundBox.setAsBox(1, 1);

    body.createFixture(groundBox, 0.0f);
    groundBox.dispose();
  }

  public void render() {

  }
}
