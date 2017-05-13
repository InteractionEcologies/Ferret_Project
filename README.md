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

In the 'database' folder, check server.py file to specify the port number. It is specified as 5001 for now. This port number is identical to the Ferret Logger file's configuration, which is in ferret/logger/app/src/main/java/com/google/research/ic/alogger/URLConstants.java file. Make sure the port number in the server.py file is identical to the URLConstants.java file's configuration.

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


### Step2. Modifying the web page ###
There are three big parts in the index.html file.

- Description for set up
- Description for tasks
- Appetize.io emulator interface

In order to change the description for tasks without changing other parts, start from the line 88 in the index.html file.


### Step3. Gather interaction logs ###
- This project can be published as Amazon Mechanical Turk HIT to get a lot of different interaction logs. When publishing a HIT, the format of it would be 'Survey'. Instead of providing the link for the survey, use the web page link. Specify other conditions to gather more qualified interaction logs. (e.g. 95% or higher acceptance rate, at least 500 approved HITs, region)
- Download a csv file 


### Step4.  ###
