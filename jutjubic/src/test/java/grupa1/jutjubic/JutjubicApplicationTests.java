package grupa1.jutjubic;

import grupa1.jutjubic.model.enums.ActivityType;
import grupa1.jutjubic.service.IActivityService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static grupa1.jutjubic.model.enums.ActivityType.VIEW;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;


@SpringBootTest
@AutoConfigureMockMvc
class JutjubicApplicationTests {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private IActivityService activityService;

	@Test
	@Transactional
	void spamKomentaraTest() throws Exception {
		// test prikatuje ponasanje kada korisnik pokusa da okaci
		// vise od 60 komentara u periodu od sat vrememe

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

	@Test
	@Transactional
	void trendingSimulatorTest() {
		// Test proverava da li video sa mnogo vise aktivnosti
		// bude rangiran kao prvi u trending listi za datu lokaciju
		// podrucje u kom proveravamo trending je u ovom slucaju manje

		double lon = 20.460;
		double lat = 44.810;
		double radius = 200; // radijus pretrage u kom gledamo trending

		// video 1 – puno aktivnosti
		for (int i = 0; i < 50; i++) {
			activityService.logActivity(1L, null, ActivityType.LIKE, lon, lat);
			activityService.logActivity(1L, null, ActivityType.VIEW, lon, lat);
		}

		// video 2 – malo aktivnosti
		for (int i = 0; i < 10; i++) {
			activityService.logActivity(2L, null, VIEW, lon, lat);
		}

		var trending = activityService.getTrendingVideos(lon, lat, radius, 2);

		assertEquals(1L, trending.get(0));
	}


	@Test
	@Transactional
	void trendingDistributedCitiesTest() {

		// test proverava aktivnosti u razlicitim gradovima
		// potvrdjeno je da se lokalni trending racuna u zavisnosti
		// od geografske lokacije zahteva i da aktivnosti iz
		// drugih gradova ne utiču na rezultat


		// Beograd
		double bgLon = 20.460;
		double bgLat = 44.810;

		// Novi Sad
		double nsLon = 19.833;
		double nsLat = 45.267;

		double radius = 1000; // 1km

		// aktivnosti u Beogradu – video 1
		for (int i = 0; i < 30; i++) {
			activityService.logActivity(1L, null, ActivityType.VIEW, bgLon, bgLat);
			activityService.logActivity(1L, null, ActivityType.LIKE, bgLon, bgLat);
		}

		// aktivnosti u Novom Sadu – video 2
		for (int i = 0; i < 30; i++) {
			activityService.logActivity(2L, null, ActivityType.VIEW, nsLon, nsLat);
			activityService.logActivity(2L, null, ActivityType.LIKE, nsLon, nsLat);
		}

		// trending za Beograd
		var bgTrending =
				activityService.getTrendingVideos(bgLon, bgLat, radius, 5);

		assertTrue(bgTrending.contains(1L));
		assertFalse(bgTrending.contains(2L));
	}

}



