INSERT INTO role (authority) VALUES ('ROLE_COURSE_ADMIN');
INSERT INTO role (authority) VALUES ('ROLE_INSTITUTE_ADMIN');
INSERT INTO role (authority) VALUES ('ROLE_USER_ADMIN');
INSERT INTO role (authority) VALUES ('ROLE_ADMISSION_ADMIN');

INSERT INTO users (password, username) VALUES
('$2a$10$RNjpi/CGfQ9t.abfnkgD7e2xUFcbHqjCKpbiPOQvod5VCoaw2VGJ.', 'Admin');

insert into user_roles (user_id, role_id) values (1,1);
insert into user_roles (user_id, role_id) values (1,2);
insert into user_roles (user_id, role_id) values (1,3);
insert into user_roles (user_id, role_id) values (1,4);

insert into course (name, course_code, isced_code, erasmus_code, ects, language, facultative, stationary) values ('Epistemology', 'EP-001', '01.6', '0223', 10, 'english', true, true);
insert into course (name, course_code, isced_code, erasmus_code, ects, language, facultative, stationary) values ('Ethics', 'ET-001', '01.6', '0223', 10, 'english', true, true);
insert into course (name, course_code, isced_code, erasmus_code, ects, language, facultative, stationary) values ('Introduction to Analytic Philosophy', 'AN-001', '01.6', '0223', 10, 'english', true, true);
insert into course (name, course_code, isced_code, erasmus_code, ects, language, facultative, stationary) values ('Philosophical Problems in Science', 'PP-001', '01.6', '0223', 10, 'english', true, true);
insert into course (name, course_code, isced_code, erasmus_code, ects, language, facultative, stationary) values ('History of Philosophy I', 'HP1-001', '01.6', '0223', 10, 'english', true, true);
insert into course (name, course_code, isced_code, erasmus_code, ects, language, facultative, stationary) values ('History of Philosophy II', 'HP2-001', '01.6', '0223', 10, 'english', true, true);
insert into course (name, course_code, isced_code, erasmus_code, ects, language, facultative, stationary) values ('Logic I', 'L1-001', '01.6', '0223', 10, 'english', true, true);
insert into course (name, course_code, isced_code, erasmus_code, ects, language, facultative, stationary) values ('Logic II', 'L2-001', '01.6', '0223', 10, 'english', true, true);
insert into course (name, course_code, isced_code, erasmus_code, ects, language, facultative, stationary) values ('Ontology', 'ON-001', '01.6', '0223', 10, 'english', true, true);

insert into prerequisites (child_id, prerequisite_id) values (2,1);

insert into program (name, description) values ('Philosophy', 'Blah blah');
insert into program (name) values ('Cognitive Science');

insert into admission (name, program_id, student_limit, closed) values ('Philosophy 2020', 1, 150, false);

insert into semester (name, number, program_id) values ('Summer 2021', 1, 1);

insert into institute (name, code, url, phone, address) values ('Institute of Philosophy', 'PHIL001', 'https:/example.com', '00-00', 'idk');
insert into institute (name) values ('Institute of Computer Science');

insert into course_year (course_id, start_date, end_date, description) values (1, '2020-12-17', '2021-12-01', 'just a test year');
insert into course_year (course_id, start_date, end_date, description) values (1, '2020-12-17', '2021-12-01', 'just a test year');
insert into course_year (course_id, start_date, end_date, description) values (1, '2020-10-17', '2021-10-01', 'just a test year');
insert into course_year (course_id, start_date, end_date, description) values (1, '2019-12-17', '2020-12-01', 'just a test year');


insert into course_type (name) values ('Lecture');
insert into study_group (name, course_year_id, description, course_type_id) values ('A', 1, 'A group', 1);
insert into study_group (name, course_year_id, description, course_type_id, ongoing) values ('B', 1, 'A group', 1, true);


INSERT INTO users (password, username) VALUES
('$2a$10$RNjpi/CGfQ9t.abfnkgD7e2xUFcbHqjCKpbiPOQvod5VCoaw2VGJ.', 'Teacher');
INSERT INTO teacher (user_id, name, surname) values (2, 'James', 'Bond');

INSERT INTO users (password, username) VALUES
('$2a$10$RNjpi/CGfQ9t.abfnkgD7e2xUFcbHqjCKpbiPOQvod5VCoaw2VGJ.', 'User');

insert into course_semester(course_id, semester_id) values (1,1);
insert into course_semester(course_id, semester_id) values (2,1);

insert into course_literature(author, title, edition, pages, description, obligatory, course_id) values ('Daniel Kahneman', 'Thinking Slow, Thinking Fast', 'idk', 'pp. 20-23', 'just a good book', true, 1);

insert into group_literature(author, title, group_id) values ('Robert Audi', 'Introduction to Epistemology', 1);
