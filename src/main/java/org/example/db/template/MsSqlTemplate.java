package org.example.db.template;


import java.io.IOException;
import java.io.InputStream;
import java.util.logging.LogManager;

public class MsSqlTemplate extends DbTemplate {

    static {
        try (InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream("logging.properties")) {
            LogManager.getLogManager().readConfiguration(is);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public MsSqlTemplate() {
        super("jdbc:p6spy:sqlserver://localhost;database=msdb;encrypt=true;trustServerCertificate=true;logStatement=true",
                "sa", "test123321",
                "src/main/resources/mssql.sql");
    }
}
