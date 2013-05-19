package net.catacombsnatch.game.core.entity.components;

import net.catacombsnatch.game.core.entity.EntityComponent;
import net.catacombsnatch.game.core.entity.renderers.Renderer;

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
