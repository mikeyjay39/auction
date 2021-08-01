package com.example.auction.test;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestFactory;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.Duration;

import static org.junit.Assert.assertTrue;

@SpringBootTest
@RunWith(SpringRunner.class)
@Testcontainers
public class ElasticsearchTestFactory extends PostgresFactory {

	private static final String dockerImageName = "docker.elastic.co/elasticsearch/elasticsearch:7.13.4";

	@Container
	private static AuctionElasticsearchContainer container =
			new AuctionElasticsearchContainer(dockerImageName);

	@BeforeAll
	static void beforeAll() {
		container.start();
		container.setWaitStrategy(Wait.forHttp("/")
				.forPort(9200)
				.forStatusCode(200)
				.withStartupTimeout(Duration.ofSeconds(60)));
	}

	@BeforeEach
	void testIsContainerRunning() {
		assertTrue(container.isRunning());
	}

	@AfterAll
	static void destroy() {
		container.stop();
	}
}
