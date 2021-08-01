package com.example.auction.test;

import org.testcontainers.elasticsearch.ElasticsearchContainer;

public class AuctionElasticsearchContainer extends ElasticsearchContainer {

	private static final String CLUSTER_NAME = "cluster.name";

	private static final String ELASTIC_SEARCH = "elasticsearch";

	public AuctionElasticsearchContainer(String dockerImageName) {
		super(dockerImageName);
		this.addFixedExposedPort(9200, 9200);
		this.addFixedExposedPort(9300, 9300);
		this.addEnv(CLUSTER_NAME, ELASTIC_SEARCH);
	}
}
