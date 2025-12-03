USE prison_management_system;
-- Security Logs Table
CREATE TABLE security_logs (
    log_id VARCHAR(50) PRIMARY KEY,
    event_type VARCHAR(50) NOT NULL,
    severity VARCHAR(20) NOT NULL, -- 'Low', 'Medium', 'High', 'Critical'
    description TEXT NOT NULL,
    location VARCHAR(100),
    timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    employee_id VARCHAR(50),
    affected_entity VARCHAR(100),
    ip_address VARCHAR(45),
    status VARCHAR(20) DEFAULT 'Pending', -- 'Pending', 'Investigating', 'Resolved'
    resolution_notes TEXT,
    resolved_at TIMESTAMP NULL,
    resolved_by VARCHAR(50),
    
    INDEX idx_security_logs_timestamp (timestamp),
    INDEX idx_security_logs_severity (severity),
    INDEX idx_security_logs_status (status),
    FOREIGN KEY (employee_id) REFERENCES employees(employee_id) ON DELETE SET NULL,
    FOREIGN KEY (resolved_by) REFERENCES employees(employee_id) ON DELETE SET NULL
);

-- Security Alerts Table
CREATE TABLE security_alerts (
    alert_id VARCHAR(50) PRIMARY KEY,
    alert_type VARCHAR(50) NOT NULL, -- 'Intrusion', 'Equipment_Failure', 'Unauthorized_Access', 'Emergency'
    severity VARCHAR(20) NOT NULL, -- 'Low', 'Medium', 'High', 'Critical'
    description TEXT NOT NULL,
    location VARCHAR(100),
    triggered_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    acknowledged_at TIMESTAMP NULL,
    resolved_at TIMESTAMP NULL,
    triggered_by VARCHAR(50), -- System or Employee ID
    acknowledged_by VARCHAR(50),
    resolved_by VARCHAR(50),
    status VARCHAR(20) DEFAULT 'Active', -- 'Active', 'Acknowledged', 'Resolved'
    assigned_to VARCHAR(50),
    notes TEXT,
    requires_response BOOLEAN DEFAULT TRUE,
    response_time INT DEFAULT 5, -- in minutes
    
    INDEX idx_security_alerts_status (status),
    INDEX idx_security_alerts_severity (severity),
    INDEX idx_security_alerts_triggered (triggered_at),
    FOREIGN KEY (acknowledged_by) REFERENCES employees(employee_id) ON DELETE SET NULL,
    FOREIGN KEY (resolved_by) REFERENCES employees(employee_id) ON DELETE SET NULL,
    FOREIGN KEY (assigned_to) REFERENCES employees(employee_id) ON DELETE SET NULL
);

-- Access Control Table
CREATE TABLE access_controls (
    control_id VARCHAR(50) PRIMARY KEY,
    employee_id VARCHAR(50) NOT NULL,
    employee_name VARCHAR(100) NOT NULL,
    module VARCHAR(50) NOT NULL,
    permission_level VARCHAR(20) NOT NULL, -- 'None', 'View', 'Edit', 'Full'
    granted_by VARCHAR(50),
    granted_date DATE ,
    expires_on DATE NULL,
    is_active BOOLEAN DEFAULT TRUE,
    
    INDEX idx_access_controls_employee (employee_id),
    INDEX idx_access_controls_module (module),
    UNIQUE KEY unique_access (employee_id, module),
    FOREIGN KEY (employee_id) REFERENCES employees(employee_id) ON DELETE CASCADE,
    FOREIGN KEY (granted_by) REFERENCES employees(employee_id) ON DELETE SET NULL
);

-- Surveillance Cameras Table
CREATE TABLE surveillance_cameras (
    camera_id VARCHAR(50) PRIMARY KEY,
    camera_name VARCHAR(100) NOT NULL,
    location VARCHAR(100) NOT NULL,
    camera_type VARCHAR(20), -- 'PTZ', 'Fixed', 'Dome', 'Bullet'
    status VARCHAR(20) DEFAULT 'Online', -- 'Online', 'Offline', 'Maintenance'
    ip_address VARCHAR(45) NOT NULL,
    resolution INT DEFAULT 1080,
    last_active TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    last_maintenance DATE NULL,
    is_recording BOOLEAN DEFAULT TRUE,
    recording_schedule VARCHAR(50) DEFAULT '24/7',
    storage_days INT DEFAULT 30,
    
    INDEX idx_cameras_status (status),
    INDEX idx_cameras_location (location),
    UNIQUE KEY unique_ip (ip_address)
);

-- Emergency Procedures Table
CREATE TABLE emergency_procedures (
    procedure_id INT PRIMARY KEY AUTO_INCREMENT,
    procedure_type VARCHAR(50) NOT NULL, -- 'Lockdown', 'Fire', 'Medical', 'Intruder'
    title VARCHAR(100) NOT NULL,
    description TEXT NOT NULL,
    steps TEXT NOT NULL,
    contact_person VARCHAR(100),
    contact_phone VARCHAR(20),
    last_updated TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_by VARCHAR(50),
    
    INDEX idx_procedures_type (procedure_type),
    FOREIGN KEY (updated_by) REFERENCES employees(employee_id) ON DELETE SET NULL
);

-- Insert default emergency procedures
INSERT INTO emergency_procedures (procedure_type, title, description, steps) VALUES
('Lockdown', 'Full Facility Lockdown', 'Complete lockdown of all prison areas', 
'1. Press red lockdown button\n2. Announce lockdown over PA system\n3. Secure all cell doors\n4. Seal all exits\n5. Account for all personnel'),
('Fire', 'Fire Emergency Procedure', 'Response to fire emergencies',
'1. Activate fire alarm\n2. Call fire department\n3. Evacuate affected areas\n4. Use fire extinguishers if safe\n5. Account for all personnel'),
('Medical', 'Medical Emergency Response', 'Response to medical emergencies',
'1. Call medical response team\n2. Provide first aid if trained\n3. Secure the area\n4. Document the incident\n5. Notify next of kin if necessary');