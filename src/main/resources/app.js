var asciidoctor = Asciidoctor()


// var file = 'src/main/resources/example-manual.adoc';
var file = 'src/main/resources/simple.adoc';

asciidoctor.convert(`= Simple

Very simple.

`, {to_file: true, safe: 'UNSAFE'});
