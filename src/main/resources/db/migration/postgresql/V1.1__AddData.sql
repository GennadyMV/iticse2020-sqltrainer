SET client_encoding = 'UTF8';

--
-- Add databases
--

INSERT INTO public.database (created, name, description, sql_init_statements) VALUES (
    now(),
    'Authors, Books, and Genres',
    'The database contains information about authors, books, and genres. Each author may have written several books, and each book may have several authors. Each book, however, has just a single genre.',
'CREATE TABLE Authors (id serial, name varchar(32));
CREATE TABLE Genres (id serial, genre varchar(32));
CREATE TABLE Books (id serial, title varchar(32), publication_year bigint, genre_id bigint, CONSTRAINT fk_book_genre FOREIGN KEY (genre_id) REFERENCES Genres(id));
CREATE TABLE BookAuthors (books_id bigint, authors_id bigint, CONSTRAINT fk_bookauthors_book FOREIGN KEY (books_id) REFERENCES Books(id), CONSTRAINT fk_bookauthors_author FOREIGN KEY (authors_id) REFERENCES Authors(id));
INSERT INTO Authors (name) VALUES (''Patrick Rothfuss'');
INSERT INTO Authors (name) VALUES (''Mary Shelley'');
INSERT INTO Authors (name) VALUES (''Nancy Kress'');
INSERT INTO Authors (name) VALUES (''Frank Herbert'');
INSERT INTO Authors (name) VALUES (''George Orwell'');
INSERT INTO Authors (name) VALUES (''Marissa Meyer'');
INSERT INTO Genres (genre) VALUES (''Science fiction'');
INSERT INTO Genres (genre) VALUES (''Horror'');
INSERT INTO Genres (genre) VALUES (''Dystopian'');
INSERT INTO Genres (genre) VALUES (''Fantasy'');
INSERT INTO Books (title, publication_year, genre_id) VALUES (''Cinder'', 2012, (SELECT id FROM Genres WHERE genre = ''Dystopian''));
INSERT INTO Books (title, publication_year, genre_id) VALUES (''1984'', 1949, (SELECT id FROM Genres WHERE genre = ''Dystopian''));
INSERT INTO Books (title, publication_year, genre_id) VALUES (''Dune'', 1965, (SELECT id FROM Genres WHERE genre = ''Science fiction''));
INSERT INTO Books (title, publication_year, genre_id) VALUES (''Frankenstein'', 1818, (SELECT id FROM Genres WHERE genre = ''Horror''));
INSERT INTO Books (title, publication_year, genre_id) VALUES (''Beggars in Spain'', 1993, (SELECT id FROM Genres WHERE genre = ''Science fiction''));
INSERT INTO Books (title, publication_year, genre_id) VALUES (''The Name of the Wind'', 2007, (SELECT id FROM Genres WHERE genre = ''Fantasy''));
INSERT INTO Books (title, publication_year, genre_id) VALUES (''The Fear of the Wise Man'', 2011, (SELECT id FROM Genres WHERE genre = ''Fantasy''));
INSERT INTO BookAuthors (books_id, authors_id) VALUES ((SELECT id FROM Books WHERE title = ''Cinder''), (SELECT id FROM Authors WHERE name = ''Marissa Meyer''));
INSERT INTO BookAuthors (books_id, authors_id) VALUES ((SELECT id FROM Books WHERE title = ''1984''), (SELECT id FROM Authors WHERE name = ''George Orwell''));
INSERT INTO BookAuthors (books_id, authors_id) VALUES ((SELECT id FROM Books WHERE title = ''Dune''), (SELECT id FROM Authors WHERE name = ''Frank Herbert''));
INSERT INTO BookAuthors (books_id, authors_id) VALUES ((SELECT id FROM Books WHERE title = ''Frankenstein''), (SELECT id FROM Authors WHERE name = ''Mary Shelley''));
INSERT INTO BookAuthors (books_id, authors_id) VALUES ((SELECT id FROM Books WHERE title = ''Beggars in Spain''), (SELECT id FROM Authors WHERE name = ''Nancy Kress''));
INSERT INTO BookAuthors (books_id, authors_id) VALUES ((SELECT id FROM Books WHERE title = ''The Name of the Wind''), (SELECT id FROM Authors WHERE name = ''Patrick Rothfuss''));
INSERT INTO BookAuthors (books_id, authors_id) VALUES ((SELECT id FROM Books WHERE title = ''The Fear of the Wise Man''), (SELECT id FROM Authors WHERE name = ''Patrick Rothfuss''));'
);

