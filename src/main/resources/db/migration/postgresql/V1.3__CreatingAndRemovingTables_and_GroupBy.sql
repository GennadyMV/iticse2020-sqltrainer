SET client_encoding = 'UTF8';

INSERT INTO public.database (created, name, description, sql_init_statements) VALUES (
    now(),
    'An empty database',
    'The database does not have any tables in it.',
''
);

INSERT INTO public.topic (rank, name, description, created) VALUES (
    6, 'Oh no, the database is empty!', 'Practise adding and removing tables.', now()
);

INSERT INTO public.exercise (name, handout, sql_model_solution, created, topic_id, database_id) VALUES (
    'Adding a table', 
    'Create a table called Exams. The table should have two columns: id and name, where id is an integer and the name is a string with up to 255 characters.', 
    'CREATE TABLE Exams (
  id INTEGER,
  name VARCHAR(255)
);',
    now(), 
    (SELECT id FROM public.topic WHERE name = 'Oh no, the database is empty!'), 
    (SELECT id FROM public.database WHERE name = 'An empty database')
);

INSERT INTO public.exercise (name, handout, sql_model_solution, created, topic_id, database_id) VALUES (
    'Adding a table with primary key', 
    'Create a table called Invoice. The table should three columns: id, client, and amount. The amount is represented in Euros. The id is an integer and should be marked as primary key. The client is a string with up to 120 characters. The amount is an integer.', 
    'CREATE TABLE Invoice (
  id INTEGER PRIMARY KEY,
  client VARCHAR(120),
  amount INTEGER
);',
    now(), 
    (SELECT id FROM public.topic WHERE name = 'Oh no, the database is empty!'), 
    (SELECT id FROM public.database WHERE name = 'An empty database')
);

INSERT INTO public.exercise (name, handout, sql_model_solution, created, topic_id, database_id) VALUES (
    'Removing a table', 
    'Remove the table called Review.', 
    'DROP TABLE Review;',
    now(), 
    (SELECT id FROM public.topic WHERE name = 'Oh no, the database is empty!'), 
    (SELECT id FROM public.database WHERE name = 'Hotel reviews')
);

INSERT INTO public.exercise (name, handout, sql_model_solution, created, topic_id, database_id) VALUES (
    'Removing all tables', 
    'Remove all tables from the database.', 
    'DROP TABLE Review;
DROP TABLE Hotel;',
    now(), 
    (SELECT id FROM public.topic WHERE name = 'Oh no, the database is empty!'), 
    (SELECT id FROM public.database WHERE name = 'Hotel reviews')
);


INSERT INTO public.topic (rank, name, description, created) VALUES (
    7, 'Moar data!', 'Practise adding data to a database.', now()
);

INSERT INTO public.exercise (name, handout, sql_model_solution, created, topic_id, database_id) VALUES (
    'Adding a new hotel', 
    'Add a new hotel. Note that the value of the id field in the table Hotel is automatically generated. The name of the new hotel must be ''Abad Atrium'' and it is located in ''Kerala, India''.', 
    'INSERT INTO Hotel (name, location) VALUES (''Abad Atrium'', ''Kerala, India'');',
    now(), 
    (SELECT id FROM public.topic WHERE name = 'Moar data!'), 
    (SELECT id FROM public.database WHERE name = 'Hotel reviews')
);

INSERT INTO public.exercise (name, handout, sql_model_solution, created, topic_id, database_id) VALUES (
    'Adding a new author', 
    'Add a new author. Note that the value of the id field in the table Authors is automatically generated. The name of the new author must be ''John Lanchester''.', 
    'INSERT INTO Authors (name) VALUES (''John Lanchester'');',
    now(), 
    (SELECT id FROM public.topic WHERE name = 'Moar data!'), 
    (SELECT id FROM public.database WHERE name = 'Authors, Books, and Genres')
);

