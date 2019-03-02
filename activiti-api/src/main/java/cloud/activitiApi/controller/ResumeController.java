package cloud.activitiApi.controller;

import cloud.activitiApi.utils.ActivitiUtils;
import org.activiti.engine.*;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;

import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
public class ResumeController {

    @Autowired
    RepositoryService repositoryService;

    @Autowired
    RuntimeService runtimeService;

    @Autowired
    TaskService taskService;

//    @RequestMapping("/list")
//    public List<ProcessDto> queryDefinitionList(){
//        ProcessDefinitionQuery query = repositoryService.createProcessDefinitionQuery();
//        query.processDefinitionKey("myProcess_1");//从数据act_re_procdef获知
//        query.orderByDeploymentId().desc();
//
//        // 分页查询
//        query.listPage(0, 10);
//        List<ProcessDefinition> list = query.list();
//        List<ProcessDto> dtoList = new ArrayList<>();
//        for(ProcessDefinition def : list){
//            ProcessDto dto = new ProcessDto();
//            dto.setId(def.getId());
//            dto.setName(def.getName());
//            dto.setKey(def.getKey());
//            dto.setDeploymentId(def.getDeploymentId());
//            dto.setResourceName(def.getResourceName());
//            dto.setVersion(def.getVersion());
//            dtoList.add(dto);
//        }
//        return dtoList;
//    }
//
//    /**
//     * 执行工作流流程
//     */
//    @RequestMapping("/execute")
//    public void executeResumeProcess(){
//
//        // 根据bpmn文件部署流程
//        Deployment deployment = repositoryService.createDeployment()
//                .addClasspathResource("processes/simple-process.bpmn")
//                .addClasspathResource("processes/simple-process.png")
//                .deploy();
//
//        // 获取流程定义
//        ProcessDefinition definition = repositoryService.createProcessDefinitionQuery().deploymentId(deployment.getId()).singleResult();
//
//        // 启动流程定义，返回流程实例
//        ProcessInstance instance = runtimeService.startProcessInstanceById(definition.getId());
//        System.out.println("流程创建成功，当前流程实例id为："+ instance.getId());
//
//        // 第一次执行流程任务
//        Task task = taskService.createTaskQuery().processInstanceId(instance.getId()).singleResult();
//        System.out.println("第1次执行前，任务名称：" + task.getName());
//        taskService.complete(task.getId());
//
//        // 第二次执行流程任务
//        task = taskService.createTaskQuery().processInstanceId(instance.getId()).singleResult();
//        System.out.println("第2次执行前，任务名称：" + task.getName());
//        taskService.complete(task.getId());
//
//        task = taskService.createTaskQuery().processInstanceId(instance.getId()).singleResult();
//        System.out.println("任务执行完毕:" + task);
//
//    }

    @GetMapping(value = "/myActivity")
    public void myActivity(){
        // 根据bpmn文件部署流程
        Deployment deployment = repositoryService.createDeployment()
                .addClasspathResource("processes/activity.bpmn")
                .deploy();

        // 获取流程定义
        ProcessDefinition definition = repositoryService.createProcessDefinitionQuery().deploymentId(deployment.getId()).singleResult();

        //启动流程定义
        ProcessInstance instance = runtimeService.startProcessInstanceById(definition.getId());

        // 执行流程第一个任务
        Task task = taskService.createTaskQuery().processInstanceId(instance.getId()).singleResult();
        taskService.setAssignee(task.getId(),"张三");
        System.out.println("执行第一个任务，任务名称：" + task.getName());
        taskService.complete(task.getId());

        // 执行流程第二个任务
        task = taskService.createTaskQuery().processInstanceId(instance.getId()).singleResult();
        taskService.setAssignee(task.getId(),"李四");
        System.out.println("执行第二个任务，任务名称："+task.getName());
        Map<String,Object> map = new HashMap<>();
        map.put("pass",true);
        taskService.complete(task.getId(),map);

    }

