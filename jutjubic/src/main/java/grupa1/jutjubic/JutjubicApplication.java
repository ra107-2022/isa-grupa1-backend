package grupa1.jutjubic;

import org.modelmapper.ModelMapper;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.CommandLineRunner;

@SpringBootApplication
@EnableCaching
@EnableScheduling
@EnableRabbit
public class JutjubicApplication {

	@Bean
	public ModelMapper getModelMapper() {
		return new ModelMapper();
	}

	public static void main(String[] args) {
		SpringApplication.run(JutjubicApplication.class, args);
	}

	@Bean
	public CacheManager cacheManager() {
		return new ConcurrentMapCacheManager("videoComments");
	}

	@Bean
	public CommandLineRunner testRabbit(RabbitTemplate rabbitTemplate) {
		return args -> {
			System.out.println("Testing RabbitMQ connection...");
			System.out.println(rabbitTemplate.getConnectionFactory().createConnection());
		};
	}
}