INSERT INTO public.exercise (name, handout, sql_model_solution, created, topic_id, database_id) VALUES (
    'Adding a new genre', 
    'Add a new genre. Note that the value of the id field in the table Genres is automatically generated. The name of the new genre must be ''Educational''.', 
    'INSERT INTO Genres (genre) VALUES (''Educational'');',
    now(), 
    (SELECT id FROM public.topic WHERE name = 'Moar data!'), 
    (SELECT id FROM public.database WHERE name = 'Authors, Books, and Genres')
);

INSERT INTO public.topic (rank, name, description, created) VALUES (
    8, 'Oops, this was not what I wanted to add!', 'Fix things using update and delete.', now()
);

INSERT INTO public.exercise (name, handout, sql_model_solution, created, topic_id, database_id) VALUES (
    'Changing the name of a book', 
    'The Books table contains a book with the title Cinder. Change the title of the book to Cinderella.', 
    'UPDATE Books SET title = ''Cinderella'' WHERE title = ''Cinder'';',
    now(), 
    (SELECT id FROM public.topic WHERE name = 'Oops, this was not what I wanted to add!'), 
    (SELECT id FROM public.database WHERE name = 'Authors, Books, and Genres')
);

INSERT INTO public.exercise (name, handout, sql_model_solution, created, topic_id, database_id) VALUES (
    'Fix the name of an author', 
    'The name ''George Orwell'' is a pen name. Change the name of the author to ''Eric Arthur Blair''.', 
    'UPDATE Authors SET name = ''Eric Arthur Blair'' WHERE name = ''George Orwell'';',
    now(), 
    (SELECT id FROM public.topic WHERE name = 'Oops, this was not what I wanted to add!'), 
    (SELECT id FROM public.database WHERE name = 'Authors, Books, and Genres')
);

INSERT INTO public.exercise (name, handout, sql_model_solution, created, topic_id, database_id) VALUES (
    'Removing all the data', 
    'Remove all the data from the database. Do not remove the tables though. Also, make sure to delete the data in the correct order to avoid referential integrity issues.', 
    'DELETE FROM Review;
DELETE FROM Hotel;',
    now(), 
    (SELECT id FROM public.topic WHERE name = 'Oops, this was not what I wanted to add!'), 
    (SELECT id FROM public.database WHERE name = 'Hotel reviews')
);

INSERT INTO public.topic (rank, name, description, created) VALUES (
    9, 'Fun with functions', 'Practice using functions.', now()
);

INSERT INTO public.exercise (name, handout, sql_model_solution, created, topic_id, database_id) VALUES (
    'Total reviews', 
    'Find out the total number of given reviews. Output the total number under a column called ''total''.', 
    'SELECT COUNT(*) AS total FROM Review;',
    now(), 
    (SELECT id FROM public.topic WHERE name = 'Fun with functions'), 
    (SELECT id FROM public.database WHERE name = 'Hotel reviews')
);

INSERT INTO public.exercise (name, handout, sql_model_solution, created, topic_id, database_id) VALUES (
    'Publication year of the oldest book', 
    'Find out the publication year of the oldest book in the database. Output the publication year of the oldest book under a column called ''year''.', 
    'SELECT MIN(publication_year) AS year FROM Books;',
    now(), 
    (SELECT id FROM public.topic WHERE name = 'Fun with functions'), 
    (SELECT id FROM public.database WHERE name = 'Authors, Books, and Genres')
);

INSERT INTO public.exercise (name, handout, sql_model_solution, created, topic_id, database_id) VALUES (
    'Books published between 1900 and 2000', 
    'Find out the number of books that have been published between 1900 and 2000 (inclusive). Output the number under a column called ''books''.', 
    'SELECT COUNT(*) AS books FROM Books WHERE publication_year >= 1900 AND publication_year <= 2000;',
    now(), 
    (SELECT id FROM public.topic WHERE name = 'Fun with functions'), 
    (SELECT id FROM public.database WHERE name = 'Authors, Books, and Genres')
);

