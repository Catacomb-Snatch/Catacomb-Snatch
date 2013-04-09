package net.catacombsnatch.game.core.scene.scenes;

import net.catacombsnatch.game.core.resource.Art;
import net.catacombsnatch.game.core.resource.Language;
import net.catacombsnatch.game.core.resource.options.OptionGroup;
import net.catacombsnatch.game.core.scene.MenuScene;
import net.catacombsnatch.game.core.scene.ReusableAction;
import net.catacombsnatch.game.core.scene.SceneManager;

import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

public class OptionsScene extends MenuScene {
	
	public OptionsScene() {
		super(Art.pyramid);
		
		addTextButton(Language.get("general.back"), 0, 0).addAction(new ReusableAction() {
			@Override
			public boolean use(float delta) {
				SceneManager.exit();
				return true;
			}
		});
		
		init(null);
	}
	
	public void init(final OptionGroup group) {
		if(group != null) for (String k : group.getKeys()) {
			final String key = group.getCurrentPath() + k;
			final Object o = group.get(key);
			
			if (o instanceof OptionGroup) {
				addTextButton(Language.get("option." + key), 0, 0).addAction(new ReusableAction() {
					@Override
					public boolean use(float delta) {
						OptionsScene scene = SceneManager.switchTo(OptionsScene.class);
						scene.init((OptionGroup) o);
						return true;
					}
				});
				
			} else if (o instanceof Boolean) {
				final boolean b = (Boolean) o;
				final TextButton button = addTextButton(Language.get("option." + key + (b ? ".disable" : ".enable")), 0, 0);
				
				button.addAction(new ReusableAction() {
					protected boolean down = b;
					
					@Override
					public boolean use(float delta) {
						down = !down;
						group.set(key, down);
						button.setText(Language.get("option." + key + (down ? ".disable" : ".enable")));
						
						return true;
					}
				});
			}
			
			// TODO add missing types
		}
		
		super.init();
	}

	@Override
	public void render(float delta) {
		super.render(delta);
		
		drawCharacter();
	}
	
}
