# Ferret - Interaction Logger

## About the project
This project is for helping UX researchers to collect user logs and determine similarities among them by using Ferret Logger and Ferret Analyzer. The Java code for these applications are in 'ferret' folder.
In order to get as many user logs as possible, the Ferret Logger application is wrapped by a mobile application emulator called [Appetize.io](https://appetize.io), and hosted by a web page. The HTML code for this web page is in 'web' folder.
As soon as users perform tasks in the web page, their interaction logs are stored in the sqlite database. The Python codes for running the server, retrieving the data from the database, and analyzing the data to determine the similarities are in 'database' folder.

## How to start Ferret project
