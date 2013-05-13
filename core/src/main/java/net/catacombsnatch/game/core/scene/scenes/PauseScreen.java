package net.catacombsnatch.game.core.scene.scenes;

import net.catacombsnatch.game.core.Game;
import net.catacombsnatch.game.core.resource.Language;
import net.catacombsnatch.game.core.scene.MenuScene;
import net.catacombsnatch.game.core.scene.ReusableAction;
import net.catacombsnatch.game.core.scene.SceneManager;
import net.catacombsnatch.game.core.screen.Screen;

import com.badlogic.gdx.graphics.Color;

public class PauseScreen extends MenuScene {
	
	public PauseScreen() {
		super(Screen.createBlank(new Color(0f, 0f, 0f, 0.25f)));
		
		addTextButton(Language.get("scene.pause.title"), 0, 0).addAction(new ReusableAction() {
			@Override
			public boolean use(float delta) {
				SceneManager.switchTo(TitleScreen.class, true);
				return true;
			}
		});
		
		addTextButton(Language.get("scene.pause.options"), 0, 0).addAction(new ReusableAction() {
			@Override
			public boolean use(float delta) {
				SceneManager.switchTo(OptionsScene.class).init(Game.options);
				return true;
			}
		});
		
		addTextButton(Language.get("scene.pause.resume"), 0, 0).addAction(new ReusableAction() {
			@Override
			public boolean use(float delta) {
				SceneManager.exit();
				return true;
			}
		});
		
		init();
	}

	@Override
	public void tick(float delta) {
		super.tick(delta);
		
		drawCharacter();
	}
	
}
