package com.xsunrpc.demo.rpc.client;

import com.xsun.rpc.demo.client.BootstrapBean;
import org.junit.Test;

import javax.annotation.Resource;

/**
 * created at 20:40, 2019/3/23
 *
 * @author sunhaoran <nuaa_sunhr@yeah.net>
 */
public class BootStrapTest extends AbstractTest {

    @Resource
    private BootstrapBean bootstrapBean ;

    @Test
    public void helloTest(){
        bootstrapBean.hello("yejiawen") ;
    }
}
