package ua.edu.ukma.springdb.routing;

import org.springframework.stereotype.Component;

@Component
public class DataSourceContext {

    private ThreadLocal<String> names;

    public DataSourceContext() {
        names =  new ThreadLocal<>();
    }

    public void setBranchContext(String dataSourceEnum) {
        names.set(dataSourceEnum);
    }

    public String getBranchContext() {
        return names.get();
    }

    public void clearBranchContext() {
        names.remove();
    }

}
