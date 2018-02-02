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

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author Christian Tzolov
 */
@ConfigurationProperties(prefix = "spring.cloud.dataflow.metrics.influxdb")
public class MetricInfluxDbProperties {

	private String influxDatabase = "SpringMetrics";
	private String influxUrl = "http://localhost:8086";
	private String influxUser = "admin";
	private String influxPassword = "admin";
	private String influxRetentionPolicy = "autogen";
	private InfluxDB.LogLevel influxLogLevel = InfluxDB.LogLevel.NONE;
	private InfluxDB.ConsistencyLevel consistencyLevel = InfluxDB.ConsistencyLevel.ALL;
	private String measurementName = "SpringCloudStream";

	public String getInfluxUrl() {
		return influxUrl;
	}

	public void setInfluxUrl(String influxUrl) {
		this.influxUrl = influxUrl;
	}

	public String getInfluxUser() {
		return influxUser;
	}

	public void setInfluxUser(String influxUser) {
		this.influxUser = influxUser;
	}

	public String getInfluxPassword() {
		return influxPassword;
	}

	public void setInfluxPassword(String influxPassword) {
		this.influxPassword = influxPassword;
	}

	public String getInfluxDatabase() {
		return influxDatabase;
	}

	public void setInfluxDatabase(String influxDatabase) {
		this.influxDatabase = influxDatabase;
	}

	public String getInfluxRetentionPolicy() {
		return influxRetentionPolicy;
	}

	public void setInfluxRetentionPolicy(String influxRetentionPolicy) {
		this.influxRetentionPolicy = influxRetentionPolicy;
	}

	public InfluxDB.ConsistencyLevel getConsistencyLevel() {
		return consistencyLevel;
	}

	public void setConsistencyLevel(InfluxDB.ConsistencyLevel consistencyLevel) {
		this.consistencyLevel = consistencyLevel;
	}

	public String getMeasurementName() {
		return measurementName;
	}

	public void setMeasurementName(String measurementName) {
		this.measurementName = measurementName;
	}

	public InfluxDB.LogLevel getInfluxLogLevel() {
		return influxLogLevel;
	}

	public void setInfluxLogLevel(InfluxDB.LogLevel influxLogLevel) {
		this.influxLogLevel = influxLogLevel;
	}
}
