-- Enable uuid-ossp extension
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- Inserting sample courses with UUIDs
INSERT INTO Course (id, name, course_code, description) VALUES
                                                            (uuid_generate_v4(), 'Mathematics', 12345, 'Basic math concepts'),
                                                            (uuid_generate_v4(), 'History', 67890, 'World history overview'),
                                                            (uuid_generate_v4(), 'Computer Science', 54321, 'Introduction to programming');