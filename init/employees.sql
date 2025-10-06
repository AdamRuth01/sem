-- Rensa och skapa databasen
DROP DATABASE IF EXISTS employees;
CREATE DATABASE employees;
USE employees;

-- Skapa tabeller
CREATE TABLE employees (
                           emp_no      INT             NOT NULL,
                           birth_date  DATE            NOT NULL,
                           first_name  VARCHAR(14)     NOT NULL,
                           last_name   VARCHAR(16)     NOT NULL,
                           gender      ENUM ('M','F')  NOT NULL,
                           hire_date   DATE            NOT NULL,
                           PRIMARY KEY (emp_no)
);

CREATE TABLE departments (
                             dept_no     CHAR(4)         NOT NULL,
                             dept_name   VARCHAR(40)     NOT NULL,
                             PRIMARY KEY (dept_no),
                             UNIQUE KEY (dept_name)
);

CREATE TABLE dept_manager (
                              emp_no       INT             NOT NULL,
                              dept_no      CHAR(4)         NOT NULL,
                              from_date    DATE            NOT NULL,
                              to_date      DATE            NOT NULL,
                              FOREIGN KEY (emp_no) REFERENCES employees (emp_no) ON DELETE CASCADE,
                              FOREIGN KEY (dept_no) REFERENCES departments (dept_no) ON DELETE CASCADE,
                              PRIMARY KEY (emp_no, dept_no)
);

CREATE TABLE dept_emp (
                          emp_no      INT             NOT NULL,
                          dept_no     CHAR(4)         NOT NULL,
                          from_date   DATE            NOT NULL,
                          to_date     DATE            NOT NULL,
                          FOREIGN KEY (emp_no) REFERENCES employees (emp_no) ON DELETE CASCADE,
                          FOREIGN KEY (dept_no) REFERENCES departments (dept_no) ON DELETE CASCADE,
                          PRIMARY KEY (emp_no, dept_no)
);

CREATE TABLE titles (
                        emp_no      INT             NOT NULL,
                        title       VARCHAR(50)     NOT NULL,
                        from_date   DATE            NOT NULL,
                        to_date     DATE,
                        FOREIGN KEY (emp_no) REFERENCES employees (emp_no) ON DELETE CASCADE,
                        PRIMARY KEY (emp_no, title, from_date)
);

CREATE TABLE salaries (
                          emp_no      INT             NOT NULL,
                          salary      INT             NOT NULL,
                          from_date   DATE            NOT NULL,
                          to_date     DATE            NOT NULL,
                          FOREIGN KEY (emp_no) REFERENCES employees (emp_no) ON DELETE CASCADE,
                          PRIMARY KEY (emp_no, from_date)
);

-- Skapa vyer
CREATE OR REPLACE VIEW dept_emp_latest_date AS
SELECT emp_no, MAX(from_date) AS from_date, MAX(to_date) AS to_date
FROM dept_emp
GROUP BY emp_no;

CREATE OR REPLACE VIEW current_dept_emp AS
SELECT l.emp_no, dept_no, l.from_date, l.to_date
FROM dept_emp d
         INNER JOIN dept_emp_latest_date l
                    ON d.emp_no = l.emp_no AND d.from_date = l.from_date AND l.to_date = d.to_date;

-- Lägg till testdata
INSERT INTO employees (emp_no, birth_date, first_name, last_name, gender, hire_date)
VALUES
    (10001, '1980-01-01', 'Alice', 'Andersson', 'F', '2005-06-01'),
    (10002, '1975-03-15', 'Bob', 'Berg', 'M', '2003-09-23'),
    (10003, '1990-07-22', 'Charlie', 'Carlsson', 'M', '2010-01-10');

INSERT INTO departments (dept_no, dept_name)
VALUES
    ('d001', 'HR'),
    ('d002', 'Engineering');

INSERT INTO dept_emp (emp_no, dept_no, from_date, to_date)
VALUES
    (10001, 'd001', '2005-06-01', '2025-01-01'),
    (10002, 'd002', '2003-09-23', '2025-01-01');

INSERT INTO titles (emp_no, title, from_date, to_date)
VALUES
    (10001, 'HR Manager', '2005-06-01', '2025-01-01'),
    (10002, 'Engineer', '2003-09-23', '2025-01-01');

INSERT INTO salaries (emp_no, salary, from_date, to_date)
VALUES
    (10001, 55000, '2005-06-01', '2025-01-01'),
    (10002, 62000, '2003-09-23', '2025-01-01');