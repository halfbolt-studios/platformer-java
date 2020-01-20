package net.halfbolt.platformer.world;

import java.util.HashMap;
import java.util.Map;
import net.halfbolt.platformer.enemy.Enemy;
import net.halfbolt.platformer.player.Player;
import net.halfbolt.platformer.player.bow.Arrow;
import net.halfbolt.platformer.world.tilemap.tile.Tile;

public class CollisionBits {
    private static final Map<Class<?>, Short> bits = new HashMap<>();

    static {
        bits.put(Tile.class,   (short) 0b1000);
        bits.put(Enemy.class,  (short) 0b0100);
        bits.put(Player.class, (short) 0b0010);
        bits.put(Arrow.class,  (short) 0b0001);
    }

    public static short getBits(Class<?> c) {
        return bits.get(c);
    }
}
