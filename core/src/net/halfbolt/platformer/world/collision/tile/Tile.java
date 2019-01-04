package net.halfbolt.platformer.world.collision.tile;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import net.halfbolt.platformer.helper.Point;
import net.halfbolt.platformer.world.World;

public class Tile {
    private Body body;

    public Tile(World w, Point pos) {
        BodyDef groundBodyDef = new BodyDef();

        body = w.getWorld().createBody(groundBodyDef);
        body.setTransform(new Vector2(pos.getX() + 0.5f, pos.getY() + 0.5f), 0);

        PolygonShape groundBox = new PolygonShape();
        groundBox.setAsBox(0.5f, 0.5f);

        body.createFixture(groundBox, 0.0f);
        groundBox.dispose();
    }
}
