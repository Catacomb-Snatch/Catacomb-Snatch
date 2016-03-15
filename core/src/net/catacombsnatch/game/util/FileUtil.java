package net.catacombsnatch.game.util;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;

import java.io.BufferedReader;
import java.io.IOException;

public class FileUtil {

    /**
     * <b>If the {@link FileHandle} is a directory this function returns null!</b><br />
     * Reads a simple text file with this simple format:
     * <ul>
     * <li>A commented out line begins with a <code>#</code> character</li>
     * <li>Each new line begins with a return: either <code>\n</code>, <code>\r</code> or both (<code>\n\r</code>)</li>
     * </ul>
     *
     * @param handle The file to read
     * @return A list of all parsed lines (commented lines will be ignored)
     * @throws IOException
     */
    public static Array<String> readSimpleFile(FileHandle handle) throws IOException {
        if (handle.isDirectory()) return null;

        Array<String> lines = new Array<String>();
        BufferedReader br = new BufferedReader(handle.reader());
        String line;

        while ((line = br.readLine()) != null) {
            if (line.startsWith("#") || line.trim().length() < 1) continue; // 2 because of '\n'

            lines.add(line);
        }

        return lines;
    }

}
