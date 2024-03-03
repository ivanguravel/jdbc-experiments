package org.example.db.template;

import oracle.jdbc.driver.OracleDriver;
import org.example.ScriptRunner;
import org.slf4j.bridge.SLF4JBridgeHandler;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.sql.*;
import java.util.HashMap;
import java.util.Properties;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class DbTemplate {

    static {
        SLF4JBridgeHandler.install();
        Logger logger = Logger.getLogger("");
        logger.setLevel(Level.FINEST);
    }

    private final String url;
    private final String user;
    private final String password;
    private final String initScript;

    private ScriptRunner scriptRunner;
    private Connection connection;

    DbTemplate(String url, String user, String password, String initScript) {
        this.url = url;
        this.user = user;
        this.password = password;
        this.initScript = initScript;
    }



    public void process() {
        try {
            connect();
            init();
            save();
            select();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    void connect() throws SQLException {
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        Properties props = new Properties();
        props.setProperty("user", this.user);
        props.setProperty("password", this.password);
        props.setProperty("oracle.jdbc.Trace", "true");
        props.setProperty("oracle.jdbc.TraceLevel", "DEBUG");
        this.connection = DriverManager.getConnection(this.url, props);
    }
    void init() throws IOException, SQLException {
        this.scriptRunner = new ScriptRunner(this.connection, false, true);
        this.scriptRunner.runScript(new BufferedReader(new FileReader(this.initScript)));
    }
    void save() {
        File file = new File("src/main/resources/postgres.sql");
        try (FileInputStream stream = new FileInputStream(file);
             PreparedStatement statement = connection
                     .prepareStatement("INSERT INTO ACCOUNTS (ID, NAME, PROPERTIES, PHOTO) VALUES (?, ?, ?, ?)");) {

            connection.setAutoCommit(false);
            setUUID(statement);

            statement.setString(2, "name");
            statement.setObject(3, null);
            statement.setBinaryStream(4, stream,file.length());
            statement.execute();
            connection.commit();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    void select() {
         try {
             Statement statement = connection.createStatement();
             statement.execute("select * from ACCOUNTS");
             ResultSet resultSet = statement.getResultSet();
             while (resultSet.next()) {
                 System.out.println(resultSet.getString("id"));
             }
         } catch (SQLException e) {
             throw new RuntimeException(e);
         }
     }


    void setUUID(PreparedStatement statement) throws SQLException {
        statement.setObject(1, UUID.randomUUID());
    }
}
