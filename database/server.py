from flask import Flask, request
import sqlite3

app = Flask(__name__)
DATABASE = 'output/database.db'

def insert_readings(turker, task, log, time):
    DATABASE = 'output/database.db'
    with sqlite3.connect(DATABASE) as con:
        cur = con.cursor()
        cur.execute('CREATE TABLE IF NOT EXISTS UserLogs (turker TEXT, task TEXT, log TEXT, time_stamp TEXT)')
        cur.execute('INSERT INTO UserLogs (turker, task, log, time_stamp) VALUES (?,?,?,?)', (turker, task, log, time))
        print "Here"
        con.commit()

@app.route("/", methods=['GET', 'POST'])
def index():
    if request.method=="POST":
        insert_readings(request.form['turk_id'], request.form['task_id'], request.form['user_log'], request.form['time'])
        return "Post Request"
    else:
        return "No Request"

if __name__ == "__main__":
    app.run(host='0.0.0.0', port=5001)