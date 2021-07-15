/*
RESTFul Services by NodeJS
Author: seungwoo
*/

var path = require('path');
var crypto = require('crypto');
var uuid = require('uuid');
var express = require('express');
var mysql = require('mysql');
var bodyParser = require('body-parser');
var app = express();
var fs = require('fs');
var cash_default = 200000;
// process.on('warning', e => console.warn(e.stack));
process.setMaxListeners(0);

//Connect to MySQL
var con = mysql.createConnection({
    host: 'localhost', // Replace your HOST IP
    user: 'root',
    password: 'rhwldn',
    database: 'shopping'
});

con.connect;


//PASSWORD UTIL
var genRandomString = function(length){
    return crypto.randomBytes(Math.ceil(length/2))
    .toString('hex') /* convert to hexa format */
    .slice(0, length); /* return required number of characters */
};

var sha512 = function(password, salt){
    var hash = crypto.createHmac('sha512', salt);
    hash.update(password);
    var value = hash.digest('hex');
    return {
        salt:salt,
        passwordHash:value
    }
};

function saltHashPassword(userPassword){
    var salt = genRandomString(16); //Gen random string with 16 character to salt
    var passwordData = sha512(userPassword, salt);
    return passwordData;
}


app.use(bodyParser.json()); // Accept JSON params
app.use(bodyParser.urlencoded({extended: true})); // Accept URL Encoded params

app.get("/", (req,res,next)=>{
    console.log('Password: 123456');
    var encrypt = saltHashPassword("123456");
    console.log('Encrypt: '+ encrypt.passwordHash);
    console.log('Salt: '+ encrypt.passwordHash);
    res.send('Hello World!');
})


// // show category list by mysql2 await
// app.get("/category", async (req, res, next) => {

//     console.log('GET category');
//     var resultCode;
//     var category_arr = [];
//     var category_sub_arr = [];

//     const connection = await connPool.getConnection();

//     await connection.beginTransaction();

//     const queryStr1 = 'SELECT * FROM category_main ORDER BY category_main_id';

//     let result = await connection.query(queryStr1);
    
//     if (result[0].length < 1){
//         res.status(404).send()
//         res.json('There is no Category!!!')
//     }







// show category list
app.get("/category_main", (req, res, next) => {

    console.log('GET category_main');
    var resultCode;
    var category_main_arr = [];
    
    con.query('SELECT * FROM category_main ORDER BY category_main_id', function(err, result, fields){
        con.on('error', function(err){
            console.log('[MySQL ERROR]', err);
        });
        // console.log('category_main result', result);
        if(result && result.length){
            for(i=0; i<result.length;i++){
                category_main_arr.push(result[i].category_main_name);
                console.log("category_main_name", result[i].category_main_name);
            }
            console.log("category_main_arr", category_main_arr);
            resultCode = 200;

            const objToSend = {
                code: resultCode,
                name: category_main_arr
            }

            res.json(objToSend)
        }

        else
        {
            resultCode = 404;
            const objToSend = {
                code: resultCode,
                name: category_main_arr
            }
        
            res.json(objToSend)
            console.log('There is no Category!!!')
        }

    })
})

app.post("/category_sub", (req, res, next) => {

    console.log('POST category_sub');
    var resultCode;
    var category_sub_arr = [];

    const query = {
        category_main_name: req.body.category_main,
    }
    con.query('SELECT * FROM category_sub WHERE category_main_name = ? ORDER BY category_sub_id', [query.category_main_name], function(err, result, fields){
        con.on('error', function(err){
            console.log('[MySQL ERROR]', err);
        });
        console.log('category_main_name and sub result', query.category_main_name, result);

        if(result && result.length){
            // console.log('category_sub result', result0);
            for(j=0; j<result.length;j++){
                category_sub_arr.push(result[j].category_sub_name);
            }
            console.log("category_sub_arr", category_sub_arr);
            resultCode = 200;

            const objToSend = {
                code: resultCode,
                name: category_sub_arr
            }

            res.json(objToSend)

        }
        else{
            resultCode = 404;
            category_sub_arr = [];
            console.log("There is no category_sub_arr in ", query.category_main_name);
            const objToSend = {
                code: resultCode,
                name: category_sub_arr
            }
            res.json(objToSend)
        }
        
    });

})


            
// show product list
app.post("/product/", (req,res,next)=>{

    console.log('POST product');
    var resultCode;
    var name_arr = [];
    var price_arr = [];
    var qty_arr = [];
    var product_img_arr = [];
    
    const query = {
        category_sub_name: req.body.subCategory,
    }

    con.query('SELECT product_name, price, product_quantity, product_img FROM product WHERE category_sub_name = ?', [query.category_sub_name], function(err,result,fields){
        con.on('error',function(err){
            console.log('[MySQL ERROR]', err);
        });
        console.log('product result', result);
        if(result && result.length){
            resultCode = 200;

            for(i=0; i<result.length; i++){
                name_arr.push(result[i].product_name);
                price_arr.push(result[i].price);
                qty_arr.push(result[i].product_quantity);
                product_img_arr.push(result[i].product_img);
            }

            const objToSend = {
                code: resultCode,
                name: name_arr,
                price: price_arr,
                qty: qty_arr,
                img: product_img_arr
            }
        
            res.json(objToSend)
        }
            
        else
        {
            resultCode = 404;
            const objToSend = {
                code: resultCode,
                name: name_arr,
                price: price_arr,
                qty: qty_arr,
                img: product_img_arr
            }
        
            res.json(objToSend)
        }
    });

})

