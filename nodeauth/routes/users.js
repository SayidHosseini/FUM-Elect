var express = require('express');
var router = express.Router();
var multer = require('multer');
var upload = multer({ dest: 'uploads/' });
var request = require('request');

/* GET users listing. */
router.get('/', function(req, res, next) {
  res.send('respond with a resource');
});

router.get('/register', function(req, res, next) {
  res.render('register', {title: 'Register'});
});

router.get('/login', function(req, res, next) {
  res.render('login', {title: 'Login'});
});

router.post('/register', upload.single('profileimage'), function(req, res, next) {
  var name = req.body.name;
  var email = req.body.email;
  var password = req.body.password;
  var password2 = req.body.password2;

  if(req.file)
  {
    var profileimage = req.file.filename;
  }
  else
  {
    var profileimage = 'noimage.jpg';
  }

  // Form Validation
  req.checkBody('name', 'Name field is required!').notEmpty();
  req.checkBody('email', 'E-mail field is required!').notEmpty();
  req.checkBody('email', 'E-mail is NOT valid!').isEmail();
  req.checkBody('password', 'Password field is required!').notEmpty();
  req.checkBody('password', 'Passwords do NOT match!').equals(req.body.password2);

  var errors = req.validationErrors();

  if(errors)
  {
    res.render('register', {
       title: 'Register',
       name: name,
       email: email,
       errors: errors
      })
  }
  else
  {
    request.post({url: 'http://fumelect.tk:2000/users/register', form: {name: name, email: email, password: password, profileimage: profileimage}}, function(error, response, body) {
      console.log('error', error);
      console.log('statusCode', response && response.statusCode);
      console.log('body:', body);
    });
    res.location();
    res.redirect();
  }
});

router.post('/login', function(req, res, next){
  var email = req.body.email;
  var password = req.body.password;

  request.post({url: 'http://fumelect.tk:2000/users/login', form: {email: email, password: password}}, function(error, response, body){
    console.log('error', error);
    console.log('statusCode', response && response.statusCode);
    console.log('body:', body);
  });
  res.location();
  res.redirect();
});

module.exports = router;
