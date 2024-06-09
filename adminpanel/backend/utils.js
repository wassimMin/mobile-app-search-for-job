const connection = require('./db');

const countUsers = (callback)=>{
    connection.query('SELECT COUNT(*) AS count FROM users',(err,results)=>{
        if(err){
            return callback(err,null);
        }
        const count = results[0].count;
        callback(null,count);
    });
}

const countJobs = (callback) =>{
    connection.query('SELECT COUNT(*) AS count FROM job', (err,results)=>{
        if(err){
            return callback(err,null);
        }
        const count = results[0].count;
        callback(null,count);
    });
}

const countApplication = (callback)=>{
    connection.query('SELECT COUNT(*) AS count FROM application',(err,results)=>{
        if(err){
            return callback(err,null);
        }
        const count = results[0].count;
        callback(null,count);
    });
}
module.exports = { countUsers , countJobs ,countApplication };
