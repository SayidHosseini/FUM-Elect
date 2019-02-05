var express = require('express');
var router = express.Router();

var User = require('../models/user');
var LoggedIn = require('../models/loggedIn');
var jwt = require('../jwt/JWTservice');

router.post('/register', function(req, res, next) {
  var name = req.body.name;
  var email = req.body.email;
  var password = req.body.password;

  if(!name || !email || !password)
  {
    res.status(400);
    res.send({
      "msg": "Something went wrong"
    });
  }
  else
  {
    var newUser = new User({
      name: name,
      email: email,
      password: password,
      profileimage: 'noimage.jpg',
      role: 'user'
    });

    res.setHeader('Content-Type', 'application/json');

    User.createUser(newUser, function(err, user){
      if(err)
      {
        //throw err;
        res.status(500);
        res.send({
          "msg": "Something went wrong! Try again later..."
        });
      }
      else
      {
        res.send({
          "msg": "Registered successfully"
        });
      }
    });
  }
});

router.post('/login', function(req, res, next){
  var email = req.body.email;
  var password = req.body.password;

  User.getUserByEmail(email, function(err, user){
    if(err || !user)
    {
      res.status(401);
      res.send({
        "msg": "Invalid Username or Password!"
      });
    }
    else
    {
      User.comparePassword(password, user.password, function(err){
        if(isMatched === 1)
        {
          var payload = {
            email: email
          };

          var signOptions = {
            issuer:  "FUM-Elect Server",
            subject:  email,
            audience:  "FUM-Elect Client",
          };

          var token = jwt.sign(payload, signOptions);

          var newLoggedIn = new LoggedIn({
            "email": email,
            "jwt": token
          });

          LoggedIn.createLoggedIn(newLoggedIn, function(err, loggedInRecord){
            if(err)
            {
              res.status(500);
              res.send({
                "msg": "Something went wrong! Try again later..."
              });
            }
            else
            {
              res.status(200);
              res.setHeader('Content-Type', 'application/json');
              res.send({
                "JWT": token,
                "msg": "Logged in Successfully!"
              });
            }
          });
        }
        else
        {
          res.status(401);
          res.send({
            "msg": "Invalid Username or Password!"
          });
        }
      });
    }
  });
});

router.post('/isLoggedIn', function(req, res, next){
  var token = req.body.JWT;

  LoggedIn.getRecordByJWT(token, function(err, record){
    if(err, !record)
    {
      res.status(401);
      res.setHeader('Content-Type', 'application/json');
      res.send({
        "msg": "Sorry! You are NOT logged in!"
      });
    }
    else
    {
      if(validateJWT(token))
      {
        res.status(200);
        res.setHeader('Content-Type', 'application/json');
        res.send({
          "msg": "Yes! You are logged in!"
        });
      }
      else
      {
        res.status(403);
        res.setHeader('Content-Type', 'application/json');
        res.send({
          "msg": "Your session has expired / is invalid!"
        });
      }
    }
  });
});

router.post('/getRole', function(req, res, next){
  var token = req.body.JWT;
  var email = req.body.email;

  LoggedIn.getRecordByJWT(token, function(err, record){
    if(err, !record)
    {
      res.status(401);
      res.setHeader('Content-Type', 'application/json');
      res.send({
        "msg": "You must be logged in to get role!"
      });
    }
    else
    {
      if(validateJWT(token))
      {
        User.getUserByEmail(email, function(err, user){
          if(err || !user)
          {
            res.status(404);
            res.setHeader('Content-Type', 'application/json');
            res.send({
              "msg": "E-mail not found!"
            });
          }
          else
          {
            var role = user.role;

            res.status(200);
            res.setHeader('Content-Type', 'application/json');
            res.send({
              "email": email,
              "role": role
            });
          }
        });
      }
      else
      {
        res.status(403);
        res.setHeader('Content-Type', 'application/json');
        res.send({
          "msg": "Your session has expired / is invalid!"
        });
      }
    }
  });
});

router.post('/setRole', function(req, res, next){
  var token = req.body.JWT;
  var email = req.body.email;
  var role = req.body.role;

  LoggedIn.getRecordByJWT(token, function(err, record){
    if(err, !record)
    {
      res.status(401);
      res.setHeader('Content-Type', 'application/json');
      res.send({
        "msg": "You must be logged in to set role!"
      });
    }
    else
    {
      if(validateJWT(token))
      {
        User.getUserByEmail(record.email, function(err, user){
          if(err || !user)
          {
            res.status(500);
            res.setHeader('Content-Type', 'application/json');
            res.send({
              "msg": "Internal Server Error! No idea what the hell is happening..."
            });
          }
          else
          {
            if(user.role == 'admin')
            {
              if(role === 'admin' || role === 'user')
              {
                User.updateRole(email, role, function(err, record){
                  if(err)
                  {
                    res.status(500);
                    res.setHeader('Content-Type', 'application/json');
                    res.send({
                      "msg": "Internal Server Error! No idea what the hell is happening..."
                    });
                  }
                  else
                  {
                    res.status(200);
                    res.setHeader('Content-Type', 'application/json');
                    res.send({
                      "msg": "Changed role successfully"
                    });
                  }
                });
              }
              else
              {
                res.status(406);
                res.setHeader('Content-Type', 'application/json');
                res.send({
                  "msg": "Acceptable Roles are 'user' and 'admin'!"
                });
              }
            }
            else
            {
              res.status(401);
              res.setHeader('Content-Type', 'application/json');
              res.send({
                "msg": "You must be an admin to set role!"
              });
            }
          }
        });
      }
      else
      {
        res.status(403);
        res.setHeader('Content-Type', 'application/json');
        res.send({
          "msg": "Your session has expired / is invalid!"
        });
      }
    }
  });
});

function validateJWT(token) {
  var verifyOptions = {
    issuer:  "FUM-Elect Server",
    audience:  "FUM-Elect Client",
  };
  var legit = JSON.parse(JSON.stringify(jwt.verify(token, verifyOptions)));
  currentTime = new Date().getTime() / 1000 | 0;

  if(currentTime > legit.iat && currentTime < legit.exp)
    return true;
  else
    return false;
}

module.exports = router;
