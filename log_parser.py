from sys import argv
# store the command parameters in variable
script, file = argv
ts_total = 0
tj_total = 0
count = 0
with open(file, 'r') as f:
    lines = f.readlines()
    for line in lines:
        content = line.strip().split(" ")
        ts_total += float(content[0])
        tj_total += float(content[1])
    count = len(lines)
ts_average = ts_total/count
tj_average = tj_total/count
print("for {0} entries, ts_average: {1}, tj_average: {2}".format(count, ts_average, tj_average))
