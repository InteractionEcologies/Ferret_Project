import csv

def get_by_file(filename):
	turker_list = []
	with open(filename) as csvfile:
		csvreader = csv.reader(csvfile, delimiter=',', quotechar='"')
		for row in csvreader:
			if row[0] == "Worker ID":
				continue
			else:
				turker_list.append(row[0])
	return turker_list