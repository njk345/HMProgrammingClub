public class Fib {
  public static void main (String[] args) {
    long sum = 0;
    int i = 1;
    long currFib = fib(i);
    while (currFib <= 4000000) {
      if (currFib % 2 == 0) sum += currFib;
      i++;
      currFib = fib(i);
    }
    System.out.println(sum);
  }
  public static long fib (int n) {
    if (n == 1) return 1;
    else if (n == 2) return 2;
    return fib (n-1) + fib(n-2);
  }
}
