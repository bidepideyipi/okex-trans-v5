package com.supermancell.okex.trans.trader.redis;

import com.supermancell.okex.trans.trader.enums.Prediction;
import com.supermancell.okex.trans.trader.model.Signal;
import com.supermancell.okex.trans.trader.service.OkxGridTransService;
import com.supermancell.okex.trans.trader.service.OkxTransService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.connection.stream.Consumer;
import org.springframework.data.redis.connection.stream.MapRecord;
import org.springframework.data.redis.connection.stream.ReadOffset;
import org.springframework.data.redis.connection.stream.StreamOffset;
import org.springframework.data.redis.connection.stream.StreamReadOptions;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.math.BigDecimal;
import java.time.Duration;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Component
public class RedisStreamConsumer {

    private static final Logger log = LoggerFactory.getLogger(RedisStreamConsumer.class);

    private static final String STREAM_KEY = "signals";
    private static final String GROUP_NAME = "gp";
    private static final String CONSUMER_NAME = "consumer-1";

    private volatile long lastProcessedId = 0L;

    private final StringRedisTemplate redisTemplate;

    private final OkxGridTransService okxTransService;

    private ExecutorService executorService;
    private Thread consumerThread;

    public RedisStreamConsumer(StringRedisTemplate redisTemplate, OkxGridTransService okxTransService) {
        this.redisTemplate = redisTemplate;
        this.okxTransService = okxTransService;
    }

    @PostConstruct
    public void init() {
        executorService = Executors.newSingleThreadExecutor();
        consumerThread = new Thread(this::startConsuming);
        executorService.submit(consumerThread);
    }

    private void startConsuming() {
        try {
            // 创建消费者组（如果不存在）
            try {
                redisTemplate.opsForStream().createGroup(STREAM_KEY, GROUP_NAME);
                log.info("Created Redis Stream consumer group: {}", GROUP_NAME);
            } catch (Exception e) {
                // 组已存在，忽略异常
                log.info("Redis Stream consumer group already exists: {}", GROUP_NAME);
            }

            // 从消费者组的最后一个位置开始消费
            StreamOffset<String> offset = StreamOffset.create(STREAM_KEY, ReadOffset.lastConsumed());
            Consumer consumer = Consumer.from(GROUP_NAME, CONSUMER_NAME);

            log.info("Starting Redis Stream consumer for stream: {}, group: {}", STREAM_KEY, GROUP_NAME);

            while (!consumerThread.isInterrupted()) {
                try {
                    // 读取消息，阻塞100毫秒
                    StreamReadOptions options = StreamReadOptions.empty().block(Duration.ofMillis(100));
                    List<MapRecord<String, Object, Object>> records = redisTemplate.opsForStream()
                            .read(consumer, options, offset);

                    if (records != null && !records.isEmpty()) {
                        for (MapRecord<String, Object, Object> record : records) {
                            // 处理消息
                            processMessage(record);
                        }
                    }
                } catch (Exception e) {
                    log.error("Error consuming Redis Stream messages", e);
                    // 短暂休眠后继续
                    Thread.sleep(1000);
                }
            }
        } catch (Exception e) {
            log.error("Unexpected error in Redis Stream consumer", e);
        } finally {
            if (executorService != null) {
                executorService.shutdown();
            }
        }
    }

    private void processMessage(MapRecord<String, Object, Object> record) {
        try {
            String messageId = record.getId().getValue();
            long messageTime =  Long.parseLong(messageId.split("-")[0]);

            if (messageTime < System.currentTimeMillis() -  1000 * 60) {
                // 超过1分钟的消息ACK掉
                redisTemplate.opsForStream().acknowledge(GROUP_NAME, record);
                return;
            }

            if(messageTime > lastProcessedId){
                lastProcessedId = messageTime;
            } else {
                //更旧的信息ACK掉
                redisTemplate.opsForStream().acknowledge(GROUP_NAME, record);
                return;
            }

            String stream = record.getStream();
            java.util.Map<?, ?> message = record.getValue();

            log.info("Received message from stream {}: id={}, content={}", stream, messageId, message);

            // 这里可以添加具体的业务逻辑处理
            // 例如：解析消息内容，调用交易API等
            Signal signal = Converter.mapToSignal(message, messageId);
            // 确认消息处理完成（可选，根据业务需求）
            // redisTemplate.opsForStream().acknowledge(GROUP_NAME, record);
//            String signalType = signal.getPrediction();
//            BigDecimal signalProbability = signal.getProbability().get(signalType);

//            if(signal.getPrediction().equals(Prediction.MOVING_SIDEWAYS.getCode())){
//                //当Prediction为横盘时，要多看一眼。看PredictionHigh和PredictionLow是否为横盘
//                if(!signal.getPredictionHigh().equals(Prediction.HIGH_MOVING_SIDEWAYS.getCode()) ){
//                    String highSignalType = signal.getPredictionHigh();
//                    BigDecimal highSignalProbability = signal.getProbabilityHigh().get(highSignalType);
//                    if(highSignalProbability.compareTo(signalProbability) > 0){
//                        signalType = (Integer.parseInt(highSignalType) +2) +"";
//                        signalProbability = highSignalProbability;
//                    }
//                }
//
//                if(!signal.getPredictionLow().equals(Prediction.LOW_MOVING_SIDEWAYS.getCode())){
//                    String lowSignalType = signal.getPredictionLow();
//                    BigDecimal lowSignalProbability = signal.getProbabilityLow().get(lowSignalType);
//                    if(lowSignalProbability.compareTo(signalProbability) > 0){
//                        signalType = lowSignalType;
//                        signalProbability = lowSignalProbability;
//                    }
//                }

//               if(signalType.equals("3")){
//                   redisTemplate.opsForStream().acknowledge(GROUP_NAME, record);
//                   return;
//               }
//            }

            //TODO 交易触发
            boolean success = okxTransService.acceptSign(signal);
            if(success){
                redisTemplate.opsForStream().acknowledge(GROUP_NAME, record);
            }

        } catch (Exception e) {
            log.error("Error processing Redis Stream message", e);
        }
    }

    @PreDestroy
    public void destroy() {
        log.info("Shutting down Redis Stream consumer...");
        
        // 中断消费者线程
        if (consumerThread != null && consumerThread.isAlive()) {
            consumerThread.interrupt();
            log.info("Interrupted Redis Stream consumer thread");
        }
        
        // 关闭线程池
        if (executorService != null && !executorService.isShutdown()) {
            executorService.shutdownNow();
            log.info("Shutdown Redis Stream consumer executor service");
        }
        
        log.info("Redis Stream consumer shutdown completed");
    }

}
