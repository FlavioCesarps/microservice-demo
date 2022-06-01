package com.tutorial.userservice.feignClient;

import com.tutorial.userservice.model.Bike;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name="bike-service", url="http://localhost:8003/bikes")
public interface BikeFeignClient {

    @PostMapping()
    Bike save (@RequestBody Bike bike);

    @GetMapping("/byuser/{userID}")
    List<Bike> getBikes(@PathVariable int userID );
}
