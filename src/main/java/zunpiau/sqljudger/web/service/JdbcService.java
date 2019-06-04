package zunpiau.sqljudger.web.service;

import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.util.JdbcConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import zunpiau.sqljudger.database.DatabaseUtils;
import zunpiau.sqljudger.database.entity.ResultWrapper;
import zunpiau.sqljudger.database.entity.SimpleTable;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Slf4j
@Service
public class JdbcService {

    private final DataSource dataSource;
    private final RandomStringGenerator stringGenerator;

    public JdbcService(@Qualifier("judgerDataSource") DataSource dataSource, RandomStringGenerator stringGenerator) {
        this.dataSource = dataSource;
        this.stringGenerator = stringGenerator;
    }

    public static String formatSQL(String sql) {
        if (sql == null || sql.isEmpty()) {
            return sql;
        }
        return SQLUtils.format(sql, JdbcConstants.POSTGRESQL);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public List<SimpleTable> excuteAndRetrieve(String sql) throws SQLException {
        String schema = generateRandomSchema();
        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(false);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            DatabaseUtils.createAndSetSchema(connection, schema);
            return DatabaseUtils.excuteAndRetrieve(connection, sql);
        }
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public ResultWrapper excute(String dataAndSchema, String sql, boolean rowOrder, boolean columnOrder) {
        String schema = generateRandomSchema();
        ResultWrapper resultWrapper;
        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(false);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            DatabaseUtils.createAndSetSchema(connection, schema);
            resultWrapper = DatabaseUtils.excute(connection, dataAndSchema, sql);
            if (resultWrapper.getDataSet() != null) {
                final List<String> headers = resultWrapper.getDataSet().getHeaders();
                final List<List<String>> data = resultWrapper.getDataSet().getData();
                if (!columnOrder) {
                    orderColumn(headers, data);
                }
                if (!rowOrder) {
                    data.sort(Comparator.comparing(List::toString));
                }
            }
        } catch (Exception e) {
            resultWrapper = new ResultWrapper(e.getMessage());
        }
        resultWrapper.setFormated(formatSQL(sql));
        return resultWrapper;
    }

    List<StringListTuple> orderColumn(List<String> headers, List<List<String>> data) {
        final List<StringListTuple> tuples = StringListTuple.build(headers, data);
        tuples.sort(Comparator.comparing(StringListTuple::getHeader));
        headers.clear();
        ArrayList<ArrayList<String>> columns = new ArrayList<>(data.size());
        for (int i = 0; i < data.size(); i++) {
            columns.add(new ArrayList<>(data.get(0).size()));
        }
        for (int i = 0; i < tuples.size(); i++) {
            StringListTuple tuple = tuples.get(i);
            headers.add(tuple.header);
            List<String> strings = tuple.columns;
            for (int j = 0; j < strings.size(); j++) {
                columns.get(j)
                        .add(strings.get(j));
            }
        }
        data.clear();
        data.addAll(columns);
        return tuples;
    }

    private String generateRandomSchema() {
        return stringGenerator.generate("judger_", 6);
    }

    public static class StringListTuple {

        private final String header;
        private final List<String> columns;

        private StringListTuple(String header, List<String> columns) {
            this.header = header;
            this.columns = columns;
        }

        static List<StringListTuple> build(List<String> headers, List<List<String>> data) {
            List<StringListTuple> tuples = new ArrayList<>(headers.size());
            for (int i = 0; i < headers.size(); i++) {
                List<String> columns = new ArrayList<>(data.size());
                for (int j = 0; j < data.size(); j++) {
                    columns.add(data.get(j).get(i));
                }
                tuples.add(new StringListTuple(headers.get(i), columns));
            }
            return tuples;
        }

        public String getHeader() {
            return header;
        }

    }

}
