package org.example.db.template;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Objects;
import java.util.Properties;
import java.util.UUID;

public class OracleTemplate extends DbTemplate {

    static{
        try {
            Class.forName ("oracle.jdbc.OracleDriver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        Properties props = System.getProperties();
        props.setProperty("java.util.logging.config.file", "src/main/resources/logging.properties");
    }

    public OracleTemplate() {

        super("jdbc:p6spy:oracle:thin:@localhost:1521/xe",
                "system", "oracle",
                "src/main/resources/oracle.sql");

    }

    void setUUID(PreparedStatement statement) throws SQLException {

        statement.setBytes(1, uuidToBytes(UUID.randomUUID()));
    }
    private byte[] uuidToBytes(final UUID uuid) {
        if (Objects.isNull(uuid)) {
            return null;
        }

        final byte[] uuidAsBytes = new byte[16];

        ByteBuffer.wrap(uuidAsBytes)
                .order(ByteOrder.BIG_ENDIAN)
                .putLong(uuid.getMostSignificantBits())
                .putLong(uuid.getLeastSignificantBits());

        return uuidAsBytes;
    }
}