// show best product list
app.get("/product_best", (req,res,next)=>{

    console.log('POST product');
    var resultCode;
    var product_best_arr = [];
    

    con.query('SELECT product_name, price, product_quantity, hit FROM product', function(err,result,fields){
        con.on('error',function(err){
            console.log('[MySQL ERROR]', err);
        });
        console.log('best product result', result);
        if(result && result.length){
            resultCode = 200;
            
            for(i=0; i<result.length; i++){
                var objProduct = {
                    name: result[i].product_name,
                    price: result[i].price,
                    qty: result[i].product_quantity,
                    hit: result[i].hit
                }


                product_best_arr.push(objProduct);
            }

            const objToSend = {
                code: resultCode,
                best: product_best_arr
            }
            console.log("product_best_arr result", objToSend);
        
            res.json(objToSend)
        }
            
        else
        {
            resultCode = 404;
            const objToSend = {
                code: resultCode,
                best: product_best_arr
            }
        
            res.json(objToSend)
        }
    });

})


// show product list by name
app.post("/product_name", (req,res,next)=>{

    console.log('POST product');
    var resultCode;
    var qty = 0;
    
    const query = {
        product_name: req.body.name,
    }

    con.query('SELECT product_quantity FROM product WHERE product_name = ?', [query.product_name], function(err,result,fields){
        con.on('error',function(err){
            console.log('[MySQL ERROR]', err);
        });
        console.log('product result', result);
        if(result && result.length){

            con.query('UPDATE product SET hit = hit + 1 WHERE product_name = ?', [query.product_name], function(err,result,fields){
                con.on('error',function(err){
                    console.log('[MySQL ERROR]', err);
                });
                console.log('hit updated', result);
            })
            resultCode = 200;
            
            qty = result[0].product_quantity;

            const objToSend = {
                code: resultCode,
                qty: qty
            }
        
            res.json(objToSend)
        }
            
        else
        {
            console.log('There is no product of such name', query.product_name);
            resultCode = 404;
            const objToSend = {
                code: resultCode,
                qty: qty
            }
        
            res.json(objToSend)
        }
    });

})


// signup user
app.post('/signup', (req, res) => {

    const newUser = {
        name: req.body.name,
        email: req.body.email,
        password: req.body.password
    }
    var plaint_password = newUser.password;
    var hash_data = saltHashPassword(plaint_password);
    var password_hash = hash_data.passwordHash; // Get hash value
    var salt = hash_data.salt; // Get salt
    console.log("signup");
    console.log("password_hash", password_hash);

    con.query('SELECT * FROM user where email=?', [newUser.email], function(err,result,fields){
        con.on('error',function(err){
            console.log('[MySQL ERROR]', err);
        });
        console.log('signup select:', result);
        if(result && result.length){

            res.json('User already exists!!!');
            res.status(400).send()
            console.log("already exist signup", result);
        }
        else
        {
            con.query('INSERT INTO user(email, password_hash, cash, user_name, salt) VALUES(?,?,?,?,?)',[newUser.email, password_hash, cash_default, newUser.name, salt], function(err, result, fields){
                con.on('error',function(err){
                    console.log('errorInsert [MySQL ERROR]',err);
                    res.json('Resgister error: ', err);
                });
                res.json('Register successful');
                console.log("inserted user signup:", result);
                res.status(200).send()
            })
        }
    });
})

    // const query = { email: newUser.email }

    // collection.findOne(query, (err, result) => {
        
    //     if(result == null) {
    //         collection.insertOne(newUser, (err, result) => {
    //             res.status(200).send()
    //         })
    //     } else {
    //         res.status(400).send()
    //     }
    // })


