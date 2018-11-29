package net.minimist.platformer.world.tile;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import net.minimist.platformer.world.World;

public class Tile {
  public Tile(World w, int x, int y) {
    BodyDef groundBodyDef = new BodyDef();

    Body b1 = w.getWorld().createBody(groundBodyDef);
    b1.setTransform(new Vector2(10, 30), (float) (20f / 180f * Math.PI));

    Body b2 = w.getWorld().createBody(groundBodyDef);
    b2.setTransform(new Vector2(50, 30), (float) (-20f / 180f * Math.PI));

    PolygonShape groundBox = new PolygonShape();
    groundBox.setAsBox(1, 1);

    b1.createFixture(groundBox, 0.0f);
    b2.createFixture(groundBox, 0.0f);
    groundBox.dispose();
  }
}
