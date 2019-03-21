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

package com.tairanchina.csp.dew.kernel.resource;

import com.tairanchina.csp.dew.helper.KubeHelper;
import com.tairanchina.csp.dew.kernel.Dew;
import com.tairanchina.csp.dew.kernel.config.FinalConfig;
import io.kubernetes.client.models.V1ConfigMap;
import io.kubernetes.client.models.V1ConfigMapBuilder;
import io.kubernetes.client.models.V1ObjectMetaBuilder;

import java.util.Map;

public class KubeConfigMapBuilder implements KubeResourceBuilder<V1ConfigMap> {

    @Override
    public V1ConfigMap build(FinalConfig config) {
        return null;
    }

    public V1ConfigMap build(String name, Map<String, String> labels, Map<String, String> data) {
        V1ConfigMapBuilder builder = new V1ConfigMapBuilder();
        builder.withKind(KubeHelper.RES.CONFIG_MAP.getVal())
                .withApiVersion("v1")
                .withData(data)
                .withMetadata(new V1ObjectMetaBuilder()
                        .withName(name)
                        .withNamespace(Dew.config.getNamespace())
                        .withLabels(labels)
                        .build())
                .build();
        return builder.build();
    }

}
