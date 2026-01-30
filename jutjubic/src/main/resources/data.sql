INSERT INTO ROLE (id, name)
SELECT 1, 'ROLE_USER'
WHERE NOT EXISTS (SELECT 1 FROM ROLE WHERE id = 1);

CREATE INDEX IF NOT EXISTS idx_activity_location_created ON activity_logs USING GIST (location, created_at);