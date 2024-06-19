package com.hetech.batch.anonymization.useranonymization.batch;

import com.hetech.batch.anonymization.useranonymization.processors.UserItemProcessor;
import com.hetech.batch.anonymization.useranonymization.utils.AESUtil;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Component;

import com.hetech.batch.anonymization.useranonymization.models.User;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.database.builder.JdbcCursorItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.BeanPropertyRowMapper;

import javax.crypto.SecretKey;
import javax.sql.DataSource;

@Component
public class UserAnonBatch {

    @Autowired
    private DataSource dataSource;

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private DataSourceTransactionManager transactionManager;


    @Bean
    public SecretKey secretKey() throws Exception {
        return AESUtil.generateKey();
    }

    @Bean
    public UserItemProcessor userItemProcessor(SecretKey secretKey) {
        return new UserItemProcessor(secretKey);
    }

    @Bean
    public Job anonymizationJob(UserItemProcessor userItemProcessor) {
        return new JobBuilder("anonymizationJob", jobRepository)
                .incrementer(new RunIdIncrementer())
                .flow(anonymizationStep(userItemProcessor))
                .end()
                .build();
    }

    public Step anonymizationStep(UserItemProcessor userItemProcessor) {
        return new StepBuilder("anonymizationStep", jobRepository)
                .<User, User>chunk(10, transactionManager)
                .reader(userItemReader())
                .processor(userItemProcessor)
                .writer(userItemWriter())
                .build();
    }

    public JdbcCursorItemReader<User> userItemReader() {
        return new JdbcCursorItemReaderBuilder<User>()
                .dataSource(dataSource)
                .name("userItemReader")
                .sql("SELECT * FROM users WHERE anonymized = FALSE")
                .rowMapper(new BeanPropertyRowMapper<>(User.class))
                .build();
    }

    public JdbcBatchItemWriter<User> userItemWriter() {
        return new JdbcBatchItemWriterBuilder<User>()
                .dataSource(dataSource)
                .sql("UPDATE users SET name = :name, email = :email, phone = :phone, address = :address, anonymized = TRUE WHERE id = :id")
                .beanMapped()
                .build();
    }

}
