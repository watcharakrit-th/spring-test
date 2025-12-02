package com.app.my_project;

// import org.junit.jupiter.api.Test;
// import org.springframework.boot.test.context.SpringBootTest;

// @SpringBootTest
// class MyProjectApplicationTests {

// 	@Test
// 	void contextLoads() {
// 	}

// }

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

// Static imports make the code readable (copy these!)
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;

@SpringBootTest
@AutoConfigureMockMvc // <--- 1. Enables the fake HTTP client
@Transactional // <--- 2. Rolls back DB changes after each test
class MyProjectApplicationTests {

	@Autowired
	private MockMvc mockMvc; // <--- 3. Inject the tool

	// ... Tests go here ...
	@Test
	void testGetCharacters() throws Exception {
		// Perform a GET request to /characters
		mockMvc.perform(get("/myOwnNextjsWeb")

				.param("page", "0")
				.param("size", "10")
				.param("direction", "ASC")
				.param("key", "")) // Query Params

				// Validate the Response
				.andExpect(status().isOk()) // Expect HTTP 200
				.andExpect(jsonPath("$.content", hasSize(greaterThan(0)))) // Check list is not empty
				.andExpect(jsonPath("$.content[0].name").exists()) // Check first item has a name
				.andExpect(jsonPath("$.sort.sorted").value(true)); // Check sort metadata
	}
}