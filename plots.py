import matplotlib.pyplot as plt
import csv

def timeAndVertices(data, case, edges):
    filtered = [row for row in data if row[0] == case]
    filtered_edges = [row for row in filtered if row[4] == str(edges)]
    filtered_edges_time = [row[1] for row in filtered_edges]
    filtered_edges_vertices = [row[3] for row in filtered_edges]
    return filtered_edges_vertices, filtered_edges_time

if __name__ == "__main__":

    data = None
    with open('output/data.csv','r') as csvfile:
        data = csv.reader(csvfile, delimiter = ',')
        data = list(data)

    library_e10_vertices, library_e10_times = timeAndVertices(data, 'Library', 10)
    library_e100_vertices, library_e100_times = timeAndVertices(data, 'Library', 100)
    library_e1000_vertices, library_e1000_times = timeAndVertices(data, 'Library', 1000)
    library_e10000_vertices, library_e10000_times = timeAndVertices(data, 'Library', 10000)

    fig, axs = plt.subplots(2, 2)
    axs[0, 0].plot(library_e10_vertices, library_e10_times, '--bo')
    axs[0, 0].set_title('Edges = 10')
    axs[0, 1].plot(library_e100_vertices, library_e100_times, '--bo')
    axs[0, 1].set_title('Edges = 100')
    axs[1, 0].plot(library_e1000_vertices, library_e1000_times, '--bo')
    axs[1, 0].set_title('Edges = 1,000')
    axs[1, 1].plot(library_e10000_vertices, library_e10000_times, '--bo')
    axs[1, 1].set_title('Edges = 10,000')

    for ax in axs.flat:
        ax.set(xlabel='x-label', ylabel='y-label')

    # Hide x labels and tick labels for top plots and y ticks for right plots.
    for ax in axs.flat:
        ax.label_outer()

    # plt.plot(vertices, time, color = 'g')
    # plt.xlabel('Edges')
    # plt.ylabel('Seconds')
    # plt.title('Library with Varying Edges (v = ~100,000)')
    # plt.legend()
    plt.show()
