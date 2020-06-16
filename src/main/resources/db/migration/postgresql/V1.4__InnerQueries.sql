SET client_encoding = 'UTF8';

INSERT INTO public.database (created, name, description, sql_init_statements) VALUES (
    now(),
    'Salaries and employees',
    'A database with a single table that contains employee information and their salary data.',
'CREATE TABLE Employee (id serial, name varchar(32), salary int, supervisor_id bigint);
INSERT INTO Employee (name, salary) VALUES (''Patrick'', 32000);
INSERT INTO Employee (name, salary, supervisor_id) VALUES (''Mary'', 34000, SELECT id FROM Employee WHERE name = ''Patrick'');
INSERT INTO Employee (name, salary, supervisor_id) VALUES (''Nancy'', 22000, SELECT id FROM Employee WHERE name = ''Patrick'');
INSERT INTO Employee (name, salary, supervisor_id) VALUES (''Frank'', 18000, SELECT id FROM Employee WHERE name = ''Patrick'');
INSERT INTO Employee (name, salary, supervisor_id) VALUES (''George'', 26000, SELECT id FROM Employee WHERE name = ''Patrick'');
INSERT INTO Employee (name, salary, supervisor_id) VALUES (''Marissa'', 25000, SELECT id FROM Employee WHERE name = ''Mary'');
INSERT INTO Employee (name, salary, supervisor_id) VALUES (''Jake'', 32000, SELECT id FROM Employee WHERE name = ''Mary'');
INSERT INTO Employee (name, salary, supervisor_id) VALUES (''Connor'', 34000, SELECT id FROM Employee WHERE name = ''Mary'');
INSERT INTO Employee (name, salary, supervisor_id) VALUES (''Callum'', 22000, SELECT id FROM Employee WHERE name = ''Mary'');
INSERT INTO Employee (name, salary, supervisor_id) VALUES (''Jacob'', 18000, SELECT id FROM Employee WHERE name = ''Mary'');
INSERT INTO Employee (name, salary, supervisor_id) VALUES (''Kyle'', 26000, SELECT id FROM Employee WHERE name = ''Mary'');
INSERT INTO Employee (name, salary, supervisor_id) VALUES (''Joe'', 28000, SELECT id FROM Employee WHERE name = ''Nancy'');
INSERT INTO Employee (name, salary, supervisor_id) VALUES (''Reece'', 24000, SELECT id FROM Employee WHERE name = ''Nancy'');
INSERT INTO Employee (name, salary, supervisor_id) VALUES (''Rhys'', 21000, SELECT id FROM Employee WHERE name = ''Nancy'');
INSERT INTO Employee (name, salary, supervisor_id) VALUES (''Charlie'', 18000, SELECT id FROM Employee WHERE name = ''Nancy'');
INSERT INTO Employee (name, salary, supervisor_id) VALUES (''Damian'', 33000, SELECT id FROM Employee WHERE name = ''Nancy'');
INSERT INTO Employee (name, salary, supervisor_id) VALUES (''Margaret'', 37000, SELECT id FROM Employee WHERE name = ''Nancy'');
INSERT INTO Employee (name, salary, supervisor_id) VALUES (''Samantha'', 36000, SELECT id FROM Employee WHERE name = ''Nancy'');
INSERT INTO Employee (name, salary, supervisor_id) VALUES (''Bethany'', 27000, SELECT id FROM Employee WHERE name = ''Nancy'');
INSERT INTO Employee (name, salary, supervisor_id) VALUES (''Elizabeth'', 24000, SELECT id FROM Employee WHERE name = ''George'');
INSERT INTO Employee (name, salary, supervisor_id) VALUES (''Joanne'', 20000, SELECT id FROM Employee WHERE name = ''George'');
INSERT INTO Employee (name, salary, supervisor_id) VALUES (''Megan'', 16000, SELECT id FROM Employee WHERE name = ''George'');
INSERT INTO Employee (name, salary, supervisor_id) VALUES (''Victoria'', 39000, SELECT id FROM Employee WHERE name = ''George'');
INSERT INTO Employee (name, salary, supervisor_id) VALUES (''Lauren'', 40000, SELECT id FROM Employee WHERE name = ''George'');
INSERT INTO Employee (name, salary, supervisor_id) VALUES (''Michelle'', 26000, SELECT id FROM Employee WHERE name = ''George'');
INSERT INTO Employee (name, salary, supervisor_id) VALUES (''Tracy'', 25000, SELECT id FROM Employee WHERE name = ''George'');'
);


INSERT INTO public.topic (rank, name, description, created) VALUES (
    12, 'Nested queries', 'Practise nested queries.', now()
);

INSERT INTO public.exercise (name, handout, sql_model_solution, created, topic_id, database_id) VALUES (
    'Better salaries', 
    'List the names of the employees whose salary is bigger than the salary of George.', 
    'SELECT name FROM Employee WHERE salary > (SELECT salary FROM Employee WHERE name = ''George'');',
    now(), 
    (SELECT id FROM public.topic WHERE name = 'Nested queries'), 
    (SELECT id FROM public.database WHERE name = 'Salaries and employees')
);

INSERT INTO public.exercise (name, handout, sql_model_solution, created, topic_id, database_id) VALUES (
    'Worse salaries', 
    'List the names of the employees whose salary is smaller than the salary of Tracy.', 
    'SELECT name FROM Employee WHERE salary < (SELECT salary FROM Employee WHERE name = ''Tracy'');',
    now(), 
    (SELECT id FROM public.topic WHERE name = 'Nested queries'), 
    (SELECT id FROM public.database WHERE name = 'Salaries and employees')
);

INSERT INTO public.exercise (name, handout, sql_model_solution, created, topic_id, database_id) VALUES (
    'Better than average', 
    'List the names of the employees whose salary is bigger than the average salary of all the employees.', 
    'SELECT name FROM Employee WHERE salary > (SELECT AVG(salary) FROM Employee);',
    now(), 
    (SELECT id FROM public.topic WHERE name = 'Nested queries'), 
    (SELECT id FROM public.database WHERE name = 'Salaries and employees')
);

INSERT INTO public.exercise (name, handout, sql_model_solution, created, topic_id, database_id) VALUES (
    'Nancy''s team', 
    'List the names of the employees whose supervisor is Nancy and who get higher salary than Nancy.',
    'SELECT name FROM Employee WHERE supervisor_id = (SELECT id FROM Employee WHERE name = ''Nancy'') AND salary > (SELECT salary FROM Employee WHERE name = ''Nancy'');',
    now(), 
    (SELECT id FROM public.topic WHERE name = 'Nested queries'), 
    (SELECT id FROM public.database WHERE name = 'Salaries and employees')
);
