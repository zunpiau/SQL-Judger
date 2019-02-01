package zunpiau.sqljudger.database.ResultSetExtractor;

import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapperResultSetExtractor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

public class MatrixResultSetExtractor implements ResultSetExtractor<List<List<String>>> {

    private static final MatrixResultSetExtractor instance = new MatrixResultSetExtractor();
    private final ResultSetExtractor<List<List<String>>> extractor;

    private MatrixResultSetExtractor() {
        extractor = new RowMapperResultSetExtractor<>((resultSet, i) -> {
            int columnCount = resultSet.getMetaData().getColumnCount();
            List<String> row = new LinkedList<>();
            for (int j = 0; j < columnCount; j++) {
                row.add(resultSet.getString(j + 1));
            }
            return row;
        });
    }

    public static MatrixResultSetExtractor getInstance() {
        return instance;
    }

    @Override
    public List<List<String>> extractData(ResultSet rs) throws SQLException {
        return extractor.extractData(rs);
    }

}
