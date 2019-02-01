package zunpiau.sqljudger.database.viewer;

import org.springframework.jdbc.core.RowMapperResultSetExtractor;
import org.springframework.jdbc.support.JdbcUtils;
import zunpiau.sqljudger.database.DatabaseUtils;
import zunpiau.sqljudger.database.ResultSetExtractor.MatrixResultSetExtractor;
import zunpiau.sqljudger.database.entity.Column;
import zunpiau.sqljudger.database.entity.Constraint;
import zunpiau.sqljudger.database.entity.ForignKey;
import zunpiau.sqljudger.database.entity.Index;
import zunpiau.sqljudger.database.entity.PrimaryKey;
import zunpiau.sqljudger.database.entity.ResultWrapper;
import zunpiau.sqljudger.database.entity.SimpleTable;
import zunpiau.sqljudger.database.entity.Table;

import javax.annotation.Nullable;
import javax.sql.rowset.CachedRowSet;
import javax.sql.rowset.RowSetProvider;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;

@SuppressWarnings("unused")
public abstract class Viewer {

    private final Connection connection;
    private final String database;
    private final RowMapperResultSetExtractor<String> tableNameExtractor;
    private final RowMapperResultSetExtractor<String> columnNamesExtractor;
    private final RowMapperResultSetExtractor<Column> columnsExtractor;
    private final RowMapperResultSetExtractor<PrimaryKey> primaryExtractor;
    private final RowMapperResultSetExtractor<ForignKey> forignKeyExtractor;
    private String schema;

    protected Viewer(Connection connection) throws SQLException {
        this.connection = connection;
        database = connection.getCatalog();
        schema = connection.getSchema();
        columnNamesExtractor = new RowMapperResultSetExtractor<>((rs, i) -> rs.getString("COLUMN_NAME"));
        tableNameExtractor = new RowMapperResultSetExtractor<>((rs, i) -> rs.getString("table_name"));
        columnsExtractor = new RowMapperResultSetExtractor<>(
                (rs, i) -> new Column(rs.getInt("ORDINAL_POSITION"),
                        rs.getString("COLUMN_NAME"),
                        rs.getString("TYPE_NAME"),
                        rs.getInt("NULLABLE"),
                        rs.getString("COLUMN_DEF")));
        primaryExtractor = new RowMapperResultSetExtractor<>(
                (rs, i) -> new PrimaryKey(rs.getString("pk_name"), rs.getString("column_name")));
        forignKeyExtractor = new RowMapperResultSetExtractor<>(
                (rs, i) -> new ForignKey(rs.getString("fk_name"),
                        rs.getString("fkcolumn_name"),
                        rs.getString("pktable_name"),
                        rs.getString("pkcolumn_name"),
                        rs.getInt("update_rule"),
                        rs.getInt("delete_rule")));
    }

    public static void main(String[] args)
            throws ClassNotFoundException, SQLException, IOException, InterruptedException {
        Class.forName("org.postgresql.Driver");
        try (Connection connection = DriverManager
                .getConnection("jdbc:postgresql://127.0.0.1:5432/sql_judger", "postgres", "1122")) {
            Viewer viewer = newInstance(connection);
            List<Table> tables = viewer.getTables();
            for (Table table : tables) {
                System.out.println("table = " + table);
            }
        }
    }

    public static Viewer newInstance(Connection connection) throws SQLException {
        String product = connection.getMetaData().getDatabaseProductName();
        if ("PostgreSQL".equals(product)) {
            return new PgViewer(connection);
        }
        return new DafaultViewer(connection);
    }

    public String getSchema() {
        return schema;
    }

    public void setSchema(String schema) throws SQLException {
        connection.setSchema(schema);
        this.schema = schema;
    }

    public Connection getConnection() {
        return connection;
    }

    public List<Table> getTables() throws SQLException {
        return foreachTables((m, s) -> getTable(m, s));
    }

    public List<SimpleTable> getSimpleTables() throws SQLException {
        return foreachTables((m, s) -> getSimpleTable(m, s));
    }

