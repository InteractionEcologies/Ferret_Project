import sqlite3
from getTurker import get_by_file

conn = sqlite3.connect('database.db')
cur = conn.cursor()

user_info = get_by_file('turker.csv')

for i in range(len(user_info)):
	cur.execute('select * from UserLogs where turker=?', (user_info[i],))
	fname = 'log/user'+str(i+1)+'.log'
	f = open(fname, 'a')
	for row in cur.fetchall():
		f.write(row[2])
		f.write("\n")
	f.close()

conn.close()
