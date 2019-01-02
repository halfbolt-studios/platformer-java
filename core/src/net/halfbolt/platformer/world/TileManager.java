package net.halfbolt.platformer.world;

import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Vector2;
import net.halfbolt.platformer.world.tile.Tile;

import java.util.HashMap;

public class TileManager {
    private TiledMapTileLayer hitboxes;
    private HashMap<Vector2, Tile> tiles = new HashMap<>();

    public TileManager(World w) {
        hitboxes = w.getTilemap().getLayer(0);
        for (int y = 0; y < hitboxes.getHeight(); y++) {
            for (int x = 0; x < hitboxes.getWidth(); x++) {
                TiledMapTileLayer.Cell cell = hitboxes.getCell(x, y * -1 + hitboxes.getHeight());
                if (cell != null) {
                    TiledMapTile t = cell.getTile();
                    tiles.put(new Vector2(x, y), new Tile(w, new Vector2(x, y)));
                }
            }
        }
    }
}
