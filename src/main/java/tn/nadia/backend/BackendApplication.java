package tn.nadia.backend;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import tn.nadia.backend.entities.Payment;
import tn.nadia.backend.entities.PaymentStatus;
import tn.nadia.backend.entities.PaymentType;
import tn.nadia.backend.entities.Student;
import tn.nadia.backend.repository.PaymentRepository;
import tn.nadia.backend.repository.StudentRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Random;

@SpringBootApplication
public class BackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(BackendApplication.class, args);
	}

	@Bean
	CommandLineRunner commandLineRunner(StudentRepository studentRepository,
										PaymentRepository paymentRepository) {
		return args -> {

			if (studentRepository.count() > 0) {
				System.out.println("Database already initialized. Skipping seed.");
				return;
			}

			List<Student> students = List.of(
					Student.builder().code("ST001").firstName("Yassine").lastName("Ben Ali").programId("GLSID").photo("yassine.jpg").build(),
					Student.builder().code("ST002").firstName("Salma").lastName("Trabelsi").programId("BDCC").photo("salma.jpg").build(),
					Student.builder().code("ST003").firstName("Hamza").lastName("Mansouri").programId("GLSID").photo("hamza.jpg").build(),
					Student.builder().code("ST004").firstName("Sara").lastName("Jlassi").programId("BDCC").photo("sara.jpg").build(),
					Student.builder().code("ST005").firstName("Omar").lastName("Chaari").programId("GLSID").photo("omar.jpg").build(),
					Student.builder().code("ST006").firstName("Nadia").lastName("Chaieb").programId("BDCC").photo("nadia.jpg").build(),
					Student.builder().code("ST007").firstName("Amine").lastName("Kallel").programId("GLSID").photo("amine.jpg").build(),
					Student.builder().code("ST008").firstName("Lina").lastName("Fakhfakh").programId("BDCC").photo("lina.jpg").build(),
					Student.builder().code("ST009").firstName("Youssef").lastName("Gharbi").programId("GLSID").photo("youssef.jpg").build(),
					Student.builder().code("ST010").firstName("Mouna").lastName("Khemiri").programId("BDCC").photo("mouna.jpg").build()
			);

			studentRepository.saveAll(students);

			PaymentType[] paymentTypes = PaymentType.values();
			PaymentStatus[] paymentStatuses = PaymentStatus.values();
			Random random = new Random();

			for (Student student : students) {
				for (int i = 0; i <11 ; i++) {
					Payment payment = Payment.builder()
							.date(LocalDate.now().minusDays(random.nextInt(30)))
							.amount(1000 + random.nextInt(20000))
							.type(paymentTypes[random.nextInt(paymentTypes.length)])
							.status(paymentStatuses[random.nextInt(paymentStatuses.length)])
							.file(null)
							.student(student)
							.build();

					paymentRepository.save(payment);
				}
			}

			System.out.println("Seed data inserted successfully.");
		};
	}
}