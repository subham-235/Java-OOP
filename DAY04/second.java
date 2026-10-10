class second {

    static class Animal {

        int age;
        String name;

        public Animal(int age, String name) {
            this.age = age;
            this.name = name;
        }

        // Method to display animal details
        public void displayDetails() {
            System.out.println("Animal Name: " + name);
            System.out.println("Animal Age: " + age);
        }
    }

    static class Dog extends Animal {

        private int eye;

        public Dog(int age, String name, int eye) {
            super(age, name);
            this.eye = eye;
        }

        public void bark() {
            System.out.println("Woof! Woof!");
        }
    }

    public static void main(String[] args) {

        Animal animal = new Animal(5, "Buddy");

        animal.displayDetails();

        Dog dog = new Dog(3, "Max", 2);

        dog.displayDetails();
        System.out.println("Number of Eyes: " + dog.eye);
        dog.bark();
    }
}