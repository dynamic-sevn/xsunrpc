package com.xsun.rpc.demo.server.impl;

import com.xsun.rpc.demo.api.HelloWorldService;
import com.xsun.rpc.server.XsunRpc;

/**
 * created at 20:40, 2019/3/23
 *
 * @author sunhaoran <nuaa_sunhr@yeah.net>
 */
@XsunRpc(HelloWorldService.class)
public class HelloWorldServiceImpl implements HelloWorldService {
    @Override
    public String hello(String name) {
        return name + "hello world !";
    }
}
