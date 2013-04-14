DROP TABLE urls;
DROP TABLE hits;

CREATE TABLE urls
(
  id         INT AUTO_INCREMENT PRIMARY KEY,
  url        VARCHAR(1500)                           NOT NULL,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL
);

CREATE TABLE hits
(
  id         INT AUTO_INCREMENT PRIMARY KEY,
  url_id     INT                                     NOT NULL,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
);

CREATE INDEX url_id_idx ON urls (id);
CREATE INDEX hashed_url_idx ON urls (url);

CREATE INDEX hit_url_id_idx ON hits (url_id);
ALTER TABLE hits ADD FOREIGN KEY (url_id) REFERENCES urls (id);
