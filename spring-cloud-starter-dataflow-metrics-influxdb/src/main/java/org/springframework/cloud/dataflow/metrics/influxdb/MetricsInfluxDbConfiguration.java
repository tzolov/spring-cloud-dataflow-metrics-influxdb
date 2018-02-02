/*
 * Copyright 2018 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.cloud.dataflow.metrics.influxdb;

import org.influxdb.InfluxDB;
import org.influxdb.InfluxDBFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.dataflow.metrics.influxdb.support.MetricJsonSerializer;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.messaging.Sink;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Christian Tzolov
 */
@Configuration
@EnableBinding(Sink.class)
@EnableConfigurationProperties(MetricInfluxDbProperties.class)
public class MetricsInfluxDbConfiguration {

	@Autowired
	private MetricInfluxDbProperties properties;

	@Bean
	public MetricJsonSerializer jsonSerializer() {
		return new MetricJsonSerializer();
	}

	@Bean
	public MetricsAggregator metricsAggregator(InfluxDB influxDB, MetricInfluxDbProperties properties) {
		return new MetricsAggregator(influxDB, properties);
	}

	@Bean
	public InfluxDB influxDB() {
		InfluxDB influxDB = InfluxDBFactory.connect(properties.getInfluxUrl(),
				properties.getInfluxUser(), properties.getInfluxPassword());
		influxDB.setLogLevel(properties.getInfluxLogLevel());
		influxDB.setRetentionPolicy(properties.getInfluxRetentionPolicy());
		influxDB.setConsistency(properties.getConsistencyLevel());
		influxDB.setDatabase(properties.getInfluxDatabase());
		return influxDB;
	}

}
