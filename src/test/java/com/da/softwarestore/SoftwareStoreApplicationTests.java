package com.da.softwarestore;

import com.da.softwarestore.configuration.DatabaseLoader;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SoftwareStoreApplicationTests {

	@Autowired
	private ApplicationContext applicationContext;

	@Test
	public void welcomeCommandLineRunnerShouldBeAvailable() {
		// Since we're a @SpringBootTest all beans should be available.
		assertThat(this.applicationContext.getBean(DatabaseLoader.class))
				.isNotNull();
	}

}
