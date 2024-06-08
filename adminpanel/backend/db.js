const mysql = require('mysql');

const connection = mysql.createConnection({
    host:'localhost',
    user:'root',
    password:'',
    database:'memoire'
});

connection.connect((err)=>{
    if(err){
        console.error('Error connecting to database: ' +err.stack);
        return;
    }
    console.log('Connected to Database as id' + connection.threadId);
})

module.exports = connection;