INSERT INTO ROLE (id, name) VALUES (1, 'ROLE_USER');

CREATE INDEX idx_activity_location_created ON activity_logs USING GIST (location, created_at);