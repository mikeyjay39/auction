package com.example.auction.test;

import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.TestPropertySourceUtils;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.ArrayList;
import java.util.List;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@RunWith(SpringJUnit4ClassRunner.class)
@Testcontainers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class PostgresFactory {


	public static PostgreSQLContainer<?> postgreDBContainer = new PostgreSQLContainer<>("postgres:11.8");

	static {
		List<String> ports = new ArrayList<>();
		ports.add("5432:5432");
		postgreDBContainer.setPortBindings(ports);
		postgreDBContainer.withDatabaseName("postgres");
		postgreDBContainer.withUsername("postgres");
		postgreDBContainer.withPassword("password");
		postgreDBContainer.start();

	}

	public static class DockerPostgreDataSourceInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

		@Override
		public void initialize(ConfigurableApplicationContext applicationContext) {

			TestPropertySourceUtils.addInlinedPropertiesToEnvironment(
					applicationContext,
					"spring.datasource.url=" + postgreDBContainer.getJdbcUrl(),
					"spring.datasource.username=" + postgreDBContainer.getUsername(),
					"spring.datasource.password=" + postgreDBContainer.getPassword()
			);
		}
	}

}
