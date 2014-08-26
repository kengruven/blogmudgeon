-- On top of the raw schema, here's my basic info.
-- Clearly this won't work for anyone who has a different email address than me.

-- password is (cemerick.friend.credentials/hash-bcrypt "abc")
INSERT INTO users (login, email, name, password)
       VALUES ('ken', 'kengruven@gmail.com', 'Ken Harris', '$2a$10$vPpCjCwe0zzfdRaW5/tPeeilr4mR/jfpoDF53aSxJtDRmfZK1DhWm');

INSERT INTO blogs (uuid, title, subtitle, user_id)
       VALUES ('21498d42-24c0-409b-b847-98e87f017ee4',
               'Kenmudgeon',
               'All software is terrible',
               (select id from users where login = 'ken' limit 1));

INSERT INTO posts (uuid, blog_id, title, content, created, updated, published)
       VALUES ('56284cd1-f3cb-4826-966b-2e808f1786d0',
               (select id from users where login = 'ken' limit 1),
               'Greeting',
               'Hello, world!',
               CURRENT_TIMESTAMP,
               CURRENT_TIMESTAMP,
               TRUE);

INSERT INTO docs (blog_id, name, slug, content)
       VALUES ((select id from users where login = 'ken' limit 1),
               'About',
               'about',
               'This is Ken''s blog.  He hasn''t written anything about it yet.');
