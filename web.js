var url = require('url');
var path = require('path');

var express = require('express');
var ejs = require('ejs');

var conf = {
    'port': process.env.PORT || 3000,
    'baseUrl': 'https://codast-jegga.herokuapp.com'
};

var app = express();
app.configure(function () {
    app.set("views", path.join(__dirname, 'views'));
    app.engine('html', require('ejs').renderFile);
});


app.get('/', function (req, res) {
    res.render('index.html');
});

app.get('/auth/step-1', function (req, res) {
    var apiUrl,
        conKey = req.query.conKey;  // consumer key
    
    if (conKey && conKey.length > 0) {
        apiUrl = url.format({
            'protocol': 'https',
            'hostname': 'login.salesforce.com',
            'pathname': '/services/oauth2/authorize',
            'query': {
                'response_type': 'code',
                'client_id': conKey,
                'state': conKey,
                'redirect_uri': conf.baseUrl + '/auth/callback'
            }
        });
        res.redirect(apiUrl);
    } else {
        res.render('step-1.html');
    }
});

app.get('/auth/step-2', function (req, res) {
    var apiUrl,
        conKey = req.query.state || req.query.conKey,  // consumer secret
        conSecret = req.query.conSecret;  // consumer secret
    if (conSecret && conSecret.length > 0) {
        apiUrl = url.format({
            'protocol': 'https',
            'hostname': 'login.salesforce.com',
            'pathname': '/services/oauth2/authorize',
            'query': {
                'response_type': 'code',
                //'client_id': conKey,
                'client_secret': conSecret,
                'redirect_uri': conf.baseUrl + '/auth/callback'
            }
        });
        res.redirect(apiUrl);
    } else {
        res.render('step-2.html', {'conKey': conKey});
    }
});

app.get('/auth/callback', function (req, res) {
    res.render('callback.html');
});

app.listen(conf.port);
