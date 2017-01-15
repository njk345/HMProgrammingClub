import matplotlib.pyplot as plt
import sys
from math import sqrt
import random

def get_input():
    f = open('input.txt')
    pts = []
    for line in f:
        x, y = [float(e) for e in line.strip().split()]
        pts.append((x, y))
    return pts
    
def get_random(npts):
    pts = []
    for _ in range(npts):
        pts.append((random.random(), random.random()))
    return pts
    
def plot(pts, edges):
    xdata = [p[0] for p in pts]
    ydata = [p[1] for p in pts]
    plt.plot(xdata, ydata, 'r.')
    xdata1 = []
    ydata1 = []
    for e in edges:
        xdata1.append(pts[e[0]][0])
        xdata1.append(pts[e[1]][0])
        xdata1.append(None)
        ydata1.append(pts[e[0]][1])
        ydata1.append(pts[e[1]][1])
        ydata1.append(None)
    plt.plot(xdata1, ydata1)
    
def argmin(cs, qs):
    cmin = sys.float_info.max
    v = -1
    for i in range(len(cs)):
        if qs[i] and cs[i] < cmin:
            cmin = cs[i]
            v = i
    return v
    
def distance(a, b):
    x = a[0] - b[0]
    y = a[1] - b[1]
    return sqrt(x * x + y * y)
    
def mst(pts):
    qs = [True for _ in pts]
    es = [None for _ in pts]
    cs = [sys.float_info.max for _ in pts]
    edges = []
    npts = len(pts)
    while len(edges) < npts - 1:
        v = argmin(cs, qs)
        qs[v] = False
        if es[v] != None:
            edges.append(es[v])
        for w in range(npts):
            d = distance(pts[v], pts[w])
            if d < cs[w]:
                cs[w] = d
                es[w] = (v, w)
    return edges
    
pts = get_random(1000)
edges = mst(pts)
print('len edges =', len(edges))
plot(pts, edges)