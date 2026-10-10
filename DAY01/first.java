import java.util.Scanner;
class first {
    public static void main(String[] args) {
        Scanner sc=new Scanner(System.in);
        System.out.print("Enter a number : ");
        int a =sc.nextInt();
        String str="Hello, World!";
        System.out.println(a);
        System.out.println("Hello, World!");
        System.out.println(str.substring(0,5));
        // Explicit Type Casting
        double num=100.3;
        final int intNum=(int)num;
        System.out.println(intNum);1
    }
}