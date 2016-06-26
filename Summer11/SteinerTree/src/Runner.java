/**
 * Created by njk on 6/8/16.
 */
import java.util.ArrayList;
import java.util.Scanner;
import java.io.IOException;
public class Runner {
    public static String[] algorithms = {"In Input Order", "Greedy", "Simulated Annealing — Two Point Switch", "Prim"
                                        , "Simulated Annealing — Steiner Points"};
    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);
        ArrayList<Point> points = Utils.getInput();

        System.out.println("Choose An Algorithm:");
        printChoices();
        int choice = scanner.nextInt();

        Algorithm algo = getAlgorithm(choice, scanner);
        while (algo == null) {
            System.out.print("Invalid Choice, Pick Again: ");
            int c = scanner.nextInt();
            algo = getAlgorithm(c, scanner);
        }

        System.out.println("Solving...");
        ArrayList<Line> tree = algo.makeTree(points);
        if (!(algo instanceof SteinerSimAnneal)) {
            System.out.println("Finished - Score = " + Utils.scoreTree(tree));
            Utils.writeOutput(tree);
        }
    }
    private static void printChoices() {
        for (int i = 0; i < algorithms.length; i++) {
            System.out.println("(" + (i+1) + "): " + algorithms[i]);
        }
    }
    private static Algorithm getAlgorithm(int choice, Scanner scanner) {
        switch (choice) {
            case 1:
                return new SpitBack();
            case 2:
                return new Greedy();
            case 3:
                System.out.print("Run For How Many Minutes? ");
                double m = scanner.nextDouble();
                return new SimAnneal_Switcher(m);
            case 4:
                return new Prim();
            case 5:
                System.out.print("Run For How Many Minutes? ");
                double m2 = scanner.nextDouble();
                scanner.nextLine();
                System.out.print("Start From Best Solution? (y/n): ");
                String resp = scanner.nextLine().toLowerCase();
                System.out.print("Hill Climb? (y/n): ");
                String resp2 = scanner.nextLine().toLowerCase();
                return new SteinerSimAnneal(m2, resp.equals("y"), resp2.equals("y"));
            default:
                return null;
        }
    }
}