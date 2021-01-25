var express = require("express");
var bodyParser = require('body-parser');


var app = express();
app.use(bodyParser.urlencoded({ extended: true }));
app.use(bodyParser.json());


var mysql = require('mysql');
const bcrypt = require('bcrypt');
const saltRounds = 10;

var connection = mysql.createConnection({
  host     : 'localhost',
  user     : 'root',
  password : '',
  database : "gachamondb"
});

connection.connect(function(err) {
  if (err) {
    console.error('error connecting: ' + err.stack);
    return;
  }
  console.log('connected as id ' + connection.threadId);
});
register = async function(req,res){
  const password = req.body.password;
  const encryptedPassword = await bcrypt.hash(password, saltRounds)

  var users={
     "email":req.body.email,
     "password":encryptedPassword,
     "pokemons":"1, 4, 7"
   }
  
  connection.query('INSERT INTO users SET ?',users, function (error, results, fields) {
    if (error) {
        res.send('400');
    } else {
        res.send('200');
      }
  });
}

login = async function(req,res){
  var email= req.body.email;
  var password = req.body.password;
  connection.query('SELECT * FROM users WHERE email = ?',[email], async function (error, results, fields) {
    if (error) {
        res.send('400');
    }else{
      if(results.length >0){
        const comparision = await bcrypt.compare(password, results[0].password)
        if(comparision){
            res.send('200');
        }
        else{
            res.send('204');
        }
      }
      else{
            res.send('206');
      }
    }
    });
}

getpokemons = async function(req,res){
  var email= req.body.email
  //console.log(email)
  connection.query('SELECT pokemons FROM users WHERE email = ?',email, async function (error, results, fields) {
      if(results.length >0){
            res.send(results)
        }
        else{
            res.send('204');
        }
      })
    }
    
postpokemons = async function(req,res){
    var pokelist=req.body.pokelist    
    var email=req.body.email
    connection.query('UPDATE users SET pokemons=CONCAT(IFNULL(pokemons,""),", ",?) WHERE email=?',[pokelist,email],function (error, results, fields) {
    //console.log(pokelist)
        if (error) {
            res.send('400');
        } else {
            res.send('200');
          }
      });
    }



var router = express.Router();

router.post('/register',register);
router.post('/login',login);
router.post('/addpokemons',postpokemons);
router.post('/getpokemons',getpokemons);



app.use(router);
app.listen(4000);