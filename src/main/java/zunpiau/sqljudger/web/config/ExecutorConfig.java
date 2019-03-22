package zunpiau.sqljudger.web.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.task.TaskExecutionProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.task.TaskExecutorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
public class ExecutorConfig {

    private static ThreadPoolTaskExecutor build(TaskExecutionProperties properties) {
        TaskExecutionProperties.Pool pool = properties.getPool();
        TaskExecutorBuilder builder = new TaskExecutorBuilder();
        builder = builder.queueCapacity(pool.getQueueCapacity())
                .corePoolSize(pool.getCoreSize())
                .maxPoolSize(pool.getMaxSize())
                .allowCoreThreadTimeOut(pool.isAllowCoreThreadTimeout())
                .keepAlive(pool.getKeepAlive())
                .threadNamePrefix(properties.getThreadNamePrefix());
        return builder.build();
    }

    @Bean("examExecutionProperties")
    @ConfigurationProperties("judger.task.exam")
    public TaskExecutionProperties examExecutionProperties() {
        return new TaskExecutionProperties();
    }

    @Bean("answersheetExecutionProperties")
    @ConfigurationProperties("judger.task.answersheet")
    public TaskExecutionProperties answersheetExecutionProperties() {
        return new TaskExecutionProperties();
    }

    @Bean("postExecutionProperties")
    @ConfigurationProperties("judger.task.post")
    public TaskExecutionProperties postExecutionProperties() {
        return new TaskExecutionProperties();
    }

    @Bean("examExecutor")
    public ThreadPoolTaskExecutor examExecutor(
            @Qualifier("examExecutionProperties") TaskExecutionProperties properties) {
        return build(properties);
    }

    @Bean("answersheetExecutor")
    public ThreadPoolTaskExecutor answersheetExecutor(
            @Qualifier("answersheetExecutionProperties") TaskExecutionProperties properties) {
        return build(properties);
    }

    @Bean("postExecutor")
    public ThreadPoolTaskExecutor postExecutor(
            @Qualifier("postExecutionProperties") TaskExecutionProperties properties) {
        return build(properties);
    }

}
