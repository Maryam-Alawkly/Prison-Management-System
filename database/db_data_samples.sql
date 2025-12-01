-- Prison Management System Initial Data
-- Sample data for testing and development

USE prison_management_system;

-- Insert sample prisoners
INSERT INTO prisoners (prisoner_id, name, phone, crime, cell_number, sentence_duration, admission_date) VALUES 
('P001', 'Ahmed Mohamed', '0912345678', 'Theft', 'A-101', '5 years', '2024-01-15'),
('P002', 'Mohamed Ali', '0911223344', 'Fraud', 'B-202', '3 years', '2024-03-20'),
('P003', 'Khalid Hassan', '0911556677', 'Assault', 'C-303', '7 years', '2024-02-10');

-- Insert sample employees
INSERT INTO employees (employee_id, name, phone, position, department, salary, hire_date) VALUES 
('E001', 'John Smith', '0912334455', 'Prison Officer', 'Security', 2500.00, '2023-05-10'),
('E002', 'Sarah Johnson', '0911445566', 'Administrator', 'Administration', 3000.00, '2023-02-15'),
('E003', 'Mike Brown', '0911667788', 'Warden', 'Management', 5000.00, '2022-11-20');

-- Insert sample cells
INSERT INTO cells (cell_number, cell_type, capacity, security_level) VALUES 
('A-101', 'General', 4, 'Medium'),
('B-202', 'Single', 1, 'Maximum'),
('C-303', 'General', 6, 'Minimum'),
('D-404', 'Solitary', 1, 'Maximum');

-- Insert sample visitors
INSERT INTO visitors (visitor_id, name, phone, relationship, prisoner_id, status) VALUES 
('V001', 'Fatima Ahmed', '0912778899', 'Wife', 'P001', 'Approved'),
('V002', 'Ali Mohamed', '0912889900', 'Brother', 'P002', 'Approved'),
('V003', 'Hassan Khalid', '0912990011', 'Father', 'P003', 'Pending');

-- Insert sample visits
INSERT INTO visits (visit_id, prisoner_id, visitor_id, scheduled_datetime, duration, status) VALUES 
('VIS001', 'P001', 'V001', '2024-12-01 14:00:00', 60, 'Scheduled'),
('VIS002', 'P002', 'V002', '2024-12-02 10:30:00', 45, 'Completed'),
('VIS003', 'P003', 'V003', '2024-12-03 16:00:00', 30, 'Scheduled');

-- Display counts for verification
SELECT 
    (SELECT COUNT(*) FROM prisoners) as prisoner_count,
    (SELECT COUNT(*) FROM employees) as employee_count,
    (SELECT COUNT(*) FROM cells) as cell_count,
    (SELECT COUNT(*) FROM visitors) as visitor_count,
    (SELECT COUNT(*) FROM visits) as visit_count;