// login user
app.post('/login', (req, res) => {

    var hash;
    var hash_password;

    const query = {
        email: req.body.email,
        password: req.body.password
    }
    con.query('SELECT salt FROM user where email=?', [query.email], function(err,result,fields){
        con.on('error',function(err){
            console.log('[MySQL ERROR]', err);
        });
        if(result && result.length){
            hash = result[0].salt;
            console.log('hash', hash);
            hash_password = sha512(query.password, hash).passwordHash;
            console.log("hash_password", hash_password);
            con.query('SELECT * FROM user where (email, password_hash)=(?,?)', [query.email, hash_password], function(err,result,fields){
                console.log('1');
                con.on('error',function(err){
                    console.log('[MySQL ERROR]', err);
                });
                if(result && result.length){
                    console.log('2');
                    resultCode = 200
                    const objToSend = {
                        code: resultCode,
                        name: result[0].user_name,
                        email: result[0].email
                    }
                    res.json(objToSend)
                }
                else
                {
                    console.log('password is different');
                    resultCode = 404
                    const objToSend = {
                        code: resultCode,
                        name: '',
                        email: ''
                    }
                    res.json(objToSend)
                }
            })
        }
        else
        {
            resultCode = 404
            const objToSend = {
                code: resultCode,
                name: '',
                email: ''
            }
            res.json(objToSend)
        }
    });
})

    // var sql = 'SELECT * FROM user';
    // con.query(sql, function(err, rows, fields){
    //     if(err) console.log(err);
    //     console.log('rows', rows);
    //     console.log('files', fields);
    // })
    // con.end();


// check user
app.post('/checkUser', (req, res) => {

    const query = {
        email: req.body.email,
        name: req.body.name
    }

    con.query('SELECT * FROM user where email=?', [query.email], function(err,result,fields){
        con.on('error',function(err){
            console.log('[MySQL ERROR]', err);
        });
        console.log('checkUser selected:', result);
        if(result && result.length){
            res.status(200).send()
        }
        else
        {
            con.query('INSERT INTO user(email, user_name, cash, salt) VALUES(?,?,?,?)',[query.email, query.name, cash_default, 'a'], function(err, result, fields){
                con.on('error',function(err){
                    console.log('errorInsert [MySQL ERROR]',err);
                    res.json('checkUser error: ', err);
                });
                res.json('checkUser successful');
                console.log("inserted user checkUser:", result);
                res.status(200).send()
            })
        }
    });
})


// add product to the Cart
app.post('/addCart', (req, res) => {

    const query = {
        email: req.body.email,
        product_name: req.body.product_name,
        num: parseInt(req.body.num)
    }
    // console.log('num type', typeof query.num);
    console.log('num', query.num);
    console.log('user_email', query.email);
    console.log('user_product_name', query.product_name);

    con.query('SELECT basket_quantity FROM basket WHERE email=? AND product_name=?', [query.email, query.product_name], function(err, result, fields){
        con.on('error',function(err){
            console.log('[MySQL ERROR]', err);
        });
        console.log('basket quantity result', result);
        con.query('SELECT product_quantity FROM product WHERE product_name = ?', [query.product_name], function(err, result1, fields){
            // console.log('product_quantity', result1[0].product_quantity);
            // console.log('product_quantity type', typeof result1[0].product_quantity);
            // console.log('product_quantity-query.num type', typeof result1[0].product_quantity-query.num);
            con.query('UPDATE product SET product_quantity = ? WHERE product_name = ?', [result1[0].product_quantity-query.num, query.product_name], function(err, result2, fields){
                con.on('error', function(err){
                    console.log('[MySQL ERROR]', err)
                });
                // res.json('update product_quantity successful');
                console.log('update product_quantity successful:', result2);
                res.status(200).send()
            })
        })
        if(result.length){
            con.query('UPDATE basket SET basket_quantity = ? WHERE email = ? AND product_name = ?', [result[0].basket_quantity+query.num, query.email, query.product_name], function(err, result, fields){
                con.on('error', function(err){
                    console.log('[MySQL ERROR]', err)
                });
                // res.json('update basket successful');
                console.log('update basket successful:', result);
                res.status(200).send()
            })

        }
        else{
            con.query('INSERT INTO basket values(?, ?, ?)', [query.email, query.product_name, query.num], function(err,result,fields){
                con.on('error',function(err){
                    console.log('[MySQL ERROR]', err);
                });
                console.log("basket insert:", result);
                console.log("basket result length", result.length);
                if(result){
                    con.on('error',function(err){
                        console.log('errorInsert [MySQL ERROR]',err);
                        res.json('addCart error: ', err);
                    });
                    // res.json('addCart successful');
                    console.log("inserted ", result);
                    res.status(200).send()
                }
                else
                {
                    res.status(404).send()
                }
            });
        }
    })


})


