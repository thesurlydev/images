INSERT INTO users (id, username, email) VALUES ('1aeabbac-84a5-11ee-9c3b-37b635df60b6', 'test', 'user@test.com') ON CONFLICT DO NOTHING;
INSERT INTO images (id, user_id, path) VALUES ('32084e30-84a5-11ee-aed1-2b32021344f3', '1aeabbac-84a5-11ee-9c3b-37b635df60b6', 'test') ON CONFLICT DO NOTHING;