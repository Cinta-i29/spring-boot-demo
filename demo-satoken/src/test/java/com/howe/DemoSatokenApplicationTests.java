package com.howe;

import cn.dev33.satoken.stp.StpUtil;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class DemoSatokenApplicationTests {

	@Test
	void contextLoads() {
		StpUtil.login(1001);
	}

}
