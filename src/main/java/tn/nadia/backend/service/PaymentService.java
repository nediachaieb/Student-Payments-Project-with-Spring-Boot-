package tn.nadia.backend.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import tn.nadia.backend.entities.Payment;
import tn.nadia.backend.entities.PaymentStatus;
import tn.nadia.backend.entities.PaymentType;
import tn.nadia.backend.entities.Student;
import tn.nadia.backend.repository.PaymentRepository;
import tn.nadia.backend.repository.StudentRepository;

import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.util.UUID;

@Service
@Transactional
public class PaymentService {

    private PaymentRepository  paymentRepository;
    private StudentRepository  studentRepository;

    public PaymentService(PaymentRepository paymentRepository, StudentRepository studentRepository) {
        this.paymentRepository = paymentRepository;
        this.studentRepository = studentRepository;
    }

    public Payment savePayment(MultipartFile file, double amount, PaymentType type,
                                 LocalDate date, String studentCode) throws IOException {
        Path folderPath = Paths.get(System.getProperty("user.home"),"enset-students","payments");
        if(!Files.exists(folderPath)){
            Files.createDirectories(folderPath);
        }
        String fileName = UUID.randomUUID().toString();
        Path filePath = Paths.get(System.getProperty("user.home"),"students","payments",fileName+".pdf");
        Files.copy(file.getInputStream(), filePath);
        Student  student = studentRepository.findByCode(studentCode);
        Payment payment=Payment.builder()
                .type(type)
                .status(PaymentStatus.CREATED)
                .date(date)
                .student(student)
                .amount(amount)
                .file(filePath.toUri().toString())
                .build();
        return paymentRepository.save(payment);

    }

    public byte[] getPaymentFile(Long id) throws IOException {
        Payment payment = paymentRepository.findById(id).get();
        return Files.readAllBytes(Path.of(URI.create(payment.getFile())));

    }
    public Payment updatePayment(Long id,
                                 double amount,
                                 PaymentType type,
                                 LocalDate date,
                                 String studentCode,
                                 MultipartFile file) throws IOException {

        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Payment not found"));

        Student student = studentRepository.findByCode(studentCode);

        if (student == null) {
            throw new RuntimeException("Student not found");
        }

        payment.setAmount(amount);
        payment.setType(type);
        payment.setDate(date);
        payment.setStudent(student);

        if (file != null && !file.isEmpty()) {
            Path folderPath = Paths.get(System.getProperty("user.home"), "students", "payments");

            if (!Files.exists(folderPath)) {
                Files.createDirectories(folderPath);
            }

            String fileName = UUID.randomUUID().toString();
            Path filePath = folderPath.resolve(fileName + ".pdf");
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            payment.setFile(filePath.toUri().toString());
        }

        return paymentRepository.save(payment);
    }










    }



