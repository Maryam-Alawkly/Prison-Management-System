package model;

/**
 * Camera class represents a surveillance camera
 */
public class Camera {

    private String cameraId;
    private String cameraName;
    private String location; // e.g., "Main Gate", "Cell Block A", "Visitation Room"
    private String cameraType; // "PTZ", "Fixed", "Dome", "Bullet"
    private String status; // "Online", "Offline", "Maintenance", "Recording"
    private String ipAddress;
    private int resolution; // e.g., 1080, 720, 480
    private String lastActive;
    private String lastMaintenance;
    private boolean isRecording;
    private String recordingSchedule; // e.g., "24/7", "Motion Only", "Scheduled"
    private int storageDays; // Number of days to keep recordings

    /**
     * Constructor for Camera
     */
    public Camera(String cameraId, String cameraName, String location,
            String cameraType, String ipAddress) {
        this.cameraId = cameraId;
        this.cameraName = cameraName;
        this.location = location;
        this.cameraType = cameraType;
        this.ipAddress = ipAddress;
        this.status = "Online";
        this.resolution = 1080;
        this.isRecording = true;
        this.recordingSchedule = "24/7";
        this.storageDays = 30;
        this.lastActive = java.time.LocalDateTime.now().toString();
    }

    // Getters and Setters
    public String getCameraId() {
        return cameraId;
    }

    public void setCameraId(String cameraId) {
        this.cameraId = cameraId;
    }

    public String getCameraName() {
        return cameraName;
    }

    public void setCameraName(String cameraName) {
        this.cameraName = cameraName;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getCameraType() {
        return cameraType;
    }

    public void setCameraType(String cameraType) {
        this.cameraType = cameraType;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public int getResolution() {
        return resolution;
    }

    public void setResolution(int resolution) {
        this.resolution = resolution;
    }

    public String getLastActive() {
        return lastActive;
    }

    public void setLastActive(String lastActive) {
        this.lastActive = lastActive;
    }

    public String getLastMaintenance() {
        return lastMaintenance;
    }

    public void setLastMaintenance(String lastMaintenance) {
        this.lastMaintenance = lastMaintenance;
    }

    public boolean isRecording() {
        return isRecording;
    }

    public void setRecording(boolean recording) {
        isRecording = recording;
    }

    public String getRecordingSchedule() {
        return recordingSchedule;
    }

    public void setRecordingSchedule(String recordingSchedule) {
        this.recordingSchedule = recordingSchedule;
    }

    public int getStorageDays() {
        return storageDays;
    }

    public void setStorageDays(int storageDays) {
        this.storageDays = storageDays;
    }

    /**
     * Check if camera is online
     */
    public boolean isOnline() {
        return "Online".equals(status);
    }

    /**
     * Check if camera needs maintenance
     */
    public boolean needsMaintenance() {
        if (lastMaintenance == null) {
            return false;
        }

        java.time.LocalDate lastMaintenanceDate = java.time.LocalDate.parse(lastMaintenance);
        java.time.LocalDate ninetyDaysAgo = java.time.LocalDate.now().minusDays(90);

        return lastMaintenanceDate.isBefore(ninetyDaysAgo);
    }

    /**
     * Get camera details summary
     */
    public String getCameraSummary() {
        return String.format("%s - %s (%s) - %s",
                cameraName, location, cameraType, status);
    }
}
