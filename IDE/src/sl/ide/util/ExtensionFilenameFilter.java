package sl.ide.util;

import java.io.File;
import java.io.FilenameFilter;

/**
 *
 * @author Полевая Евгения
 */
public class ExtensionFilenameFilter implements FilenameFilter {

    private String extension;

    public ExtensionFilenameFilter(String extension) {
        this.extension = extension;
    }

    public boolean accept(File dir, String name) {
        return (name.endsWith("." + extension));
    }
}
