class first {

    abstract static class Car {
        abstract public void start();

        public void noise() {
            System.out.print("Hello Brooooo");
        }
    }

    static class ManualCar extends Car {
        @Override 
        public void start() {
            System.out.print("The car is getting started!!!");
        }
    }

    public static void main(String[] args) {

        ManualCar m1 = new ManualCar();

        m1.start();
    }
}