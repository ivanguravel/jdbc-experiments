package org.example;

import org.example.db.template.DbTemplate;
import org.example.db.template.MsSqlTemplate;
import org.example.db.template.OracleTemplate;
import org.example.db.template.PostgresTemplate;

import java.util.HashMap;
import java.util.Map;

public class Main {

    private static final String POSTGRES = "postgres";
    private static final String MSSQL = "mssql";
    private static final String ORACLE = "oracle";

    public static void main(String[] args) {
        Map<String, DbTemplate> dbTemplateMap = new HashMap<>();
        dbTemplateMap.put(POSTGRES, new PostgresTemplate()); // working with strings in out and in streams
        dbTemplateMap.put(MSSQL, new MsSqlTemplate());
        dbTemplateMap.put(ORACLE, new OracleTemplate());

        dbTemplateMap.get(ORACLE).process();
    }
}