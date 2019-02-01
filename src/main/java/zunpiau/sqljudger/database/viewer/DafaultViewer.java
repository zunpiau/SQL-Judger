package zunpiau.sqljudger.database.viewer;

import zunpiau.sqljudger.database.entity.Constraint;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

public class DafaultViewer extends Viewer {

    public DafaultViewer(Connection connection) throws SQLException {
        super(connection);
    }

    @Override
    List<Constraint> getConstraints(String table) throws SQLException {
        return Collections.emptyList();
    }
}
