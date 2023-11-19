DELETE FROM users;
INSERT INTO users (id, username, email) VALUES ('1aeabbac-84a5-11ee-9c3b-37b635df60b6', 'test', 'user@test.com') ON CONFLICT DO NOTHING;
-- DELETE FROM images;
-- INSERT INTO images (id, user_id, path, status, type, file_size) VALUES ('32084e30-84a5-11ee-aed1-2b32021344f3', '1aeabbac-84a5-11ee-9c3b-37b635df60b6', 'test.png', 'processing', 'image/png', '1024') ON CONFLICT DO NOTHING;
DELETE FROM operations;
INSERT INTO operations(name, description) VALUES('resize', 'Resize an image given x and y dimensions') ON CONFLICT DO NOTHING;
INSERT INTO operations(name, description) VALUES('rotate', 'Rotate an image given degrees and direction parameters') ON CONFLICT DO NOTHING;