package com.xsun.rpc.registry;

/**
 * created at 20:35, 2019/3/23
 *
 * @author sunhaoran <nuaa_sunhr@yeah.net>
 */
public interface ServiceDiscovery {
    String discover(String serviceName) ;
}
