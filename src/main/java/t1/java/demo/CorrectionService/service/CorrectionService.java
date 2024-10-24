package t1.java.demo.CorrectionService.service;

import org.springframework.kafka.annotation.KafkaListener;

public interface CorrectionService {
    @KafkaListener(topics = "t1_demo_client_transaction_errors", groupId = "group_id")
    void handleError(Long transactionId);
}
