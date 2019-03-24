package com.xsun.rpc.zookeeper.registry;

import com.xsun.rpc.registry.ServiceRegistry;
import org.I0Itec.zkclient.ZkClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * created at 20:39, 2019/3/23
 *
 * @author sunhaoran <nuaa_sunhr@yeah.net>
 */
public class ZookeeperServiceRegistry implements ServiceRegistry {
    private static final Logger LOG = LoggerFactory.getLogger(ZookeeperServiceRegistry.class) ;

    private final ZkClient zkClient ;

    public ZookeeperServiceRegistry(String clientAddress){
        this.zkClient = new ZkClient(clientAddress) ;
    }

    @Override
    public void registry(String serviceName, String serviceAddress) {

        String registryPath = Constant.ZK_REGISTRY_PATH ;

        if(!zkClient.exists(registryPath)){
            zkClient.createPersistent(registryPath);
        }

        String servicePath = registryPath + "/" +serviceName ;
        if(!zkClient.exists(servicePath)){
            zkClient.createPersistent(servicePath);
        }

        String addressPath = servicePath + "/address-" ;
        zkClient.createEphemeralSequential(addressPath, serviceAddress) ;
    }
}
