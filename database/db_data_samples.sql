-- Prison Management System Initial Data
-- Sample data for testing and development

USE prison_management_system;


-- Insert default emergency procedures
INSERT INTO emergency_procedures (procedure_type, title, description, steps) VALUES
('Lockdown', 'Full Facility Lockdown', 'Complete lockdown of all prison areas', 
'1. Press red lockdown button\n2. Announce lockdown over PA system\n3. Secure all cell doors\n4. Seal all exits\n5. Account for all personnel'),
('Fire', 'Fire Emergency Procedure', 'Response to fire emergencies',
'1. Activate fire alarm\n2. Call fire department\n3. Evacuate affected areas\n4. Use fire extinguishers if safe\n5. Account for all personnel'),
('Medical', 'Medical Emergency Response', 'Response to medical emergencies',
'1. Call medical response team\n2. Provide first aid if trained\n3. Secure the area\n4. Document the incident\n5. Notify next of kin if necessary');

-- ==================== DUMMY DATA FOR TESTING ====================
-- Insert dummy employees for testing
INSERT INTO employees (employee_id, name, phone, position, department, username, password, role) VALUES
('EMP001', 'John Smith', '555-0101', 'Security Officer', 'Security', 'john.smith', 'password123', 'Officer'),
('EMP002', 'Sarah Johnson', '555-0102', 'Warden', 'Administration', 'admin', 'admin123', 'Administrator'),
('EMP003', 'Mike Brown', '555-0103', 'Guard', 'Security', 'mike.b', 'guard123', 'Officer');

-- Insert dummy prisoners
INSERT INTO prisoners (prisoner_id, name, phone, crime, cell_number, sentence_duration, status) VALUES
('PR001', 'Robert Wilson', '555-0201', 'Burglary', 'A101', '5 years', 'In Custody'),
('PR002', 'James Davis', '555-0202', 'Assault', 'A102', '10 years', 'In Custody'),
('PR003', 'Thomas Miller', '555-0203', 'Fraud', 'B201', '3 years', 'In Custody');

-- Insert dummy cells
INSERT INTO cells (cell_number, cell_type, capacity, current_occupancy, security_level, status) VALUES
('A101', 'Standard', 2, 1, 'Medium', 'Occupied'),
('A102', 'Standard', 2, 1, 'Medium', 'Occupied'),
('B201', 'Standard', 2, 1, 'Low', 'Occupied'),
('C301', 'Solitary', 1, 0, 'High', 'Vacant');

-- Insert dummy tasks
INSERT INTO tasks (task_id, task_name, description, assigned_to_id, assigned_to_name, status, due_date) VALUES
('TASK001', 'Cell Inspection', 'Inspect all cells in Block A', 'EMP001', 'John Smith', 'Pending', DATE_ADD(CURDATE(), INTERVAL 1 DAY)),
('TASK002', 'Security Patrol', 'Perimeter patrol during night shift', 'EMP003', 'Mike Brown', 'In Progress', CURDATE()),
('TASK003', 'Report Review', 'Review daily security reports', 'EMP002', 'Sarah Johnson', 'Completed', CURDATE());

-- Insert dummy security alert
INSERT INTO security_alerts (alert_id, alert_type, severity, description, location, triggered_by, status) VALUES
('ALERT001', 'Security Breach', 'High', 'Unauthorized access attempt at Main Gate', 'Main Gate', 'System', 'Active');

-- Insert dummy guard duty
INSERT INTO guard_duties (duty_id, officer_id, officer_name, duty_type, location, start_time, end_time, duty_date, status) VALUES
('DUTY001', 'EMP001', 'John Smith', 'Security Patrol', 'Perimeter', '08:00', '16:00', CURDATE(), 'Scheduled');