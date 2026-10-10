import java.util.*;

class Example {
    int instanceVar = 10;

    static void staticMethod() {
        // Creating an instance to access non-static members
        Example obj = new Example(); 
        
        System.out.println("Instance variable: " + obj.instanceVar);
    }
}

class Main {
    public static void main(String[] args) {
        Example.staticMethod();
    }
}