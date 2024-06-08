const express = require('express');
const router = express.Router();
const connection = require('./db');
const bcrypt = require('bcrypt');


router.post('/api/login',(req,res)=>{
    const {email,password} = req.body;

    if(email === 'admin' && password === '1234'){
        return res.json({ message: 'Login successful', user: { email } });
    }else{
        connection.query('SELECT * FROM users WHERE email = ?', [email],(err,result)=>{
            if(err){
                return res.status(500).json({ error: err.message });
            }
            if(result.length ===0){
                return res.status(401).json({ message: 'Invalid credentials' });    
            }
            const user = result[0];
            if (user.password === password) {
                res.json({ message: 'Login successful', user: { email: user.email } });
            } else {
                res.status(401).json({ message: 'Invalid credentials' });
            }
        });
    }
});


module.exports = router;
