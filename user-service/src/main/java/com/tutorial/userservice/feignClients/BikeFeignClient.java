package com.tutorial.userservice.feignClients;

import com.tutorial.userservice.model.Bike;
import com.tutorial.userservice.model.Car;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;
//url = "http://localhost:8003/bikes"
@FeignClient(name = "bike-service")
@RequestMapping("/bikes")
public interface BikeFeignClient {

    @PostMapping()
    Bike save (@RequestBody Bike bike);

    @GetMapping("/byuser/{userID}")
    List<Bike> getBikes(@PathVariable int userID);
}
