var mongoose = require('mongoose');
var dbConfig = require('../db/config.json')

var db = mongoose.connect(dbConfig.DB);

// User Schema
var UserSchema = mongoose.Schema({
    name: {
        type: String,
    },
    email: {
        type: String,
        index: true
    },
    password: {
        type: String
    },
    profileimage: {
        type: String
    },
    role: {
        type: String
    }
})

var User = module.exports = mongoose.model('User', UserSchema);

module.exports.createUser = function(newUser, callback){
    newUser.save(callback);
};

module.exports.getUserByEmail = function(email, callback){
    var query = {
        email: email
    };

    User.findOne(query, callback);
};

module.exports.comparePassword = function(password, dbPassword, callback){
    if(password === dbPassword)
        isMatched = 1;
    else
        isMatched = 0;

    callback(null, isMatched);
};

module.exports.updateRole = function(email, role, callback) {
    User.getUserByEmail(email, function(err, user){
        if(err)
        {
            callback(err, null);
        }
        else
        {
            user.role = role;
            user.save(callback);
        }
    });
};