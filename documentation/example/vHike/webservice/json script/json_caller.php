<?php
// Erzeugt eine Datei request.json mit dem JSON Objekt in echo
// Und sendet diesen an die Android App
header('Content-type: application/txt');
header('Content-Disposition: attachment; filename="request.json"');

echo '{"as_of":"Thu, 25 Feb 2010 11:30:17 +0000","trends":[{"name":"#nowplaying","url":"http://search.twitter.com/search?q=%23nowplaying"},{"name":"#nothingworsethan","url":"http://search.twitter.com/search?q=%23nothingworsethan"},{"name":"Dubai Mall","url":"http://search.twitter.com/search?q=%22Dubai+Mall%22"},{"name":"iPad Gets","url":"http://search.twitter.com/search?q=%22iPad+Gets%22"},{"name":"#SuperJuniorTrot","url":"http://search.twitter.com/search?q=%23SuperJuniorTrot"},{"name":"Justin Bieber","url":"http://search.twitter.com/search?q=%22Justin+Bieber%22"},{"name":"Click","url":"http://search.twitter.com/search?q=Click"},{"name":"Jaebum","url":"http://search.twitter.com/search?q=Jaebum"},{"name":"#tosavemoney","url":"http://search.twitter.com/search?q=%23tosavemoney"},{"name":"Protection","url":"http://search.twitter.com/search?q=Protection"}]}';

?>
