app.post('/addCart', (req, res) => {

    const query = {
        email: req.body.email,
        product_name: req.body.product_name,
        num: req.body.num
    }

    con.query('INSERT basket values(?, ?, ?)', [query.email, query.product_name, query.num], function(err,result,fields){
        con.on('error',function(err){
            console.log('[MySQL ERROR]', err);
        });
        if(result && result.length){
            con.on('error',function(err){
                console.log('errorInsert [MySQL ERROR]',err);
                res.json('Resgister error: ', err);
            });
            res.json('Register successful');
            console.log("inserted user signup", result);
            res.status(200).send()
        }
        else
        {
            res.status(404).send()
        }
    });
})