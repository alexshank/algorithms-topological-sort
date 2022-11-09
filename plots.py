import matplotlib.pyplot as plt
import csv

time = []
vertices = []
edges = []

with open('output/data.csv','r') as csvfile:
    plots = csv.reader(csvfile, delimiter = ',')

    library = [row for row in plots if row[0] == 'Library' and row[3] == '100000']
    dfs = [row for row in plots if row[0] == 'DFS']

    for row in library:
        print(row)
        time.append(row[1])
        vertices.append(int(row[4]))
        edges.append(int(row[5]))


# for row in dfs:
    #     print(row)
    #     x.append(row[0])
    #     y.append(int(row[2]))

plt.plot(vertices, time, color = 'g')
plt.xlabel('Edges')
plt.ylabel('Seconds')
plt.title('Library with Varying Edges (v = ~100,000)')
plt.legend()
plt.show()