package net.catacombsnatch.game;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.badlogic.gdx.backends.android.AndroidInputThreePlus;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.utils.BufferUtils;

import java.nio.ByteBuffer;

/**
 * This class extends {@link com.badlogic.gdx.backends.android.AndroidInputThreePlus} and
 * overrides the cursor image setting method as Ouya actually supports (partial) cursor setup.
 */
public class OuyaInput extends AndroidInputThreePlus {

    public OuyaInput(Application activity, Context context, Object view, AndroidApplicationConfiguration config) {
        super(activity, context, view, config);
    }

    public void setCursorVisible(boolean showCursor) {
        String ACTION_SHOW_CURSOR = "tv.ouya.controller.action.SHOW_CURSOR";
        String ACTION_HIDE_CURSOR = "tv.ouya.controller.action.HIDE_CURSOR";
        Intent intent = new Intent(showCursor ? ACTION_SHOW_CURSOR : ACTION_HIDE_CURSOR);
        ((Activity) Gdx.app).getApplicationContext().sendBroadcast(intent);
    }

    //TODO: Fix this, chaged: https://github.com/libgdx/libgdx/pull/2841
    //@Override
    public void setCursorImage(Pixmap pixmap, int xHotspot, int yHotspot) {
        if (pixmap == null) {
            setCursorVisible(false);
            return;
        }
        setCursorVisible(true);

        Bitmap bitmap = convertPixmapToBitmap(pixmap);

        Intent intent = new Intent("tv.ouya.controller.action.SET_CURSOR_BITMAP");
        intent.putExtra("CURSOR_BITMAP", bitmap);
        //TODO modify hotspot if needed
        intent.putExtra("HOTSPOT_X", (float) xHotspot / (float) pixmap.getWidth());
        intent.putExtra("HOTSPOT_Y", (float) yHotspot / (float) pixmap.getHeight());
        ((Activity) Gdx.app).getApplicationContext().sendBroadcast(intent);
    }

    private static Bitmap convertPixmapToBitmap(Pixmap pixmap) {
        Pixmap.Format pixmapFormat = pixmap.getFormat();

        Bitmap bitmap = Bitmap.createBitmap(pixmap.getWidth(), pixmap.getHeight(),
                getConfigForFormat(pixmapFormat));

        ByteBuffer src = pixmap.getPixels();
        ByteBuffer dst = src;

        if (pixmapFormat == Pixmap.Format.RGBA4444 || pixmapFormat == Pixmap.Format.RGBA8888) {
            int capacity = pixmap.getWidth() * pixmap.getHeight() * 4;
            dst = BufferUtils.newByteBuffer(capacity);

            byte[] orig = new byte[4];
            byte[] modif = new byte[4];
            for (int i = 0; i < capacity; i += 4) {
                src.get(orig);
                System.arraycopy(orig, 0, modif, 1, 3);
                modif[0] = orig[3];
                dst.put(modif);
            }

        } else if (pixmapFormat == Pixmap.Format.RGB888) {
            int capacity = pixmap.getWidth() * pixmap.getHeight() * 4;
            dst = BufferUtils.newByteBuffer(capacity);

            byte[] orig = new byte[3];
            byte[] modif = new byte[4];
            for (int i = 0; i < capacity; i += 4) {
                src.get(orig);
                System.arraycopy(orig, 0, modif, 1, 3);
                modif[0] = Byte.MAX_VALUE;
                dst.put(modif);
            }
        }

        dst.position(0);

        bitmap.copyPixelsFromBuffer(dst);

        return bitmap;
    }

    private static Bitmap.Config getConfigForFormat(Pixmap.Format format) {
        switch (format) {
            case Alpha:
                return Bitmap.Config.ALPHA_8;
            case Intensity:
                return Bitmap.Config.ALPHA_8;
            case LuminanceAlpha:
                return Bitmap.Config.ALPHA_8;
            case RGB565:
                return Bitmap.Config.RGB_565;
            case RGBA4444:
                return Bitmap.Config.ARGB_4444;
            case RGB888:
                return Bitmap.Config.ARGB_8888;
            case RGBA8888:
                return Bitmap.Config.ARGB_8888;
        }
        return null;
    }
}