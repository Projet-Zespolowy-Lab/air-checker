CREATE TABLE MeasureHistory (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    nationalAirQualityIndex REAL,
    color TEXT,
    pm10 TEXT,
    pm25 TEXT,
    no2 TEXT,
    so2 TEXT,
    o3 TEXT,
    timestamp TEXT DEFAULT CURRENT_TIMESTAMP
);
