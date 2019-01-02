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
        sb.begin();
        for (int y = 0; y < layer.getHeight(); y++) {
            for (int x = 0; x < layer.getWidth(); x++) {
                if (layer.getCell(x, y * -1 + layer.getHeight()) != null) {
                    TiledMapTile tile = layer.getCell(x, y * -1 + layer.getHeight()).getTile();
                    TextureRegion region = tile.getTextureRegion();
                    sb.draw(region, x * tileSize, y * tileSize, tileSize, tileSize);
                }
            }
        }
        sb.end();
    }
}
