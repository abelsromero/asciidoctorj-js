package org.asciidoctor.graal.js.internal;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class IncludeResolver {

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
}
