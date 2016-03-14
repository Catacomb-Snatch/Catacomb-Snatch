package net.catacombsnatch.game.scene.scenes;

import net.catacombsnatch.game.CatacombSnatch;
import net.catacombsnatch.game.resource.Language;
import net.catacombsnatch.game.scene.MenuScene;
import net.catacombsnatch.game.scene.ReusableAction;
import net.catacombsnatch.game.scene.SceneManager;
import net.catacombsnatch.game.screen.Screen;

import com.badlogic.gdx.graphics.Color;

public class PauseScreen extends MenuScene {

    public PauseScreen() {
        super(Screen.createBlank(new Color(0f, 0f, 0f, 0.45f)));

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
                SceneManager.switchTo(OptionsScene.class).init(CatacombSnatch.options);
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
