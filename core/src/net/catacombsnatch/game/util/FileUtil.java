package net.catacombsnatch.game.util;

import com.badlogic.gdx.files.FileHandle;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

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
    public static List<String> readSimpleFile(FileHandle handle) throws IOException {
        if (handle.isDirectory()) return Collections.emptyList();

        final List<String> lines = new LinkedList<>();
        try (BufferedReader br = new BufferedReader(handle.reader())) {
            String line;

            while ((line = br.readLine()) != null) {
                if (!line.startsWith("#") && line.trim().length() >= 1) {
                    lines.add(line);
                }
            }
        }

        return lines;
    }

}
