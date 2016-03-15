package net.catacombsnatch.game.statistic.achievement;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.ObjectMap;

public class Achievements {
    protected static ObjectMap<String, Achievement> achievements;

    static {
        achievements = new ObjectMap<String, Achievement>();
    }

    public static boolean register(String id, Class<? extends Achievement> achievement) {
        try {
            Achievement instance = achievement.newInstance();
            achievements.put(id, instance);
            return true;

        } catch (Exception e) {
            Gdx.app.error("Achievements", "Could not register achievment with id: " + id, e);
        }

        return false;
    }

    public static Achievement get(String id) {
        return achievements.get(id);
    }

}
