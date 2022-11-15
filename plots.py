import matplotlib.pyplot as plt
import csv
from scipy.stats import linregress
import pandas

def time_and_vertices(local_data, case, edges):
    filtered = [row for row in local_data if row[0] == case]
    filtered_edges = [row for row in filtered if row[4] == str(edges)]
    filtered_edges_time = [int(row[1]) for row in filtered_edges]
    filtered_edges_vertices = [int(row[3]) for row in filtered_edges]
    return filtered_edges_vertices, filtered_edges_time

def time_and_edges(local_data, case, vertices):
    filtered = [row for row in local_data if row[0] == case]
    filtered_edges = [row for row in filtered if row[2] == str(vertices)]
    filtered_edges_time = [int(row[1]) for row in filtered_edges]
    filtered_edges_vertices = [int(row[5]) for row in filtered_edges]
    return filtered_edges_vertices, filtered_edges_time

def time_and_sum(local_data, case):
    filtered = [row for row in local_data if row[0] == case]
    filtered_edges_time = [int(row[1]) for row in filtered]
    filtered_edges_vertices = [int(row[5]) + int(row[3]) for row in filtered]
    return filtered_edges_vertices, filtered_edges_time

if __name__ == "__main__":

    # data = None
    # with open('output/many_edges_data.csv', 'r') as csvfile:
    #     data = csv.reader(csvfile, delimiter = ',')
    #     data = list(data)

    data = pandas.read_csv('output/many_edges_data.csv')
    # print(data.head)
    data = data.query("type == 'Library'")
    print(data.head)
    row_count = data.shape[0]
    # print(row_count)
    print(data['actualVertexCount'])
    print([data.at[int((row_count - 1) / i), 'actualVertexCount'] for i in reversed(range(1,5))])
    print([data.at[int((row_count - 1) / i), 'actualEdgeCount'] for i in reversed(range(1,5))])
