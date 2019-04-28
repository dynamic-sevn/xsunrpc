package com.xsun.rpc.demo.client;

import com.xsun.rpc.client.annotation.Reference;
import com.xsun.rpc.demo.api.HelloWorldService;
import org.springframework.stereotype.Component;

/**
 * created at 20:40, 2019/3/23
 *
 * @author sunhaoran <nuaa_sunhr@yeah.net>
 */
@Component
public class BootstrapBean {
    @Reference
    private HelloWorldService helloWorldService ;

    public String hello(String name){
        return helloWorldService.hello(name) ;
    }
}
