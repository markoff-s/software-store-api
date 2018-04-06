package com.da.softwarestore;

import com.da.softwarestore.model.software.BigImage;
import com.da.softwarestore.model.software.Application;
import com.da.softwarestore.repository.BigImageRepository;
import com.da.softwarestore.repository.ApplicationRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Optional;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SoftwareStoreApplicationTests {

	@Autowired
	private ApplicationRepository applicationRepository;

	@Autowired
	private BigImageRepository bigImageRepository;

	@Test
	public void contextLoads() {

	}

	@Test
	public void	loadApplications() {
		List<Application> name = applicationRepository.findAll(new Sort(Sort.Direction.ASC, "name"));
	}

	@Test
	public void	findBigImage() {

		for (int i = 0; i < 200; i++) {
			Optional<BigImage> img = bigImageRepository.findById(Long.valueOf(i));

			if (img.isPresent())
				break;
		}

	}

}
