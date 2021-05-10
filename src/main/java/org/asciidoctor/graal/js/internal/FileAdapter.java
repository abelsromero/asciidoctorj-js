package org.asciidoctor.graal.js.internal;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.TimeZone;

/**
 * Implements File & Dir related methods not supported in GraalVM runtime.
 */
public class FileAdapter {

    /**
     * ::File
     */
    
    public String read(String path) throws IOException, URISyntaxException {
        Path filePath = Paths.get(path);

        if (filePath.toFile().exists()) {
            return Files.readString(filePath, StandardCharsets.UTF_8);
        } else {
            Path fileName = filePath.getFileName();
            URL url = this.getClass().getClassLoader().getResource(fileName.toString());
            if (url != null) {
                return Files.readString(Paths.get(url.toURI()), StandardCharsets.UTF_8);
            } else {
                return "";
            }
        }
    }

    public void write(String path, String data) throws IOException {
        // Force UTF-8 to comply with Asciidoctor Ruby with uses mode: FILE_WRITE_MODE
        Files.writeString(Path.of(path), data, StandardCharsets.UTF_8);
    }

    public Object mtime(String path) throws IOException {
        BasicFileAttributes basicFileAttributes = Files.readAttributes(Path.of(path), BasicFileAttributes.class);
        return new DateAdapter(basicFileAttributes.lastModifiedTime(), TimeZone.getDefault());
    }
    
    public boolean isDirectory(String path) {
        return new File(path).isDirectory();
    }

    /**
     * ::Dir
     */
    
    public int mkdir(String path) {
        new File(path).mkdir();
        return 0;
    }

    public String pwd() {
        return Paths.get("").toAbsolutePath().toString();
    }
}
