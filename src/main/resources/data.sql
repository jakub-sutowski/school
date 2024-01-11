-- Enable uuid-ossp extension
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- -- Inserting sample courses with UUIDs
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
