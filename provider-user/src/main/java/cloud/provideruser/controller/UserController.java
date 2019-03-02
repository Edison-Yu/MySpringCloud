package cloud.provideruser.controller;

import cloud.provideruser.dao.UserRepository;
import cloud.provideruser.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

/**
 * <p>
 * Created  by  jinboYu  on  2019/2/14
 */
@RequestMapping("/users")
@RestController
public class UserController {

    @Autowired
    private UserRepository userRepository;


    @Value("${server.port}")
    String port;

    @GetMapping("/{id}")
    public Optional<User> findById(@PathVariable long id){
       return this.userRepository.findById(id);
    }

    @GetMapping(value = "/hi")
    public String hi (){
        return "hello"+port;
    }

    @GetMapping(value = "findAll")
    public String findAll(){
        return this.userRepository.findAll().toString();
    }
}
