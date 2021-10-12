# stoic_calendar_server

This is the server for the "Stoid Calendar App"


Port = 8080


Available endpoints:

POST /register - expects a request body with an object with of the following structure:
{
  "username" : "username", 
  "password": "password"
}

POST /login - expects a request body with the same object as in /register. If the login is successful it returns a jwt token
{
    "token": "<token>"
}
