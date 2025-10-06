package com.napier.sem;

import java.sql.*;

public class App {
    /**
     * Connection to MySQL database.
     */
    private Connection con = null;

    /**
     * Connect to the MySQL database.
     */
    public void connect() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("Could not load SQL driver");
            System.exit(-1);
        }

        int retries = 10;
        for (int i = 0; i < retries; ++i) {
            System.out.println("Connecting to database... attempt " + (i + 1));
            try {
                con = DriverManager.getConnection(
                        "jdbc:mysql://mysql-db:3306/employees?allowPublicKeyRetrieval=true&useSSL=false",
                        "root",
                        "rootpass"
                );

                System.out.println("Successfully connected");
                break;
            } catch (SQLException sqle) {
                System.out.println("Failed to connect to database attempt " + (i + 1));
                System.out.println(sqle.getMessage());

                if (i < retries - 1) {
                    try {
                        Thread.sleep(5000); // Wait 5 seconds before retry
                    } catch (InterruptedException ie) {
                        System.out.println("Thread interrupted? Should not happen.");
                        break;
                    }
                }
            }
        }
    }

    /**
     * Disconnect from the MySQL database.
     */
    public void disconnect() {
        if (con != null) {
            try {
                con.close();
                System.out.println("Connection closed.");
            } catch (Exception e) {
                System.out.println("Error closing connection to database.");
            }
        }
    }

    /**
     * Retrieve employee information from the database.
     *
     * @param empId Employee ID
     * @return
     */
    public Employee getEmployeeInfo(int empId) {
        if (con == null) {
            System.out.println("No database connection.");
            return null;
        }

        try {
            String query = "SELECT e.emp_no, e.first_name, e.last_name, " +
                    "t.title, s.salary, d.dept_name, m.first_name AS manager " +
                    "FROM employees e " +
                    "LEFT JOIN titles t ON e.emp_no = t.emp_no " +
                    "LEFT JOIN salaries s ON e.emp_no = s.emp_no " +
                    "LEFT JOIN dept_emp de ON e.emp_no = de.emp_no " +
                    "LEFT JOIN departments d ON de.dept_no = d.dept_no " +
                    "LEFT JOIN dept_manager dm ON d.dept_no = dm.dept_no " +
                    "LEFT JOIN employees m ON dm.emp_no = m.emp_no " +
                    "WHERE e.emp_no = ?";

            PreparedStatement stmt = con.prepareStatement(query);
            stmt.setInt(1, empId);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Employee emp = new Employee();
                emp.emp_no = rs.getInt("emp_no");
                emp.first_name = rs.getString("first_name");
                emp.last_name = rs.getString("last_name");
                emp.title = rs.getString("title");
                emp.salary = rs.getInt("salary");
                emp.dept_name = rs.getString("dept_name");
                emp.manager = rs.getString("manager");
                return emp;
            }

            rs.close();
            stmt.close();
        } catch (SQLException e) {
            System.out.println("Error retrieving employee information: " + e.getMessage());
        }
        return null;

        /**
         * Main method to run the application.
         */


    }

    public void displayEmployee(Employee emp)
    {
        if (emp != null)
        {
            System.out.println(
                    emp.emp_no + " "
                            + emp.first_name + " "
                            + emp.last_name + "\n"
                            + emp.title + "\n"
                            + "Salary:" + emp.salary + "\n"
                            + emp.dept_name + "\n"
                            + "Manager: " + emp.manager + "\n");
        }
    }
}