// get basket info
app.post("/basket", (req,res,next)=>{

    console.log('POST basket');
    
    const query = {
        email: req.body.email,
    }
    var name_arr = [];
    var price_arr = [];
    var qty_arr = [];

    con.query('SELECT product_name, basket_quantity, price FROM basket NATURAL JOIN product WHERE email = ?', [query.email], function(err,result,fields){
        con.on('error',function(err){
            console.log('[MySQL ERROR]', err);
        });
        console.log('basket result', result);
        if(result && result.length){
            for(i=0;i<result.length;i++){
                name_arr.push(result[i].product_name);
            price_arr.push(result[i].price);
            qty_arr.push(result[i].basket_quantity);
            }

            resultCode = 200;

            const objToSend = {
                code: resultCode,
                name: name_arr,
                price: price_arr,
                qty: qty_arr
            }

            console.log('basket obj result', objToSend);
        
            res.json(objToSend)
        }
            
        else
        {
            resultCode = 404;
            const objToSend = {
                code: resultCode,
                name: name_arr,
                price: price_arr,
                qty: qty_arr
            }
        
            res.json(objToSend)
        }
    });

})

var dir = path.join('./image');

var mime = {
    html: 'text/html',
    txt: 'text/plain',
    css: 'text/css',
    gif: 'image/gif',
    jpg: 'image/jpeg',
    png: 'image/png',
    svg: 'image/svg+xml',
    js: 'application/javascript' 
};

// show image by product_name
app.post('/image', function (req, res) {
    var image_path;
    const query = {
        product_name: req.body.name,
    }

    con.query('SELECT product_img FROM product WHERE product_name=?', [query.product_name], function(err, result, fields){
        con.on('error',function(err){
            console.log('[MySQL ERROR]', err);
        });
        if(result && result.length)
        {
            image_path = result[0].product_img;
            console.log('product_img', result);
            var file = path.join(dir, image_path);
            if (file.indexOf(dir + path.sep) !== 0) {
                return res.status(403).end('Forbidden');
            }
            console.log('file', file);
            var type = mime[path.extname(file).slice(1)] || 'text/plain';
            console.log('type', type);
            var s = fs.createReadStream(file);

            s.on('open', function () {
                res.set('Content-Type', type);
                s.pipe(res);

            });
            s.on('error', function () {
                res.set('Content-Type', 'text/plain');

            });
        }


    })

})


// show cash by email
app.post('/cash', function (req, res) {
    var cash;
    var resultCode;
    const query = {
        email: req.body.email,
    }

    con.query('SELECT cash FROM user WHERE email=?', [query.email], function(err, result, fields){
        con.on('error',function(err){
            console.log('[MySQL ERROR]', err);
        });
        if(result && result.length)
        {

            console.log("show cash", result[0].cash);
            cash = result[0].cash;
            resultCode = 200;

            const objToSend = {
                code: resultCode,
                cash: cash
            }

            console.log('basket obj result', objToSend);
        
            res.json(objToSend)

        }
        else{
            console.log("There's no such email in user DB");
            resultCode = 404

            const objToSend = {
                code: resultCode,
                cash: cash
            }
            res.json(objToSend)
        }


    })

})

