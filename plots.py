import matplotlib.pyplot as plt
import csv
from scipy.stats import linregress

def time_and_vertices(local_data, case, edges):
    filtered = [row for row in local_data if row[0] == case]
    filtered_edges = [row for row in filtered if row[4] == str(edges)]
    filtered_edges_time = [int(row[1]) for row in filtered_edges]
    filtered_edges_vertices = [int(row[3]) for row in filtered_edges]
    return filtered_edges_vertices, filtered_edges_time

if __name__ == "__main__":

    data = None
    with open('output/data.csv','r') as csvfile:
        data = csv.reader(csvfile, delimiter = ',')
        data = list(data)

    library_e10_vertices, library_e10_times = time_and_vertices(data, 'Library', 0)
    library_e100_vertices, library_e100_times = time_and_vertices(data, 'Library', 50000)
    library_e1000_vertices, library_e1000_times = time_and_vertices(data, 'Library', 100000)
    library_e10000_vertices, library_e10000_times = time_and_vertices(data, 'Library', 150000)

    max_time = max([max(x) for x in [library_e10_times, library_e100_times, library_e1000_times, library_e10000_times]])
    min_time = min([min(x) for x in [library_e10_times, library_e100_times, library_e1000_times, library_e10000_times]])

    x1 = linregress(library_e10_vertices, library_e10_times)
    x2 = linregress(library_e100_vertices, library_e100_times)
    x3 = linregress(library_e1000_vertices, library_e1000_times)
    x4 = linregress(library_e10000_vertices, library_e10000_times)

    fig, axs = plt.subplots(2, 2)
    axs[0, 0].plot(library_e10_vertices, library_e10_times, '--bo')
    axs[0, 0].plot([0, 1_000_000], [x1.intercept, x1.intercept + 1_000_000 * x1.slope], '--g')
    axs[0, 0].set_title('Edges = 0')
    axs[0, 0].set_ylim([min_time, max_time])

    axs[0, 1].plot(library_e100_vertices, library_e100_times, '--bo')
    axs[0, 1].plot([0, 1_000_000], [x2.intercept, x2.intercept + 1_000_000 * x2.slope], '--g')
    axs[0, 1].set_title('Edges = 50,000')
    axs[0, 1].set_ylim([min_time, max_time])

    axs[1, 0].plot(library_e1000_vertices, library_e1000_times, '--bo')
    axs[1, 0].plot([0, 1_000_000], [x3.intercept, x3.intercept + 1_000_000 * x3.slope], '--g')
    axs[1, 0].set_title('Edges = 100,000')
    axs[1, 0].set_ylim([min_time, max_time])

    axs[1, 1].plot(library_e10000_vertices, library_e10000_times, '--bo')
    axs[1, 1].plot([0, 1_000_000], [x4.intercept, x4.intercept + 1_000_000 * x4.slope], '--g')
    axs[1, 1].set_title('Edges = 150,000')
    axs[1, 1].set_ylim([min_time, max_time])


    for ax in axs.flat:
            ax.set(xlabel='Vertices', ylabel='Milliseconds')

    # TODO leads to misleading axis
    for ax in axs.flat:
        ax.label_outer()

    # fig.supxlabel('Vertices')
    # fig.supylabel('Milliseconds')
    fig.suptitle('Library Implementation')
    fig.set_size_inches(18.5, 10.5)
    plt.savefig('output/constant_edges.png', bbox_inches='tight', dpi=150)
