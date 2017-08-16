package com.weibo.weibo;

import com.weibo.weibo.service.SearchService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class WeiboApplicationTests {

	@Autowired
	SearchService searchService;

	@Test
	public void contextLoads() {
		try {
			String key = "上山";
			searchService.search(key);
		}catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

}
