package com.amanDB.ClusterDB.bootstrapper.Users;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/usersConfig")
public class UserController {

    public final UsersService usersService;

    @Autowired
    public UserController(UsersService usersService) {
        this.usersService = usersService;
    }

    @GetMapping
    public List<User> getPredefinedUsers(@RequestParam(value = "nodeID") int nodeID ) throws Exception {
       return usersService.getPredefinedUsers(nodeID);
    }

//    @PutMapping
//    public void WriteUsers(@RequestBody List<User> users) throws Exception {
//        for (User user :
//                users) {
//            usersService.writeUser(user);
//        }
//    }

    @PostMapping
    public void registerNewUser(@RequestBody User user) throws Exception {
        usersService.writeUser(user);
    }


}
