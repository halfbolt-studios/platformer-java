package net.halfbolt.platformer.enemy;

import net.halfbolt.platformer.helper.Point;
import net.halfbolt.platformer.world.World;

public class SmallEnemy extends Enemy {
    public SmallEnemy(World w, Point pos) {
        size = 0.3f;
        speed = 120;
        maxHealth = 5;
        init(w, pos);
    }
}
