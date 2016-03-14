package net.catacombsnatch.game.client;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.backends.gwt.GwtApplication;
import com.badlogic.gdx.backends.gwt.GwtApplicationConfiguration;
import net.catacombsnatch.game.CatacombSnatch;
import net.catacombsnatch.game.PlatformDependent;

public class HtmlLauncher extends GwtApplication {

        @Override
        public GwtApplicationConfiguration getConfig () {
                return new GwtApplicationConfiguration(480, 320);
        }

        @Override
        public ApplicationListener createApplicationListener () {
                return new CatacombSnatch(new PlatformDependent() {
                        @Override
                        public void create() {

                        }

                        @Override
                        public Object[] createPlatformObjects() {
                                return new Object[0];
                        }

                        @Override
                        public void dispose() {

                        }
                });
        }
}