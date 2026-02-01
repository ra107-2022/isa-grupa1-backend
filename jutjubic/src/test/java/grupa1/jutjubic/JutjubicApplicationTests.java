package grupa1.jutjubic;

import grupa1.jutjubic.model.User;
import grupa1.jutjubic.service.IUserService;
import grupa1.jutjubic.service.impl.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.mockito.Mockito.when;
import org.springframework.boot.test.mock.web.SpringBootMockServletContext;
//import org.springframework.boot.test.mock.mockito.MockBean;




@SpringBootTest
@AutoConfigureMockMvc
class JutjubicApplicationTests {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private IUserService userService;

	@Test
	void spamKomentaraTest() throws Exception {

		for (int i = 0; i < 60; i++) {
			mockMvc.perform(
					post("/api/comments/video/1")
							.contentType(MediaType.APPLICATION_JSON)
							.content("{\"content\":\"Spam komentar " + i + "\"}")
							.with(user("testuser"))
			).andExpect(status().isOk());
		}

		mockMvc.perform(
				post("/api/comments/video/1")
						.contentType(MediaType.APPLICATION_JSON)
						.content("{\"content\":\"Spam komentar 60\"}")
						.with(user("testuser"))
		).andExpect(status().isTooManyRequests());

	}
}