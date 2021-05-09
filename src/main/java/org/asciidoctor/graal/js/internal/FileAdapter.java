package org.asciidoctor.graal.js.internal;

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
 * Implements File related methods not supported in GraalVM runtime.
 */
public class FileAdapter {

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

    public String pwd() {
        return Paths.get("").toAbsolutePath().toString();
    }

    public Object mtime(String filePath) throws IOException {
        BasicFileAttributes basicFileAttributes = Files.readAttributes(Path.of(filePath), BasicFileAttributes.class);
        return new DateAdapter(basicFileAttributes.lastModifiedTime(), TimeZone.getDefault());
    }

    public void write(String target, String output) throws IOException {
        // Force UTF-8 to comply with Asciidoctor Ruby with uses mode: FILE_WRITE_MODE
        Files.writeString(Path.of(target), output, StandardCharsets.UTF_8);
    }
}
