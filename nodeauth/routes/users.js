var express = require('express');
var router = express.Router();
var multer = require('multer');
var upload = multer({ dest: 'uploads/' });
var request = require('request');

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
  console.log(errors);

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
    request.post({url: 'http://auth:2000/users/register', form: {name: name, email: email, password: password, profileimage: profileimage}}, function(error, response, body) {
      if(response.statusCode == 200)
      {
        errors = [{
          location: 'body',
          param: 'success',
          msg: 'Successfully registered!',
          value: ''
        }];
        res.render('login', {
          title: 'Login',
          errors: errors
        });
      }
      else
      {
        errors = [{
          location: 'body',
          param: 'failed',
          msg: body.msg,
          value: ''
        }];
        res.render('register', {
          title: 'Register',
          errors: errors
        });
      }
    });
  }
});

router.post('/login', function(req, res, next){
  var email = req.body.email;
  var password = req.body.password;

  request.post({url: 'http://auth:2000/users/login', form: {email: email, password: password}}, function(error, response, body){
    if(response.statusCode == 200)
    {
      res.location('/');
      res.redirect('/');
    }
    else
    {
      errors = [{
        location: 'body',
        param: 'failed',
        msg: 'Invalid E-mail or Password!',
        value: ''
      }];
      res.render('login', {
        title: 'Login',
        errors: errors
      });
    }
  });
});

module.exports = router;
