package net.catacombsnatch.game.core.entity.components;

import net.catacombsnatch.game.core.entity.renderers.Renderer;

import com.artemis.Component;

public class Render extends Component {
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
