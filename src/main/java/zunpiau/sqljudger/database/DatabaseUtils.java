package zunpiau.sqljudger.database;

import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.support.JdbcUtils;
import org.springframework.util.StringUtils;
import zunpiau.sqljudger.database.ResultSetExtractor.MatrixResultSetExtractor;
import zunpiau.sqljudger.database.entity.DataSet;
import zunpiau.sqljudger.database.entity.ResultWrapper;
import zunpiau.sqljudger.database.entity.SimpleTable;
import zunpiau.sqljudger.database.viewer.Viewer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

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

    public static List<SimpleTable> excuteAndRetrieve(Connection connection, String sql) throws SQLException {
        try (Statement statement = connection.createStatement()) {
            statement.execute(sql);
            Viewer viewer = Viewer.newInstance(connection);
            return viewer.getSimpleTables();
        }
    }

    public static ResultWrapper excute(Connection connection, String schemaAndData, String execute)
            throws SQLException {
        if (!StringUtils.isEmpty(schemaAndData)) {
            try (Statement statement = connection.createStatement()) {
                statement.execute(schemaAndData);
            }
        }
        try (Statement statement = connection.createStatement()) {
            statement.execute(execute);
            ResultSet resultSet = statement.getResultSet();
            if (resultSet != null) {
                ArrayList<String> headers = getColumns(resultSet);
                List<List<String>> data = MatrixResultSetExtractor.getInstance().extractData(resultSet);
                DataSet dataSet = new DataSet(headers, data);
                return new ResultWrapper(dataSet);
            } else {
                Viewer viewer = Viewer.newInstance(connection);
                return new ResultWrapper(viewer.getSimpleTables());
            }
        }
    }

    private static ArrayList<String> getColumns(ResultSet resultSet) throws SQLException {
        ResultSetMetaData metaData = resultSet.getMetaData();
        int columnCount = metaData.getColumnCount();
        ArrayList<String> headers = new ArrayList<>(columnCount);
        for (int i = 1; i <= columnCount; i++) {
            headers.add(metaData.getColumnName(i));
        }
        return headers;
    }

    public static void createAndSetSchema(Connection connection, String schema) throws SQLException {
        connection.createStatement().execute("CREATE SCHEMA \"" + schema + '"');
        connection.setSchema(schema);
    }

}
