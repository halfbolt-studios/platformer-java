package net.halfbolt.platformer.enemy;

import net.halfbolt.platformer.helper.Point;
import net.halfbolt.platformer.world.World;

public class SmallEnemy extends Enemy {
    public SmallEnemy(Point pos, World w) {
        size = 0.2f;
        speed = 150;
        this.w = w;
        createBody(pos);
    }
}
