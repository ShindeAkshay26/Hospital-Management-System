package HospitalManagementSystem;

import jdk.jfr.consumer.RecordedStackTrace;

import java.sql.*;
import java.util.Scanner;

public class HospitalManagementSystem
{
    private static final String url="jdbc:mysql://localhost:3306/hospital";

    private static final String username="root";

    private static final String password="akshay@26";

    public static void main(String[] args) {

        try
        {
            Class.forName("com.mysql.cj.jdbc.Driver");
        }
        catch (ClassNotFoundException e)
        {
            e.printStackTrace();
        }

        Scanner scanner=new Scanner(System.in);
        try
        {
            Connection connection= DriverManager.getConnection(url,username,password);
            Patient patient=new Patient(connection ,scanner);
            Doctor doctor=new Doctor(connection);

            while (true)
            {
                System.out.println("HOSPITAL MANAGEMENT SYSTEM");
                System.out.println("1.Add Patients");
                System.out.println("2.View Patients");
                System.out.println("3.View Doctors");
                System.out.println("4.Book Appointment");
                System.out.println("5.Exit");
                System.out.println("Enter Your choice :");

                int choice=scanner.nextInt();

                switch (choice)
                {
                    case 1:
                        //Add Patients
                        patient.addPatient();
                        System.out.println();
                        break;
                    case 2:
                        //View Patients
                        patient.viewPatients();
                        System.out.println();
                        break;
                    case 3:
                        //View Doctors
                        doctor.viewDoctors();
                        System.out.println();
                        break;
                    case 4:
                        //Book Appointment
                        bookAppiontment(patient,doctor,connection,scanner);
                        System.out.println();
                        break;

                    case 5:
                        //Exit
                        return;

                    default:
                        System.out.println("Enter Valid Choice ");
                }

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void bookAppiontment(Patient patient,Doctor doctor,Connection connection ,Scanner scanner)
    {
        System.out.println("Enter Patient Id ");
        int patientID= scanner.nextInt();
        System.out.println("Enetr Doctor id");
        int doctorId=scanner.nextInt();
        System.out.println("Enter Appointment date (YYYY-MM-DD)");
        String appointmentdate=scanner.next();

        if(patient.getPatientById(patientID) && doctor.getDoctorsById(doctorId))
        {
            if(cheackDoctorAvaliablity(doctorId , appointmentdate,connection))
            {
                String qrey="INSERT INTO appoinments(patient_id ,doctor_id,appointment_date) VALUES (?,?,?)";
                try
                {
                    PreparedStatement preparedStatement=connection.prepareStatement(qrey);
                    preparedStatement.setInt(1,patientID);
                    preparedStatement.setInt(2,doctorId);
                    preparedStatement.setString(3,appointmentdate);

                    int affectedRows=preparedStatement.executeUpdate();
                    if(affectedRows>0)
                    {
                        System.out.println("Appointment book successfully ");
                    }
                    else
                    {
                        System.out.println("Appointment booking Failed ");
                    }

                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            else
            {
                System.out.println("Either Patient or Doctor Not avaliable on this date");
            }
        }
        else
        {
            System.out.println("Either patient or doctor dosent exist");
        }
    }

    public static boolean cheackDoctorAvaliablity(int doctorId , String appointmentDate ,Connection connection)
    {
        String Qur="SELECT COUNT(*) FROM appoinments WHERE doctor_id=? AND appointment_date=?";
        try
        {
            PreparedStatement preparedStatement=connection.prepareStatement(Qur);
            preparedStatement.setInt(1,doctorId);
            preparedStatement.setString(2,appointmentDate);

            ResultSet resultSet=preparedStatement.executeQuery();
            if(resultSet.next())
            {
                int count =resultSet.getInt(1);
                if(count==0)
                {
                    return true;
                }
                else {
                    return false;
                }
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        return false;
    }
}
