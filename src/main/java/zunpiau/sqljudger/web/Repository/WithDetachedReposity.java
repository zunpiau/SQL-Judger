package zunpiau.sqljudger.web.Repository;

public interface WithDetachedReposity<T, ID> {

    void refresh(T t);
}
