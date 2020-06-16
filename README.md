# sqltrainer

A SQL-training system where both students and teachers can create content. 
Teachers have the opportunity of creating databases, topics and exercises. 
Exercises created by students are accessible by other students, who can use 
them for practice.

The system was used at the University of Helsinki during 2018-2019 and is still 
available for testing at [https://sql-t.herokuapp.com/](https://sql-t.herokuapp.com/)
-- this installation assumes that the user has an account at [https://mooc.fi](https://mooc.fi).

The database of the testing server is cleared occasionally.

Note that the system is not actively maintained as it is no longer in use at the
University of Helsinki. 


## Development and production use

The system requires a PostgreSQL database and an OAuth server. 

For development, set the relevant configurations in:

- src/main/resources/application-default.properties
- src/main/resources/application-default.yml

For production, set the relevant configurations in:

- src/main/resources/application-production.properties
- src/main/resources/application-production.yml

As the OAuth configurations are currently injected programmatically, these 
should be reflected in:

- src/main/java/rage/sqltrainer/security/SecurityConfiguration.java

Currently, the implementation assumes that the production version will be hosted
on [Heroku](https://www.heroku.com/). If you wish to change this, adjust the
production configuration file accordingly to account for injected database
settings. The file is at

- src/main/java/rage/sqltrainer/config/HerokuProductionConfiguration.java

If you wish to change the feedback questions that are given to students after
each completed exercise, adjust the file at

- src/main/java/rage/sqltrainer/controller/ReviewController.java (starting from line 50)

The system assumes that the OAuth-server provides the following information 
about the user:

- id
- username
- email
- administrator

When integrated with Test My Code (tmc.mooc.fi), the following classes have been 
used to retrieve the user details. 

- src/main/java/rage/sqltrainer/security/UserDetailsRetrievingService.java
- src/main/java/rage/sqltrainer/security/TmcUserDetails.java

## Deployment to Heroku

The project comes with a Procfile and works with Heroku out of the box, given
an OAuth-server. 

## References

For work related to the system, see:

Leinonen et al. 2020. Crowdsourcing ContentCreation for SQL Practice. In 
Proceedings of the 2020 ACM Conference onInnovation and Technology in Computer 
Science Education (ITiCSE ’20), June 15–19, 2020, Trondheim, Norway. ACM. 

