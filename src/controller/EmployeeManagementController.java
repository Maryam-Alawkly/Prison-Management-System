package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import model.Employee;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;
import service.EmployeeService;

/**
 * Controller for Employee Management Interface. Implements Initializable for
 * JavaFX initialization. Uses Singleton pattern for service instances and
 * Strategy pattern for search filtering.
 */
public class EmployeeManagementController implements Initializable {

    // Table UI components
    @FXML
    private TableView<Employee> employeeTable;
    @FXML
    private TableColumn<Employee, String> employeeIdColumn;
    @FXML
    private TableColumn<Employee, String> nameColumn;
    @FXML
    private TableColumn<Employee, String> positionColumn;
    @FXML
    private TableColumn<Employee, String> departmentColumn;
    @FXML
    private TableColumn<Employee, String> salaryColumn;
    @FXML
    private TableColumn<Employee, String> roleColumn;
    @FXML
    private TableColumn<Employee, String> statusColumn;
    @FXML
    private TableColumn<Employee, String> hireDateColumn;

    // Form UI components
    @FXML
    private TextField employeeIdField;
    @FXML
    private TextField nameField;
    @FXML
    private TextField phoneField;
    @FXML
    private TextField positionField;
    @FXML
    private ComboBox<String> departmentComboBox;
    @FXML
    private TextField salaryField;
    @FXML
    private DatePicker hireDatePicker;
    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private ComboBox<String> roleComboBox;
    @FXML
    private ComboBox<String> statusComboBox;

    // Search UI components
    @FXML
    private TextField searchField;
    @FXML
    private ComboBox<String> filterDepartmentComboBox;
    @FXML
    private ComboBox<String> filterRoleComboBox;
    @FXML
    private ComboBox<String> filterStatusComboBox;

    // Service instance (Singleton pattern)
    private EmployeeService employeeService;

    // Data collections
    private ObservableList<Employee> employeeList;

    /**
     * Initializes the controller class. Called automatically after FXML
     * loading.
     *
     * @param location The location used to resolve relative paths for the root
     * object
     * @param resources The resources used to localize the root object
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initializeServices();
        initializeDataStructures();
        setupTableColumns();
        loadAllData();
        setupComboBoxes();
        setupTableSelectionListener();
    }

    /**
     * Initializes service instances using Singleton pattern.
     */
    private void initializeServices() {
        employeeService = EmployeeService.getInstance();
    }

    /**
     * Initializes data structures for the controller.
     */
    private void initializeDataStructures() {
        employeeList = FXCollections.observableArrayList();
    }

