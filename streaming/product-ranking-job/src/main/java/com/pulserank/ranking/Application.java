package com.pulserank.ranking;

import com.pulserank.ranking.config.JobConfiguration;
import com.pulserank.ranking.job.ProductScoreJob;
import org.apache.flink.api.common.ExecutionConfig;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

public class Application {

    public static void main(String[] args) throws Exception {

        StreamExecutionEnvironment environment =
                StreamExecutionEnvironment.getExecutionEnvironment();
        ExecutionConfig config = environment.getConfig();


        new ProductScoreJob().execute(environment);

        environment.execute(JobConfiguration.JOB_NAME);
    }
}