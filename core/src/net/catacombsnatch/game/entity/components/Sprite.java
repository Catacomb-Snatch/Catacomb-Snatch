package net.catacombsnatch.game.entity.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Sprite implements Component {
    public TextureRegion texture;


    public Sprite set(TextureRegion t) {
        this.texture = t;
        return this;
    }

}
