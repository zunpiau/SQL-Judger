package zunpiau.sqljudger.web.util;

import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.datasource.AbstractDataSource;
import org.springframework.lang.Nullable;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLTimeoutException;
import java.util.List;

@Slf4j
public class RoutingDataSource extends AbstractDataSource {

    private final List<DataSource> dataSources;
    private final int dataSourcesSize;
    @Nullable
    private final DataSource defaultDataSource;

    public RoutingDataSource(List<DataSource> dataSources, @Nullable DataSource defaultDataSource) {
        this.dataSources = dataSources;
        dataSourcesSize = dataSources.size();
        this.defaultDataSource = defaultDataSource;
    }

    @Override
    public Connection getConnection() throws SQLException {
        for (int i = 0; ; i++) {
            try {
                return determineDataSource(i).getConnection();
            } catch (SQLTimeoutException e) {
                if (i >= 3) {
                    throw e;
                }
                log.warn("get connection timeout, e:{}, retry times: {}", e.getMessage(), i);
            }
        }
    }

    @Override
    public Connection getConnection(String username, String password) throws SQLException {
        return determineDataSource(0).getConnection(username, password);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T unwrap(Class<T> iface) throws SQLException {
        if (iface.isInstance(this)) {
            return (T) this;
        }
        return determineDataSource(0).unwrap(iface);
    }

    @Override
    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        return iface.isInstance(this) || determineDataSource(0).isWrapperFor(iface);
    }

    protected DataSource determineDataSource(int retry) {
        int hash = Thread.currentThread().hashCode() % dataSourcesSize;
        if (retry > 0) {
            hash = (int) ((hash + (retry % 2 == 0 ? 1 : -1) * Math.pow(retry, 2)) % dataSourcesSize);
        }
        DataSource dataSource = dataSources.get(hash);
        if (dataSource == null && defaultDataSource != null) {
            dataSource = defaultDataSource;
            hash = -1;
        }
        if (dataSource == null) {
            throw new IllegalStateException("Cannot determine target DataSource");
        }
        if (dataSource instanceof HikariDataSource) {
            log.info("use datasource:{}, index:{}", ((HikariDataSource) dataSource).getPoolName(), hash);
        } else {
            log.info("use datasource:{}, index:{}", dataSource, hash);
        }
        return dataSource;
    }

}
