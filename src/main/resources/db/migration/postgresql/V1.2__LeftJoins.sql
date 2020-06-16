SET client_encoding = 'UTF8';

--
-- Add topics and related exercises -- 4) joining more tables
--

INSERT INTO public.database (created, name, description, sql_init_statements) VALUES (
    now(),
    'Hotel reviews',
    'The database contains information about hotels and their reviews. There are just two tables: Hotel and Review. Table Hotel contains information about the hotel name and location, while the Review-table contains a grade and a link to the actual hotel that is being reviewed.',
'CREATE TABLE Hotel (id serial, name varchar(32), location varchar(64));
CREATE TABLE Review (id serial, hotel_id bigint, grade bigint, CONSTRAINT fk_review_to_hotel FOREIGN KEY (hotel_id) REFERENCES Hotel(id));
INSERT INTO Hotel (name, location) VALUES 
(''Book and Bed Tokyo'', ''Tokyo, Japan''), 
(''Dromen Aan Zee'', ''Harlingen, Netherlands''), 
(''Hotel Costa Verde'', ''Provincia de Puntarenas, Costa Rica''),
(''Manta Resort'', ''Pemba Island, Zanzibar''), 
(''Kokopellis Cave'', ''Farmington, New Mexico''), 
(''Happy Nomads Village'', ''Karakol, Kyrgyzstan''), 
(''Palacio de Sal'', ''Salar de Uyuni, Bolivia''), 
(''Santos Express'', ''Mossel Bay, South Africa''), 
(''Treehouse Lodge Resort'', ''Iquitos, Peru'');
INSERT INTO Review (hotel_id, grade) VALUES
((SELECT id FROM Hotel WHERE name = ''Book and Bed Tokyo''), 5),
((SELECT id FROM Hotel WHERE name = ''Book and Bed Tokyo''), 4),
((SELECT id FROM Hotel WHERE name = ''Book and Bed Tokyo''), 5),
((SELECT id FROM Hotel WHERE name = ''Dromen Aan Zee''), 3),
((SELECT id FROM Hotel WHERE name = ''Dromen Aan Zee''), 4),
((SELECT id FROM Hotel WHERE name = ''Hotel Costa Verde''), 5),
((SELECT id FROM Hotel WHERE name = ''Hotel Costa Verde''), 5),
((SELECT id FROM Hotel WHERE name = ''Manta Resort''), 5),
((SELECT id FROM Hotel WHERE name = ''Manta Resort''), 4),
((SELECT id FROM Hotel WHERE name = ''Manta Resort''), 5),
((SELECT id FROM Hotel WHERE name = ''Manta Resort''), 5),
((SELECT id FROM Hotel WHERE name = ''Manta Resort''), 5),
((SELECT id FROM Hotel WHERE name = ''Manta Resort''), 3),
((SELECT id FROM Hotel WHERE name = ''Happy Nomads Village''), 3),
((SELECT id FROM Hotel WHERE name = ''Happy Nomads Village''), 4),
((SELECT id FROM Hotel WHERE name = ''Treehouse Lodge Resort''), 4),
((SELECT id FROM Hotel WHERE name = ''Treehouse Lodge Resort''), 5),
((SELECT id FROM Hotel WHERE name = ''Treehouse Lodge Resort''), 4);'
);

INSERT INTO public.topic (rank, name, description, created) VALUES (
    5, 'Any joins left?', 'Practising other types of joins.', now()
);
INSERT INTO public.exercise (name, handout, sql_model_solution, created, topic_id, database_id) VALUES (
    'Listing hotel and their reviews.', 
    'List all hotels and their associated reviews. The output should contain hotel name and the reviews that it has received. If a hotel has more than one review, list each review on a separate row. Even if a hotel has not been reviewed, it should still be included to the output.', 
    'SELECT name, grade FROM Hotel LEFT JOIN Review ON Hotel.id = Review.hotel_id;',
    now(), 
    (SELECT id FROM public.topic WHERE name = 'Any joins left?'), 
    (SELECT id FROM public.database WHERE name = 'Hotel reviews')
);
INSERT INTO public.exercise (name, handout, sql_model_solution, created, topic_id, database_id) VALUES (
    'Listing hotels that have not yet been reviewed.', 
    'List all the hotels that have not yet been reviewed. The output should just the hotel name. Sort the results based on the hotel name.', 
    'SELECT name FROM Hotel LEFT JOIN Review ON Hotel.id = Review.hotel_id WHERE Review.hotel_id IS NULL ORDER BY name;',
    now(), 
    (SELECT id FROM public.topic WHERE name = 'Any joins left?'), 
    (SELECT id FROM public.database WHERE name = 'Hotel reviews')
);