package com.jeroenreijn.examples.controller;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.ui.ModelMap;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class PresentationsControllerTest {

	@Autowired
	private PresentationsController controller;
	private ModelMap modelMap;

	@BeforeEach
	public void setUp() throws Exception {
		modelMap = new ModelMap();		
	}

	@Test
	void should_return_jsp_view() throws Exception {
		String view = controller.home(modelMap);
		assertEquals("index-jsp", view);
	}

	@Test
	void should_return_other_view() throws Exception {
		final String view = controller.showList("test", modelMap);
		assertEquals("index-test", view);
	}

}
