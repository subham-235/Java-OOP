public class first {
    static class Employee {
        String name;
        int salary;

        void setName(String name) {
            this.name = name;
        }

        void setSalary(int salary) {
            this.salary = salary;
        }

        String getName() {
            return this.name;
        }

        int getSalary() {
            return this.salary;
        }
    }

    public static void main(String[] args) {
        Employee emp = new Employee();

        emp.setName("John Doe");
        emp.setSalary(50000);

        System.out.println("Employee Name: " + emp.getName());
        System.out.println("Employee Salary: " + emp.getSalary());
    }
}