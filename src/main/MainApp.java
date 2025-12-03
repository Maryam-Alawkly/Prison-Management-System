package main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

/**
 * Main application class for Prison Management System
 */
public class MainApp extends Application {
    
    @Override
    public void start(Stage primaryStage) throws Exception {
        // Load the login screen instead of dashboard
        Parent root = FXMLLoader.load(getClass().getResource("/view/Login.fxml"));
        
        // Set up the main stage
        primaryStage.setTitle("Prison Management System - Login");
        primaryStage.getIcons().add(new Image("/images/login.png"));
        
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
        
        // Test database connection on startup
        testDatabaseConnection();
    }
    
    public static void main(String[] args) {
        launch(args);
    }
    
    /**
     * Test database connection when application starts
     */
    private static void testDatabaseConnection() {
        try {
            database.DatabaseConnection.testConnection();
            System.out.println("Database connection test completed successfully.");
        } catch (Exception e) {
            System.err.println("Warning: Database connection test failed: " + e.getMessage());
        }
    }
}