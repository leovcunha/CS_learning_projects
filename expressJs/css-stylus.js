
var express = require("express");
var app = express();


app.use(express.static(process.argv[3]||'/home/workspace/expressJs/'.join(__dirname, 'public')));

app.use(require('stylus').middleware(process.argv[3]));

app.listen(process.argv[2]);