--
-- Add topics and related exercises -- 1) simple select statements
--

INSERT INTO public.topic (rank, name, description, created) VALUES (
    1, 'Simple select statements', 'Getting started! Selecting data from a single table.', now()
);
INSERT INTO public.exercise (name, handout, sql_model_solution, created, topic_id, database_id) VALUES (
    'Selecting data from a single table', 
    'List all author names from the table Authors', 
    'SELECT name FROM Authors;',
    now(), 
    (SELECT id FROM public.topic WHERE name = 'Simple select statements'), 
    (SELECT id FROM public.database WHERE name = 'Authors, Books, and Genres')
);
INSERT INTO public.exercise (name, handout, sql_model_solution, created, topic_id, database_id) VALUES (
    'Selecting data from a single table', 
    'List all book titles from the table Books', 
    'SELECT title FROM Books;',
    now(), 
    (SELECT id FROM public.topic WHERE name = 'Simple select statements'), 
    (SELECT id FROM public.database WHERE name = 'Authors, Books, and Genres')
);
INSERT INTO public.exercise (name, handout, sql_model_solution, created, topic_id, database_id) VALUES (
    'Selecting data from a single table', 
    'List all genres from the table Genres', 
    'SELECT genre FROM Genres;',
    now(), 
    (SELECT id FROM public.topic WHERE name = 'Simple select statements'), 
    (SELECT id FROM public.database WHERE name = 'Authors, Books, and Genres')
);
INSERT INTO public.exercise (name, handout, sql_model_solution, created, topic_id, database_id) VALUES (
    'Selecting data from a single table', 
    'List all author ids and author names from the table Authors', 
    'SELECT id, name FROM Authors;',
    now(), 
    (SELECT id FROM public.topic WHERE name = 'Simple select statements'), 
    (SELECT id FROM public.database WHERE name = 'Authors, Books, and Genres')
);
INSERT INTO public.exercise (name, handout, sql_model_solution, created, topic_id, database_id) VALUES (
    'Selecting data from a single table', 
    'List all genre ids and genres from the table Genres', 
    'SELECT id, genre FROM Genres;',
    now(), 
    (SELECT id FROM public.topic WHERE name = 'Simple select statements'), 
    (SELECT id FROM public.database WHERE name = 'Authors, Books, and Genres')
);
INSERT INTO public.exercise (name, handout, sql_model_solution, created, topic_id, database_id) VALUES (
    'Selecting data from a single table', 
    'List publication years and titles from the table Books', 
    'SELECT publication_year, title FROM Books;',
    now(), 
    (SELECT id FROM public.topic WHERE name = 'Simple select statements'), 
    (SELECT id FROM public.database WHERE name = 'Authors, Books, and Genres')
);

--
-- Add topics and related exercises -- 2) filtering and ordering
--

