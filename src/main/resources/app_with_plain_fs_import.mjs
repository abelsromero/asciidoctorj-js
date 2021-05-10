import Asciidoctor from 'graalvm/asciidoctor-graalvm-module.js';

var asciidoctor = Asciidoctor();

let content = `= Simple

Very simple.

`;

var options = {
    safe: 'UNSAFE',
    // to_dir: "build",
    // mkdirs: true
}

asciidoctor.convert(content, options)
