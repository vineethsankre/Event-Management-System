create table if not exists sponsor(
    id INTEGER PRIMARY KEY AUTO_INCREMENT,
    name TEXT,
    industry TEXT
);

create table if not exists event(
    id INTEGER PRIMARY KEY AUTO_INCREMENT,
    name TEXT,
    date TEXT
);

create table if not exists event_sponsor(
    eventId INTEGER,
    sponsorId INTEGER,
    PRIMARY KEY(eventId, sponsorId),
    FOREIGN KEY (eventId) REFERENCES event(id),
    FOREIGN KEY (sponsorId) REFERENCES sponsor(id)
);