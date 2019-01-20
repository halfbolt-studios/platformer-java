package net.halfbolt.platformer.enemy;

import net.halfbolt.platformer.helper.Point;
import net.halfbolt.platformer.world.World;

public class TankEnemy extends Enemy {
    public TankEnemy(World w, Point pos) {
        size = 1f;
        speed = 60;
        maxHealth = 20;
        init(w, pos);
    }
}
