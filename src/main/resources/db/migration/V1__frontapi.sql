-- Frontend API DB

-- Application users 
CREATE TABLE users (
    id SERIAL PRIMARY KEY,
    username VARCHAR(255) UNIQUE NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    alumni_id INT
);

-- User roles (e.g. admin, operator)
CREATE TABLE user_roles (
    username VARCHAR(255) NOT NULL,
    role VARCHAR(255) NOT NULL,
    FOREIGN KEY (username) REFERENCES users(username) ON DELETE CASCADE,
    UNIQUE (username, role)
);

-- Indexes for frontend
CREATE INDEX idx_users_alumni_id ON users(alumni_id);
CREATE INDEX idx_user_roles_role ON user_roles(role);