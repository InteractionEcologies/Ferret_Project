# Ferret - Interaction Logger
This project is for helping UX researchers to collect user logs and determine similarities among them by using Ferret Logger and Ferret Analyzer. 

## About the directory hierarchy
The Java codes for Ferret Logger and Ferret Analyzer applications are in 'ferret' folder.
In order to get as many user logs as possible, the Ferret Logger application is wrapped by a mobile application emulator called [Appetize.io](https://appetize.io), and hosted by a web page. The HTML code for this web page is in 'web' folder.
As soon as users perform tasks in the web page, their interaction logs are stored in the sqlite database. The Python codes for running the server, retrieving the data from the database, and analyzing the data to determine the similarities are in 'database' folder.

## Initialize a Ferret project
### Step1. Run servers ###
This project requires two servers - one for the database, the other for hosting web pages.

#### Running a database server ####

In the 'database' folder, check server.py file to specify the port number. It is specified as 5001 for now.

```
if __name__ == "__main__":
    app.run(host='0.0.0.0', port=5001)
```

After you specified the port number, run the server.
```
python server.py
```


#### Running a web hosting server ####

The webpage can be hosted by using a Python module 'SimpleHTTPServer'. In order to run this server, go into the 'web' folder, and run the following command.
```
python -m SimpleHTTPServer 8080
```
You can specify the port number in this command. Put a different number in the place of 8080.


#### Modifying the web page ####
