package net.halfbolt.platformer.world.tile;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import net.halfbolt.platformer.world.World;

public class TileRender {
    private World w;
    private SpriteBatch sb;
    public TileRender(World w, SpriteBatch sb) {
        this.w = w;
        this.sb = sb;
    }

    public void render(int i, float tileSize) {
        TiledMapTileLayer layer = w.getTilemap().getLayer(i);
        if (!layer.isVisible()){
            return;
        }
        sb.begin();
        for (int y = 0; y < layer.getHeight(); y++) {
            for (int x = 0; x < layer.getWidth(); x++) {
                TiledMapTileLayer.Cell cell = layer.getCell(x, layer.getHeight() - y);
                if (cell != null) {
                    TiledMapTile tile = cell.getTile();
                    TextureRegion region = tile.getTextureRegion();
                    sb.draw(region, x * tileSize, y * tileSize, tileSize, tileSize);
                }
            }
        }
        sb.end();
    }
}
