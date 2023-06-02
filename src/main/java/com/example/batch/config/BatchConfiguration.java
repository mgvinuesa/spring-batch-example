package com.example.batch.config;

import java.util.HashMap;
import java.util.Map;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.data.RepositoryItemReader;
import org.springframework.batch.item.data.RepositoryItemWriter;
import org.springframework.batch.item.data.builder.RepositoryItemReaderBuilder;
import org.springframework.batch.item.data.builder.RepositoryItemWriterBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.transaction.PlatformTransactionManager;

import com.example.batch.listener.JobCompletionNotificationListener;
import com.example.batch.processor.CoffeeItemProcessor;
import com.example.batch.repository.CoffeeRepository;
import com.example.batch.repository.entity.Coffee;

@Configuration
public class BatchConfiguration {

	@Autowired
	private CoffeeRepository coffeeRepository;

	@Bean
	public Job importUserJob(JobRepository jobRepository, JobCompletionNotificationListener listener, Step step1) {
		return new JobBuilder("importUserJob", jobRepository).listener(listener).incrementer(new RunIdIncrementer())
				.flow(step1).end().build();
	}

	@Bean
	public Step step1(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
		return new StepBuilder("step1", jobRepository).<Coffee, Coffee>chunk(1, transactionManager)
				.reader(repositoryReader()).processor(processor()).writer(repositoryWriter()).build();
	}

	@Bean
	public RepositoryItemReader repositoryReader() {
		Map<String, Direction> sortMap = new HashMap<>();
		sortMap.put("brand", Direction.ASC);
		return new RepositoryItemReaderBuilder<com.example.batch.repository.entity.Coffee>()
				.name("RepositoryItemReader").repository(this.coffeeRepository).pageSize(1).sorts(sortMap)
				.methodName("findAll").build();
	}

	@Bean
	public RepositoryItemWriter repositoryWriter() {
		return new RepositoryItemWriterBuilder<com.example.batch.repository.entity.Coffee>()
				.repository(this.coffeeRepository).methodName("save").build();
	}

	@Bean
	public CoffeeItemProcessor processor() {
		return new CoffeeItemProcessor();
	}
}
