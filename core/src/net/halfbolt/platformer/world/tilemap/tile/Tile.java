package net.halfbolt.platformer.world.tilemap.tile;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import net.halfbolt.platformer.helper.Point;
import net.halfbolt.platformer.world.World;

import java.util.ArrayList;

public class Tile {
    private final TextureRegion region;
    private final int id;
    private Body body;
    private final int rot;
    private final Point pos;

    public Tile(World w, Point pos, TextureRegion region, int id, int rot) {
        this.region = region;
        this.id = id;
        this.rot = rot;
        this.pos = pos;
        ArrayList<Vector2> verts = new ArrayList<>();
        if (id == 0) { // square
            verts.add(pos.add(new Point(0, 0)).toVec()); verts.add(pos.add(new Point(1, 0)).toVec());
            verts.add(pos.add(new Point(0, 1)).toVec()); verts.add(pos.add(new Point(1, 1)).toVec());
        } else if (id == 1) { // square
            if (rot == 0) {
                verts.add(pos.add(new Point(0, 0)).toVec());
                verts.add(pos.add(new Point(0, 1)).toVec()); verts.add(pos.add(new Point(1, 1)).toVec());
            } else if (rot == 1) {
                verts.add(pos.add(new Point(0, 0)).toVec()); verts.add(pos.add(new Point(1, 0)).toVec());
                verts.add(pos.add(new Point(0, 1)).toVec());
            } else if (rot == 2) {
                verts.add(pos.add(new Point(0, 0)).toVec()); verts.add(pos.add(new Point(1, 0)).toVec());
                                                                    verts.add(pos.add(new Point(1, 1)).toVec());
            } else if (rot == 3) {
                                                                    verts.add(pos.add(new Point(1, 0)).toVec());
                verts.add(pos.add(new Point(0, 1)).toVec()); verts.add(pos.add(new Point(1, 1)).toVec());
            } else {
                throw(new RuntimeException("Rotation of tile invalid! " + rot));
            }
        } else {
            return;
        }

        BodyDef groundBodyDef = new BodyDef();
        body = w.getWorld().createBody(groundBodyDef);
        //Divided by 500 to get hit-boxes in right place
        body.setTransform(new Vector2(pos.toVec().x / 500, pos.toVec().y / 500), 0);
        //body.setTransform(new Vector2(pos.toVec()), 0);


        PolygonShape groundBox = new PolygonShape();
        System.out.println("Verts: " + verts);
        groundBox.set(verts.toArray(new Vector2[0]));
        body.createFixture(groundBox, 0.0f);
        groundBox.dispose();
    }

    public TextureRegion getTextureRegion() {
        return region;
    }

    public int getID() {
        return id;
    }

    public int getRotation() {
        return rot;
    }

    @Override
    public String toString() {
        return "Tile{" +
                "region=" + region +
                ", id=" + id +
                ", body=" + body +
                ", rot=" + rot +
                ", pos=" + pos +
                '}';
    }
}
