CREATE TABLE MeasureHistory (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    krajowyIndeks REAL,
    kolor TEXT,
    timestamp TEXT DEFAULT CURRENT_TIMESTAMP
);


-- INSERT INTO MeasureHistory (krajowyIndeks, kolor) VALUES(23.2, '#AABBAA');
