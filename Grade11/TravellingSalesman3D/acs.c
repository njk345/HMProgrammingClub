#include <stdio.h>
#include <stdlib.h>
#include <math.h>
#include <float.h>
#include <stdbool.h>

void read_input(FILE *fp, int len, int dim, int pts[][dim])
{
    for (int i = 0; i < len; i++) {
        for (int j = 0; j < dim; j++) {
            fscanf(fp, "%d", &pts[i][j]);
        }
    }
}

double randunif(void)
{
    return random() / (double)RAND_MAX;
}

double randint(int a, int b)
{
    return (int)(a + (b - a + 1) * randunif());
}

double dist(int dim, int a[], int b[])
{
    double total = 0;
    for (int j = 0; j < dim; j++)
        total += pow(a[j] - b[j], 2.0);
    total = sqrt(total);
    return total;
}

double score(int len, int dim, int pts[][dim], int path[])
{
    double total = 0;
    for (int i = 0; i < len - 1; i++) {
        total += dist(dim, pts[path[i]], pts[path[i+1]]);
    }
    total += dist(dim, pts[path[len-1]], pts[path[0]]);
    return total;
}

void get_distances(int len, int dim, int pts[][dim], 
                   double distances[][len])
{
    for (int i = 0; i < len; i++) {
        for (int j = 0; j < len; j++) {
            distances[i][j] = dist(dim, pts[i], pts[j]);
        }
    }
}

int antwalk_next(int i, int len, double alpha, double beta, double q0,
                 double distances[][len], double pher[][len],
                 bool visited[])
{
    double totprob = 0;
    double probs[len];
    double maxprob = -1;
    int maxj;

    for (int j = 0; j < len; j++) {
        if (!visited[j]) {
            double pr = pow(pher[i][j], alpha) / pow(distances[i][j], beta);
            probs[j] = pr;
            totprob += pr;
            if (pr > maxprob) {
                maxprob = pr;
                maxj = j;
            }
        }
        else {
            probs[j] = 0;
        }
    }
    for (int j = 0; j < len; j++) {
        probs[j] /= totprob;
    }

    double x = randunif();
    if (x < q0) {
        return maxj;
    }

    double r = randunif();
    double cumprob = 0;
    for (int j = 0; j < len; j++) {
        cumprob += probs[j];
        if (cumprob > r) return j;
    }

    fprintf(stderr, "ant couldn't find next step\n");
    exit(1);  // should never happen -- terminate program if it does
}

double antwalk(int len, double alpha, double beta, double rho, double q0,
               double Q, double tau0, double distances[][len], double pher[][len],
               int start, int path[])
{
    bool visited[len];
    for (int i = 0; i < len; i++) {
        visited[i] = false;
    }

    path[0] = start;
    visited[path[0]] = true;

    double total = 0;
    for (int i = 1; i < len; i++) {
        int j = antwalk_next(path[i-1], len, alpha, beta, q0, distances, pher, visited);
        path[i] = j;
        visited[j] = true;
        double d = distances[path[i-1]][path[i]];
        total += d;
    }

    total += distances[path[len-1]][path[0]];
    return total;
}

void update_pher(int len, int nants, double rho,
                 double Q, double tau0,
                 double best_pathlen, int best_path[], 
                 int paths[][len], double pher[][len])
{
    // evaporation
    for (int i = 0; i < len; i++) {
        for (int j = 0; j < len; j++) {
            pher[i][j] *= (1 - rho);
        }
    }

    // local optimizations
    for (int k = 0; k < nants; k++) {
        for (int i = 0; i < len; i++) {
            int idx1 = paths[k][i];
            int idx2 = paths[k][(i+1)%len];
            double dp = rho * tau0;
            pher[idx1][idx2] += dp;
            pher[idx2][idx1] += dp;
        }
    }

    // global optimization
    for (int i = 0; i < len; i++) {
        int idx1 = best_path[i];
        int idx2 = best_path[(i+1)%len];
        double dp = rho * Q / best_pathlen;
        pher[idx1][idx2] += dp;
        pher[idx2][idx1] += dp;
    }
}
               
double antsystem(int niter, int nants, int len, 
                 double alpha, double beta, double rho, double q0,
                 double Q, double tau0,
                 double distances[][len], int best_path[])
{
    int paths[nants][len], starts[nants];
    double pathlen[nants];
    double pher[len][len];

    for (int i = 0; i < len; i++) {
        for (int j = 0; j < len; j++) {
            pher[i][j] = tau0;
        }
    }

    double best_score = DBL_MAX;
    int best_ant = -1;
    int best_iter = -1;

    for (int k = 0; k < nants; k++) {
        starts[k] = randint(0, len - 1);
    }

    for (int n = 0; n < niter; n++) {
        for (int k = 0; k < nants; k++) {
            double sc = antwalk(len, alpha, beta, rho, q0, Q, tau0, distances,
                                pher, starts[k], paths[k]);
            if (sc < best_score) {
                best_score = sc;
                best_ant = k;
                best_iter = n;
                for (int i = 0; i < len; i++) {
                    best_path[i] = paths[k][i];
                }
                printf("..new best: iter = %d; ant = %d; score = %g\n",
                       best_iter, best_ant, best_score);
            }
            pathlen[k] = sc;
        }
        update_pher(len, nants, rho, Q, tau0, best_score, best_path, paths, pher);
    }
    return best_score;       
}

int main(int argc, char *argv[])
{
    char buf[32];
    int case_lo, case_hi, niter, nants, len, dim;
    double alpha, beta, rho, q0, Q, tau0;

    if (argc != 2) {
        printf("usage: %s <infile>\n", argv[0]);
        return EXIT_SUCCESS;
    }

    FILE *fp = fopen(argv[1], "r");
    fscanf(fp, "%s %d", buf, &case_lo);
    fscanf(fp, "%s %d", buf, &case_hi);
    fscanf(fp, "%s %d", buf, &niter);
    fscanf(fp, "%s %d", buf, &nants);
    fscanf(fp, "%s %d", buf, &len);
    fscanf(fp, "%s %d", buf, &dim);
    fscanf(fp, "%s %lf", buf, &alpha);
    fscanf(fp, "%s %lf", buf, &beta);
    fscanf(fp, "%s %lf", buf, &rho);
    fscanf(fp, "%s %lf", buf, &q0);
    fscanf(fp, "%s %lf", buf, &Q);
    fscanf(fp, "%s %lf", buf, &tau0);
    fclose(fp);

    int pts[len][dim];
    double distances[len][len];
    int path[len];

    double total = 0;

    fprintf(stderr, "Nick Keirstead\n");

    for (int n = case_lo; n <= case_hi; n++) {
        sprintf(buf, "input/%d.txt", n);
        FILE *fp = fopen(buf, "r");
        read_input(fp, len, dim, pts);
        fclose(fp);

        get_distances(len, dim, pts, distances);

        printf("Case %d:\n", n);
        double score = antsystem(niter, nants, len, alpha, beta, rho, q0,
                                 Q, tau0, distances, path);
        for (int i = 0; i < len; i++)
            fprintf(stderr, "%d ", path[i]);
        fprintf(stderr, "\n");
        total += score;
    }

    printf("Grand Total = %g\n", total);
}

