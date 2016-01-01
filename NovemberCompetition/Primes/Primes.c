#include <stdio.h>
#include <math.h>
/*
Nick Keirstead

Uses Sieve of Eratosthenes Method
*/

int main (void) {
    int max = 1000000;
	FILE *f = fopen("primes.txt", "w");
	char allnums[max];
    int i;
    
	//fill nums array with "unmarked" nums
    for (i = 2; i < max; i++) {
        allnums[i] = '0'; 
    }

    int x, j;

    for (x = 2; x < (int)sqrt(max); x++) {
        if (allnums[x] == '0') {
			fprintf(f, "%d\n", x);
            for (j = x * 2; j <= max; j += x) {
                allnums[j] = '1'; //mark multiples as composite
            }
        }
    }
    
    //print all remaining unmarked nums
    for (x = (int) sqrt(max); x <= max; x++) {
        if (allnums[x] == '0') {
            fprintf(f, "%d\n", x);
        }
    }
	
	fclose(f);
    return 0;
}
