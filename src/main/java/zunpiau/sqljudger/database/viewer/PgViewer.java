package zunpiau.sqljudger.database.viewer;

import org.springframework.jdbc.core.RowMapperResultSetExtractor;
import zunpiau.sqljudger.database.DatabaseUtils;
import zunpiau.sqljudger.database.entity.Constraint;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class PgViewer extends Viewer {

    private static final String SQL = "SELECT c.conname AS name, " +
                                      "        c.consrc AS search_condition, " +
                                      "        d.description AS comment " +
                                      "    FROM pg_class r " +
                                      "    INNER JOIN pg_namespace nr " +
                                      "        ON ( nr.oid = r.relnamespace ) " +
                                      "    INNER JOIN pg_constraint c " +
                                      "        ON ( c.conrelid = r.oid ) " +
                                      "    INNER JOIN pg_namespace nc " +
                                      "        ON ( nc.oid = c.connamespace ) " +
                                      "    LEFT OUTER JOIN pg_description d " +
                                      "        ON ( d.objoid = c.oid ) " +
                                      "    WHERE r.relkind = 'r' " +
                                      "        AND nr.nspname = ? " +
                                      "        AND r.relname = ? " +
                                      "        AND c.contype = 'c' " +
                                      "    ORDER BY r.relname, c.conname ";
    private final RowMapperResultSetExtractor<Constraint> constraintExtractor;

    public PgViewer(Connection connection) throws SQLException {
        super(connection);
        constraintExtractor = new RowMapperResultSetExtractor<>(
                (rs, i) -> new Constraint(rs.getString("name"),
                        rs.getString("search_condition"),
                        rs.getString("comment")));
    }

    /**
     * Retrieve check constraints only.
     */
    @Override
    public List<Constraint> getConstraints(String table) throws SQLException {
        PreparedStatement statement = getConnection()
                .prepareStatement(SQL, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
        statement.setString(1, getSchema());
        statement.setString(2, table);
        return DatabaseUtils.excuteAndClose(statement, constraintExtractor);
    }
}
