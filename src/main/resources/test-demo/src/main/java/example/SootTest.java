package example;

public class SootTest {

    public void printFizzBuzz(int k) {
        if (k % 15 == 0) {
            getValue();
            System.out.println("FizzBuzz");
        } else if (k % 5 == 0) {
            System.out.println("Buzz");
        } else if (k % 3 == 0) {
            System.out.println("Fizz");
        } else {
            System.out.println(k);
        }
    }

    public void sout(int k) {
        System.out.println("k = " + k);
    }

    public Integer getValue() {
        return 10;
    }
}
