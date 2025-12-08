package main;

import database.DatabaseConnection;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Main application class for Prison Management System. This class serves as the
 * entry point for the JavaFX application and initializes the primary user
 * interface components.
 */
public class MainApp extends Application {

    private static final Logger LOGGER = Logger.getLogger(MainApp.class.getName());
    private static final String LOGIN_FXML_PATH = "/view/Login.fxml";
    private static final String APPLICATION_ICON_PATH = "/images/login.png";
    private static final String APPLICATION_TITLE = "Prison Management System - Login";

    /**
     * Main method - entry point for the Java application. Launches the JavaFX
     * application.
     *
     * @param args Command line arguments passed to the application
     */
    public static void main(String[] args) {
        launch(args);
    }

    /**
     * Initializes and displays the primary stage of the application. This
     * method is called after the JavaFX runtime has been initialized.
     *
     * @param primaryStage The primary stage for this application
     */
    @Override
    public void start(Stage primaryStage) {
        try {
            initializeApplication(primaryStage);
            testDatabaseConnection();
        } catch (Exception exception) {
            LOGGER.log(Level.SEVERE, "Failed to initialize application", exception);
            showErrorDialog(exception.getMessage());
        }
    }

    /**
     * Initializes the application user interface.
     *
     * @param primaryStage The primary stage to configure
     * @throws Exception if FXML loading fails
     */
    private void initializeApplication(Stage primaryStage) throws Exception {
        Parent root = loadFXML();
        configureStage(primaryStage, root);
        displayStage(primaryStage);
    }

    /**
     * Loads the login interface from FXML file.
     *
     * @return Parent node containing the login interface
     * @throws Exception if FXML file cannot be loaded
     */
    private Parent loadFXML() throws Exception {
        return FXMLLoader.load(getClass().getResource(LOGIN_FXML_PATH));
    }

    /**
     * Configures the primary stage properties.
     *
     * @param stage The stage to configure
     * @param root The root node for the scene
     */
    private void configureStage(Stage stage, Parent root) {
        setApplicationIcon(stage);
        stage.setTitle(APPLICATION_TITLE);
        stage.setResizable(false);
        stage.setScene(new Scene(root));
    }

    /**
     * Sets the application icon for the stage.
     *
     * @param stage The stage to set the icon on
     */
    private void setApplicationIcon(Stage stage) {
        try {
            Image icon = new Image(APPLICATION_ICON_PATH);
            stage.getIcons().add(icon);
        } catch (Exception exception) {
            LOGGER.log(Level.WARNING, "Could not load application icon", exception);
        }
    }

    /**
     * Displays the primary stage.
     *
     * @param stage The stage to display
     */
    private void displayStage(Stage stage) {
        stage.show();
        LOGGER.info("Application started successfully");
    }

    /**
     * Tests the database connection during application initialization. This
     * method attempts to establish a connection to the database and logs the
     * result to the application logger.
     */
    private static void testDatabaseConnection() {
        try {
            boolean connectionSuccessful = DatabaseConnection.testStaticConnection();
            if (connectionSuccessful) {
                LOGGER.info("Database connection test completed successfully");
            } else {
                LOGGER.warning("Database connection test failed - connection not established");
            }
        } catch (Exception exception) {
            LOGGER.log(Level.SEVERE, "Database connection test failed", exception);
        }
    }

    /**
     * Displays an error dialog when application initialization fails.
     *
     * @param errorMessage The error message to display
     */
    private void showErrorDialog(String errorMessage) {
        // This method should be implemented with actual JavaFX dialog
        LOGGER.severe("Application initialization error: " + errorMessage);
        System.err.println("Critical error occurred: " + errorMessage);
        System.err.println("Please check the application logs for more details.");
    }
}
