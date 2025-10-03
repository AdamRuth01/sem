package com.napier.sem;

import java.sql.*;

public class App
{
    /**
     * Connection to MySQL database.
     */
    private Connection con = null;

    /**
     * Connect to the MySQL database.
     */
    public void connect()
    {
        try
        {
            Class.forName("com.mysql.cj.jdbc.Driver");
        }
        catch (ClassNotFoundException e)
        {
            System.out.println("Could not load SQL driver");
            System.exit(-1);
        }

        int retries = 10;
        for (int i = 0; i < retries; ++i)
        {
            System.out.println("Connecting to database... attempt " + (i + 1));
            try
            {
                con = DriverManager.getConnection(
                        "jdbc:mysql://mysql-db:3306/employees?allowPublicKeyRetrieval=true&useSSL=false",
                        "root",
                        "rootpass"
                );

                System.out.println("Successfully connected");
                break;
            }
            catch (SQLException sqle)
            {
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
    public void disconnect()
    {
        if (con != null)
        {
            try
            {
                con.close();
                System.out.println("Connection closed.");
            }
            catch (Exception e)
            {
                System.out.println("Error closing connection to database.");
            }
        }
    }

    /**
     * Retrieve employee information from the database.
     * @param empId Employee ID
     */
    public void getEmployeeInfo(int empId)
    {
        if (con == null)
        {
            System.out.println("No database connection.");
            return;
        }

        try
        {
            String query = "SELECT emp_no, first_name, last_name FROM employees WHERE emp_no = ?";
            PreparedStatement stmt = con.prepareStatement(query);
            stmt.setInt(1, empId);

            ResultSet rs = stmt.executeQuery();

            while (rs.next())
            {
                int empNo = rs.getInt("emp_no");
                String firstName = rs.getString("first_name");
                String lastName = rs.getString("last_name");

                System.out.println("Employee ID: " + empNo);
                System.out.println("Name: " + firstName + " " + lastName);
            }

            rs.close();
            stmt.close();
        }
        catch (SQLException e)
        {
            System.out.println("Error retrieving employee information: " + e.getMessage());
        }
    }

    /**
     * Main method to run the application.
     */

}