# test_edges = [data['actualVertexCount'][int(row_count / i) - 10] for i in range(1, 4)]
    # pull 4 vertices
    # print(test_edges)


    plt.imshow(a, cmap='hot', interpolation='nearest')
    plt.show()


    # TODO add a column to data frame with all the linear fit metrics (may have to be separate data frame?
    # TODO this would be the primary table for our report



    # # constant edges
    # library_e10_vertices, library_e10_times = time_and_vertices(data, 'Simple', 46)
    # library_e100_vertices, library_e100_times = time_and_vertices(data, 'Simple', 312)
    # library_e1000_vertices, library_e1000_times = time_and_vertices(data, 'Simple', 550)
    # library_e10000_vertices, library_e10000_times = time_and_vertices(data, 'Simple', 4450)
    #
    # max_time = max([max(x) for x in [library_e10_times, library_e100_times, library_e1000_times, library_e10000_times]])
    # min_time = min([min(x) for x in [library_e10_times, library_e100_times, library_e1000_times, library_e10000_times]])
    #
    # x1 = linregress(library_e10_vertices, library_e10_times)
    # x2 = linregress(library_e100_vertices, library_e100_times)
    # x3 = linregress(library_e1000_vertices, library_e1000_times)
    # x4 = linregress(library_e10000_vertices, library_e10000_times)
    #
    # fig_1, axs_1 = plt.subplots(2, 2)
    # axs_1[0, 0].plot(library_e10_vertices, library_e10_times, '--bo')
    # axs_1[0, 0].plot([0, 1_000_000], [x1.intercept, x1.intercept + 1_000_000 * x1.slope], '--g')
    # axs_1[0, 0].set_title('Edges = 0')
    # axs_1[0, 0].set_ylim([min_time, max_time])
    #
    # axs_1[0, 1].plot(library_e100_vertices, library_e100_times, '--bo')
    # axs_1[0, 1].plot([0, 1_000_000], [x2.intercept, x2.intercept + 1_000_000 * x2.slope], '--g')
    # axs_1[0, 1].set_title('Edges = 50,000')
    # axs_1[0, 1].set_ylim([min_time, max_time])
    #
    # axs_1[1, 0].plot(library_e1000_vertices, library_e1000_times, '--bo')
    # axs_1[1, 0].plot([0, 1_000_000], [x3.intercept, x3.intercept + 1_000_000 * x3.slope], '--g')
    # axs_1[1, 0].set_title('Edges = 100,000')
    # axs_1[1, 0].set_ylim([min_time, max_time])
    #
    # axs_1[1, 1].plot(library_e10000_vertices, library_e10000_times, '--bo')
    # axs_1[1, 1].plot([0, 1_000_000], [x4.intercept, x4.intercept + 1_000_000 * x4.slope], '--g')
    # axs_1[1, 1].set_title('Edges = 150,000')
    # axs_1[1, 1].set_ylim([min_time, max_time])
    #
    # for ax in axs_1.flat:
    #     ax.set(xlabel='Vertices', ylabel='Milliseconds')
    #     ax.label_outer() # TODO can be misleading if ranges are different
    #
    # fig_1.suptitle('Simple Implementation')
    # fig_1.set_size_inches(18.5, 10.5)
    # plt.savefig('output/constant_edges.png', bbox_inches='tight', dpi=150)

    # # constant vertices
    # library_e10_vertices, library_e10_times = time_and_edges(data, 'Simple', 3511014)
    # library_e100_vertices, library_e100_times = time_and_edges(data, 'Simple', 4248326)
    # library_e1000_vertices, library_e1000_times = time_and_edges(data, 'Simple', 5140473)
    # library_e10000_vertices, library_e10000_times = time_and_edges(data, 'Simple', 6219972)
    #
    # max_time = max([max(x) for x in [library_e10_times, library_e100_times, library_e1000_times, library_e10000_times]])
    # min_time = min([min(x) for x in [library_e10_times, library_e100_times, library_e1000_times, library_e10000_times]])
    #
    # x1 = linregress(library_e10_vertices, library_e10_times)
    # x2 = linregress(library_e100_vertices, library_e100_times)
    # x3 = linregress(library_e1000_vertices, library_e1000_times)
    # x4 = linregress(library_e10000_vertices, library_e10000_times)
    #
    # fig_2, axs_2 = plt.subplots(2, 2)
    # axs_2[0, 0].plot(library_e10_vertices, library_e10_times, '--bo')
    # axs_2[0, 0].plot([0, 570_000], [x1.intercept, x1.intercept + 570_000 * x1.slope], '--g')
    # axs_2[0, 0].set_title('Vertices = 70,000')
    # axs_2[0, 0].set_ylim([min_time, max_time])
    #
    # axs_2[0, 1].plot(library_e100_vertices, library_e100_times, '--bo')
    # axs_2[0, 1].plot([0, 570_000], [x2.intercept, x2.intercept + 570_000 * x2.slope], '--g')
    # axs_2[0, 1].set_title('Vertices = 140,000')
    # axs_2[0, 1].set_ylim([min_time, max_time])
    #
    # axs_2[1, 0].plot(library_e1000_vertices, library_e1000_times, '--bo')
    # axs_2[1, 0].plot([0, 570_000], [x3.intercept, x3.intercept + 570_000 * x3.slope], '--g')
    # axs_2[1, 0].set_title('Vertices = 210,000')
    # axs_2[1, 0].set_ylim([min_time, max_time])
    #
    # axs_2[1, 1].plot(library_e10000_vertices, library_e10000_times, '--bo')
    # axs_2[1, 1].plot([0, 570_000], [x4.intercept, x4.intercept + 570_000 * x4.slope], '--g')
    # axs_2[1, 1].set_title('Vertices = 280,000')
    # axs_2[1, 1].set_ylim([min_time, max_time])
    #
    # for ax in axs_2.flat:
    #     ax.set(xlabel='Edges', ylabel='Milliseconds')
    #     ax.label_outer() # TODO can be misleading if ranges are different
    #
    # fig_2.suptitle('Simple Implementation')
    # fig_2.set_size_inches(18.5, 10.5)
    # plt.savefig('output/constant_vertices.png', bbox_inches='tight', dpi=150)
    #
    # # vertices + edges
    # library_e10_vertices, library_e10_times = time_and_sum(data, 'Simple')
    # x1 = linregress(library_e10_vertices, library_e10_times)
    # plt.figure()
    # plt.scatter(library_e10_vertices, library_e10_times)
    # # TODO add the fit metric to the plot
    # plt.plot([0, max(library_e10_vertices)], [x1.intercept, x1.intercept + max(library_e10_vertices) * x1.slope], '--r')
    # plt.savefig('output/sum.png', bbox_inches='tight', dpi=150)


