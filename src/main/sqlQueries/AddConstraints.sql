ALTER TABLE tickets ADD FOREIGN KEY (book_ref) REFERENCES bookings(book_ref) ON DELETE CASCADE;

ALTER TABLE boarding_passes ADD FOREIGN KEY (flight_id, ticket_no) REFERENCES ticket_flights(flight_id, ticket_no) ON DELETE CASCADE;

ALTER TABLE seats ADD FOREIGN KEY (aircraft_code) REFERENCES aircrafts(aircraft_code) ON DELETE CASCADE;

ALTER TABLE flights ADD FOREIGN KEY (departure_airport) REFERENCES airports(airport_code) ON DELETE CASCADE;

ALTER TABLE flights ADD FOREIGN KEY (arrival_airport) REFERENCES airports(airport_code) ON DELETE CASCADE;

ALTER TABLE ticket_flights ADD FOREIGN KEY (flight_id) REFERENCES flights(flight_id) ON DELETE CASCADE;

ALTER TABLE ticket_flights ADD FOREIGN KEY (ticket_no) REFERENCES tickets(ticket_no) ON DELETE CASCADE;