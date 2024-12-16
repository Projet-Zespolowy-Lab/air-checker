CREATE TABLE MeasureHistory (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    krajowyIndeks REAL,
    kolor TEXT, -- hex zapisany jako ciąg znaków
    timestamp TEXT NOT NULL
);


-- INSERT INTO MeasureHistory (krajowyIndeks, kolor, timestamp) VALUES(23.2, '#AABBAA', 'dzisiaj');
