package com.xsun.rpc.registry;

/**
 * created at 19:31, 2019/3/23
 *
 * @author sunhaoran <nuaa_sunhr@yeah.net>
 */
public interface ServiceReigistry {
    void registry(String serviceName, String serviceAddress) ;
}
