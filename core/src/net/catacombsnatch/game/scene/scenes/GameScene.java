package net.catacombsnatch.game.scene.scenes;

import com.badlogic.ashley.core.Entity;
import net.catacombsnatch.game.CatacombSnatch;
import net.catacombsnatch.game.entity.Entities;
import net.catacombsnatch.game.entity.components.Position;
import net.catacombsnatch.game.event.EventHandler;
import net.catacombsnatch.game.event.input.InputManager;
import net.catacombsnatch.game.event.input.Key;
import net.catacombsnatch.game.event.input.events.KeyReleaseEvent;
import net.catacombsnatch.game.player.LocalPlayer;
import net.catacombsnatch.game.player.Player;
import net.catacombsnatch.game.scene.Scene;
import net.catacombsnatch.game.scene.SceneManager;
import net.catacombsnatch.game.screen.Screen;
import net.catacombsnatch.game.world.Campaign;
import net.catacombsnatch.game.world.Difficulty;
import net.catacombsnatch.game.world.level.Level;
import net.catacombsnatch.game.world.level.View;
import net.catacombsnatch.game.world.level.generator.LevelGenerator;

import java.util.LinkedList;
import java.util.List;

public class GameScene extends Scene {
    private boolean paused = false;
    private Campaign campaign;
    private List<View> views;


    public GameScene(LevelGenerator generator) {  // TODO complete
        super();

        campaign = new Campaign(Difficulty.EASY, new Campaign.MapRotation.FIRST());

        for (Player player : CatacombSnatch.getLocalPlayers()) {
            if (player == null) continue;

            campaign.players.add(player);
        }

        campaign.levels.add(generator.generate(campaign));
    }

    @Override
    public void enter(Scene before) {
        super.enter(before);

        paused = false;
        SceneManager.setDrawAllEnabled(true);
    }

    @Override
    public void leave(Scene next) {
        super.leave(next);

        paused = true;
    }

    @Override
    public void tick(float delta) {
        if (campaign.hasFinished()) {
            SceneManager.exit();
            return;
        }

        if (!paused) {
            // Tick, tock - the campaign is just a clock...
            campaign.tick(delta);

            // Prepare views for rendering and level initialization
            if (views == null) {
                final Level level = campaign.getCurrentLevel();
                views = new LinkedList<>();

                for (Player player : campaign.players) {
                    if (!(player instanceof LocalPlayer)) continue;

                    final View view = new View(level);
                    view.setTarget(player.createEntity(level).getComponent(Position.class).xy());

                    views.add(view);
                }

                // Update viewports
                update(true);
            }

            // Player movement
            int mx = 0, my = 0;

            if (InputManager.isPressed(Key.MOVE_LEFT)) mx--;
            if (InputManager.isPressed(Key.MOVE_RIGHT)) mx++;
            if (InputManager.isPressed(Key.MOVE_UP)) my++;
            if (InputManager.isPressed(Key.MOVE_DOWN)) my--;

            for (Entity player : campaign.getCurrentLevel().players) {
                Entities.velocity.get(player).add(mx << 2, my << 2);
            }
        }

        // Open the windows to see what's happening!
        getBatch().begin();

        for (View view : views) {
            view.render(this);
        }

        getBatch().end();

        // Just some overlays
        super.tick(delta);
    }

    @Override
    public void exit() {
        campaign = null;
        views = null;

        SceneManager.setDrawAllEnabled(false);
        super.exit();
    }

    @Override
    public void update(boolean resize) {
        if (!resize || views == null) return;

        float w = Screen.getWidth(), h = Screen.getHeight();

		/*
        // This did not work: (Trying to split the screen here...)

		for(float p = 0, s = views.size(); p < s; p++) {
			boolean quarters = s > (s / 2);
			float n = p / s * w, m = w / s;

			View view = views.get((int) p);

			view.setViewport(n, quarters ? h / 2 : 0, m, quarters ? h / 2 : h );
			view.update(resize);
		}*/

        for (View view : views) {
            view.setViewport(0, 0, w, h);
            view.update(true);
        }
    }

    @EventHandler
    public void keyRelease(KeyReleaseEvent event) {
        switch (event.getKey()) {
            case BACK:
                SceneManager.switchTo(PauseScreen.class);
                break;

            default: // Do nothing ...
        }
    }

}
