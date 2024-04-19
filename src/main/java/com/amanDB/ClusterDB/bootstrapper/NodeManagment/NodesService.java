package com.amanDB.ClusterDB.bootstrapper.NodeManagment;

import com.amanDB.ClusterDB.bootstrapper.FileManagment.DocumentReader;
import com.amanDB.ClusterDB.bootstrapper.FileManagment.DocumentRemover;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.amanDB.ClusterDB.bootstrapper.FileManagment.DocumentWriter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Service
public class NodesService {

    @Value("${nodesList}")
    private List<String> nodeFromPropertiesList;
    private DocumentReader reader = DocumentReader.getDocumentReader();
    private DocumentWriter writer = DocumentWriter.getDocumentWriter();
    private DocumentRemover remover = DocumentRemover.getDocumentRemover();
    public NodesService() {
    }
    public  List<Node> getNodesList() throws Exception {
        String nodesJsonArray = reader.read("ClusterNodes");
        TypeReference<List<Node>> mapType = new TypeReference<List<Node>>() {};
        return new  ObjectMapper().readValue(nodesJsonArray, mapType);
    }

    public void writeNodesFromProperties() throws Exception {
        int nodeIdCounter = 0;
        List<Node> nodesList = new ArrayList<>();
        ObjectMapper objectMapper = new ObjectMapper();
        for (String node:
                nodeFromPropertiesList) {
            nodesList.add(new Node(nodeIdCounter , node));
            nodeIdCounter++;
        }
        String listToJson = objectMapper.writeValueAsString(nodesList);
        System.out.println(listToJson);
        File nodesFile = new File("ClusterNodes");
        if(nodesFile.exists()){
            remover.remove("ClusterNodes");
        }
        writer.write("ClusterNodes" ,listToJson);
    }

}
