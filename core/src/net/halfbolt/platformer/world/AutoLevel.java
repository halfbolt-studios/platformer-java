package net.halfbolt.platformer.world;

import com.badlogic.gdx.physics.box2d.World;
import net.halfbolt.platformer.render.Render;

public class AutoLevel extends Level {
    public AutoLevel(Render r, String dir, World w, LevelManager manager) {
        super(r, dir, w, manager);
        // class for auto generated levels, which will set up random MapSegments next to each other
    }
}
