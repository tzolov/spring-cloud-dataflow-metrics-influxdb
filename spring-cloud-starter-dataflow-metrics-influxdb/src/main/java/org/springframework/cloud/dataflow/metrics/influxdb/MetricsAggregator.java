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

import java.util.concurrent.TimeUnit;

import org.influxdb.InfluxDB;
import org.influxdb.dto.BatchPoints;
import org.influxdb.dto.Point;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.cloud.dataflow.metrics.influxdb.model.ApplicationMetrics;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Sink;
import org.springframework.stereotype.Component;

/**
 * Adds the incoming {@link ApplicationMetrics} payload into the backend store
 * @author Christian Tzolov
 */
@Component
public class MetricsAggregator {

	public static final String STREAM_TAG_KEY = "stream";
	public static final String APPLICATION_NAME_TAG_KEY = "applicationName";
	public static final String APPLICATION_GUID_TAG_KEY = "applicationGuid";
	public static final String SERVER_NAME_TAG_KEY = "serverName";
	public static final String APPLICATION_TYPE_TAG_KEY = "applicationType";
	public static final String INSTANCE_INDEX_TAG_KEY = "instanceIndex";
	private InfluxDB influxDB;
	private MetricInfluxDbProperties influxDbProperties;

	private Logger logger = LoggerFactory.getLogger(MetricsAggregator.class);

	public MetricsAggregator(InfluxDB influxDB, MetricInfluxDbProperties influxDbProperties) {
		this.influxDB = influxDB;
		this.influxDbProperties = influxDbProperties;
	}

	@StreamListener(Sink.INPUT)
	public void receive(ApplicationMetrics metrics) {
		if (metrics.getProperties().get(ApplicationMetrics.APPLICATION_GUID) != null
				&& metrics.getProperties().get(ApplicationMetrics.APPLICATION_NAME) != null
				&& metrics.getProperties().get(ApplicationMetrics.STREAM_NAME) != null) {

			Point.Builder point = Point.measurement(influxDbProperties.getMeasurementName())
					.tag(STREAM_TAG_KEY, (String) metrics.getProperties().get(ApplicationMetrics.STREAM_NAME))
					.tag(APPLICATION_NAME_TAG_KEY, (String) metrics.getProperties().get(ApplicationMetrics.APPLICATION_NAME))
					.tag(APPLICATION_GUID_TAG_KEY, (String) metrics.getProperties().get(ApplicationMetrics.APPLICATION_GUID))
					.tag(APPLICATION_TYPE_TAG_KEY, (String) metrics.getProperties().get(ApplicationMetrics.APPLICATION_TYPE))
					.tag(INSTANCE_INDEX_TAG_KEY, (String) metrics.getProperties().get(ApplicationMetrics.INSTANCE_INDEX))
					.time(metrics.getCreatedTime().getTime(), TimeUnit.MILLISECONDS);

			metrics.getMetrics().stream()
//					.filter(metric -> !metric.getName().matches("integration\\.channel\\.(\\w*)\\.(\\w*)\\.mean"))
//					.collect(Collectors.toList()).stream()
					.forEach(m -> point.addField(m.getName(), m.getValue()));

			BatchPoints batch = BatchPoints.database(influxDbProperties.getInfluxDatabase())
					.retentionPolicy(influxDbProperties.getInfluxRetentionPolicy())
					.consistency(influxDbProperties.getConsistencyLevel())
					.point(point.build()).build();

			influxDB.write(batch);
		}
		else {
			if (logger.isDebugEnabled()) {
				logger.debug("Metric : {} is missing key properties and will not be consumed by the collector", metrics.getName());
			}
		}
	}

}
