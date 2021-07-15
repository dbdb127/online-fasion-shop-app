app.post('/checkUser', (req, res) => {

    const query = {
        email: req.body.email,
        name: req.body.name
    }

    con.query('SELECT * FROM user where email=?', [query.email], function(err,result,fields){
        con.on('error',function(err){
            console.log('[MySQL ERROR]', err);
        });
        if(result && result.length){
            res.status(200).send()
        }
        else
        {
            con.query('INSERT INTO user(email, user_name) VALUES(?,?)',[query.email, query.name], function(err, result, fields){
                con.on('error',function(err){
                    console.log('errorInsert [MySQL ERROR]',err);
                    res.json('Resgister error: ', err);
                });
                res.json('Register successful');
                console.log("inserted user signup", result);
                res.status(200).send()
            })
        }
    });
})