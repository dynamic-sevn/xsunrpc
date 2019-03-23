package com.xsun.rpc.zookeeper.registry;

import com.xsun.rpc.registry.ServiceReigistry;
import org.I0Itec.zkclient.ZkClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * created at 20:39, 2019/3/23
 *
 * @author sunhaoran <nuaa_sunhr@yeah.net>
 */
public class ZookeeperServiceRegistry implements ServiceReigistry {
    private static final Logger LOG = LoggerFactory.getLogger(ZookeeperServiceRegistry.class) ;

    private final ZkClient zkClient ;

    public ZookeeperServiceRegistry(String clientAddress){
        this.zkClient = new ZkClient(clientAddress) ;
    }

    @Override
    public void registry(String serviceName, String serviceAddress) {

    }
}
