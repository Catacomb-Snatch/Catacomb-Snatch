package net.catacombsnatch.game.core.scene.scenes;

import net.catacombsnatch.game.core.Game;
import net.catacombsnatch.game.core.resource.Art;
import net.catacombsnatch.game.core.resource.Language;
import net.catacombsnatch.game.core.scene.MenuScene;
import net.catacombsnatch.game.core.scene.ReusableAction;
import net.catacombsnatch.game.core.scene.Scene;
import net.catacombsnatch.game.core.scene.SceneManager;
import net.catacombsnatch.game.core.screen.Screen;
import net.catacombsnatch.game.core.world.level.View;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class PauseScreen extends MenuScene {
	
	public static Texture bg;
	public InGameScene subScene;
	
	public PauseScreen(InGameScene subScene) {
		super(null);
		
		this.subScene = subScene;

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
				SceneManager.switchTo(OptionsScene.class, false, PauseScreen.this).init(Game.options);
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
		
		if (bg == null) {
			Pixmap pm = new Pixmap(1, 1, Format.RGBA8888);
			pm.setColor(0f, 0f, 0f, 0.45f);
			pm.drawPixel(0, 0);
			Texture nbg = new Texture(pm);
			pm.dispose();
			bg = nbg;
		}
		setBackground(bg);
	}

	@Override
	public void enter(Scene scene) {
		super.enter(scene);
		if(!(scene instanceof OptionsScene)) Game.sound.startTitleMusic();
	}
	
	@Override
	public void leave(Scene scene) {
		super.leave(scene);
		if(!(scene instanceof OptionsScene)) Game.sound.stopTitleMusic();
	}
	
	public void renderViews(Scene scene) {
		subScene.paused = this;
		// Open the windows to actually see the outside!
		scene.getSpriteBatch().begin();
		for(View view : subScene.views) {
			view.render(scene);
		}
		scene.getSpriteBatch().end();
		subScene.paused = null;
	}
	
	@Override
	public void render(float delta) {
		renderViews(this);
		
		super.render(delta);
		
		getSpriteBatch().draw(Art.logo, (Screen.getWidth() - Art.logo.getWidth()) / 2, Screen.getHeight() - (int) (1.5f * Art.logo.getHeight()));
		
		drawCharacter();
	}
	
	@Override
	public void update(boolean resize) {
		if(resize) {
			subScene.update(resize);
		}
		super.update(resize);
	}
	
}
