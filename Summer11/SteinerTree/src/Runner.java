/**
 * Created by njk on 6/8/16.
 */
import java.util.ArrayList;
import java.util.Scanner;
public class Runner {
    public static String[] algorithms = {"In Input Order"};
    public static void main(String[] args) {
        ArrayList<Point> points = Utils.getInput();
        Scanner scanner = new Scanner(System.in);

        System.out.println("Choose An Algorithm:");
        printChoices();
        int choice = scanner.nextInt();

        Algorithm algo = getAlgorithm(choice);

        System.out.println("Solving...");
        ArrayList<Line> tree = algo.makeTree(points);
        System.out.println("Finished - Score = " + Utils.scoreTree(tree));

        Utils.writeOutput(tree);
    }
    private static void printChoices() {
        for (int i = 0; i < algorithms.length; i++) {
            System.out.println("(" + (i+1) + "): " + algorithms[i]);
        }
    }
    private static Algorithm getAlgorithm(int choice) {
        switch (choice) {
            case 1:
                return new SpitBack();
            default:
                return null;
        }
    }
}
