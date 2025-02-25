package com.EmployeeManagementSystem.service;
import com.EmployeeManagementSystem.model.Employee;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class EmployeeService {
    private static final String API_URL = "http://localhost:8080/api/employees";
    private static final Gson gson = new Gson();

    // Fetch all employees
    public static List<Employee> getAllEmployees() throws IOException {
        HttpURLConnection connection = (HttpURLConnection) new URL(API_URL).openConnection();
        connection.setRequestMethod("GET");
        InputStreamReader reader = new InputStreamReader(connection.getInputStream());
        return gson.fromJson(reader, new TypeToken<List<Employee>>() {}.getType());
    }

    // Add a new employee
    public static void addEmployee(Employee employee) throws IOException {
        sendRequest("POST", employee, null);
    }

    // Update an employee
    public static void updateEmployee(Long id, Employee employee) throws IOException {
        sendRequest("PUT", employee, id);
    }

    // Delete an employee
    public static void deleteEmployee(Long id) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) new URL(API_URL + "/" + id).openConnection();
        connection.setRequestMethod("DELETE");
        connection.getResponseCode();
    }

    // Generic method for POST & PUT requests
    private static void sendRequest(String method, Employee employee, Long id) throws IOException {
        URL url = (id == null) ? new URL(API_URL) : new URL(API_URL + "/" + id);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod(method);
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setDoOutput(true);

        OutputStream os = connection.getOutputStream();
        os.write(gson.toJson(employee).getBytes());
        os.flush();
        os.close();
        connection.getResponseCode();
    }
}
