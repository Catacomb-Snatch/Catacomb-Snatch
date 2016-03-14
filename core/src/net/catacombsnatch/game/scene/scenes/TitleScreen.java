package net.catacombsnatch.game.scene.scenes;

import net.catacombsnatch.game.CatacombSnatch;
import net.catacombsnatch.game.resource.Art;
import net.catacombsnatch.game.resource.Language;
import net.catacombsnatch.game.scene.MenuScene;
import net.catacombsnatch.game.scene.ReusableAction;
import net.catacombsnatch.game.scene.Scene;
import net.catacombsnatch.game.scene.SceneManager;
import net.catacombsnatch.game.screen.Screen;

import com.badlogic.gdx.Gdx;

public class TitleScreen extends MenuScene {
	
	public TitleScreen() {
		super(Art.pyramid);

		addTextButton(Language.get("scene.title.exit"), 0, 0).addAction(new ReusableAction() {
			@Override
			public boolean use(float delta) {
				Gdx.app.exit();
				return true;
			}
		});
		
		addTextButton(Language.get("scene.title.options"), 0, 0).addAction(new ReusableAction() {
			@Override
			public boolean use(float delta) {
				OptionsScene scene = SceneManager.switchTo(OptionsScene.class, false);
				scene.init(CatacombSnatch.options);
				return true;
			}
		});
		addTextButton(Language.get("scene.title.start"), 0, 0).setDisabled(true);
		
		addTextButton(Language.get("scene.title.demo"), 0, 0).addAction(new ReusableAction() {
			@Override
			public boolean use(float delta) {
				SceneManager.switchTo(StartDemoScene.class);
				return true;
			}
		});
		
		init();
	}

	@Override
	public void enter(Scene scene) {
		super.enter(scene);
		if(!(scene instanceof OptionsScene)) CatacombSnatch.sound.startTitleMusic();
	}
	
	@Override
	public void leave(Scene scene) {
		super.leave(scene);
		if(!(scene instanceof OptionsScene)) CatacombSnatch.sound.stopTitleMusic();
	}
	
	@Override
	public void tick(float delta) {
		super.tick(delta);

		getBatch().draw(Art.logo, (Screen.getWidth() - Art.logo.getWidth()) / 2, Screen.getHeight() - (int) (1.5f * Art.logo.getHeight()));
		
		drawCharacter();
	}
	
}
