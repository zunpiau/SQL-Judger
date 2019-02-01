package zunpiau.sqljudger.database;

import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.support.JdbcUtils;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public final class DatabaseUtils {

    private DatabaseUtils() {
    }

    public static <T> T extractAndClose(ResultSet resultSet, ResultSetExtractor<T> extractor) throws SQLException {
        T result = extractor.extractData(resultSet);
        JdbcUtils.closeResultSet(resultSet);
        return result;
    }

    public static <T> T excuteAndClose(Statement statement, String sql, ResultSetExtractor<T> extractor)
            throws SQLException {
        T t = extractAndClose(statement.executeQuery(sql), extractor);
        JdbcUtils.closeStatement(statement);
        return t;
    }

    public static <T> T excuteAndClose(PreparedStatement statement, ResultSetExtractor<T> extractor)
            throws SQLException {
        T t = extractAndClose(statement.executeQuery(), extractor);
        JdbcUtils.closeStatement(statement);
        return t;
    }

}
