package com.xzy.xframe.gateway.common.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author: xzy
 * @create: 2020-08-06
 **/

@Data
//@Component
//@ConfigurationProperties(prefix = "url")
public class WBUrlListProperties {

    private Boolean whiteEnable = false;

    private Boolean blackEnable = false;

    private List<String> blacklist;

    private List<String> whitelist;

}
