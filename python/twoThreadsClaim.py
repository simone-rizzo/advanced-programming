import time
from threading import *
import statistics


def bench(n_threads=1, seq_iter=1, iter=1):
    """

    :param n_threads: The number of threads
    :param seq_iter:  The number of times fun must be invoked in each thread
    :param iter:      The number of times the whole execution of the n_threads
                      threads, each invoking seq_iter times fun, is repeated.
    :return: dictionary
    """
    def bench_inner(func):
        def wrapper(*args, **kwargs):
            def thread(index):
                for i in range(seq_iter):
                    func(*args, **kwargs)
                print("ended thread n:"+str(index))

            exec_times = []
            for n in range(iter):
                threads_list = []                       # take the start time
                start_time = time.time()
                for i in range(n_threads):              #spawning thread
                    t = Thread(target=thread, args=(i,))
                    threads_list.append(t)
                    t.start()
                for t in threads_list:                  #joining all thread
                    t.join()
                end_time = time.time() - start_time
                exec_times.append(end_time)             # take the end time

            res_dict = {'fun': func.__name__, 'args': args, 'n_threads': n_threads,
                        'seq_iter': seq_iter, 'iter': iter, 'mean':statistics.mean(exec_times),
                        'variance':statistics.variance(exec_times, statistics.mean(exec_times))}
            return res_dict
        return wrapper
    return bench_inner


@bench(n_threads=6, seq_iter=1000, iter=10)
def somma(a, b):
    return a + b

# res = bench()(somma)(3,5)
res = somma(1,2)
print(res)