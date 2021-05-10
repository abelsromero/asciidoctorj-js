package org.asciidoctor.graal.js.internal;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

public class Utils {

    public static URL fromClasspathUrl(String file) {
        return Utils.class.getClassLoader().getResource(file);
    }
    
    public static String fromClasspath(String file) {
        return Utils.class.getClassLoader().getResource(file).getFile();
    }

    public static String readFromClasspath(String file) {
        InputStream is = Utils.class.getClassLoader().getResourceAsStream(file);
        return new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))
                .lines()
                .collect(Collectors.joining("\n"));
    }

}
