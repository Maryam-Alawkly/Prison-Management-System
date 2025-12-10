package database;

import model.Task;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for Task entity.
 * Implements Singleton pattern for DAO instance management.
 */
public class TaskDAO {
    
    // Singleton instance
    private static TaskDAO instance;
    
    /**
     * Private constructor to enforce Singleton pattern.
     */
    private TaskDAO() {
        // Private constructor to prevent instantiation
    }
    
    /**
     * Returns the single instance of TaskDAO.
     *
     * @return TaskDAO instance
     */
    public static synchronized TaskDAO getInstance() {
        if (instance == null) {
            instance = new TaskDAO();
        }
        return instance;
    }
    /**
     * Retrieves all tasks from the database. Tasks are ordered by creation date
     * (newest first) and priority.
     *
     * @return List of all Task objects
     */
    public List<Task> getAllTasks() {
        List<Task> tasks = new ArrayList<>();
        String query = "SELECT * FROM tasks ORDER BY created_at DESC, priority DESC";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                tasks.add(extractTaskFromResultSet(rs));
            }

        } catch (SQLException e) {
            System.err.println("Error retrieving all tasks: " + e.getMessage());
        }
        return tasks;
    }

    /**
     * Retrieves tasks by their status.
     *
     * @param status Task status to filter by
     * @return List of Task objects with specified status
     */
    public List<Task> getTasksByStatus(String status) {
        List<Task> tasks = new ArrayList<>();

        if (status == null || status.trim().isEmpty()) {
            return tasks;
        }

        String query = "SELECT * FROM tasks WHERE status = ? ORDER BY due_date DESC";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
                PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, status);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                tasks.add(extractTaskFromResultSet(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving tasks by status: " + e.getMessage());
        }
        return tasks;
    }

    /**
     * Retrieves overdue tasks that are not completed. Overdue tasks are those
     * with due date earlier than current date.
     *
     * @return List of overdue Task objects
     */
    public List<Task> getOverdueTasks() {
        List<Task> tasks = new ArrayList<>();
        String query = "SELECT * FROM tasks WHERE due_date < CURDATE() AND status != 'Completed' ORDER BY due_date DESC";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                tasks.add(extractTaskFromResultSet(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving overdue tasks: " + e.getMessage());
        }
        return tasks;
    }

    /**
     * Adds a new task to the database.
     *
     * @param task Task object to add
     * @return true if task was added successfully, false otherwise
     */
    public boolean addTask(Task task) {
        if (task == null) {
            return false;
        }

        String query = "INSERT INTO tasks (task_id, task_name, description, assigned_to_id, assigned_to_name, "
                + "priority, status, due_date, created_at, created_by, category, estimated_hours) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
                PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, task.getTaskId());
            pstmt.setString(2, task.getTaskName());
            pstmt.setString(3, task.getDescription());
            pstmt.setString(4, task.getAssignedToId());
            pstmt.setString(5, task.getAssignedToName());
            pstmt.setString(6, task.getPriority());
            pstmt.setString(7, task.getStatus());

            if (task.getDueDate() != null) {
                pstmt.setDate(8, Date.valueOf(task.getDueDate()));
            } else {
                pstmt.setNull(8, Types.DATE);
            }

            if (task.getCreatedAt() != null) {
                pstmt.setDate(9, Date.valueOf(task.getCreatedAt()));
            } else {
                pstmt.setDate(9, Date.valueOf(java.time.LocalDate.now()));
            }

            pstmt.setString(10, task.getCreatedBy());
            pstmt.setString(11, task.getCategory());
            pstmt.setInt(12, task.getEstimatedHours());

            int result = pstmt.executeUpdate();
            return result > 0;
        } catch (SQLException e) {
            System.err.println("Error adding task to database: " + e.getMessage());
            return false;
        }
    }

    /**
     * Updates an existing task in the database.
     *
     * @param task Task object with updated values
     * @return true if update was successful, false otherwise
     */
    public boolean updateTask(Task task) {
        if (task == null) {
            return false;
        }

        String query = "UPDATE tasks SET task_name = ?, description = ?, assigned_to_id = ?, "
                + "assigned_to_name = ?, priority = ?, status = ?, due_date = ?, "
                + "completed_date = ?, completed_by = ?, completion_notes = ?, "
                + "category = ?, estimated_hours = ? WHERE task_id = ?";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
                PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, task.getTaskName());
            pstmt.setString(2, task.getDescription());
            pstmt.setString(3, task.getAssignedToId());
            pstmt.setString(4, task.getAssignedToName());
            pstmt.setString(5, task.getPriority());
            pstmt.setString(6, task.getStatus());

            if (task.getDueDate() != null) {
                pstmt.setDate(7, Date.valueOf(task.getDueDate()));
            } else {
                pstmt.setNull(7, Types.DATE);
            }

            if (task.getCompletedDate() != null) {
                pstmt.setDate(8, Date.valueOf(task.getCompletedDate()));
            } else {
                pstmt.setNull(8, Types.DATE);
            }

            pstmt.setString(9, task.getCompletedBy());
            pstmt.setString(10, task.getCompletionNotes());
            pstmt.setString(11, task.getCategory());
            pstmt.setInt(12, task.getEstimatedHours());
            pstmt.setString(13, task.getTaskId());

            int result = pstmt.executeUpdate();
            return result > 0;
        } catch (SQLException e) {
            System.err.println("Error updating task: " + e.getMessage());
            return false;
        }
    }

    /**
     * Retrieves tasks assigned to a specific officer.
     *
     * @param officerId Unique identifier of the officer
     * @return List of Task objects assigned to the officer
     */
    public List<Task> getTasksByOfficer(String officerId) {
        List<Task> tasks = new ArrayList<>();

        if (officerId == null || officerId.trim().isEmpty()) {
            return tasks;
        }

        String query = "SELECT * FROM tasks WHERE assigned_to_id = ? ORDER BY due_date DESC";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
                PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, officerId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                tasks.add(extractTaskFromResultSet(rs));
            }

        } catch (SQLException e) {
            System.err.println("Error retrieving tasks by officer: " + e.getMessage());
        }
        return tasks;
    }

    /**
     * Retrieves a task by its unique identifier.
     *
     * @param taskId Unique identifier of the task
     * @return Task object if found, null otherwise
     */
    public Task getTaskById(String taskId) {
        if (taskId == null || taskId.trim().isEmpty()) {
            return null;
        }

        String query = "SELECT * FROM tasks WHERE task_id = ?";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
                PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, taskId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return extractTaskFromResultSet(rs);
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving task by ID: " + e.getMessage());
        }
        return null;
    }

    /**
     * Updates the status of a task.
     *
     * @param taskId Unique identifier of the task
     * @param status New status to set
     * @return true if status update was successful, false otherwise
     */
    public boolean updateTaskStatus(String taskId, String status) {
        if (taskId == null || status == null) {
            return false;
        }

        String query = "UPDATE tasks SET status = ? WHERE task_id = ?";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
                PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, status);
            pstmt.setString(2, taskId);

            int result = pstmt.executeUpdate();
            return result > 0;
        } catch (SQLException e) {
            System.err.println("Error updating task status: " + e.getMessage());
            return false;
        }
    }

    /**
     * Deletes a task from the database.
     *
     * @param taskId Unique identifier of the task to delete
     * @return true if deletion was successful, false otherwise
     */
    public boolean deleteTask(String taskId) {
        if (taskId == null || taskId.trim().isEmpty()) {
            return false;
        }

        String query = "DELETE FROM tasks WHERE task_id = ?";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
                PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, taskId);
            int result = pstmt.executeUpdate();
            return result > 0;
        } catch (SQLException e) {
            System.err.println("Error deleting task: " + e.getMessage());
            return false;
        }
    }

    /**
     * Searches for tasks based on multiple criteria.
     *
     * @param searchText Text to search in task name, description, or assignee
     * name
     * @param status Status to filter by (or "All" for no filter)
     * @param priority Priority to filter by (or "All" for no filter)
     * @return List of Task objects matching search criteria
     */
    public List<Task> searchTasks(String searchText, String status, String priority) {
        List<Task> tasks = new ArrayList<>();

        StringBuilder queryBuilder = new StringBuilder("SELECT * FROM tasks WHERE 1=1");
        List<Object> parameters = new ArrayList<>();

        // Add text search criteria
        if (searchText != null && !searchText.trim().isEmpty()) {
            queryBuilder.append(" AND (LOWER(task_name) LIKE ? OR LOWER(description) LIKE ? OR LOWER(assigned_to_name) LIKE ?)");
            String searchPattern = "%" + searchText.toLowerCase().trim() + "%";
            parameters.add(searchPattern);
            parameters.add(searchPattern);
            parameters.add(searchPattern);
        }

        // Add status filter
        if (status != null && !status.equals("All")) {
            queryBuilder.append(" AND status = ?");
            parameters.add(status);
        }

        // Add priority filter
        if (priority != null && !priority.equals("All")) {
            queryBuilder.append(" AND priority = ?");
            parameters.add(priority);
        }

        queryBuilder.append(" ORDER BY due_date DESC");

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
                PreparedStatement pstmt = conn.prepareStatement(queryBuilder.toString())) {

            for (int i = 0; i < parameters.size(); i++) {
                pstmt.setObject(i + 1, parameters.get(i));
            }

            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                tasks.add(extractTaskFromResultSet(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error searching tasks: " + e.getMessage());
        }
        return tasks;
    }

    /**
     * Gets the count of tasks assigned to an officer.
     *
     * @param officerId Unique identifier of the officer
     * @return Number of tasks assigned to the officer
     */
    public int getTaskCountByOfficer(String officerId) {
        if (officerId == null || officerId.trim().isEmpty()) {
            return 0;
        }

        String query = "SELECT COUNT(*) as task_count FROM tasks WHERE assigned_to_id = ?";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
                PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, officerId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return rs.getInt("task_count");
            }
        } catch (SQLException e) {
            System.err.println("Error getting task count by officer: " + e.getMessage());
        }
        return 0;
    }

    /**
     * Gets the total number of tasks in the database.
     *
     * @return Total count of tasks
     */
    public int getTotalTaskCount() {
        String query = "SELECT COUNT(*) as total_tasks FROM tasks";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(query)) {

            if (rs.next()) {
                return rs.getInt("total_tasks");
            }
        } catch (SQLException e) {
            System.err.println("Error getting total task count: " + e.getMessage());
        }
        return 0;
    }

    /**
     * Generates a unique task identifier. Format: TASK-XXXX where X is random
     * alphanumeric characters.
     *
     * @return Unique task identifier string
     */
    public String generateTaskId() {
        String prefix = "TASK-";
        String random = java.util.UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        return prefix + random;
    }

    /**
     * Marks a task as completed with completion details.
     *
     * @param taskId Unique identifier of the task
     * @param completedBy Identifier of the person completing the task
     * @param completionNotes Notes about task completion
     * @return true if task was marked as completed successfully, false
     * otherwise
     */
    public boolean markTaskAsCompleted(String taskId, String completedBy, String completionNotes) {
        String query = "UPDATE tasks SET status = 'Completed', completed_by = ?, "
                + "completion_notes = ?, completed_date = CURDATE() WHERE task_id = ?";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
                PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, completedBy);
            pstmt.setString(2, completionNotes);
            pstmt.setString(3, taskId);

            int result = pstmt.executeUpdate();
            return result > 0;
        } catch (SQLException e) {
            System.err.println("Error marking task as completed: " + e.getMessage());
            return false;
        }
    }

    /**
     * Gets tasks by category.
     *
     * @param category Category to filter by
     * @return List of Task objects in specified category
     */
    public List<Task> getTasksByCategory(String category) {
        List<Task> tasks = new ArrayList<>();

        if (category == null || category.trim().isEmpty()) {
            return tasks;
        }

        String query = "SELECT * FROM tasks WHERE category = ? ORDER BY due_date DESC";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
                PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, category);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                tasks.add(extractTaskFromResultSet(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving tasks by category: " + e.getMessage());
        }
        return tasks;
    }

    /**
     * Extracts Task object from a ResultSet.
     *
     * @param resultSet ResultSet containing task data
     * @return Task object populated with data
     * @throws SQLException if database error occurs
     */
    private Task extractTaskFromResultSet(ResultSet resultSet) throws SQLException {
        Task task = new Task();
        task.setTaskId(resultSet.getString("task_id"));

        // Read task name, fallback to title if task_name doesn't exist
        String taskName = null;
        try {
            taskName = resultSet.getString("task_name");
        } catch (SQLException e) {
            try {
                taskName = resultSet.getString("title");
            } catch (SQLException e2) {
                taskName = "Untitled Task";
            }
        }
        task.setTaskName(taskName != null ? taskName : "Untitled Task");

        task.setDescription(resultSet.getString("description"));
        task.setAssignedToId(resultSet.getString("assigned_to_id"));
        task.setAssignedToName(resultSet.getString("assigned_to_name"));
        task.setPriority(resultSet.getString("priority"));
        task.setStatus(resultSet.getString("status"));

        // Set due date
        Date dueDate = resultSet.getDate("due_date");
        if (dueDate != null) {
            task.setDueDate(dueDate.toLocalDate());
        }

        // Set completed date
        Date completedDate = resultSet.getDate("completed_date");
        if (completedDate != null) {
            task.setCompletedDate(completedDate.toLocalDate());
        }

        // Set creation date
        Date createdAt = resultSet.getDate("created_at");
        if (createdAt != null) {
            task.setCreatedAt(createdAt.toLocalDate());
        }

        // Read optional fields
        task.setCreatedBy(getOptionalString(resultSet, "created_by"));
        task.setCompletedBy(getOptionalString(resultSet, "completed_by"));
        task.setCompletionNotes(getOptionalString(resultSet, "completion_notes"));
        task.setCategory(getOptionalString(resultSet, "category"));
        task.setEstimatedHours(getOptionalInt(resultSet, "estimated_hours"));

        return task;
    }

    /**
     * Safely retrieves an optional string field from ResultSet.
     *
     * @param resultSet ResultSet containing data
     * @param columnName Name of the column to retrieve
     * @return Column value or null if column doesn't exist
     */
    private String getOptionalString(ResultSet resultSet, String columnName) {
        try {
            return resultSet.getString(columnName);
        } catch (SQLException e) {
            return null;
        }
    }

    /**
     * Safely retrieves an optional integer field from ResultSet.
     *
     * @param resultSet ResultSet containing data
     * @param columnName Name of the column to retrieve
     * @return Column value or 0 if column doesn't exist
     */
    private int getOptionalInt(ResultSet resultSet, String columnName) {
        try {
            return resultSet.getInt(columnName);
        } catch (SQLException e) {
            return 0;
        }
    }
}
