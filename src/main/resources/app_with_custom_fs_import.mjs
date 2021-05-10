import Asciidoctor from 'custom-fs-asciidoctor';

var asciidoctor = Asciidoctor();

let content = `= Simple

Very simple.

`;

var options = {
    safe: 'UNSAFE',
    // to_dir: "build",
    mkdirs: true
}

asciidoctor.convert(content, options);
