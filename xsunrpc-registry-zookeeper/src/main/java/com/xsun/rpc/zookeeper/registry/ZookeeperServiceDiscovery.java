package com.xsun.rpc.zookeeper.registry;

import com.xsun.rpc.registry.ServiceDiscovery;
import org.I0Itec.zkclient.ZkClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.List;

/**
 * created at 20:40, 2019/3/23
 *
 * @author sunhaoran <nuaa_sunhr@yeah.net>
 */
public class ZookeeperServiceDiscovery implements ServiceDiscovery {

    private static final Logger LOG = LoggerFactory.getLogger(ZookeeperServiceDiscovery.class) ;
    private String zkAddress ;

    public ZookeeperServiceDiscovery(String zkAddress){
        this.zkAddress = zkAddress ;
    }

    @Override
    public String discover(String serviceName) {

        ZkClient zkClient = new ZkClient(zkAddress, Constant.ZK_CONNECT_TIMEOUT, Constant.ZK_SESSION_TIMEOUT) ;

        String servicePath = Constant.ZK_REGISTRY_PATH + "/" + serviceName ;

        if(!zkClient.exists(servicePath)){
            throw new RuntimeException(String.format("can not find any service node on path: %s", servicePath)) ;
        }

        List<String> addressList = zkClient.getChildren(servicePath) ;

        return null;
    }
}
