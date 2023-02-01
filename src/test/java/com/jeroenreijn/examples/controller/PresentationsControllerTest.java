package com.jeroenreijn.examples.controller;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.ui.ModelMap;

@RunWith(SpringRunner.class)
@SpringBootTest
public class PresentationsControllerTest {

	@Autowired
	private PresentationsController controller;
	private ModelMap modelMap;

	@Before
	public void setUp() throws Exception {
		modelMap = new ModelMap();		
	}

	@Test
	public void should_return_jsp_view() throws Exception {
		String view = controller.home(modelMap);
		assertEquals("index-jsp", view);
	}

	@Test
	public void should_return_other_view() throws Exception {
		final String view = controller.showList("test", modelMap);
		assertEquals("index-test", view);
	}

}