    //提交
    @GetMapping(value = "/tj/{tjr}/{groupId}")
    public String tj(@PathVariable String tjr,@PathVariable String groupId){
        try {

            // 获取流程定义
            ProcessDefinition definition = repositoryService.createProcessDefinitionQuery().deploymentId(ActivitiUtils.getdDeployment(repositoryService).getId()).singleResult();
            System.out.println("流程ID："+ActivitiUtils.getdDeployment(repositoryService).getId());
            //启动流程定义
            ProcessInstance instance = runtimeService.startProcessInstanceById(definition.getId());
            System.out.println("流程ID："+definition.getId());

            // 执行流程第一个任务
            Task task = taskService.createTaskQuery().processInstanceId(instance.getId()).singleResult();
            taskService.setAssignee(task.getId(),tjr);
            taskService.addCandidateGroup(task.getId(),groupId);
            System.out.println("执行第一个任务，任务名称：" + task.getName());
            taskService.complete(task.getId());
            return "提交成功!";
        } catch (Exception e) {
            return "提交失败!";
        }
    }

    //导出流程图
    @GetMapping(value = "/viewImage/{deploymentId}")
    public void viewImage(@PathVariable String deploymentId) throws IOException {
        List<String> names = repositoryService.getDeploymentResourceNames(deploymentId);
        String imageName = null;
        for (String name : names) {
            System.out.println("name:"+name);
            if (name.indexOf(".png")>=0){
                imageName = name;
            }
        }
        System.out.println("imageName:"+imageName);
        if (imageName!=null){
            File file = new File("d:/"+imageName);
            OutputStream out = new FileOutputStream(file);
            InputStream in =repositoryService.getResourceAsStream(deploymentId,imageName);
            FileCopyUtils.copy(in,out);
        }
    }

    //审核
    @GetMapping(value = "/sh/{taskId}/{shr}/{i}")
    public String sh(@PathVariable String taskId,@PathVariable String shr,@PathVariable int i){
//        Task task =taskService.createTaskQuery().processDefinitionId(deploymentId).singleResult();
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        Map<String,Object> map = new HashMap<>();
        Boolean flag = true;
        if (i==1){
           flag =false;
        }
        map.put("pass",flag);
        taskService.setVariable(task.getId(),"审核人",shr);
        taskService.complete(task.getId(),map);
        if (flag){
            return "审核通过！审核人："+shr;
        }else {
            return "审核未通过!审核人："+shr;
        }
    }

    //根据组织ID获取该组织下的任务
    @GetMapping(value = "/getTaskNameByGroup/{groupName}")
    public String getTaskNameByGroup(@PathVariable String groupName){
        List<Task> tasks = ActivitiUtils.getProcessEngine().getTaskService().createTaskQuery().taskCandidateGroup(groupName).list();
        if (tasks!=null){
            for (Task task : tasks) {
                System.out.println("任务名称:"+task.getName());
                System.out.println("任务ID："+task.getId());
                System.out.println("流程执行ID："+ task.getExecutionId());
            }
            return "获取成功，该组织下任务数:"+tasks.size();
        }else {
            return "获取成功，该组织下任务书: 0 ";
        }
    }

    /**
     * 提交需求单
     * @param cjr
     * @param zzid
     * @return
     */
    @GetMapping(value = "/submitXqd/{cjr}/{zzid}")
    public String submitXqd(@PathVariable String cjr,@PathVariable String zzid){
        try {
            // 获取流程定义
            ProcessDefinition definition = repositoryService.createProcessDefinitionQuery().deploymentId(ActivitiUtils.getdDeployment(repositoryService).getId()).singleResult();
            //启动流程定义
            ProcessInstance instance = runtimeService.startProcessInstanceById(definition.getId());
            // 执行流程第一个任务
            Task task = taskService.createTaskQuery().processInstanceId(instance.getId()).singleResult();
            taskService.setVariable(task.getId(),"创建人",cjr);
            Map<String,Object> map = new HashMap<>();
            map.put("zzid",zzid);
            System.out.println("执行第一个任务，任务名称：" + task.getName());
            taskService.complete(task.getId(),map);
            return "提交成功!";
        } catch (Exception e) {
            return "提交失败!";
        }
    }
}
