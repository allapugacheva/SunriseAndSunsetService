-- Create the 'date' table
CREATE TABLE date (
                      id SERIAL PRIMARY KEY,
                      sun_date DATE NOT NULL
);

-- Create the 'location' table
CREATE TABLE location (
                          id SERIAL PRIMARY KEY,
                          sun_location VARCHAR(255) NOT NULL,
                          latitude DOUBLE PRECISION NOT NULL,
                          longitude DOUBLE PRECISION NOT NULL,
                          timezone_id INT,
                          FOREIGN KEY (timezone_id) REFERENCES timezone(id)
);

-- Create the 'sunrise_and_sunset_time' table
CREATE TABLE sunrise_and_sunset_time (
                                         id SERIAL PRIMARY KEY,
                                         sunrise_time TIME NOT NULL,
                                         sunset_time TIME NOT NULL
);

-- Create the 'timezone' table
CREATE TABLE timezone (
                          id SERIAL PRIMARY KEY,
                          sun_timezone VARCHAR(255) NOT NULL
);

-- Create the 'location_date_mapping' join table
CREATE TABLE location_date_mapping (
                                       date_id INT NOT NULL,
                                       location_id INT NOT NULL,
                                       PRIMARY KEY (date_id, location_id),
                                       FOREIGN KEY (date_id) REFERENCES date(id) ON DELETE CASCADE,
                                       FOREIGN KEY (location_id) REFERENCES location(id) ON DELETE CASCADE
);

-- Create the 'date_time_mapping' join table
CREATE TABLE date_time_mapping (
                                   date_id INT NOT NULL,
                                   sunrise_and_sunset_time_id INT NOT NULL,
                                   PRIMARY KEY (date_id, sunrise_and_sunset_time_id),
                                   FOREIGN KEY (date_id) REFERENCES date(id) ON DELETE CASCADE,
                                   FOREIGN KEY (sunrise_and_sunset_time_id) REFERENCES sunrise_and_sunset_time(id) ON DELETE CASCADE
);

-- Create the 'location_time_mapping' join table
CREATE TABLE location_time_mapping (
                                       location_id INT NOT NULL,
                                       sunrise_and_sunset_time_id INT NOT NULL,
                                       PRIMARY KEY (location_id, sunrise_and_sunset_time_id),
                                       FOREIGN KEY (location_id) REFERENCES location(id) ON DELETE CASCADE,
                                       FOREIGN KEY (sunrise_and_sunset_time_id) REFERENCES sunrise_and_sunset_time(id) ON DELETE CASCADE
);
