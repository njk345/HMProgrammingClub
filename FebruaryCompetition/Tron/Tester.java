import java.util.*;
public class Tester
{
   public static void main(String[] args) {
       Tree<Integer, Integer> test = new Tree<Integer, Integer>(0, null, null);
       for (int i = 1; i < 5; i++) {
           test.addChild(-i, i);
           for (int i = 6; i < 10; i++) {
               
           }
       }
       ArrayList<Tree<Integer, Integer>> children = test.getChildren();
       System.out.println(children.toString());
   }
}
