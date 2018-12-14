package net.minimist.platformer.world;

import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Vector2;
import net.minimist.platformer.world.tile.Tile;

import java.util.HashMap;

public class TileManager {
    private TiledMapTileLayer hitboxes;
    private HashMap<Vector2, Tile> tiles = new HashMap<>();

    public TileManager(World w) {
        hitboxes = w.getTilemap().getLayer(0);
        for (int y = 0; y < hitboxes.getHeight(); y++) {
            for (int x = 0; x < hitboxes.getWidth(); x++) {
                if (hitboxes.getCell(x, y * -1 + 100) != null) {
                    TiledMapTile t = hitboxes.getCell(x, y * -1 + 100).getTile();
                    tiles.put(new Vector2(x * 2, y * 2), new Tile(w, new Vector2(x * 2, y * 2)));
                }
            }
        }
    }
}
