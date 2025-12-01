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


-- Add login columns to employees table
ALTER TABLE employees 
ADD COLUMN username VARCHAR(50) UNIQUE,
ADD COLUMN password VARCHAR(100),
ADD COLUMN role VARCHAR(20) DEFAULT 'Officer',
ADD COLUMN last_login DATETIME,
ADD COLUMN is_active BOOLEAN DEFAULT TRUE;

-- Insert a dedicated admin user if needed
-- INSERT INTO employees (employee_id, name, username, password, role, position, department, salary, hire_date) 
-- VALUES ('ADMIN001', 'System Administrator', 'admin', 'admin123', 'Administrator', 'System Admin', 'IT', 5000.00, CURDATE());
