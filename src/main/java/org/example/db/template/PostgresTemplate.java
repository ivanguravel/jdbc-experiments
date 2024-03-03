package org.example.db.template;


public class PostgresTemplate extends DbTemplate {


    public PostgresTemplate() {
        super("jdbc:p6spy:postgresql://localhost/postgres?loggerLevel=DEBUG",
                "postgres", "changeme", "src/main/resources/postgres.sql");
    }

}
