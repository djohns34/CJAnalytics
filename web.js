var url = require('url');

var express = require('express');
var ejs = require('ejs');


var app = express();
app.configure(function () {
    app.set("view options", {layout: false});  // is this necessary?
    app.engine('html', require('ejs').renderFile);
});



app.get('/', function (req, res) {
    var loc = "/home/coreyf/school/codastjegga/authserver/index.html";
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
            'redirect_uri': 'https://rwell.org/'
        }
    });

    res.redirect(apiUrl);
});

app.listen(3000);
