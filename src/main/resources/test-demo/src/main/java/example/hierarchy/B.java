package example.hierarchy;

public class B {
    public void calc(A a) {
        a.calc(1); // can be A.calc, C.calc, D.calc
    }
}