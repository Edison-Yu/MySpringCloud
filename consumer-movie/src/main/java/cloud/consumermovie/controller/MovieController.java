package cloud.consumermovie.controller;

import cloud.activitiApi.utils.ActivitiUtils;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;


/**
 * <p>
 * Created  by  jinboYu  on  2019/2/14
 */
@RequestMapping("/movies")
@RestController
public class MovieController {

    @Autowired
    private RestTemplate restTemplate;

//    @Autowired
//    private UserFeignClient userFeignClient;
//
    @Autowired
    private TaskService taskService;

    @Autowired
    private RepositoryService repositoryService;

    @Autowired
    private RuntimeService runtimeService;



//    @GetMapping(value = "/users/{id}")
//    public User findById(@PathVariable Long id) {
//        // 这里用到了RestTemplate的占位符能力
////        User user = this.restTemplate().getForObject("http://localhost:8080/users/{id}", User.class, id);
////        User user = this.restTemplate.getForObject("http://provider-user/users/{id}",
////                User.class,
////                id);
//        User user = this.userFeignClient.findById(id);
//        // ...电影微服务的业务...
//        return user;
//    }
//
//
//    @GetMapping(value = "/hello")
//    public String hi() {
//        String hi = this.restTemplate.getForObject("http://provider-user/users/hi", String.class);
//        return hi;
//    }

    //提交需求单
    @GetMapping(value = "/submitXqd/{cjr}/{zzid}")
    public String submitXqd(@PathVariable String cjr, @PathVariable String zzid) {
        try {
//            ProcessEngine processEngine = ActivitiUtils.getProcessEngine();
//            // 获取流程定义
            ProcessDefinition definition = repositoryService.createProcessDefinitionQuery().deploymentId(ActivitiUtils.getDeployment().getId()).singleResult();
            ProcessDefinition definition2 =ActivitiUtils.getRepositoryService().createProcessDefinitionQuery().deploymentId(ActivitiUtils.getDeployment().getId()).singleResult();
            //启动流程定义
            ProcessInstance instance = runtimeService.startProcessInstanceById(definition.getId());
            // 执行流程第一个任务
            Task task = taskService.createTaskQuery().processInstanceId(instance.getId()).singleResult();
            taskService.setVariable(task.getId(), "创建人", cjr);
            Map<String, Object> map = new HashMap<>();
            map.put("zzid", zzid);
            System.out.println("执行第一个任务，任务名称：" + task.getName());
            taskService.complete(task.getId(), map);
//            ProcessDefinition definition = ActivitiUtils.getRepositoryService().createProcessDefinitionQuery().deploymentId(ActivitiUtils.getdDeployment().getId()).singleResult();
//            ProcessInstance instance = ActivitiUtils.getRuntimeService().startProcessInstanceById(definition.getId());
//            TaskService taskService = ActivitiUtils.getTaskService();
//            Task task = taskService.createTaskQuery().processInstanceId(instance.getId()).singleResult();
//            taskService.setVariable(task.getId(), "创建人", cjr);
//            Map<String, Object> map = new HashMap<>();
//            map.put("zzid", zzid);
//            System.out.println("执行第一个任务，任务名称：" + task.getName());
//            taskService.complete(task.getId(), map);
            return "提交成功!";
        } catch (Exception e) {
            return "提交失败!";
        }
    }

    //根据分组ID获取任务
//    @GetMapping(value = "/getTaskNameByGroup/{groupId}")
//    public String getTaskByGroup(@PathVariable String groupId) {
//        List<Task> tasks = ActivitiUtils.getProcessEngine().getTaskService().createTaskQuery().taskCandidateGroup(groupId).list();
//        if (tasks != null) {
//            for (Task task : tasks) {
//                System.out.println("任务名称:" + task.getName());
//                System.out.println("任务ID：" + task.getId());
//                System.out.println("流程执行ID：" + task.getExecutionId());
//            }
//            return "获取成功，该组织下任务数:" + tasks.size();
//        } else {
//            return "获取成功，该组织下任务书: 0 ";
//        }
//    }
//
//    //审核需求单
//    @GetMapping(value = "/sh/{taskId}/{shr}/{i}")
//    public String sh(@PathVariable String taskId, @PathVariable String shr, @PathVariable int i) {
//        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
//        Map<String, Object> map = new HashMap<>();
//        Boolean flag = true;
//        if (i == 1) {
//            flag = false;
//        }
//        map.put("pass", flag);
//        taskService.setVariable(task.getId(), "审核人", shr);
//        taskService.complete(task.getId(), map);
//        if (flag) {
//            return "审核通过！审核人：" + shr;
//        } else {
//            return "审核未通过!审核人：" + shr;
//        }
//    }
}
