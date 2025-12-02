SELECT first_name, last_name FROM people_massachusetts ;
### WHERE
SELECT first_name, last_name FROM people_massachusetts WHERE hair_color = ‘red’ ;
### BETWEEN
SELECT first_name, last_name FROM people_massachusetts WHERE birth_date BETWEEN ‘2003-01-01’ AND ‘2003-12-31’ ;
### AND
SELECT first_name, last_name FROM people_massachusetts WHERE hair_color = ‘red’ AND birth_date BETWEEN ‘2003-01-01’ AND ‘2003-12-31’ ;
### OR
SELECT first_name, last_name FROM people_massachusetts WHERE hair_color = ‘red’ OR birth_date BETWEEN ‘2003-01-01’ AND ‘2003-12-31’ ; 
### WHERE NOT
SELECT first_name, last_name FROM people_massachusetts WHERE NOT hair_color = ‘red’ ;
### ORDER BY
SELECT first_name, last_name FROM people_massachusetts WHERE hair_color = ‘red’ AND birth_date BETWEEN ‘2003-01-01’ AND ‘2003-12-31’ ORDER BY last_name ;
### GROUP BY
SELECT first_name, last_name FROM people_massachusetts WHERE hair_color = ‘red’ AND birth_date BETWEEN ‘2003-01-01’ AND ‘2003-12-31’ GROUP BY last_name ;
### LIMIT
SELECT first_name, last_name FROM people_massachusetts WHERE hair_color = ‘red’ AND birth_date BETWEEN ‘2003-01-01’ AND ‘2003-12-31’ ORDER BY last_name LIMIT 100 ;
### INSERT INTO
INSERT INTO people_massachusetts (address_city, address_state, address_zip, hair_color, age, first_name, last_name) VALUES (Cambridge, Massachusetts, 02139, blonde, 32, Jane, Doe) ;
INSERT INTO people_massachusetts VALUES (Cambridge, Massachusetts, 02139, blonde, 32, Jane, Doe) ;
INSERT INTO people_massachusetts (first_name, last_name, address_state) VALUES (Jane, Doe, Massachusetts) ;
### UPDADTE
UPDATE people_massachusetts SET hair_color = ‘brown’ WHERE first_name = ‘Jane’ AND last_name = ‘Doe’ ;
UPDATE people_massachusetts SET address_state = ‘Massachusetts’ WHERE address_state = MA ;
### DELETE
DELETE FROM people_massachusetts WHERE address_state = ‘maine’ ;
### “%” stands in for any group of digits that follow “02”
SELECT first_name, last_name WHERE address_zip LIKE ‘02%’ ;
### COUNT (use with GROUP BY)
SELECT hair_color, COUNT(hair_color) FROM people_massachusetts AND birth_date BETWEEN ‘2003-01-01’ AND ‘2003-12-31’ GROUP BY hair_color ;
### AVG
SELECT AVG(age) FROM people_massachusetts ;
### SUM
SELECT SUM(age) FROM people_massachusetts ;
### MIN, MAX
SELECT MIN(age) FROM people_massachusetts ; 
SELECT MAX(age) FROM people_massachusetts ;
### JOIN, USING
SELECT birthdate_massachusetts.first_name, birthdate_massachusetts.last_name FROM birthdate_massachusetts JOIN haircolor_massachusetts USING (user_id) WHERE hair_color = ‘red’ AND birth_date BETWEEN ‘2003-01-01’ AND ‘2003-12-31’ ORDER BY last_name ;
### CASE
SELECT first_name, last_name FROM people_massachusetts CASE WHEN hair_color = ‘brown’ THEN ‘This person has brown hair.’ WHEN hair_color = ‘blonde’ THEN ‘This person has blonde hair.’ WHEN hair_color = ‘red’ THEN ‘This person has red hair.’ ELSE ‘Hair color not known.’ END ;
###

###

###

###





