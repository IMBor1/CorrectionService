package t1.java.demo.CorrectionService.service;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import t1.java.demo.CorrectionService.model.FailedTransaction;
import t1.java.demo.CorrectionService.repository.FailedTransactionRepository;

@AllArgsConstructor
@Service
public class CorrectionServiceImpl implements CorrectionService {

    private RestTemplate restTemplate;

    private FailedTransactionRepository failedTransactionRepository;

    @Override
    @KafkaListener(topics = "t1_demo_client_transaction_errors", groupId = "group_id")
    public void handleError(Long transactionId) {

        String url = "http://localhost:8080/accounts/" + transactionId + "/unblock";

        ResponseEntity<Void> response = restTemplate.postForEntity(url, null, Void.class);

        if (response.getStatusCode().is2xxSuccessful()) {

            failedTransactionRepository.deleteById(transactionId);
        } else {

            FailedTransaction failedTransaction = new FailedTransaction(transactionId);
            failedTransactionRepository.save(failedTransaction);
        }
    }
}
