var mongoose = require('mongoose');
var bcrypt = require('bcryptjs');
var dbConfig = require('../db/config.json')

var db = mongoose.connect(dbConfig.DB);

// User Schema
var UserSchema = mongoose.Schema({
    name: {
        type: String,
    },
    email: {
        type: String,
        index: true,
        unique: true
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

module.exports.createUser = function (newUser, callback) {
    bcrypt.genSalt(10, function (err, salt) {
        bcrypt.hash(newUser.password, salt, function (err, hash) {
            newUser.password = hash;
            newUser.save(callback);
        });
    });
};

module.exports.getUserByEmail = function(email, callback){
    var query = {
        email: email
    };

    User.findOne(query, callback);
};

module.exports.comparePassword = function (password, dbPassword, callback) {
    bcrypt.compare(password, dbPassword, function (err, res) {

        if (res)
            isMatched = 1;
        else
            isMatched = 0;

        callback(null, isMatched);
    });
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