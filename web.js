var url = require('url');

var express = require('express');
var ejs = require('ejs');


var app = express();
app.configure(function () {
    app.set("view options", {layout: false});  // is this necessary?
    app.engine('html', require('ejs').renderFile);
});

app.get('/', function (req, res) {
    var loc = __dirname + "/index.html";
    res.render(loc);
});

app.get('/submit', function (req, res) {
    var apiUrl = url.format({
        'protocol': 'https',
        'hostname': 'login.salesforce.com',
        'pathname': '/services/oauth2/authorize',
        'query': {
            'response_type': 'token',
            'client_id': req.query.consumerKey,
            'redirect_uri': 'https://codast-jegga.herokuapp.com/final'
        }
    });
    res.redirect(apiUrl);
});

app.get('/final', function (req, res) {
    res.send("meow");
});

var port = process.env.PORT || 3000;
app.listen(port);