    /**
     * Sets up table columns with cell value factories.
     */
    private void setupTableColumns() {
        employeeIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        positionColumn.setCellValueFactory(new PropertyValueFactory<>("position"));
        departmentColumn.setCellValueFactory(new PropertyValueFactory<>("department"));
        salaryColumn.setCellValueFactory(cellData -> {
            Employee employee = cellData.getValue();
            return new javafx.beans.property.SimpleStringProperty(String.format("$%.2f", employee.getSalary()));
        });
        roleColumn.setCellValueFactory(new PropertyValueFactory<>("role"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
        hireDateColumn.setCellValueFactory(new PropertyValueFactory<>("hireDate"));
    }

    /**
     * Loads all employee data from service.
     */
    private void loadAllData() {
        employeeList.clear();
        employeeList.addAll(employeeService.getAllEmployees());
        employeeTable.setItems(employeeList);
    }

    /**
     * Sets up all combo boxes with appropriate options.
     */
    private void setupComboBoxes() {
        setupDepartmentComboBox();
        setupRoleComboBox();
        setupStatusComboBox();
        setupFilterComboBoxes();
        setDefaultHireDate();
    }

    /**
     * Sets up department combo box.
     */
    private void setupDepartmentComboBox() {
        ObservableList<String> departmentOptions = FXCollections.observableArrayList(
                "Security", "Administration", "Medical", "Kitchen",
                "Maintenance", "Counseling", "Records", "Human Resources", "Operations"
        );
        departmentComboBox.setItems(departmentOptions);
        departmentComboBox.getSelectionModel().selectFirst();
    }

    /**
     * Sets up role combo box.
     */
    private void setupRoleComboBox() {
        ObservableList<String> roleOptions = FXCollections.observableArrayList(
                "Officer", "Administrator", "Manager", "Supervisor",
                "Guard", "Clerk", "Doctor", "Cook", "Janitor", "Counselor"
        );
        roleComboBox.setItems(roleOptions);
        roleComboBox.getSelectionModel().selectFirst();
    }

    /**
     * Sets up status combo box.
     */
    private void setupStatusComboBox() {
        ObservableList<String> statusOptions = FXCollections.observableArrayList(
                "Active", "Inactive", "Suspended", "On Leave", "Retired"
        );
        statusComboBox.setItems(statusOptions);
        statusComboBox.getSelectionModel().selectFirst();
    }

    /**
     * Sets up filter combo boxes.
     */
    private void setupFilterComboBoxes() {
        setupFilterDepartmentComboBox();
        setupFilterRoleComboBox();
        setupFilterStatusComboBox();
    }

    /**
     * Sets up department filter combo box.
     */
    private void setupFilterDepartmentComboBox() {
        ObservableList<String> filterDeptOptions = FXCollections.observableArrayList();
        filterDeptOptions.add("All Departments");
        filterDeptOptions.addAll(departmentComboBox.getItems());
        filterDepartmentComboBox.setItems(filterDeptOptions);
        filterDepartmentComboBox.getSelectionModel().selectFirst();
    }

    /**
     * Sets up role filter combo box.
     */
    private void setupFilterRoleComboBox() {
        ObservableList<String> filterRoleOptions = FXCollections.observableArrayList();
        filterRoleOptions.add("All Roles");
        filterRoleOptions.addAll(roleComboBox.getItems());
        filterRoleComboBox.setItems(filterRoleOptions);
        filterRoleComboBox.getSelectionModel().selectFirst();
    }

    /**
     * Sets up status filter combo box.
     */
    private void setupFilterStatusComboBox() {
        ObservableList<String> filterStatusOptions = FXCollections.observableArrayList();
        filterStatusOptions.add("All Status");
        filterStatusOptions.addAll(statusComboBox.getItems());
        filterStatusComboBox.setItems(filterStatusOptions);
        filterStatusComboBox.getSelectionModel().selectFirst();
    }

    /**
     * Sets default hire date to today.
     */
    private void setDefaultHireDate() {
        hireDatePicker.setValue(LocalDate.now());
    }

    /**
     * Sets up table selection listener to display employee details when
     * selected.
     */
    private void setupTableSelectionListener() {
        employeeTable.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> {
                    if (newValue != null) {
                        displayEmployeeDetails(newValue);
                    }
                });
    }

    /**
     * Displays employee details in the form.
     *
     * @param employee The employee to display
     */
    private void displayEmployeeDetails(Employee employee) {
        employeeIdField.setText(employee.getId());
        nameField.setText(employee.getName());
        phoneField.setText(employee.getPhone());
        positionField.setText(employee.getPosition());
        departmentComboBox.setValue(employee.getDepartment());
        salaryField.setText(String.valueOf(employee.getSalary()));
        setHireDateFromEmployee(employee);
        usernameField.setText(employee.getUsername());
        passwordField.setText(employee.getPassword());
        roleComboBox.setValue(employee.getRole());
        statusComboBox.setValue(employee.getStatus());
    }

    /**
     * Sets hire date from employee data.
     *
     * @param employee The employee to get hire date from
     */
    private void setHireDateFromEmployee(Employee employee) {
        try {
            LocalDate hireDate = LocalDate.parse(employee.getHireDate());
            hireDatePicker.setValue(hireDate);
        } catch (Exception e) {
            hireDatePicker.setValue(LocalDate.now());
        }
    }

    // FXML Action Handlers
    /**
     * Handles add employee action.
     */
    @FXML
    private void handleAddEmployee() {
        if (validateInput()) {
            createAndAddEmployee();
        }
    }

    /**
     * Creates and adds a new employee from form data.
     */
    private void createAndAddEmployee() {
        try {
            Employee newEmployee = createEmployeeFromForm();

            if (employeeService.addEmployee(newEmployee)) {
                showAlert("Success", "Employee added successfully!", Alert.AlertType.INFORMATION);
                loadAllData();
                clearForm();
            } else {
                showAlert("Error", "Failed to add employee!", Alert.AlertType.ERROR);
            }
        } catch (Exception e) {
            handleEmployeeActionError("adding", e);
        }
    }

    /**
     * Handles update employee action.
     */
    @FXML
    private void handleUpdateEmployee() {
        Employee selectedEmployee = employeeTable.getSelectionModel().getSelectedItem();
        if (selectedEmployee == null) {
            showAlert("Warning", "Please select an employee to update!", Alert.AlertType.WARNING);
            return;
        }

        if (validateInput()) {
            updateSelectedEmployee(selectedEmployee);
        }
    }

    /**
     * Updates the selected employee with form data.
     *
     * @param selectedEmployee The employee to update
     */
    private void updateSelectedEmployee(Employee selectedEmployee) {
        try {
            updateEmployeeFromForm(selectedEmployee);

            if (employeeService.updateEmployee(selectedEmployee)) {
                showAlert("Success", "Employee updated successfully!", Alert.AlertType.INFORMATION);
                loadAllData();
            } else {
                showAlert("Error", "Failed to update employee!", Alert.AlertType.ERROR);
            }
        } catch (Exception e) {
            handleEmployeeActionError("updating", e);
        }
    }

    /**
     * Handles delete employee action.
     */
    @FXML
    private void handleDeleteEmployee() {
        Employee selectedEmployee = employeeTable.getSelectionModel().getSelectedItem();
        if (selectedEmployee == null) {
            showAlert("Warning", "Please select an employee to delete!", Alert.AlertType.WARNING);
            return;
        }

        confirmAndDeleteEmployee(selectedEmployee);
    }

    /**
     * Shows confirmation dialog and deletes employee if confirmed.
     *
     * @param employee The employee to delete
     */
    private void confirmAndDeleteEmployee(Employee employee) {
        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Confirm Delete");
        confirmAlert.setHeaderText("Delete Employee");
        confirmAlert.setContentText("Are you sure you want to delete employee " + employee.getId() + "?");

        confirmAlert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                if (employeeService.deleteEmployee(employee.getId())) {
                    showAlert("Success", "Employee deleted successfully!", Alert.AlertType.INFORMATION);
                    loadAllData();
                    clearForm();
                } else {
                    showAlert("Error", "Failed to delete employee!", Alert.AlertType.ERROR);
                }
            }
        });
    }

    /**
     * Handles search functionality with multiple filter criteria. Uses Strategy
     * pattern for filtering logic.
     */
    @FXML
    private void handleSearch() {
        String searchTerm = searchField.getText().toLowerCase().trim();
        String deptFilter = filterDepartmentComboBox.getValue();
        String roleFilter = filterRoleComboBox.getValue();
        String statusFilter = filterStatusComboBox.getValue();

        ObservableList<Employee> filteredList = FXCollections.observableArrayList();

        for (Employee employee : employeeService.getAllEmployees()) {
            if (matchesSearchCriteria(employee, searchTerm, deptFilter, roleFilter, statusFilter)) {
                filteredList.add(employee);
            }
        }

        employeeTable.setItems(filteredList);
    }

    /**
     * Checks if an employee matches all search criteria. Implements Strategy
     * pattern for filtering logic.
     *
     * @param employee The employee to check
     * @param searchTerm The search term
     * @param deptFilter Department filter
     * @param roleFilter Role filter
     * @param statusFilter Status filter
     * @return true if employee matches all criteria, false otherwise
     */
    private boolean matchesSearchCriteria(Employee employee, String searchTerm,
            String deptFilter, String roleFilter, String statusFilter) {
        return matchesSearchTerm(employee, searchTerm)
                && matchesDepartmentFilter(employee, deptFilter)
                && matchesRoleFilter(employee, roleFilter)
                && matchesStatusFilter(employee, statusFilter);
    }

    /**
     * Checks if employee matches search term.
     */
    private boolean matchesSearchTerm(Employee employee, String searchTerm) {
        if (searchTerm.isEmpty()) {
            return true;
        }
        return employee.getId().toLowerCase().contains(searchTerm)
                || employee.getName().toLowerCase().contains(searchTerm)
                || employee.getPosition().toLowerCase().contains(searchTerm)
                || employee.getDepartment().toLowerCase().contains(searchTerm);
    }

    /**
     * Checks if employee matches department filter.
     */
    private boolean matchesDepartmentFilter(Employee employee, String deptFilter) {
        return "All Departments".equals(deptFilter)
                || employee.getDepartment().equals(deptFilter);
    }

    /**
     * Checks if employee matches role filter.
     */
    private boolean matchesRoleFilter(Employee employee, String roleFilter) {
        return "All Roles".equals(roleFilter)
                || employee.getRole().equals(roleFilter);
    }

    /**
     * Checks if employee matches status filter.
     */
    private boolean matchesStatusFilter(Employee employee, String statusFilter) {
        return "All Status".equals(statusFilter)
                || employee.getStatus().equals(statusFilter);
    }

    /**
     * Handles clear action to reset search and form.
     */
    @FXML
    private void handleClear() {
        resetSearchFilters();
        clearForm();
        employeeTable.getSelectionModel().clearSelection();
        loadAllData();
    }

    /**
     * Resets all search filter components to their default values.
     */
    private void resetSearchFilters() {
        searchField.clear();
        filterDepartmentComboBox.getSelectionModel().selectFirst();
        filterRoleComboBox.getSelectionModel().selectFirst();
        filterStatusComboBox.getSelectionModel().selectFirst();
    }

    /**
     * Handles activate employee action.
     */
    @FXML
    private void handleActivateEmployee() {
        Employee selectedEmployee = employeeTable.getSelectionModel().getSelectedItem();
        if (selectedEmployee != null && !"Active".equals(selectedEmployee.getStatus())) {
            selectedEmployee.activateEmployee();
            if (employeeService.updateEmployee(selectedEmployee)) {
                showAlert("Success", "Employee activated successfully!", Alert.AlertType.INFORMATION);
                loadAllData();
            } else {
                showAlert("Error", "Failed to activate employee!", Alert.AlertType.ERROR);
            }
        } else {
            showAlert("Warning", "Please select an inactive employee to activate!", Alert.AlertType.WARNING);
        }
    }

    /**
     * Handles deactivate employee action.
     */
    @FXML
    private void handleDeactivateEmployee() {
        Employee selectedEmployee = employeeTable.getSelectionModel().getSelectedItem();
        if (selectedEmployee != null && "Active".equals(selectedEmployee.getStatus())) {
            selectedEmployee.terminateEmployee();
            if (employeeService.updateEmployee(selectedEmployee)) {
                showAlert("Success", "Employee deactivated successfully!", Alert.AlertType.INFORMATION);
                loadAllData();
            } else {
                showAlert("Error", "Failed to deactivate employee!", Alert.AlertType.ERROR);
            }
        } else {
            showAlert("Warning", "Please select an active employee to deactivate!", Alert.AlertType.WARNING);
        }
    }

    /**
     * Handles promote employee action.
     */
    @FXML
    private void handlePromoteEmployee() {
        Employee selectedEmployee = employeeTable.getSelectionModel().getSelectedItem();
        if (selectedEmployee != null) {
            showPromotionDialog(selectedEmployee);
        } else {
            showAlert("Warning", "Please select an employee to promote!", Alert.AlertType.WARNING);
        }
    }

    /**
     * Shows promotion dialog for the selected employee.
     *
     * @param employee The employee to promote
     */
    private void showPromotionDialog(Employee employee) {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Promote Employee");
        dialog.setHeaderText("Promote " + employee.getName());

        GridPane grid = createPromotionDialogGrid(employee);
        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        dialog.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                handlePromotionConfirmation(employee, grid);
            }
        });
    }

    /**
     * Creates grid pane for promotion dialog.
     *
     * @param employee The employee being promoted
     * @return Configured GridPane
     */
    private GridPane createPromotionDialogGrid(Employee employee) {
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new javafx.geometry.Insets(20, 150, 10, 10));

        TextField newPositionField = new TextField(employee.getPosition());
        TextField newSalaryField = new TextField(String.valueOf(employee.getSalary()));

        grid.add(new Label("New Position:"), 0, 0);
        grid.add(newPositionField, 1, 0);
        grid.add(new Label("New Salary:"), 0, 1);
        grid.add(newSalaryField, 1, 1);

        return grid;
    }

    /**
     * Handles promotion confirmation with form validation.
     *
     * @param employee The employee to promote
     * @param grid The grid containing promotion data
     */
    private void handlePromotionConfirmation(Employee employee, GridPane grid) {
        try {
            TextField newPositionField = (TextField) grid.getChildren().get(1);
            TextField newSalaryField = (TextField) grid.getChildren().get(3);

            String newPosition = newPositionField.getText().trim();
            double newSalary = Double.parseDouble(newSalaryField.getText().trim());

            employee.promoteEmployee(newPosition, newSalary);
            if (employeeService.updateEmployee(employee)) {
                showAlert("Success", "Employee promoted successfully!", Alert.AlertType.INFORMATION);
                loadAllData();
            } else {
                showAlert("Error", "Failed to promote employee!", Alert.AlertType.ERROR);
            }
        } catch (NumberFormatException e) {
            showAlert("Error", "Please enter a valid salary!", Alert.AlertType.ERROR);
        }
    }

    /**
     * Handles view details action.
     */
    @FXML
    private void handleViewDetails() {
        Employee selectedEmployee = employeeTable.getSelectionModel().getSelectedItem();
        if (selectedEmployee != null) {
            Alert detailsAlert = new Alert(Alert.AlertType.INFORMATION);
            detailsAlert.setTitle("Employee Details");
            detailsAlert.setHeaderText("Details for " + selectedEmployee.getName());
            detailsAlert.setContentText(selectedEmployee.getEmployeeDetails());
            detailsAlert.showAndWait();
        } else {
            showAlert("Warning", "Please select an employee to view details!", Alert.AlertType.WARNING);
        }
    }

    /**
     * Handles change password action.
     */
    @FXML
    private void handleChangePassword() {
        Employee selectedEmployee = employeeTable.getSelectionModel().getSelectedItem();
        if (selectedEmployee != null) {
            showPasswordChangeDialog(selectedEmployee);
        } else {
            showAlert("Warning", "Please select an employee to change password!", Alert.AlertType.WARNING);
        }
    }

    /**
     * Shows password change dialog.
     *
     * @param employee The employee to change password for
     */
    private void showPasswordChangeDialog(Employee employee) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Change Password");
        dialog.setHeaderText("Change password for " + employee.getName());
        dialog.setContentText("Enter new password:");

        dialog.showAndWait().ifPresent(newPassword -> {
            if (!newPassword.isEmpty()) {
                if (employeeService.updatePassword(employee.getId(), newPassword)) {
                    employee.setPassword(newPassword);
                    showAlert("Success", "Password changed successfully!", Alert.AlertType.INFORMATION);
                } else {
                    showAlert("Error", "Failed to change password!", Alert.AlertType.ERROR);
                }
            }
        });
    }

    /**
     * Handles calculate annual salary action.
     */
    @FXML
    private void handleCalculateAnnualSalary() {
        Employee selectedEmployee = employeeTable.getSelectionModel().getSelectedItem();
        if (selectedEmployee != null) {
            double annualSalary = selectedEmployee.calculateAnnualSalary();
            showAlert("Annual Salary",
                    selectedEmployee.getName() + "'s annual salary: $" + String.format("%.2f", annualSalary),
                    Alert.AlertType.INFORMATION);
        } else {
            showAlert("Warning", "Please select an employee to calculate annual salary!", Alert.AlertType.WARNING);
        }
    }

    /**
     * Handles refresh action.
     */
    @FXML
    private void handleRefresh() {
        loadAllData();
        showAlert("Success", "Data refreshed successfully!", Alert.AlertType.INFORMATION);
    }

    /**
     * Creates an Employee object from form data.
     *
     * @return A populated Employee object
     */
    private Employee createEmployeeFromForm() {
        String employeeId = employeeIdField.getText().trim();
        String name = nameField.getText().trim();
        String phone = phoneField.getText().trim();
        String position = positionField.getText().trim();
        String department = departmentComboBox.getValue();
        double salary = Double.parseDouble(salaryField.getText().trim());
        String username = usernameField.getText().trim();
        String password = passwordField.getText();
        String role = roleComboBox.getValue();
        String status = statusComboBox.getValue();

        Employee newEmployee = new Employee(employeeId, name, phone, position,
                department, salary, username, password, role);
        newEmployee.setStatus(status);
        newEmployee.setHireDate(hireDatePicker.getValue().toString());
        newEmployee.setActive("Active".equals(status));

        return newEmployee;
    }

    /**
     * Updates employee object with form data.
     *
     * @param employee The employee to update
     */
    private void updateEmployeeFromForm(Employee employee) {
        employee.setName(nameField.getText().trim());
        employee.setPhone(phoneField.getText().trim());
        employee.setPosition(positionField.getText().trim());
        employee.setDepartment(departmentComboBox.getValue());
        employee.setSalary(Double.parseDouble(salaryField.getText().trim()));
        employee.setHireDate(hireDatePicker.getValue().toString());
        employee.setUsername(usernameField.getText().trim());
        employee.setPassword(passwordField.getText());
        employee.setRole(roleComboBox.getValue());
        employee.setStatus(statusComboBox.getValue());
        employee.setActive("Active".equals(statusComboBox.getValue()));
    }

    /**
     * Validates form input data.
     *
     * @return true if form is valid, false otherwise
     */
    private boolean validateInput() {
        return validateEmployeeId()
                && validateName()
                && validatePhone()
                && validatePosition()
                && validateSalary()
                && validateUsername()
                && validatePassword();
    }

    /**
     * Validates employee ID field.
     *
     * @return true if valid, false otherwise
     */
    private boolean validateEmployeeId() {
        if (employeeIdField.getText().trim().isEmpty()) {
            showAlert("Validation Error", "Employee ID is required!", Alert.AlertType.ERROR);
            employeeIdField.requestFocus();
            return false;
        }
        return true;
    }

    /**
     * Validates name field.
     *
     * @return true if valid, false otherwise
     */
    private boolean validateName() {
        if (nameField.getText().trim().isEmpty()) {
            showAlert("Validation Error", "Name is required!", Alert.AlertType.ERROR);
            nameField.requestFocus();
            return false;
        }
        return true;
    }

    /**
     * Validates phone field.
     *
     * @return true if valid, false otherwise
     */
    private boolean validatePhone() {
        String phone = phoneField.getText().trim();
        if (!phone.isEmpty() && !phone.matches("\\d{10,15}")) {
            showAlert("Validation Error", "Phone number must be 10-15 digits!", Alert.AlertType.ERROR);
            phoneField.requestFocus();
            return false;
        }
        return true;
    }

    /**
     * Validates position field.
     *
     * @return true if valid, false otherwise
     */
    private boolean validatePosition() {
        if (positionField.getText().trim().isEmpty()) {
            showAlert("Validation Error", "Position is required!", Alert.AlertType.ERROR);
            positionField.requestFocus();
            return false;
        }
        return true;
    }

    /**
     * Validates salary field.
     *
     * @return true if valid, false otherwise
     */
    private boolean validateSalary() {
        try {
            double salary = Double.parseDouble(salaryField.getText().trim());
            if (salary <= 0) {
                showAlert("Validation Error", "Salary must be greater than 0!", Alert.AlertType.ERROR);
                salaryField.requestFocus();
                return false;
            }
        } catch (NumberFormatException e) {
            showAlert("Validation Error", "Salary must be a valid number!", Alert.AlertType.ERROR);
            salaryField.requestFocus();
            return false;
        }
        return true;
    }

    /**
     * Validates username field.
     *
     * @return true if valid, false otherwise
     */
    private boolean validateUsername() {
        if (usernameField.getText().trim().isEmpty()) {
            showAlert("Validation Error", "Username is required!", Alert.AlertType.ERROR);
            usernameField.requestFocus();
            return false;
        }
        return true;
    }

    /**
     * Validates password field.
     *
     * @return true if valid, false otherwise
     */
    private boolean validatePassword() {
        if (passwordField.getText().isEmpty()) {
            showAlert("Validation Error", "Password is required!", Alert.AlertType.ERROR);
            passwordField.requestFocus();
            return false;
        }
        return true;
    }

    /**
     * Clears all form fields.
     */
    private void clearForm() {
        employeeIdField.clear();
        nameField.clear();
        phoneField.clear();
        positionField.clear();
        departmentComboBox.getSelectionModel().selectFirst();
        salaryField.clear();
        hireDatePicker.setValue(LocalDate.now());
        usernameField.clear();
        passwordField.clear();
        roleComboBox.getSelectionModel().selectFirst();
        statusComboBox.getSelectionModel().selectFirst();
    }

    /**
     * Handles employee action errors.
     *
     * @param action The action being performed
     * @param e The exception that occurred
     */
    private void handleEmployeeActionError(String action, Exception e) {
        showAlert("Error", "Error " + action + " employee: " + e.getMessage(), Alert.AlertType.ERROR);
    }

    /**
     * Shows an alert dialog with specified parameters.
     *
     * @param title The alert title
     * @param message The alert message
     * @param alertType The type of alert
     */
    private void showAlert(String title, String message, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
