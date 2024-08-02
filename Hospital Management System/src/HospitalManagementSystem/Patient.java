package HospitalManagementSystem;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class Patient {

    private Connection connection;

    private Scanner scanner;

    public Patient(Connection connection , Scanner scanner)
    {
        this.connection=connection;
        this.scanner=scanner;

    }

    public void addPatient()
    {
        System.out.print("Enter Patient Name :");
        String name =scanner.next();

        System.out.print("Enter Patient Age :");
        int age=scanner.nextInt();

        System.out.print("Enter Patient Gender :");
        String gender=scanner.next();

        try
        {
            String query="INSERT INTO patients(name , age ,  gender) VALUES (? ,? , ?)";
            PreparedStatement preparedStatement=connection.prepareStatement(query);
            preparedStatement.setString(1,name);
            preparedStatement.setInt(2,age);
            preparedStatement.setString(3,gender);

            int affectedRows=preparedStatement.executeUpdate();

            if (affectedRows>0)
            {
                System.out.println("Details inserted successfullly");

            }
            else {
                System.out.println("Failed to insert Patient Details  ");
            }

        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }

    }

    public void viewPatients()
    {
        String  qry="select * from patients";
        try
        {
            PreparedStatement preparedStatement=connection.prepareStatement(qry);
            ResultSet resultSet=preparedStatement.executeQuery();

            System.out.println("Patients :");
            System.out.println("+----------+-----------------+-----------+---------+");
            System.out.println("Patient Id | Name            | Age       | Gender   |");
            System.out.println("+----------+-----------------+-----------+---------+");
            while (resultSet.next())
            {
                int id=resultSet.getInt("id");
                String name=resultSet.getString("name");
                int age =resultSet.getInt("age");
                String gender =resultSet.getString("gender");

                System.out.printf("|%-10s|%-17s|%-11s|%-9s| \n",id,name,age,gender);
                System.out.print("+----------+-----------------+-----------+---------+");

            }


        }
        catch(SQLException e)
        {
            e.printStackTrace();
        }


    }
    public boolean getPatientById(int id)
    {
        String qry="select * from  patients  WHERE id= ?";
        try{
            PreparedStatement preparedStatement=connection.prepareStatement(qry);
            preparedStatement.setInt(1,id);
            ResultSet resultSet=preparedStatement.executeQuery();
            if (resultSet.next())
            {
                return true;

            }
            else
            {
                return false;
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        return false;
    }
}
