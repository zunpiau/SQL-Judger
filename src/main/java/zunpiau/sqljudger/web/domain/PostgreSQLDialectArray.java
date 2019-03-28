package zunpiau.sqljudger.web.domain;

import org.hibernate.dialect.PostgreSQL95Dialect;

import java.sql.Types;

public class PostgreSQLDialectArray extends PostgreSQL95Dialect {

    public PostgreSQLDialectArray() {
        registerHibernateType(Types.ARRAY, "array");
        registerColumnType(Types.ARRAY, "long[]");
    }
}