    public List<String> getTableNames(DatabaseMetaData metaData) throws SQLException {
        ResultSet resultSet = metaData.getTables(database, schema, "%", new String[]{"TABLE"});
        return DatabaseUtils.extractAndClose(resultSet, tableNameExtractor);
    }

    public Table getTable(DatabaseMetaData metaData, String tableName) throws SQLException {
        return new Table(tableName,
                getColumns(metaData, tableName),
                getData(tableName),
                getPrimaryKey(metaData, tableName),
                getForignKeys(metaData, tableName),
                getIndexes(metaData, tableName),
                getConstraints(tableName));
    }

    public SimpleTable getSimpleTable(DatabaseMetaData metaData, String tableName) throws SQLException {
        return new SimpleTable(tableName, getColumnNames(metaData, tableName), getData(tableName));
    }

    public List<String> getColumnNames(DatabaseMetaData metaData, String table) throws SQLException {
        return DatabaseUtils.extractAndClose(metaData.getColumns(database, schema, table, null), columnNamesExtractor);
    }

    public List<Column> getColumns(DatabaseMetaData metaData, String table) throws SQLException {
        return DatabaseUtils.extractAndClose(metaData.getColumns(database, schema, table, null), columnsExtractor);
    }

    @Nullable
    public PrimaryKey getPrimaryKey(DatabaseMetaData metaData, String table) throws SQLException {
        ResultSet resultSet = metaData.getPrimaryKeys(database, schema, table);
        List<PrimaryKey> primaryKeys = DatabaseUtils.extractAndClose(resultSet, primaryExtractor);
        if (primaryKeys.isEmpty()) {
            return null;
        }
        return primaryKeys.get(0);
    }

    public List<ForignKey> getForignKeys(DatabaseMetaData metaData, String table) throws SQLException {
        return DatabaseUtils.extractAndClose(metaData.getImportedKeys(database, schema, table), forignKeyExtractor);
    }

    public List<Index> getIndexes(DatabaseMetaData metaData, String table) throws SQLException {
        ResultSet resultSet = metaData.getIndexInfo(database, schema, table, false, false);
        LinkedList<Index> list = new LinkedList<>();
        while (resultSet.next()) {
            String indexName = resultSet.getString("index_name");
            final String columnName = resultSet.getString("column_name");
            Index last;
            // ResultSet order by INDEX_NAME, check the last element only.
            if (!list.isEmpty() && (last = list.get(list.size() - 1)) != null && last.getName().equals(indexName)) {
                last.getColumns().add(columnName);
                return null;
            } else {
                LinkedList<String> columns = new LinkedList<>();
                columns.add(columnName);
                list.add(new Index(indexName, columns, "f".equals(resultSet.getString("non_unique")),
                        resultSet.getInt("type")));
            }
        }
        JdbcUtils.closeResultSet(resultSet);
        return list;
    }

    public List<List<String>> getData(String table) throws SQLException {
        Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        return DatabaseUtils
                .excuteAndClose(statement, "SELECT * FROM " + table, MatrixResultSetExtractor.getInstance());
    }

    public void close() throws SQLException {
        JdbcUtils.closeConnection(connection);
    }

    public ResultWrapper excute(String sql) throws SQLException {
        Statement statement = connection.createStatement();
        statement.execute(sql);
        CachedRowSet rowSet = RowSetProvider.newFactory().createCachedRowSet();
        rowSet.setReadOnly(true);
        rowSet.populate(statement.getResultSet());
        ResultWrapper wrapper = new ResultWrapper(rowSet, statement.getUpdateCount());
        JdbcUtils.closeStatement(statement);
        return wrapper;
    }

    private <T> List<T> foreachTables(SQLFunction<DatabaseMetaData, String, T> function) throws SQLException {
        DatabaseMetaData metaData = connection.getMetaData();
        List<String> tableNames = getTableNames(metaData);
        LinkedList<T> list = new LinkedList<>();
        for (String name : tableNames) {
            list.add(function.apply(metaData, name));
        }
        return list;
    }

    abstract List<Constraint> getConstraints(String table) throws SQLException;

    @FunctionalInterface
    interface SQLFunction<T, U, R> {

        R apply(T t, U u) throws SQLException;

    }

}
