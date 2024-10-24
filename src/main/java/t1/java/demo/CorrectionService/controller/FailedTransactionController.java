package t1.java.demo.CorrectionService.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import t1.java.demo.CorrectionService.model.FailedTransaction;
import t1.java.demo.CorrectionService.repository.FailedTransactionRepository;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/failed-transactions")
public class FailedTransactionController {

    private FailedTransactionRepository failedTransactionRepository;

    private RestTemplate restTemplate;
    @GetMapping("/")
    public ResponseEntity<List<FailedTransaction>> getAllFailedTransactions() {
        List<FailedTransaction> failedTransactions = failedTransactionRepository.findAll();
        return ResponseEntity.ok(failedTransactions);
    }


    @PostMapping("/{transactionId}/retry")
    public ResponseEntity<Void> retryFailedTransaction(@PathVariable Long transactionId) {
        FailedTransaction failedTransaction = failedTransactionRepository.findById(transactionId)
                .orElseThrow(() -> new RuntimeException("Неудачная транзакция не найдена"));

        String url = "http://localhost:8080/accounts/" + failedTransaction.getTransactionId() + "/unblock";
        ResponseEntity<Void> response = restTemplate.postForEntity(url, null, Void.class);

        if (response.getStatusCode().is2xxSuccessful()) {
            failedTransactionRepository.deleteById(transactionId);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(response.getStatusCode()).build();
        }
    }
}
