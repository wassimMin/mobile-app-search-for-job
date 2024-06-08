const express = require('express');
const connection = require('./db');
const bodyParser = require('body-parser');
const cors = require('cors');
const authRoute = require('./auth');


const app = express();
const PORT  = process.env.PORT || 5000;

app.use('/auth', authRoute);

app.use(cors());
app.use(bodyParser.json());



app.listen(PORT, () => console.log(`Server running on port ${PORT}`));