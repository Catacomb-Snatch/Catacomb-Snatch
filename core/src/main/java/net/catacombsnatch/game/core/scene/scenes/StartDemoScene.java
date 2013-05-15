package net.catacombsnatch.game.core.scene.scenes;

import net.catacombsnatch.game.core.resource.Art;
import net.catacombsnatch.game.core.resource.Language;
import net.catacombsnatch.game.core.scene.MenuScene;
import net.catacombsnatch.game.core.scene.ReusableAction;
import net.catacombsnatch.game.core.scene.SceneManager;
import net.catacombsnatch.game.core.world.level.generator.DebugLevelGenerator;
import net.catacombsnatch.game.core.world.level.generator.RandomLevelGenerator;
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
				SceneManager.switchTo(new InGameScene(new TmxLevelGenerator("maps/demo.tmx").generate()), true);
				return true;
			}
		});
		
		addTextButton(Language.get("scene.title.demo"), 0, 0).addAction(new ReusableAction() {
			@Override
			public boolean use(float delta) {
				SceneManager.switchTo(new InGameScene(new DebugLevelGenerator().generate()), true);
				return true;
			}
		});
		
		addTextButton(Language.get("scene.title.demo"), 0, 0).addAction(new ReusableAction() {
			@Override
			public boolean use(float delta) {
				SceneManager.switchTo(new InGameScene(new RandomLevelGenerator().generate()), true);
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
