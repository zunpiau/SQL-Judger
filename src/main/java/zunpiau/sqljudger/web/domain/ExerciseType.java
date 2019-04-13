package zunpiau.sqljudger.web.domain;

import com.fasterxml.jackson.annotation.JsonValue;

public enum ExerciseType {

    SIMPLE_QUERY("简单查询"),
    JOIN_QUERY("多表查询"),
    CONDITION_QUERY("条件查询"),
    CREATE_TABLE("创建表"),
    ALTER_TABLE("修改表"),
    INSERT("INSERT"),
    DELETE("DELETE"),
    UPDATE("UPDATE");

    final String name;

    ExerciseType(String name) {
        this.name = name;
    }

    @Override
    @JsonValue
    public String toString() {
        return name;
    }
}
