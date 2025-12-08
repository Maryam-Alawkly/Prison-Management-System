-- Prison Management System Database Schema
-- Created by: Maryam Alawkly
-- Date: 2025

CREATE DATABASE IF NOT EXISTS prison_management_system;
USE prison_management_system;

-- Table: prisoners
CREATE TABLE prisoners (
    prisoner_id VARCHAR(20) PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    phone VARCHAR(15),
    crime VARCHAR(200),
    cell_number VARCHAR(10),
    sentence_duration VARCHAR(50),
    status VARCHAR(20) DEFAULT 'In Custody',
    admission_date DATE,
    release_date DATE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Table: employees
CREATE TABLE employees (
    employee_id VARCHAR(20) PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    phone VARCHAR(15),
    position VARCHAR(50),
    department VARCHAR(50),
    salary DECIMAL(10,2),
    hire_date DATE,
    status VARCHAR(20) DEFAULT 'Active',
    username VARCHAR(50) UNIQUE,
    password VARCHAR(50),
    role VARCHAR(20) DEFAULT 'Officer',
    last_login DATE DEFAULT NULL,
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Table: cells
CREATE TABLE cells (
    cell_number VARCHAR(10) PRIMARY KEY,
    cell_type VARCHAR(20),
    capacity INT,
    current_occupancy INT DEFAULT 0,
    security_level VARCHAR(20),
    status VARCHAR(20) DEFAULT 'Vacant',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Table: visitors
CREATE TABLE visitors (
    visitor_id VARCHAR(20) PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    phone VARCHAR(15),
    relationship VARCHAR(50),
    prisoner_id VARCHAR(20),
    visit_count INT DEFAULT 0,
    last_visit_date DATE,
    status VARCHAR(20) DEFAULT 'Pending',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (prisoner_id) REFERENCES prisoners(prisoner_id)
    ON DELETE CASCADE
);

-- Table: visits
CREATE TABLE visits (
    visit_id VARCHAR(20) PRIMARY KEY,
    prisoner_id VARCHAR(20),
    visitor_id VARCHAR(20),
    scheduled_datetime DATETIME,
    duration INT,
    status VARCHAR(20) DEFAULT 'Scheduled',
    notes TEXT,
    actual_start_datetime DATETIME,
    actual_end_datetime DATETIME,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (prisoner_id) REFERENCES prisoners(prisoner_id)
    ON DELETE CASCADE,
    FOREIGN KEY (visitor_id) REFERENCES visitors(visitor_id)
    ON DELETE CASCADE
);

-- ==================== DAILY REPORTS ====================
CREATE TABLE daily_reports (
    report_id VARCHAR(50) PRIMARY KEY,
    officer_id VARCHAR(20),
    officer_name VARCHAR(100),
    report_type VARCHAR(50),
    priority VARCHAR(20),
    report_date DATE,
    status VARCHAR(20) DEFAULT 'Draft',
    incidents_summary TEXT,
    actions_taken TEXT,
    patrols_completed VARCHAR(10),
    cell_inspections VARCHAR(10),
    visitor_screenings VARCHAR(10),
    activity_details TEXT,
    additional_notes TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    reviewed_by VARCHAR(100),
    review_date DATE,
    admin_comments TEXT,
    security_level VARCHAR(20),
    alerts_handled TEXT,
    headcount VARCHAR(10),
    transfers VARCHAR(10),
    disciplinary_actions VARCHAR(10),
    behavior_notes TEXT,
    equipment_checks VARCHAR(10),
    issues_encountered TEXT,
    recommendations TEXT,
    INDEX idx_report_date (report_date),
    INDEX idx_officer_id (officer_id),
    INDEX idx_status (status)
);

-- ==================== SECURITY TABLES ====================
-- Security Alerts Table
CREATE TABLE security_alerts (
    alert_id VARCHAR(50) PRIMARY KEY,
    alert_type VARCHAR(50) NOT NULL,
    severity VARCHAR(20) NOT NULL,
    description TEXT,
    location VARCHAR(100),
    triggered_by VARCHAR(50),
    triggered_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    status VARCHAR(20) DEFAULT 'Active',
    acknowledged_by VARCHAR(50),
    acknowledged_at TIMESTAMP NULL,
    resolved_by VARCHAR(50),
    resolved_at TIMESTAMP NULL,
    resolution_notes TEXT,
    assigned_to VARCHAR(50),
    INDEX idx_alert_status (status),
    INDEX idx_alert_severity (severity),
    INDEX idx_triggered_at (triggered_at)
);

-- Security Logs Table
CREATE TABLE security_logs (
    log_id VARCHAR(50) PRIMARY KEY,
    event_type VARCHAR(100) NOT NULL,
    severity VARCHAR(20) NOT NULL,
    description TEXT NOT NULL,
    location VARCHAR(100),
    timestamp DATETIME NOT NULL,
    employee_id VARCHAR(50),
    affected_entity VARCHAR(100),
    ip_address VARCHAR(45),
    status VARCHAR(20) DEFAULT 'Active',
    resolution_notes TEXT,
    resolved_by VARCHAR(50),
    resolved_at DATETIME,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Access Control Table
CREATE TABLE access_controls (
    control_id VARCHAR(20) PRIMARY KEY,
    employee_id VARCHAR(20),
    employee_name VARCHAR(100),
    module VARCHAR(50),
    permission_level VARCHAR(20),
    granted_date DATE,
    granted_by VARCHAR(50),
    status VARCHAR(20) DEFAULT 'Active',
    revoked_date DATE,
    revoked_by VARCHAR(50),
    INDEX idx_access_employee (employee_id),
    INDEX idx_access_module (module)
);

-- ==================== TASKS & DUTIES ====================
-- Tasks Table (ONLY ONE DEFINITION)
CREATE TABLE tasks (
    task_id VARCHAR(20) PRIMARY KEY,
    task_name VARCHAR(100),
    description TEXT,
    assigned_to_id VARCHAR(20),
    assigned_to_name VARCHAR(100),
    priority VARCHAR(20) DEFAULT 'Medium',
    status VARCHAR(20) DEFAULT 'Pending',
    due_date DATE,
    completed_date DATE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(50),
    completed_by VARCHAR(50),
    completion_notes TEXT,
    category VARCHAR(50),
    estimated_hours INT DEFAULT 0,
    INDEX idx_task_status (status),
    INDEX idx_assigned_to (assigned_to_id),
    INDEX idx_due_date (due_date)
);

-- Guard Duties Table
CREATE TABLE guard_duties (
    duty_id VARCHAR(50) PRIMARY KEY,
    officer_id VARCHAR(50),
    officer_name VARCHAR(100),
    duty_type VARCHAR(50),
    location VARCHAR(100),
    start_time VARCHAR(10),
    end_time VARCHAR(10),
    duty_date DATE,
    status VARCHAR(20) DEFAULT 'Scheduled',
    priority VARCHAR(20) DEFAULT 'Medium',
    completed_time VARCHAR(10),
    notes TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_duty_date (duty_date),
    INDEX idx_officer_duty (officer_id),
    INDEX idx_duty_status (status)
);

-- ==================== FEEDBACK & COMMENTS ====================
-- Report Comments Table
CREATE TABLE report_comments (
    comment_id INT AUTO_INCREMENT PRIMARY KEY,
    report_id VARCHAR(50),
    officer_id VARCHAR(20),
    comment_text TEXT,
    commented_by VARCHAR(100),
    comment_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (report_id) REFERENCES daily_reports(report_id) ON DELETE CASCADE,
    INDEX idx_comment_report (report_id)
);

-- Report Feedback Table (ONLY ONE DEFINITION)
CREATE TABLE report_feedback (
    feedback_id INT AUTO_INCREMENT PRIMARY KEY,
    report_id VARCHAR(50),
    officer_id VARCHAR(20),
    feedback TEXT,
    from_admin VARCHAR(100),
    sent_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    read_status BOOLEAN DEFAULT FALSE,
    FOREIGN KEY (report_id) REFERENCES daily_reports(report_id) ON DELETE CASCADE,
    INDEX idx_feedback_report (report_id),
    INDEX idx_feedback_officer (officer_id)
);

-- Report Templates Table
CREATE TABLE report_templates (
    template_id INT AUTO_INCREMENT PRIMARY KEY,
    template_name VARCHAR(100),
    report_type VARCHAR(50),
    template_content TEXT,
    created_by VARCHAR(100),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- ==================== EMERGENCY PROCEDURES ====================
-- Emergency Procedures Table
CREATE TABLE emergency_procedures (
    procedure_id INT PRIMARY KEY AUTO_INCREMENT,
    procedure_type VARCHAR(50) NOT NULL,
    title VARCHAR(100) NOT NULL,
    description TEXT NOT NULL,
    steps TEXT NOT NULL,
    contact_person VARCHAR(100),
    contact_phone VARCHAR(20),
    last_updated TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_by VARCHAR(50),
    INDEX idx_procedures_type (procedure_type)
    -- Note: Removed foreign key to avoid dependency issues during setup
);
