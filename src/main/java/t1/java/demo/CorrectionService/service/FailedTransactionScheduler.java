package t1.java.demo.CorrectionService.service;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import t1.java.demo.CorrectionService.model.FailedTransaction;
import t1.java.demo.CorrectionService.repository.FailedTransactionRepository;

import java.util.List;

@AllArgsConstructor
@Component
public class FailedTransactionScheduler {

    private FailedTransactionRepository failedTransactionRepository;

    private RestTemplate restTemplate;

    @Scheduled(fixedRateString = "${failed.transaction.retry.interval}")
    public void retryFailedTransactions() {
        List<FailedTransaction> failedTransactions = failedTransactionRepository.findAll();

        for (FailedTransaction failedTransaction : failedTransactions) {
            String url = "http://localhost:8080/accounts/" + failedTransaction.getTransactionId() + "/unblock";

            ResponseEntity<Void> response = restTemplate.postForEntity(url, null, Void.class);

            if (response.getStatusCode().is2xxSuccessful()) {
                failedTransactionRepository.deleteById(failedTransaction.getId());
            } else {
                System.out.println("Не удалось разблокировать счет для транзакции ID: " + failedTransaction.getTransactionId());
            }
        }
    }
}
