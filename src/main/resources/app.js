load('graalvm/asciidoctor.js')

var asciidoctor = Asciidoctor();

var file = 'src/main/resources/example-manual.adoc';

let content = `= Simple

Very simple.

`;

var options = {
    safe: 'UNSAFE',
    // to_dir: "build",
    mkdirs: true
}

asciidoctor.convertFile(file, options);
