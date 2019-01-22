package net.halfbolt.platformer.world;

import com.badlogic.gdx.physics.box2d.World;
import net.halfbolt.platformer.render.Render;
import net.halfbolt.platformer.world.tilemap.Tilemap;

public class MapSegment {
    private World w;
    private Tilemap map;
    private Boolean paused = false;
    private Render render;

    public MapSegment(Render render, World w) {
        this.render = render;
        this.w = w;
        map = new Tilemap(this, "levels/level0");
    }


    public World getWorld() {
        return w;
    }


    public Tilemap getTilemap() {
        return map;
    }

    public Tilemap getMap() {
        return map;
    }

    public Render getRender() {
        return render;
    }

    public void dispose() {
        w.dispose();
    }

}
