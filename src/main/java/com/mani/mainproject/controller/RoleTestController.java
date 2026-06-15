package com.mani.mainproject.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
    @RequestMapping("/api")
public class RoleTestController {


    @GetMapping("/admin/test")
    public String adminTest(){
        return "Admin Access";
    }

    @GetMapping("/manager/test")
    public String managerTest(){
        return "Manager Access";
    }

    @GetMapping("/employee/test")
    public String employeeTest(){
        return "Employee Access";
    }

    @GetMapping("/support/test")
    public String supportTest(){
        return "Support Access";
    }

}
