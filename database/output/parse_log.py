import difflib


files = ['log/modified_log-1.txt', 'log/modified_log-2.txt', 'log/modified_log-3.txt', 'log/modified_log-4.txt', 'log/modified_log-5.txt', 'log/modified_log-6.txt', 'log/modified_log-7.txt', 'log/modified_log-8.txt', 'log/modified_log-9.txt', 'log/new_modified_log-10.txt']

standard = 'log/modified_log-11.txt'

results = {}

index = 1
for item in files:
	f = open(item, 'r')
	for line in f:
		if 'CLICK-android.widget.' in line:
			interim = line.split('CLICK-android.widget.')[1]
			if index in results:
				results[index].append(interim.split('-com')[0].replace("-"," "))
			else:
				results[index] = [interim.split('-com')[0].replace("-"," ")]
	index += 1
	f.close()

standard_lst = []
f = open(standard, 'r')
for line in f:
	if 'CLICK-android.widget.' in line:
		interim = line.split('CLICK-android.widget.')[1]
		standard_lst.append(interim.split('-com')[0].replace("-"," "))
f.close()

for key in results:
	diff = difflib.SequenceMatcher(None, standard_lst, results[key])
	print(key, diff.ratio())
