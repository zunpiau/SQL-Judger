package zunpiau.sqljudger.web.service;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import zunpiau.sqljudger.database.DatabaseUtils;
import zunpiau.sqljudger.database.entity.ResultWrapper;
import zunpiau.sqljudger.database.entity.SimpleTable;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

@Service
public class JdbcService {

    private final DataSource dataSource;
    private final RandomStringGenerator stringGenerator;

    public JdbcService(@Qualifier("judgerDataSource") DataSource dataSource,
            RandomStringGenerator stringGenerator) {
        this.dataSource = dataSource;
        this.stringGenerator = stringGenerator;
    }

    @Transactional
    public List<SimpleTable> excuteAndRetrieve(String sql) throws SQLException {
        String schema = generateRandomSchema();
        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(false);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            DatabaseUtils.createAndSetSchema(connection, schema);
            return DatabaseUtils.excuteAndRetrieve(connection, sql);
        }
    }

    @Transactional
    public ResultWrapper excute(String dataAndSchema, String sql) throws SQLException {
        String schema = generateRandomSchema();
        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(false);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            DatabaseUtils.createAndSetSchema(connection, schema);
            return DatabaseUtils.excute(connection, dataAndSchema, sql);
        }
    }

    private String generateRandomSchema() {
        return stringGenerator.generate("judger_", 6);
    }

}
