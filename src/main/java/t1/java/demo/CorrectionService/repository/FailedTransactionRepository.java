package t1.java.demo.CorrectionService.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import t1.java.demo.CorrectionService.model.FailedTransaction;

public interface FailedTransactionRepository extends JpaRepository<FailedTransaction, Long> {
}
