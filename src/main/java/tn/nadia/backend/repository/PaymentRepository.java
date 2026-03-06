package tn.nadia.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tn.nadia.backend.entities.Payment;
import tn.nadia.backend.entities.PaymentStatus;
import tn.nadia.backend.entities.PaymentType;

import java.util.List;
@Repository

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    List<Payment> findByStudent_Code(String code);

    List<Payment> findByStatus(PaymentStatus status);

    List<Payment> findByType(PaymentType type);

}
