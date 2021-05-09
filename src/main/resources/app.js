load('graalvm/asciidoctor.js')

var asciidoctor = Asciidoctor();

var file = '/home/asalgadr/github/graal-js-runner/src/main/resources/example-manual.adoc';

let content = `= Simple

Very simple.

`;

var ast = asciidoctor.load(content);
console.log(ast);

var ast = asciidoctor.loadFile(file);
console.log(ast);

var options = {
    safe: 'UNSAFE',
    // to_dir: "build",
    mkdirs: true
}

asciidoctor.convertFile(file, options);
