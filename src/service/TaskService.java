package service;

import database.TaskDAO;
import model.Task;
import java.util.List;

/**
 * Service layer for Task management operations.
 * Implements Singleton pattern to provide a single instance throughout the application.
 * Handles business logic for task assignment, tracking, and completion.
 */
public class TaskService {
    
    private static TaskService instance;
    private final TaskDAO taskDAO;
    
    /**
     * Private constructor to enforce Singleton pattern.
     * Initializes the DAO instance.
     */
    private TaskService() {
        this.taskDAO = TaskDAO.getInstance();
    }
    
    /**
     * Returns the single instance of TaskService.
     * Implements thread-safe lazy initialization.
     *
     * @return TaskService instance
     */
    public static synchronized TaskService getInstance() {
        if (instance == null) {
            instance = new TaskService();
        }
        return instance;
    }
    
    /**
     * Retrieves all tasks from the database.
     *
     * @return List of all Task objects
     */
    public List<Task> getAllTasks() {
        return taskDAO.getAllTasks();
    }
    
    /**
     * Retrieves tasks by their status.
     *
     * @param status Task status to filter by
     * @return List of Task objects with specified status
     */
    public List<Task> getTasksByStatus(String status) {
        return taskDAO.getTasksByStatus(status);
    }
    
    /**
     * Retrieves overdue tasks that are not completed.
     *
     * @return List of overdue Task objects
     */
    public List<Task> getOverdueTasks() {
        return taskDAO.getOverdueTasks();
    }
    
    /**
     * Adds a new task to the database.
     *
     * @param task Task object to add
     * @return true if task was added successfully, false otherwise
     */
    public boolean addTask(Task task) {
        return taskDAO.addTask(task);
    }
    
    /**
     * Updates an existing task in the database.
     *
     * @param task Task object with updated values
     * @return true if update was successful, false otherwise
     */
    public boolean updateTask(Task task) {
        return taskDAO.updateTask(task);
    }
    
    /**
     * Retrieves tasks assigned to a specific officer.
     *
     * @param officerId Unique identifier of the officer
     * @return List of Task objects assigned to the officer
     */
    public List<Task> getTasksByOfficer(String officerId) {
        return taskDAO.getTasksByOfficer(officerId);
    }
    
    /**
     * Retrieves a task by its unique identifier.
     *
     * @param taskId Unique identifier of the task
     * @return Task object if found, null otherwise
     */
    public Task getTaskById(String taskId) {
        return taskDAO.getTaskById(taskId);
    }
    
    /**
     * Updates the status of a task.
     *
     * @param taskId Unique identifier of the task
     * @param status New status to set
     * @return true if status update was successful, false otherwise
     */
    public boolean updateTaskStatus(String taskId, String status) {
        return taskDAO.updateTaskStatus(taskId, status);
    }
    
    /**
     * Deletes a task from the database.
     *
     * @param taskId Unique identifier of the task to delete
     * @return true if deletion was successful, false otherwise
     */
    public boolean deleteTask(String taskId) {
        return taskDAO.deleteTask(taskId);
    }
    
    /**
     * Searches for tasks based on multiple criteria.
     *
     * @param searchText Text to search in task name, description, or assignee name
     * @param status Status to filter by
     * @param priority Priority to filter by
     * @return List of Task objects matching search criteria
     */
    public List<Task> searchTasks(String searchText, String status, String priority) {
        return taskDAO.searchTasks(searchText, status, priority);
    }
    
    /**
     * Gets the count of tasks assigned to an officer.
     *
     * @param officerId Unique identifier of the officer
     * @return Number of tasks assigned to the officer
     */
    public int getTaskCountByOfficer(String officerId) {
        return taskDAO.getTaskCountByOfficer(officerId);
    }
    
    /**
     * Gets the total number of tasks in the database.
     *
     * @return Total count of tasks
     */
    public int getTotalTaskCount() {
        return taskDAO.getTotalTaskCount();
    }
    
    /**
     * Generates a unique task identifier.
     *
     * @return Unique task identifier string
     */
    public String generateTaskId() {
        return taskDAO.generateTaskId();
    }
    
    /**
     * Marks a task as completed with completion details.
     *
     * @param taskId Unique identifier of the task
     * @param completedBy Identifier of the person completing the task
     * @param completionNotes Notes about task completion
     * @return true if task was marked as completed successfully, false otherwise
     */
    public boolean markTaskAsCompleted(String taskId, String completedBy, String completionNotes) {
        return taskDAO.markTaskAsCompleted(taskId, completedBy, completionNotes);
    }
    
    /**
     * Gets tasks by category.
     *
     * @param category Category to filter by
     * @return List of Task objects in specified category
     */
    public List<Task> getTasksByCategory(String category) {
        return taskDAO.getTasksByCategory(category);
    }
}