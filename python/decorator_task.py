import math as m
from functools import wraps


def infinite_list():
    n = 0
    while True:
        yield n
        n += 1


def block_ten_dec(func):
    def wrapper(*args, **kwargs):
        gen = func(*args, **kwargs)
        bloks_dim = 10
        for value in gen:
            res = []
            n_bloks = m.ceil(len(value)/bloks_dim)
            for b in range(n_bloks):
                res.append(value[b*bloks_dim:(b+1)*bloks_dim])
            yield res
    return wrapper


def block_dec(argument):
    def block_ten_dec(func):
        def wrapper(*args, **kwargs):
            gen = func(*args, **kwargs)
            bloks_dim = argument
            for value in gen:
                res = []
                n_bloks = m.ceil(len(value) / bloks_dim)
                for b in range(n_bloks):
                    res.append(value[b * bloks_dim:(b + 1) * bloks_dim])
                yield res
        return wrapper
    return block_ten_dec


def fib_gen():
    fib_nums = []
    for elem in infinite_list():
        fib_nums.append(elem if elem in {0, 1} else fib_nums[-1]+fib_nums[-2])
        yield fib_nums

print("First Decorator TenDec")
iter = block_ten_dec(fib_gen)()
for i in range(100):
    print(next(iter))

print("Parametric iterator with parameter 5")
iter = (block_dec(5)(fib_gen))()
for i in range(100):
    print(next(iter))


