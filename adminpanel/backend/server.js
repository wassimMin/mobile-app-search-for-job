const express = require('express');
const connection = require('./db');
const bodyParser = require('body-parser');
const cors = require('cors');
const { countUsers, countJobs , countApplication } = require('./utils');

const app = express();
const PORT  = process.env.PORT || 5000;

app.use(cors());
app.use(bodyParser.json());


// add User
const addUser = (userData)=>{
    let {name,email,password,userType} = userData;
    userType = userType.toLowerCase() === 'company' ? 1 : 0;
    connection.query(
        'INSERT INTO users (name, email, password, userType) VALUES (?, ?, ?, ?)',
        [name, email, password, userType],
        (err, results) => {
            if (err) {
                console.error('Error adding user:', err);
            } else {
                console.log('User added successfully');
            }
        }
    );
};



app.post('/api/login',(req,res)=>{
    const {email,password} = req.body;

    if(email === 'admin@g' && password === '1234'){
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

app.get('/api/users/count',(req,res)=>{
    countUsers((err,count)=>{
        if(err){
            return res.status(500).json({ error: err.message });
        }
        res.json({ userCount: count });
    });
});

app.get('/api/jobs/count',(req,res)=>{
    countJobs((err,count)=>{
        if(err){
            return res.status(500).json({ error: err.message });
        }
        res.json({jobsCount: count});
    });
});

app.get('/api/application/count',(req,res)=>{
    countApplication((err,count)=>{
        if(err){
            return res.status(500).json({ error: err.message });
        }
        res.json({applicationCount: count});
    });
});
// fetch users
app.get('/api/users',(req,res)=>{
    connection.query('SELECT userid,name, email FROM users',(err,results)=>{
        if(err){
            return res.status(500).json({ error: err.message });
        }
        res.json({users: results});
    });
});
// fetch jobs
app.get('/api/jobs',(req,res)=>{
    connection.query('SELECT id,job_title, company_name, location, created_at FROM job',(err,results)=>{
        if(err){
            return res.status(500).json({ error: err.message });
        }
        res.json({jobs: results});
    });
});

// delete job
app.delete('/api/jobs/:id',(req,res)=>{
    const jobId = req.params.id;
    connection.query('DELETE FROM job WHERE id = ?',[jobId],(err,results)=>{
        if (err) {
            return res.status(500).json({ error: err.message });
        }
        res.json({ message: 'Job deleted successfully' });
    })
})

// delete user
app.delete('/api/users/:userid', (req, res) => {
    const userId = req.params.userid;
    connection.query('DELETE FROM users WHERE userid = ?', [userId], (err, results) => {
        if (err) {
            return res.status(500).json({ error: err.message });
        }
        if (results.affectedRows === 0) {
            return res.status(404).json({ message: 'User not found' });
        }
        res.json({ message: 'User deleted successfully' });
    });
});
// edit user 
app.put('/api/users/:userid', (req, res) => {
    const userId = req.params.userid;
    const { name, email, password } = req.body;

    connection.query(
        'UPDATE users SET name = ?, email = ?, password = ? WHERE userid = ?',
        [name, email, password, userId],
        (err, results) => {
            if (err) {
                return res.status(500).json({ error: err.message });
            }
            if (results.affectedRows === 0) {
                return res.status(404).json({ message: 'User not found' });
            }
            res.json({ message: 'User updated successfully' });
        }
    );
});
app.post('/api/users/adduser', (req, res) => {
    const userData = req.body;
    addUser(userData);
    res.json({ message: 'User added successfully' });
});
app.listen(PORT, () => console.log(`Server running on port ${PORT}`));