package com.tutorial.userservice.feignClients;

import com.tutorial.userservice.model.Car;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "car-service")

//url = "http://localhost:8002/cars"
public interface CarFeignClient {

    @PostMapping("/cars")
    Car save (@RequestBody Car car);

    @GetMapping("/cars/byuser/{userID}")
    List<Car> getCars(@PathVariable int userID);
}
