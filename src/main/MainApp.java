package main;

import controller.ApplicationController;
import javafx.application.Application;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Main application entry point - ONLY responsible for launching JavaFX. All
 * business logic is delegated to ApplicationController.
 */
public class MainApp extends Application {

    private static final Logger LOGGER = Logger.getLogger(MainApp.class.getName());
    private static final String APPLICATION_ICON_PATH = "/images/login.png";
    private static final String FALLBACK_ICON_PATH = "/images/login.png";

    public static void main(String[] args) {
        try {
            // Set logging format for better readability
            System.setProperty("java.util.logging.SimpleFormatter.format",
                    "[%1$tF %1$tT] [%4$-7s] %5$s %n");

            launch(args);
        } catch (Exception e) {
            System.err.println("FATAL: Application failed to launch");
            e.printStackTrace();
            System.exit(1);
        }
    }

    @Override
    public void start(Stage primaryStage) {
        try {
            LOGGER.info("Application starting...");

            // Set application icon with fallback mechanism
            setApplicationIcon(primaryStage);

            // Get ApplicationController instance (Facade pattern)
            ApplicationController appController = ApplicationController.getInstance();

            // Pass primary stage to controller
            appController.setPrimaryStage(primaryStage);

            // Delegate ALL initialization to ApplicationController
            appController.initializeApplication();

        } catch (Exception exception) {
            LOGGER.log(Level.SEVERE, "Critical failure during startup", exception);
            showEmergencyErrorDialog(exception);
            System.exit(1);
        }
    }

    /**
     * Set application icon with multiple fallback options
     */
    private void setApplicationIcon(Stage stage) {
        InputStream iconStream = null;

        try {
            // Try primary icon path
            iconStream = getClass().getResourceAsStream(APPLICATION_ICON_PATH);

            if (iconStream == null) {
                LOGGER.warning("Primary icon not found: " + APPLICATION_ICON_PATH);

                // Try fallback icon
                iconStream = getClass().getResourceAsStream(FALLBACK_ICON_PATH);

                if (iconStream == null) {
                    LOGGER.warning("Fallback icon not found: " + FALLBACK_ICON_PATH);
                    return; // No icon available
                }
            }

            Image icon = new Image(iconStream);
            stage.getIcons().add(icon);
            LOGGER.info("Application icon loaded successfully");

        } catch (Exception exception) {
            LOGGER.log(Level.WARNING, "Could not load application icon", exception);
        } finally {
            if (iconStream != null) {
                try {
                    iconStream.close();
                } catch (Exception e) {
                    LOGGER.log(Level.FINE, "Error closing icon stream", e);
                }
            }
        }
    }

    /**
     * Show emergency error dialog when JavaFX fails to initialize
     */
    private void showEmergencyErrorDialog(Exception exception) {
        // Console fallback if GUI is not available
        System.err.println("=========================================");
        System.err.println("CRITICAL APPLICATION ERROR");
        System.err.println("=========================================");
        System.err.println("Error: " + exception.getMessage());
        System.err.println("Application cannot start. Please check:");
        System.err.println("1. JavaFX is properly installed");
        System.err.println("2. Required libraries are available");
        System.err.println("3. System meets minimum requirements");
        System.err.println("=========================================");
        exception.printStackTrace();
    }

    @Override
    public void stop() {
        LOGGER.info("Application stopping...");

        // Notify ApplicationController to cleanup
        ApplicationController.getInstance().cleanup();

        LOGGER.info("Application stopped");
    }
}
