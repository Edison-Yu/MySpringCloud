package cloud.consumermovie.feign;

import cloud.consumermovie.pojo.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * <p>
 * Created  by  jinboYu  on  2019/2/18
 */
@FeignClient(name = "provider-user")
public interface UserFeignClient {

    @GetMapping("/users/{id}")
    User findById(@PathVariable("id") Long id);
}
