/*
 *    Copyright (c) 2018-2025, lengleng All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice,
 * this list of conditions and the following disclaimer.
 * Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 * Neither the name of the pig4cloud.com developer nor the names of its
 * contributors may be used to endorse or promote products derived from
 * this software without specific prior written permission.
 * Author: lengleng (wangiegie@gmail.com)
 */

package io.starskyoio.oss.spring.boot.autoconfigure;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * oss 自动配置类
 *
 * @author lengleng
 * @author 858695266
 * @author L.cm
 */
@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties({ OssProperties.class })
public class OssAutoConfiguration {

	/**
	 * OSS操作模板
	 * @return OSS操作模板
	 */
	@Bean
	@ConditionalOnMissingBean(OssTemplate.class)
	@ConditionalOnProperty(prefix = OssProperties.PREFIX, name = "enable", havingValue = "true", matchIfMissing = true)
	public OssTemplate ossTemplate(OssProperties properties) {
		return new OssTemplate(properties);
	}

	/**
	 * OSS客户端
	 * @param ossTemplate
	 * @param properties
	 * @return
	 */
	@Bean
	@ConditionalOnMissingBean(OssClient.class)
	@ConditionalOnProperty(prefix = OssProperties.PREFIX, name = "enable", havingValue = "true", matchIfMissing = true)
	public OssClient ossClient(OssTemplate ossTemplate, OssProperties properties) {
		return new OssClient(ossTemplate, properties);
	}
}
