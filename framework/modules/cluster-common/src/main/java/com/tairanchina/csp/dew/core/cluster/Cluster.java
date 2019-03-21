/*
 * Copyright 2019. the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.tairanchina.csp.dew.core.cluster;

import com.ecfront.dew.common.$;
import com.ecfront.dew.common.DependencyHelper;
import com.tairanchina.csp.dew.core.cluster.ha.ClusterHA;
import com.tairanchina.csp.dew.core.cluster.ha.H2ClusterHA;
import com.tairanchina.csp.dew.core.cluster.ha.dto.HAConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * 集群服务
 */
public class Cluster {
    private static final Logger logger = LoggerFactory.getLogger(Cluster.class);

    private static Function<String, Map<String, Object>> _mqGetHeader;
    private static Consumer<Object[]> _mqSetHeader;
    private static ClusterHA clusterHA = null;
    private static String applicationName = "_default";
    public static String instanceId = $.field.createUUID();

    public static void init(String appName, String instId) {
        applicationName = appName;
        instanceId = instId;
    }

    public static void initMQHeader(Function<String, Map<String, Object>> mqGetHeader, Consumer<Object[]> mqSetHeader) {
        _mqGetHeader = mqGetHeader;
        _mqSetHeader = mqSetHeader;
    }

    public static void ha() {
        ha(new HAConfig());
    }

    public static void ha(HAConfig haConfig) {
        if (DependencyHelper.hasDependency("org.h2.jdbcx.JdbcConnectionPool")) {
            clusterHA = new H2ClusterHA();
        } else {
            logger.warn("Not found HA implementation drives , HA disabled.");
            return;
        }
        try {
            if (haConfig.getStoragePath() == null || haConfig.getStoragePath().isEmpty()) {
                haConfig.setStoragePath("./");
            } else {
                if (!haConfig.getStoragePath().endsWith("/")) {
                    haConfig.setStoragePath(haConfig.getStoragePath() + "/");
                }
            }
            if (haConfig.getStorageName() == null || haConfig.getStorageName().isEmpty()) {
                haConfig.setStorageName(applicationName);
            }
            clusterHA.init(haConfig);
            logger.info("HA initialized");
        } catch (SQLException e) {
            logger.error("HA init error", e);
        }
    }

    public static boolean haEnabled() {
        return clusterHA != null;
    }

    public static ClusterHA getClusterHA() {
        return clusterHA;
    }

    public static Map<String, Object> getMQHeader(String name) {
        if (_mqGetHeader != null) {
            return _mqGetHeader.apply(name);
        } else {
            return new HashMap<>();
        }
    }

    public static void setMQHeader(String name, Map<String, Object> header) {
        if (_mqSetHeader != null) {
            _mqSetHeader.accept(new Object[]{name, header});
        }
    }

    /**
     * MQ服务
     */
    public ClusterMQ mq;

    /**
     * 分布式锁服务
     */
    public ClusterLockWrap lock;

    /**
     * 分布式Map服务
     */
    public ClusterMapWrap map;

    /**
     * 缓存服务
     */
    public ClusterCache cache;

    /**
     * 领导者选举服务
     */
    public ClusterElectionWrap election;

}
