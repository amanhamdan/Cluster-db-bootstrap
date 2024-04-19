package com.amanDB.ClusterDB.bootstrapper;

import com.amanDB.ClusterDB.bootstrapper.NodeManagment.NodesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ClusterDbBootstrapperApplication {

	private static NodesService nodesService;

	@Autowired
	public ClusterDbBootstrapperApplication(NodesService nodesService ){
		this.nodesService = nodesService;
	}

	public static void main(String[] args) throws Exception {
		SpringApplication.run(ClusterDbBootstrapperApplication.class, args);
		nodesService.writeNodesFromProperties();
	}

}
