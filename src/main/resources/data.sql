-- Enable uuid-ossp extension
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- Inserting sample courses with UUIDs
INSERT INTO Course (id, name, course_code, description) VALUES
                                                            (uuid_generate_v4(), 'Mathematics', 1, 'Basic math concepts'),
                                                            (uuid_generate_v4(), 'History', 2, 'World history overview'),
                                                            (uuid_generate_v4(), 'Computer Science', 3, 'Introduction to programming'),
                                                            (uuid_generate_v4(), 'Physics', 4, 'Fundamental principles of physics'),
                                                            (uuid_generate_v4(), 'English Literature', 5, 'Classic literature exploration'),
                                                            (uuid_generate_v4(), 'Chemistry', 6, 'Essential concepts in chemistry'),
                                                            (uuid_generate_v4(), 'Art History', 7, 'Survey of art through the ages'),
                                                            (uuid_generate_v4(), 'Psychology', 8, 'Introduction to psychological principles'),
                                                            (uuid_generate_v4(), 'Economics', 9, 'Basic economic concepts'),
                                                            (uuid_generate_v4(), 'Biology', 10, 'Overview of biological sciences');

-- Inserting sample users with UUIDs
INSERT INTO _user (id, first_name, last_name, email, password, role) VALUES
                                                                         (uuid_generate_v4(), 'John', 'Doe', 'john.doe@example.com', '$2a$10$4VjNUa8o9esI.A69H19TTufEOd/8/psBd9uDjwC4/Uj0chCMno5s2', 'STUDENT'),
                                                                         (uuid_generate_v4(), 'Jane', 'Smith', 'jane.smith@example.com', '$2a$10$4VjNUa8o9esI.A69H19TTufEOd/8/psBd9uDjwC4/Uj0chCMno5s2', 'STUDENT'),
                                                                         (uuid_generate_v4(), 'Bob', 'Johnson', 'bob.johnson@example.com', '$2a$10$4VjNUa8o9esI.A69H19TTufEOd/8/psBd9uDjwC4/Uj0chCMno5s2', 'STUDENT'),
                                                                         (uuid_generate_v4(), 'Alice', 'Williams', 'alice.williams@example.com', '$2a$10$4VjNUa8o9esI.A69H19TTufEOd/8/psBd9uDjwC4/Uj0chCMno5s2', 'STUDENT'),
                                                                         (uuid_generate_v4(), 'David', 'Jones', 'david.jones@example.com', '$2a$10$4VjNUa8o9esI.A69H19TTufEOd/8/psBd9uDjwC4/Uj0chCMno5s2', 'STUDENT'),
                                                                         (uuid_generate_v4(), 'Emily', 'Brown', 'emily.brown@example.com', '$2a$10$4VjNUa8o9esI.A69H19TTufEOd/8/psBd9uDjwC4/Uj0chCMno5s2', 'STUDENT'),
                                                                         (uuid_generate_v4(), 'Alex', 'Miller', 'alex.miller@example.com', '$2a$10$4VjNUa8o9esI.A69H19TTufEOd/8/psBd9uDjwC4/Uj0chCMno5s2', 'STUDENT'),
                                                                         (uuid_generate_v4(), 'Grace', 'Davis', 'grace.davis@example.com', '$2a$10$4VjNUa8o9esI.A69H19TTufEOd/8/psBd9uDjwC4/Uj0chCMno5s2', 'STUDENT'),
                                                                         (uuid_generate_v4(), 'Daniel', 'Wilson', 'daniel.wilson@example.com', '$2a$10$4VjNUa8o9esI.A69H19TTufEOd/8/psBd9uDjwC4/Uj0chCMno5s2', 'STUDENT'),
                                                                         (uuid_generate_v4(), 'Olivia', 'Moore', 'olivia.moore@example.com', '$2a$10$4VjNUa8o9esI.A69H19TTufEOd/8/psBd9uDjwC4/Uj0chCMno5s2', 'STUDENT'),
                                                                         (uuid_generate_v4(), 'Mason', 'Taylor', 'mason.taylor@example.com', '$2a$10$4VjNUa8o9esI.A69H19TTufEOd/8/psBd9uDjwC4/Uj0chCMno5s2', 'STUDENT'),
                                                                         (uuid_generate_v4(), 'Lily', 'Anderson', 'lily.anderson@example.com', '$2a$10$4VjNUa8o9esI.A69H19TTufEOd/8/psBd9uDjwC4/Uj0chCMno5s2', 'STUDENT'),
                                                                         (uuid_generate_v4(), 'Ethan', 'Clark', 'ethan.clark@example.com', '$2a$10$4VjNUa8o9esI.A69H19TTufEOd/8/psBd9uDjwC4/Uj0chCMno5s2', 'STUDENT'),
                                                                         (uuid_generate_v4(), 'Ava', 'Hill', 'ava.hill@example.com', '$2a$10$4VjNUa8o9esI.A69H19TTufEOd/8/psBd9uDjwC4/Uj0chCMno5s2', 'STUDENT'),
                                                                         (uuid_generate_v4(), 'Noah', 'Wright', 'noah.wright@example.com', '$2a$10$4VjNUa8o9esI.A69H19TTufEOd/8/psBd9uDjwC4/Uj0chCMno5s2', 'STUDENT');