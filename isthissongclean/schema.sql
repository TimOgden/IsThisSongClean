DROP TABLE IF EXISTS songs;

CREATE TABLE songs (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    title TEXT NOT NULL,
    artist TEXT NOT NULL,
    lyrics TEXT NOT NULL
);