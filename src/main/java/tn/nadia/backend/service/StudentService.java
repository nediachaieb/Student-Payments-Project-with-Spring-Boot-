package tn.nadia.backend.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import tn.nadia.backend.entities.Student;
import tn.nadia.backend.repository.StudentRepository;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.*;
import java.util.UUID;

@Service
@Transactional
public class StudentService {

    private final StudentRepository studentRepository;

    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public Student saveStudent(String code,
                               String firstName,
                               String lastName,
                               String programId,
                               MultipartFile photo) throws IOException {

        code = code.trim();
        firstName = firstName.trim();
        lastName = lastName.trim();
        programId = programId.trim();

        if (studentRepository.existsByCode(code)) {
            throw new RuntimeException("Student code already exists");
        }

        String photoUri = null;

        if (photo != null && !photo.isEmpty()) {
            Path folderPath = Paths.get(System.getProperty("user.home"), "students", "photos");

            if (!Files.exists(folderPath)) {
                Files.createDirectories(folderPath);
            }

            String fileName = UUID.randomUUID().toString();
            String originalFileName = photo.getOriginalFilename();
            String extension = "";

            if (originalFileName != null && originalFileName.contains(".")) {
                extension = originalFileName.substring(originalFileName.lastIndexOf("."));
            }

            Path filePath = folderPath.resolve(fileName + extension);
            Files.copy(photo.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            photoUri = filePath.toUri().toString();
        }

        Student student = Student.builder()
                .code(code)
                .firstName(firstName)
                .lastName(lastName)
                .programId(programId)
                .photo(photoUri)
                .build();

        return studentRepository.save(student);
    }

    public Student getStudentById(Long id) {
        return studentRepository.findById(id).orElse(null);
    }

    public Path resolveStudentPhotoPath(Student student) {
        if (student == null || student.getPhoto() == null || student.getPhoto().isBlank()) {
            return null;
        }

        try {
            return Path.of(new URI(student.getPhoto()));
        } catch (URISyntaxException | IllegalArgumentException e) {
            return null;
        }
    }

    public byte[] readStudentPhoto(Path path) throws IOException {
        return Files.readAllBytes(path);
    }

    public Student updateStudent(String code,
                                 String firstName,
                                 String lastName,
                                 String programId,
                                 MultipartFile photo) throws IOException {

        Student student = studentRepository.findByCode(code);

        if (student == null) {
            throw new RuntimeException("Student not found with code: " + code);
        }

        student.setFirstName(firstName.trim());
        student.setLastName(lastName.trim());
        student.setProgramId(programId.trim());

        if (photo != null && !photo.isEmpty()) {
            Path folderPath = Paths.get(System.getProperty("user.home"), "students", "photos");

            if (!Files.exists(folderPath)) {
                Files.createDirectories(folderPath);
            }

            String fileName = UUID.randomUUID().toString();
            String originalFileName = photo.getOriginalFilename();
            String extension = "";

            if (originalFileName != null && originalFileName.contains(".")) {
                extension = originalFileName.substring(originalFileName.lastIndexOf("."));
            }

            Path filePath = folderPath.resolve(fileName + extension);
            Files.copy(photo.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            student.setPhoto(filePath.toUri().toString());
        }

        return studentRepository.save(student);
    }


}