INSERT INTO public.topic (rank, name, description, created) VALUES (
    10, 'Group by ...', 'Practice using group by and functions.', now()
);

INSERT INTO public.exercise (name, handout, sql_model_solution, created, topic_id, database_id) VALUES (
    'Number of books in each genre', 
    'Find out the number of books in each genre. The output should contain the genre name (column called ''genre'') and the number of books (column called ''books'').', 
    'SELECT Genres.genre AS genre, COUNT(Books.id) AS books FROM Genres
LEFT JOIN Books ON Books.genre_id = Genres.id
GROUP BY Genres.genre;',
    now(), 
    (SELECT id FROM public.topic WHERE name = 'Group by ...'), 
    (SELECT id FROM public.database WHERE name = 'Authors, Books, and Genres')
);

INSERT INTO public.exercise (name, handout, sql_model_solution, created, topic_id, database_id) VALUES (
    'Number of books written by each author', 
    'Find out the number of books that each author has written. If a book has multiple authors, count the book as one for each of the authors. The output should contain the name of the author (column called ''author'') and the number of written books (column called ''books'').', 
    'SELECT Authors.name AS author, COUNT(Books.id) AS books FROM Authors
JOIN BookAuthors ON Authors.id = BookAuthors.authors_id
JOIN Books ON Books.id = BookAuthors.books_id
GROUP BY Authors.name;',
    now(), 
    (SELECT id FROM public.topic WHERE name = 'Group by ...'), 
    (SELECT id FROM public.database WHERE name = 'Authors, Books, and Genres')
);

INSERT INTO public.exercise (name, handout, sql_model_solution, created, topic_id, database_id) VALUES (
    'Number of reviews for each hotel', 
    'Find out the number of reviews that each hotel has received. Even if a hotel has not received any reviews, it should still be included. The output should contain the name of the hotel (column called ''hotel'') and the number of reviews (column called ''reviews'').', 
    'SELECT Hotel.name AS hotel, COUNT(Review.id) AS reviews FROM Hotel
LEFT JOIN Review ON Hotel.id = Review.hotel_id
GROUP BY Hotel.name;',
    now(), 
    (SELECT id FROM public.topic WHERE name = 'Group by ...'), 
    (SELECT id FROM public.database WHERE name = 'Hotel reviews')
);

INSERT INTO public.topic (rank, name, description, created) VALUES (
    11, 'More aggregate queries', 'Functions, group by, having, order.', now()
);

INSERT INTO public.exercise (name, handout, sql_model_solution, created, topic_id, database_id) VALUES (
    'Often visited hotels', 
    'Find out the hotels with at least four (4) reviews. The output should contain the name of the hotel (column called ''hotel''), and the output should be sorted by the hotel name.', 
    'SELECT Hotel.name AS hotel FROM Hotel
JOIN Review ON Hotel.id = Review.hotel_id
GROUP BY Hotel.name
HAVING COUNT(Review.id) > 3
ORDER BY Hotel.name DESC;',
    now(), 
    (SELECT id FROM public.topic WHERE name = 'More aggregate queries'), 
    (SELECT id FROM public.database WHERE name = 'Hotel reviews')
);

INSERT INTO public.exercise (name, handout, sql_model_solution, created, topic_id, database_id) VALUES (
    'Oldest publication in each genre', 
    'Find out the oldest publication year in each genre. The output should contain the name of the genre (column called ''genre'') and the oldest publication year (column called ''oldest''). Sort the output based on genre name.', 
    'SELECT Genres.genre AS genre, MIN(Books.publication_year) AS oldest FROM Genres
JOIN Books ON Genres.id = Books.genre_id
GROUP BY Genres.genre
ORDER BY Genres.genre',
    now(), 
    (SELECT id FROM public.topic WHERE name = 'More aggregate queries'), 
    (SELECT id FROM public.database WHERE name = 'Authors, Books, and Genres')
);
