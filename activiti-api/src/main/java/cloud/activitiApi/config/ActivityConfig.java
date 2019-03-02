package cloud.activitiApi.config;

import org.activiti.engine.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * <p>
 * Created  by  jinboYu  on  2019/3/1
 */
@Configuration
public class ActivityConfig {


    public ProcessEngine getProcessEngine() {
        //1.创建Activity配置对象的实例
        ProcessEngineConfiguration configuration = ProcessEngineConfiguration.createStandaloneProcessEngineConfiguration();
        //2.设置数据库连接信息
        //设置数据库地址
        configuration.setJdbcUrl("jdbc:mysql://127.0.0.1:3306/activity?characterEncoding=utf8&useSSL=true&serverTimezone=GMT%2B8");
        //数据库驱动
        configuration.setJdbcDriver("com.mysql.jdbc.Driver");
        //用户名
        configuration.setJdbcUsername("root");
        //密码
        configuration.setJdbcPassword("root");
        //设置数据建表策略
        /**
         *DB_SCHEMA_UPDATE_TRUE 如果不存在表就创建表，存在表就使用
         */
        configuration.setDatabaseSchemaUpdate(ProcessEngineConfiguration.DB_SCHEMA_UPDATE_TRUE);
        ProcessEngine processEngine = configuration.buildProcessEngine();
        return processEngine;
    }

    @Bean(name = "repositoryService")
    public RepositoryService repositoryService() {
        RepositoryService repositoryService = getProcessEngine().getRepositoryService();
        return repositoryService;
    }

    @Bean(name = "taskService")
    public TaskService taskService() {
        TaskService taskService = getProcessEngine().getTaskService();
        return taskService;
    }

    @Bean(name = "runtimeService")
    public RuntimeService runtimeService() {
        RuntimeService runtimeService = getProcessEngine().getRuntimeService();
        return runtimeService;
    }

}
