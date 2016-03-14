package net.catacombsnatch.game.entity.components;

import net.catacombsnatch.game.entity.EntityComponent;
import net.catacombsnatch.game.entity.renderers.Renderer;

public class Render extends EntityComponent {
	protected Renderer renderer;
	
	public Render(Renderer renderer) {
		setRenderer(renderer);
	}
	
	public Renderer getRenderer() {
		return renderer;
	}
	
	public final void setRenderer(Renderer renderer) {
		this.renderer = renderer;
	}

}
