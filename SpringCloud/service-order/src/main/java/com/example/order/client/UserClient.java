package com.example.order.client;

import com.example.common.api.ApiResponse;
import com.example.common.dto.UserDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "service-user")
public interface UserClient {

    @GetMapping("/users/{id}")
    ApiResponse<UserDTO> getById(@PathVariable("id") Long id);
}
