package net.catacombsnatch.game.core.scene.scenes;

import net.catacombsnatch.game.core.resources.Art;
import net.catacombsnatch.game.core.resources.Language;
import net.catacombsnatch.game.core.scene.MenuScene;
import net.catacombsnatch.game.core.scene.ReusableAction;
import net.catacombsnatch.game.core.scene.SceneManager;
import net.catacombsnatch.game.core.world.level.generator.DebugLevelGenerator;
import net.catacombsnatch.game.core.world.level.generator.TmxLevelGenerator;

public class StartDemoScene extends MenuScene {

	public StartDemoScene() {
		super(Art.pyramid);
		
		addTextButton(Language.get("general.back"), 0, 0).addAction(new ReusableAction() {
			@Override
			public boolean use(float delta) {
				SceneManager.exit();
				return true;
			}
		});
		
		addTextButton(Language.get("scene.title.start"), 0, 0).addAction(new ReusableAction() {
			@Override
			public boolean use(float delta) {
				InGameScene scene = SceneManager.switchTo(InGameScene.class, true);
				scene.init(new TmxLevelGenerator("maps/demo.tmx").generate());
				return true;
			}
		});
		
		addTextButton(Language.get("scene.title.demo"), 0, 0).addAction(new ReusableAction() {
			@Override
			public boolean use(float delta) {
				InGameScene scene = SceneManager.switchTo(InGameScene.class, true);
				scene.init(new DebugLevelGenerator().generate());
				return true;
			}
		});
		
		init();
	}
	
	@Override
	public void render(float delta) {
		super.render(delta);
		
		drawCharacter();
	}

}
