-- sqlite3 air_checker.db

CREATE TABLE MeasureHistory (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    place TEXT,
    qualityIndex REAL,
    qualityCategory TEXT,
    color TEXT,
    pm10 TEXT,
    pm25 TEXT,
    no2 TEXT,
    so2 TEXT,
    o3 TEXT,
    timestamp TEXT DEFAULT CURRENT_TIMESTAMP
);


-- Załadowanie danych miejscowości
-- sqlite3 air_checker.db
-- sqlite> .read places.sql
CREATE TABLE Places (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    name TEXT NOT NULL,
    district TEXT,
    voivodeship TEXT,
    lat REAL NOT NULL,
    lon REAL NOT NULL
);
