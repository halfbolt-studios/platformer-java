package net.halfbolt.platformer.world.collision;

import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Vector2;
import net.halfbolt.platformer.helper.Point;
import net.halfbolt.platformer.world.World;
import net.halfbolt.platformer.world.collision.tile.Tile;

import java.util.HashMap;

public class TileManager {
    private TiledMapTileLayer hitboxes;
    private HashMap<Point, Tile> tiles = new HashMap<>();

    public TileManager(World w) {
        hitboxes = w.getTilemap().getLayer(0);
        for (int y = 0; y < hitboxes.getHeight(); y++) {
            for (int x = 0; x < hitboxes.getWidth(); x++) {
                TiledMapTileLayer.Cell cell = hitboxes.getCell(x, y * -1 + hitboxes.getHeight());
                if (cell != null) {
                    TiledMapTile t = cell.getTile();
                    tiles.put(new Point(x, y), new Tile(w, new Point(x, y)));
                }
            }
        }
    }

    public Tile get(Point pos) {
        return tiles.get(pos);
    }

    public HashMap<Point, Tile> getMap() {
        return tiles;
    }
}
