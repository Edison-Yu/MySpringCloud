package cloud.activitiApi.utils;

import cloud.activitiApi.config.ActivityConfig;
import org.activiti.engine.*;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;

import java.util.ArrayList;
import java.util.List;

public class ActivitiUtils {


    private static final ProcessEngine processEngine = null;
    private static final Deployment deployment = null;
    private static final TaskService taskService = null;
    private static final RuntimeService runtimeService = null;
    private static final RepositoryService repositoryService = null;

    /**
     * 当前用户-->当前用户正在执行的任务--->当前正在执行的任务的piid-->该任务所在的流程实例
     * @param assignee
     * @return
     */
    public static List<ProcessInstance> getPIByUser(String assignee){
        List<ProcessInstance> pis = new ArrayList<ProcessInstance>();
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();

        // 该用户正在执行的任务
        List<Task> tasks = processEngine.getTaskService()
                .createTaskQuery()
                .taskAssignee(assignee)
                .list();
        for (Task task : tasks) {
            // 根据task-->piid-->pi
            String piid = task.getProcessInstanceId();
            ProcessInstance pi = processEngine.getRuntimeService()
                    .createProcessInstanceQuery()
                    .processInstanceId(piid)
                    .singleResult();
            pis.add(pi);
        }
        return pis;
    }

    /**
     * 根据当前的登录人能够推导出所在的流程定义
     */
    public static void getProcessInstance(String assignee){
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        List<Task> tasks = processEngine.getTaskService()
                .createTaskQuery()
                .taskAssignee(assignee)
                .list();
        for (Task task : tasks) {
            String pdid = task.getProcessDefinitionId();
            ProcessDefinition processDefinition = processEngine.getRepositoryService()
                    .createProcessDefinitionQuery()
                    .processDefinitionId(pdid)
                    .singleResult();
        }
    }



    //获取Deployment对象
    public static Deployment getdDeployment(RepositoryService repositoryService){
        // 根据bpmn文件部署流程
        if (deployment==null){
//            RepositoryService repositoryService = getProcessEngine().getRepositoryService();
            Deployment deployment = repositoryService.createDeployment()
                    .addClasspathResource("processes/xqd.bpmn")
                    .deploy();
            return deployment;
        }else {
            return deployment;
        }
    }

    //获取ProcessEngine对象
    public static ProcessEngine getProcessEngine(){
        if (processEngine==null){
//            ProcessEngine processEngine = ActivityConfig.getProcessEngine();
            ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
            return processEngine;
        }else {
            return processEngine;
        }
    }


    //获取TaskService对象
    public static TaskService getTaskService(){
        if (taskService==null){
            TaskService taskService = getProcessEngine().getTaskService();
            return taskService;
        }else {
            return taskService;
        }
    }


    //获取RuntimeService对象
    public static RuntimeService getRuntimeService(){
        if (runtimeService==null){
            RuntimeService runtimeService = getProcessEngine().getRuntimeService();
            return runtimeService;
        }else {
            return runtimeService;
        }
    }

    //获取RepositoryService对象
    public static RepositoryService getRepositoryService(){
        if (repositoryService==null){
            RepositoryService repositoryService = getProcessEngine().getRepositoryService();
            return repositoryService;
        }else {
            return repositoryService;
        }
    }

    public static Deployment getDeployment() {
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
        // 根据bpmn文件部署流程
//            RepositoryService repositoryService = getProcessEngine().getRepositoryService();
        Deployment deployment = processEngine.getRepositoryService().createDeployment()
                .addClasspathResource("processes/xqd.bpmn")
                .deploy();
        return deployment;
    }
}