// payment by email and price of cart
app.post('/payment', function (req, res) {
    var resultCode;
    var objToSend;
    const query = {
        email: req.body.email,
        price: req.body.price,
        time: req.body.time
    }
    var email = query.email;
    var price = query.price;
    var time = query.time;
    // var datetime = new Date();
    // var datetime_str = datetime.toISOString().slice(0,10);
    // console.log('datetime str', datetime_str);

    con.query('SELECT cash FROM user WHERE email=?', [email], function(err, result, fields){
        con.on('error',function(err){
            console.log('[MySQL ERROR]', err);
        });
        if(result && result.length)
        {
            console.log("show cash", result[0].cash);
            cash = result[0].cash;
            if(cash >= price){

                con.query('UPDATE user SET cash = cash-? WHERE email = ?', [price, email], function(err, result0, fields){
                    con.on('error',function(err){
                        console.log('[MySQL ERROR]', err);
                    });
                    console.log("updated cash", result0);
                })

                con.query('SELECT * FROM basket WHERE email = ?', [email], function(err, result1, fields){
                    con.on('error',function(err){
                        console.log('[MySQL ERROR]', err);
                    });
                    console.log("selected basket", result1);
                    for(i=0; i<result1.length; i++){
                        con.query('INSERT INTO purchase(email, product_name, product_quantity, time) VALUES(?, ?, ?, ?)', [result1[i].email, result1[i].product_name, result1[i].basket_quantity, time],function(err, result2, fields){
                            con.on('error',function(err){
                                console.log('[MySQL ERROR]', err);
                            });
                            console.log("Insert purchase row", result2);
                        })
                    }
                })

                con.query('DELETE FROM basket WHERE email = ?', [email], function(err, result3, fields){
                    con.on('error',function(err){
                        console.log('[MySQL ERROR]', err);
                    });
                    console.log("deleted basket", result3);
                })

                resultCode = 200;
                objToSend = {
                    code: resultCode,
                }
                res.json(objToSend)
            }

            else{
                resultCode = 404
                objToSend = {
                    code: resultCode
                }
                res.json(objToSend)
            }
        }
    })

})

// purchase
app.post('/purchase', function (req, res) {
    var resultCode;
    const query = {
        email: req.body.email,
    }
    var email = query.email;
    var product_name_arr=[];
    var product_qty_arr=[];
    var time_arr=[];

    con.query('SELECT product_name, product_quantity, time FROM purchase WHERE email=?', [query.email], function(err, result, fields){
        con.on('error',function(err){
            console.log('[MySQL ERROR]', err);
        });
        if(result && result.length)
        {
            for(i=0; i<result.length; i++){
                product_name_arr.push(result[i].product_name);
                product_qty_arr.push(result[i].product_quantity);
                time_arr.push(result[i].time);
            }
            resultCode = 200
            const objToSend = {
                code: resultCode,
                name: product_name_arr,
                qty: product_qty_arr,
                time: time_arr
            }

            console.log('purchase obj result', objToSend);
        
            res.json(objToSend)

        }
        else{
            console.log("There's no such email in product DB");
            resultCode = 404
            const objToSend = {
                code: resultCode,
                name: product_name_arr,
                qty: product_qty_arr,
                time: time_arr
            }
        
            res.json(objToSend)
        }


    })

})


// delete cart by email and product_name
app.post('/delete_cart', function (req, res) {
    var resultCode;
    var objToSend;
    const query = {
        email: req.body.email,
        product_name: req.body.name,
        product_quantity: req.body.qty
    }
    var email = query.email;
    var product_name = query.product_name;
    var product_quantity = query.product_quantity;

    con.query('DELETE FROM basket WHERE email=? AND product_name = ?', [email, product_name], function(err, result, fields){
        con.on('error',function(err){
            console.log('[MySQL ERROR]', err);
        });
        if(result && result.length)
        {

            con.query('UPDATE product SET product_quantity = product_quantity+? WHERE product_name = ?', [product_quantity ,product_name], function(err, result, fields){
                con.on('error',function(err){
                    console.log('[MySQL ERROR]', err);
                });
            })
            console.log("show delete basket result", result[0]);

            resultCode = 200;
            // objToSend = {
            //     code: resultCode
            // }
            // res.json(objToSend)
            res.status(resultCode);
        }

        else{
            console.log("There's no such basket");
            resultCode = 404
            // objToSend = {
            //     code: resultCode
            // }
            // res.json(objToSend)
            res.status(resultCode);
        }
    })
})


//Start Server
app.listen(80, () => {
    console.log('seungwoo Restful running on port 80')
})