INSERT INTO public.topic (rank, name, description, created) VALUES (
    2, 'Filtering and ordering', 'Filtering and ordering data selected from a single table.', now()
);
INSERT INTO public.exercise (name, handout, sql_model_solution, created, topic_id, database_id) VALUES (
    'Filtering select results', 
    'Find the book titles that have been published before 2000.', 
    'SELECT title FROM Books WHERE publication_year < 2000;',
    now(), 
    (SELECT id FROM public.topic WHERE name = 'Filtering and ordering'), 
    (SELECT id FROM public.database WHERE name = 'Authors, Books, and Genres')
);
INSERT INTO public.exercise (name, handout, sql_model_solution, created, topic_id, database_id) VALUES (
    'Filtering select results', 
    'Find the book titles that have been published during or after 1993.', 
    'SELECT title FROM Books WHERE publication_year >= 1993;',
    now(), 
    (SELECT id FROM public.topic WHERE name = 'Filtering and ordering'), 
    (SELECT id FROM public.database WHERE name = 'Authors, Books, and Genres')
);
INSERT INTO public.exercise (name, handout, sql_model_solution, created, topic_id, database_id) VALUES (
    'Filtering select results', 
    'Find the genres that contain the character ''o''.', 
    'SELECT genre FROM Genres WHERE genre LIKE ''%o%'';',
    now(), 
    (SELECT id FROM public.topic WHERE name = 'Filtering and ordering'), 
    (SELECT id FROM public.database WHERE name = 'Authors, Books, and Genres')
);
INSERT INTO public.exercise (name, handout, sql_model_solution, created, topic_id, database_id) VALUES (
    'Filtering select results', 
    'Find the titles of all books that have been published before 2000 or that have the character ''a'' in their title.', 
    'SELECT title FROM Books WHERE publication_year < 2000 OR title LIKE ''%a%'';',
    now(), 
    (SELECT id FROM public.topic WHERE name = 'Filtering and ordering'), 
    (SELECT id FROM public.database WHERE name = 'Authors, Books, and Genres')
);
INSERT INTO public.exercise (name, handout, sql_model_solution, created, topic_id, database_id) VALUES (
    'Filtering select results', 
    'Find all genres contain the character ''a'' or the character ''e''.', 
    'SELECT genre FROM Genres WHERE genre LIKE ''%a%'' OR genre LIKE ''%e%'';',
    now(), 
    (SELECT id FROM public.topic WHERE name = 'Filtering and ordering'), 
    (SELECT id FROM public.database WHERE name = 'Authors, Books, and Genres')
);
INSERT INTO public.exercise (name, handout, sql_model_solution, created, topic_id, database_id) VALUES (
    'Sorting select results', 
    'List all book titles. The books should be ordered based on their publication year.', 
    'SELECT title FROM Books ORDER BY publication_year;',
    now(), 
    (SELECT id FROM public.topic WHERE name = 'Filtering and ordering'), 
    (SELECT id FROM public.database WHERE name = 'Authors, Books, and Genres')
);
INSERT INTO public.exercise (name, handout, sql_model_solution, created, topic_id, database_id) VALUES (
    'Sorting select results', 
    'List all genres. The genres should be ordered in descending order.', 
    'SELECT genre FROM Genres ORDER BY genre DESC;',
    now(), 
    (SELECT id FROM public.topic WHERE name = 'Filtering and ordering'), 
    (SELECT id FROM public.database WHERE name = 'Authors, Books, and Genres')
);

--
-- Add topics and related exercises -- 3) joining a few tables
--

