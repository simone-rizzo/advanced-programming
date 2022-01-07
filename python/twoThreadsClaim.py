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
                # print("started thread: "+str(index))
                for j in range(seq_iter):
                    func(*args, **kwargs)
                # print("ended thread n:"+str(index))

            exec_times = []
            for n in range(iter):
                threads_list = []
                start_time = time.perf_counter()               # take the start time
                for i in range(n_threads):              # spawning thread
                    t = Thread(target=thread, args=(i,))
                    threads_list.append(t)
                    t.start()
                for t in threads_list:                  # joining all thread
                    t.join()
                end_time = time.perf_counter() - start_time
                exec_times.append(end_time)             # take the end time

            res_dict = {'fun': func.__name__, 'args': args, 'n_threads': n_threads,
                        'seq_iter': seq_iter, 'iter': iter, 'mean': statistics.mean(exec_times),
                        'variance': statistics.variance(exec_times, statistics.mean(exec_times))}
            return res_dict
        return wrapper
    return bench_inner


def somma(a, b):
    time.sleep(0.001)
    return a + b


def just_wait(n): # NOOP for n/10 seconds
    time.sleep(n * 0.1)


def grezzo(n): # CPU intensive
    for i in range(2**n):
        pass


# res = bench()(somma)(3,5)
"""res = bench(n_threads=1, seq_iter=16, iter=1)(grezzo)(20)
print(res)
res = bench(n_threads=2, seq_iter=8, iter=1)(grezzo)(20)
print(res)
res = bench(n_threads=4, seq_iter=4, iter=1)(grezzo)(20)
print(res)
res = bench(n_threads=8, seq_iter=2, iter=1)(grezzo)(20)
print(res)"""


def test(iter, fun, args):
    f = open("./"+fun.__name__+"_"+str(args)+"_1_16", "w")
    res = bench(n_threads=1, seq_iter=16, iter=iter)(fun)(args)
    f.write(str(res))
    f.close()

    f = open("./" + fun.__name__ + "_" + str(args) + "_2_8", "w")
    res = bench(n_threads=2, seq_iter=8, iter=iter)(fun)(args)
    f.write(str(res))
    f.close()

    f = open("./" + fun.__name__ + "_" + str(args) + "_4_4", "w")
    res = bench(n_threads=4, seq_iter=4, iter=iter)(fun)(args)
    f.write(str(res))
    f.close()

    f = open("./" + fun.__name__ + "_" + str(args) + "_8_2", "w")
    res = bench(n_threads=8, seq_iter=2, iter=iter)(fun)(args)
    f.write(str(res))
    f.close()


test(10, grezzo, 20) #test proved
test(10, just_wait, 1)

