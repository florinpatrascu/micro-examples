drop table projects;
drop table todos;

CREATE TABLE projects
(
id INT AUTO_INCREMENT PRIMARY KEY,
name VARCHAR(200) UNIQUE,
created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL
);

CREATE TABLE todos
(
id INT AUTO_INCREMENT PRIMARY KEY,
title VARCHAR(500),
ord INT,
done BOOLEAN,
project_id INT NOT NULL,
created_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
modified_at TIMESTAMP
);

CREATE INDEX projects_id_idx  ON projects(id);

CREATE INDEX projects_name_idx  ON projects(name);

CREATE INDEX todos_id_idx  ON todos(id);

CREATE INDEX todos_project_id_idx  ON todos(project_id);

ALTER TABLE todos ADD FOREIGN KEY (project_id) REFERENCES projects (id);
