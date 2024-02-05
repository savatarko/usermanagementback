package org.raf.usermanagement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@SpringBootApplication
public class UsermanagementApplication {

	public static void main(String[] args) {
		SpringApplication.run(UsermanagementApplication.class, args);
	}


	//@Bean(name = "threadPoolTaskExecutor")
	//public Executor asyncExecutor() {
	//	ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
	//	executor.setCorePoolSize(4);
	//	executor.setMaxPoolSize(4);
	//	executor.setQueueCapacity(50);
	//	executor.setThreadNamePrefix("AsynchThread::");
	//	executor.initialize();
	//	return executor;
	//}
}
