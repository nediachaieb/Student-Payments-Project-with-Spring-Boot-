package tn.nadia.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import tn.nadia.backend.entities.Payment;
import tn.nadia.backend.entities.PaymentStatus;
import tn.nadia.backend.entities.PaymentType;
import tn.nadia.backend.entities.Student;
import tn.nadia.backend.repository.PaymentRepository;
import tn.nadia.backend.repository.StudentRepository;

import java.time.LocalDate;
import java.util.Random;
import java.util.UUID;
@SpringBootApplication
public class BackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(BackendApplication.class, args);
	}

	@Bean
	CommandLineRunner commandLineRunner(StudentRepository studentRepository,
										PaymentRepository paymentRepository) {
		return args -> {

			// Création des étudiants
			studentRepository.save(Student.builder()
					//.id(UUID.randomUUID().toString())
					.code("ST001")
					.firstName("Yassine")
					.programId("GLSID")
					.build());

			studentRepository.save(Student.builder()
					//.id(UUID.randomUUID().toString())
					.code("ST002")
					.firstName("Salma")
					.programId("BDCC")
					.build());

			studentRepository.save(Student.builder()
					//.id(UUID.randomUUID().toString())
					.code("ST003")
					.firstName("Hamza")
					.programId("GLSID")
					.build());

			studentRepository.save(Student.builder()
					//.id(UUID.randomUUID().toString())
					.code("ST004")
					.firstName("Sara")
					.programId("BDCC")
					.build());

			studentRepository.save(Student.builder()
					//.id(UUID.randomUUID().toString())
					.code("ST005")
					.firstName("Omar")
					.programId("GLSID")
					.build());

			studentRepository.save(Student.builder()
					//.id(UUID.randomUUID().toString())
					.code("ST006")
					.firstName("Nadia")
					.programId("BDCC")
					.build());

			// Génération des paiements
			PaymentType[] paymentTypes = PaymentType.values();
			Random random = new Random();

			studentRepository.findAll().forEach(student -> {
				for (int i = 0; i < 9; i++) {

					int index = random.nextInt(paymentTypes.length);

					Payment payment = Payment.builder()
							.date(LocalDate.now())
							.amount(1000 + random.nextInt(20000))
							.type(paymentTypes[index])
							.status(PaymentStatus.CREATED)
							.student(student)
							.build();

					paymentRepository.save(payment);
				}
			});
		};
	}
}

