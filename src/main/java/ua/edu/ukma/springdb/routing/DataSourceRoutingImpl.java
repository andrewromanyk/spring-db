package ua.edu.ukma.springdb.routing;

import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.PropertySources;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Primary
@Component
public class DataSourceRoutingImpl extends AbstractRoutingDataSource {

    private DataSourceContext dataSourceContext;

    @Autowired
    public void setDataSourceContext(DataSourceContext dataSourceContext) {
        this.dataSourceContext = dataSourceContext;
    }

    public DataSourceRoutingImpl(@Qualifier("mainDb") DataSource dataSource1,  @Qualifier("sndDb") DataSource dataSource2) {
        Map<Object, Object> dataSourceMap = new HashMap<>();
        dataSourceMap.put("main", dataSource1);
        dataSourceMap.put("sec", dataSource2);
        this.setTargetDataSources(dataSourceMap);
        this.setDefaultTargetDataSource(dataSource1);
    }

    @Override
    protected Object determineCurrentLookupKey() {
        return dataSourceContext.getBranchContext();
    }
}
