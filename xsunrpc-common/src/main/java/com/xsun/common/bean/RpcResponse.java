package com.xsun.common.bean;

import lombok.Data;

/**
 * created at 20:40, 2019/3/23
 *
 * @author sunhaoran <nuaa_sunhr@yeah.net>
 */
@Data
public class RpcResponse {
    private Object result ;

    private Exception exception ;
}
