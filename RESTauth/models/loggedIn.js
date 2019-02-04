var mongoose = require('mongoose');

mongoose.connect('mongodb://localhost/nodeauth');

var db = mongoose.connection;

var LoggedInSchema = {
    email: {
        type: String,
        index: true
    },
    jwt: {
        type: String
    }
};

var LoggedIn = module.exports = mongoose.model('LoggedIn', LoggedInSchema);

module.exports.createLoggedIn = function(newLoggedIn, callback) {
    newLoggedIn.save(callback);
};

module.exports.getRecordByJWT = function(jwt, callback) {
    var query = {
        jwt: jwt
    };
    LoggedIn.findOne(query, callback);
};