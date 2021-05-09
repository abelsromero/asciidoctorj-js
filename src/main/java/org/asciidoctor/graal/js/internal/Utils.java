package org.asciidoctor.graal.js.internal;

import graal.js.runner.Runner;
import org.asciidoctor.graal.js.GraalJsAsciidoctor;

public class Utils {

    public static String fromClasspath(String file) {
        return GraalJsAsciidoctor.class.getClassLoader().getResource(file).getFile();
    }

}