INSERT INTO public.topic (rank, name, description, created) VALUES (
    3, 'Selecting data from a few tables', 'Selecting content from a few tables.', now()
);
INSERT INTO public.exercise (name, handout, sql_model_solution, created, topic_id, database_id) VALUES (
    'Selecting data from multiple tables', 
    'List all book titles and their genres.', 
    'SELECT title, genre FROM Books JOIN Genres ON Books.genre_id = Genres.id;',
    now(), 
    (SELECT id FROM public.topic WHERE name = 'Selecting data from a few tables'), 
    (SELECT id FROM public.database WHERE name = 'Authors, Books, and Genres')
);
INSERT INTO public.exercise (name, handout, sql_model_solution, created, topic_id, database_id) VALUES (
    'Selecting data from multiple tables', 
    'List all the book titles with Fantasy as their genre.', 
    'SELECT title FROM Books JOIN Genres ON Books.genre_id = Genres.id WHERE Genres.genre = ''Fantasy'';',
    now(), 
    (SELECT id FROM public.topic WHERE name = 'Selecting data from a few tables'), 
    (SELECT id FROM public.database WHERE name = 'Authors, Books, and Genres')
);
INSERT INTO public.exercise (name, handout, sql_model_solution, created, topic_id, database_id) VALUES (
    'Selecting data from multiple tables', 
    'List all the book titles that have either Fantasy or Dystopian as their genre.', 
    'SELECT title FROM Books JOIN Genres ON Books.genre_id = Genres.id WHERE Genres.genre = ''Fantasy'' OR Genres.genre = ''Dystopian'';',
    now(), 
    (SELECT id FROM public.topic WHERE name = 'Selecting data from a few tables'), 
    (SELECT id FROM public.database WHERE name = 'Authors, Books, and Genres')
);
INSERT INTO public.exercise (name, handout, sql_model_solution, created, topic_id, database_id) VALUES (
    'Selecting data from multiple tables', 
    'List the genres of the books that have been published before 2000.', 
    'SELECT genre FROM Genres JOIN Books ON Books.genre_id = Genres.id WHERE Books.publication_year < 2000;',
    now(), 
    (SELECT id FROM public.topic WHERE name = 'Selecting data from a few tables'), 
    (SELECT id FROM public.database WHERE name = 'Authors, Books, and Genres')
);
INSERT INTO public.exercise (name, handout, sql_model_solution, created, topic_id, database_id) VALUES (
    'Selecting data from multiple tables', 
    'List the genres of the books that have been published before 2000. Order results in descending order.', 
    'SELECT genre FROM Genres JOIN Books ON Books.genre_id = Genres.id WHERE Books.publication_year < 2000 ORDER BY genre DESC;',
    now(), 
    (SELECT id FROM public.topic WHERE name = 'Selecting data from a few tables'), 
    (SELECT id FROM public.database WHERE name = 'Authors, Books, and Genres')
);


--
-- Add topics and related exercises -- 4) joining more tables
--

INSERT INTO public.topic (rank, name, description, created) VALUES (
    4, 'Selecting data from more tables', 'Selecting content from a few more tables.', now()
);
INSERT INTO public.exercise (name, handout, sql_model_solution, created, topic_id, database_id) VALUES (
    'Joining more tables', 
    'List the book titles that Patrick Rothfuss has written.', 
    'SELECT title FROM Books JOIN BookAuthors ON BookAuthors.books_id = Books.id
JOIN Authors ON BookAuthors.authors_id = Authors.id
WHERE Authors.name = ''Patrick Rothfuss'';',
    now(), 
    (SELECT id FROM public.topic WHERE name = 'Selecting data from more tables'), 
    (SELECT id FROM public.database WHERE name = 'Authors, Books, and Genres')
);
INSERT INTO public.exercise (name, handout, sql_model_solution, created, topic_id, database_id) VALUES (
    'Selecting data from multiple tables', 
    'Find the name of the author who wrote the book with title ''Cinder''.', 
    'SELECT name FROM Authors
JOIN BookAuthors ON BookAuthors.authors_id = Authors.id
JOIN Books ON BookAuthors.books_id = Books.id
WHERE Books.title = ''Cinder'';',
    now(), 
    (SELECT id FROM public.topic WHERE name = 'Selecting data from more tables'), 
    (SELECT id FROM public.database WHERE name = 'Authors, Books, and Genres')
);
INSERT INTO public.exercise (name, handout, sql_model_solution, created, topic_id, database_id) VALUES (
    'Selecting data from multiple tables', 
    'Find the names of the authors and the genres that they have written. Sort the results based on the author name and genre. Each result row should be distinct.', 
    'SELECT DISTINCT name, genre FROM Authors
JOIN BookAuthors ON BookAuthors.authors_id = Authors.id
JOIN Books ON BookAuthors.books_id = Books.id
JOIN Genres ON Books.genre_id = Genres.id
ORDER BY name, genre;',
    now(), 
    (SELECT id FROM public.topic WHERE name = 'Selecting data from more tables'), 
    (SELECT id FROM public.database WHERE name = 'Authors, Books, and Genres')
);