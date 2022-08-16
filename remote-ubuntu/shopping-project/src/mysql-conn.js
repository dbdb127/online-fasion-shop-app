require('dotenv').config();
import mysql from 'mysql2/promise';

const {MYSQL_HOST, MYSQL_PORT, MYSQL_ID, MYSQL_PW, MYSQL_DB}=process.env;

//DB 연결
let connPool=mysql.createPool({
    host: 'localhost', // Replace your HOST IP
    user: 'root',
    password: '', // Replace your password
    database: 'shopping', // Replace your db name
    port:80,
    connectionLimit:10
});

export default connPool;
