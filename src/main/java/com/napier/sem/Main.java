package com.napier.sem;

// Press Shift twice to open the Search Everywhere dialog and type `show whitespaces`,
// then press Enter. You can now see whitespace characters in your code.
public class Main {
    public static void main(String[] args)
    {
        App app = new App();
        app.connect();

        // Example: retrieve employee with ID 10001
        app.getEmployeeInfo(10001);

        app.disconnect();
    }

}
