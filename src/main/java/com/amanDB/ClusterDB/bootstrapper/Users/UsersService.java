package com.amanDB.ClusterDB.bootstrapper.Users;

import com.amanDB.ClusterDB.bootstrapper.FileManagment.DocumentReader;
import com.amanDB.ClusterDB.bootstrapper.FileManagment.DocumentWriter;
import com.amanDB.ClusterDB.bootstrapper.NodeManagment.Node;
import com.amanDB.ClusterDB.bootstrapper.NodeManagment.NodesService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


@Service
@Setter
@Getter
@NoArgsConstructor
public class UsersService {

    @Value("${numberOfNodes}")
    private int  numberOfNodes;
    private static final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
    private DocumentReader reader = DocumentReader.getDocumentReader();
    private DocumentWriter writer = DocumentWriter.getDocumentWriter();
    private NodesService nodesService = new NodesService();
    public void writeUser(User user) throws Exception {
        File databaseUsers = new File("DatabaseUsers");
        File[] databaseUsersFiles = databaseUsers.listFiles();
        assert databaseUsersFiles != null;
        int usersCount = databaseUsersFiles.length;
        user.setUserID(usersCount+1);
        user.setRole("USER");
        String path = "DatabaseUsers/" + user.getUsername() + ".json";
        String userJsonObj = new ObjectMapper().writeValueAsString(user);
        writer.write(path, userJsonObj);

        List<Node> nodeList = nodesService.getNodesList();
        for (Node node :
                nodeList) {
            try{
                sendUserToNode(node.getNodeURL(),user);
            }catch (Exception ex){
                System.out.println(user.getUsername()+" was not saved on Node "+ node.getNodeID());
            }
        }

    }

    public User buildUser(User user) {
        UserBuilder builder = new UserBuilder();
        User builtUser = builder
                .username(user.getUsername())
                .password(user.getPassword())
                .role(user.getRole()).build();
        return builtUser;
    }

    public static UserDetails getUserFromFiles(String username) throws Exception {
        DocumentReader documentReader = DocumentReader.getDocumentReader();
        String path = "DatabaseUsers/" + username + ".json";
        String userDocument;
        try {
            userDocument = documentReader.read(path);
        } catch (Exception ex) {
            throw new UsernameNotFoundException(username);
        }
        User user = new ObjectMapper().readValue(userDocument, User.class);
        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getUsername())
                .password(passwordEncoder.encode(user.getPassword()))
                .roles(user.getRole())
                .build();
    }

    public List<User> getPredefinedUsers(int nodeID) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        UserBuilder builder = new UserBuilder();
        String path = "DatabaseUsers";
        File usersFile = new File(path);
        File[] files = usersFile.listFiles();
        List<User> users = new ArrayList<>();
        if (files == null) {
            return users;
        }
        for (File file : files) {
            String userString = reader.read(path + "/" + file.getName());
            User user = objectMapper.readValue(userString,User.class);
            if( (user.getUserID() % numberOfNodes) == nodeID){
                users.add(user);
            }

        }
        return users;
    }


    public void sendUserToNode(String nodeURL,User user){
        HttpHeaders headers = new HttpHeaders();
        nodeURL += "/users";
        headers.setBasicAuth("aman", "12345");
        headers.setContentType(MediaType.APPLICATION_JSON);

        RestTemplate restTemplate  = new RestTemplate();
        HttpEntity<User> request =  new HttpEntity<>(user,headers);

        restTemplate.exchange(
                nodeURL,
                HttpMethod.POST,
                request,
                String.class);
    }
}
