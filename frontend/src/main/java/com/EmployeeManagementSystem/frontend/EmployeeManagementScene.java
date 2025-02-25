package com.EmployeeManagementSystem.frontend;
import com.EmployeeManagementSystem.model.Employee;
import com.EmployeeManagementSystem.service.EmployeeService;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.SimpleDoubleProperty;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.List;

public class EmployeeManagementScene extends Application {

    private TableView<Employee> tableView = new TableView<>();
    private ObservableList<Employee> employeeList = FXCollections.observableArrayList();

    // Input Fields
    private TextField nameField = new TextField();
    private TextField designationField = new TextField();
    private TextField salaryField = new TextField();

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Employee Management System");

        // Table columns
        TableColumn<Employee, String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getName()));

        TableColumn<Employee, String> designationCol = new TableColumn<>("Designation");
        designationCol.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getDesignation()));

        TableColumn<Employee, Double> salaryCol = new TableColumn<>("Salary");
        salaryCol.setCellValueFactory(cell -> new SimpleDoubleProperty(cell.getValue().getSalary()).asObject());


        tableView.getColumns().addAll(nameCol, designationCol, salaryCol);
        tableView.setItems(employeeList);

        // Input Fields
        nameField.setPromptText("Enter Name");
        designationField.setPromptText("Enter Designation");
        salaryField.setPromptText("Enter Salary");

        // Buttons
        Button addButton = new Button("Add Employee");
        Button updateButton = new Button("Update Employee");
        Button deleteButton = new Button("Delete Employee");

        addButton.setOnAction(e -> addEmployee());
        updateButton.setOnAction(e -> updateEmployee());
        deleteButton.setOnAction(e -> deleteEmployee());

        // Layout
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(10));
        layout.getChildren().addAll(
                tableView, 
                new Label("Name:"), nameField, 
                new Label("Designation:"), designationField, 
                new Label("Salary:"), salaryField,
                addButton, updateButton, deleteButton
        );

        // Load employees on startup
        loadEmployees();

        Scene scene = new Scene(layout, 600, 500);
        scene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void loadEmployees() {
        try {
            List<Employee> employees = EmployeeService.getAllEmployees();
            employeeList.setAll(employees);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void addEmployee() {
        String name = nameField.getText();
        String designation = designationField.getText();
        String salaryText = salaryField.getText();

        if (name.isEmpty() || designation.isEmpty() || salaryText.isEmpty()) {
            showAlert("All fields must be filled!");
            return;
        }

        try {
            double salary = Double.parseDouble(salaryText);
            Employee employee = new Employee(null, name, designation, salary);
            EmployeeService.addEmployee(employee);
            loadEmployees();
            clearFields();
        } catch (NumberFormatException e) {
            showAlert("Invalid salary format! Enter a valid number.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void updateEmployee() {
        Employee selectedEmployee = tableView.getSelectionModel().getSelectedItem();
        if (selectedEmployee == null) {
            showAlert("Select an employee to update.");
            return;
        }

        String name = nameField.getText();
        String designation = designationField.getText();
        String salaryText = salaryField.getText();

        if (name.isEmpty() || designation.isEmpty() || salaryText.isEmpty()) {
            showAlert("All fields must be filled!");
            return;
        }

        try {
            double salary = Double.parseDouble(salaryText);
            selectedEmployee.setName(name);
            selectedEmployee.setDesignation(designation);
            selectedEmployee.setSalary(salary);

            EmployeeService.updateEmployee(selectedEmployee.getId(), selectedEmployee);
            loadEmployees();
            clearFields();
        } catch (NumberFormatException e) {
            showAlert("Invalid salary format! Enter a valid number.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void deleteEmployee() {
        Employee selectedEmployee = tableView.getSelectionModel().getSelectedItem();
        if (selectedEmployee == null) {
            showAlert("Select an employee to delete.");
            return;
        }

        try {
            EmployeeService.deleteEmployee(selectedEmployee.getId());
            loadEmployees();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void clearFields() {
        nameField.clear();
        designationField.clear();
        salaryField.clear();
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Warning");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}