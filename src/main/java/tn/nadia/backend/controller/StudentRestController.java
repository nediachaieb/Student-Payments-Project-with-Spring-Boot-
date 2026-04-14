package tn.nadia.backend.controller;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tn.nadia.backend.entities.Payment;
import tn.nadia.backend.entities.PaymentStatus;
import tn.nadia.backend.entities.PaymentType;
import tn.nadia.backend.entities.Student;
import tn.nadia.backend.repository.PaymentRepository;
import tn.nadia.backend.repository.StudentRepository;
import tn.nadia.backend.service.PaymentService;
import tn.nadia.backend.service.StudentService;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.nio.file.*;

/**
 * REST Controller pour gérer les opérations liées aux étudiants et aux paiements.
 * Ce controller expose plusieurs endpoints REST pour consulter et gérer les données.
 */
@RestController
@CrossOrigin("*") // Autorise les requêtes provenant de n'importe quelle origine
public class StudentRestController {

    // Repository pour accéder aux données des étudiants
    private final StudentRepository studentRepository;

    // Repository pour accéder aux données des paiements
    private final PaymentRepository paymentRepository;

    // Service contenant la logique métier des paiements
    private final PaymentService paymentService;
    // Service contenant la logique métier des étudiants
    private final StudentService studentService;

    /**
     * Injection des dépendances via le constructeur
     */
    public StudentRestController(StudentRepository studentRepository,
                                 PaymentRepository paymentRepository,
                                 PaymentService paymentService,
                                 StudentService studentService)
                                 {
        this.studentRepository = studentRepository;
        this.paymentRepository = paymentRepository;
        this.paymentService = paymentService;
        this.studentService = studentService;
    }

    // ======================= STUDENTS ENDPOINTS =======================

    /**
     * Récupérer la liste de tous les étudiants
     * URL : GET /students
     */
    @GetMapping(path = "/students")
    public List<Student> allStudents(){
        return studentRepository.findAll();
    }

    /**
     * Récupérer un étudiant par son code unique
     * URL : GET /students/{code}
     */
    @GetMapping("/students/{code}")
    public Student getStudentByCode(@PathVariable String code){
        return studentRepository.findByCode(code);
    }

    /**
     * Récupérer liste des étudiants selon leur programme
     * URL : GET /studentsByProgram?programId=GLSID
     */
    @GetMapping(path = "/studentsByProgram")
    public List<Student> studentsByProgram(@RequestParam String programId){
        return studentRepository.findByProgramId(programId);
    }
/**
 * Ajouter un nouvel étudiant avec une photo
 * URL : POST /students
 * Content-Type : multipart/form-data
 */
    @PostMapping(path="/students", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Student saveStudent(@RequestParam String code,
                               @RequestParam String firstName,
                               @RequestParam String lastName,
                               @RequestParam String programId,
                               @RequestParam MultipartFile photo) throws IOException {

        return studentService.saveStudent(code, firstName, lastName, programId, photo);
    }

    /**
     * Télécharger la photo d'un étudiant
    * @return ResponseEntity contenant les données de la photo ou un statut d'erreur
     * URL : GET /students/{id}/photo
     * Ce endpoint gère plusieurs cas :
     * 1) Si la photo est null ou vide, il retourne un statut 204 No Content.
     *  2) Si l'URL de la photo est cassée (non résolvable), il retourne un statut 400 Bad Request.
     *  3) Si le fichier de la photo a été supprimé, il retourne un statut 404 Not Found.
     *
     */

    @GetMapping("/students/{id}/photo")
    public ResponseEntity<byte[]> getStudentPhoto(@PathVariable Long id) throws IOException {
        Student student = studentService.getStudentById(id);

        if (student == null) {
            return ResponseEntity.notFound().build();
        }

        // 1) Photo null
        if (student.getPhoto() == null || student.getPhoto().isBlank()) {
            return ResponseEntity.noContent().build(); // 204
        }

        Path path = studentService.resolveStudentPhotoPath(student);

        // 2) URL cassée
        if (path == null) {
            return ResponseEntity.badRequest().build(); // 400
        }

        // 3) Fichier supprimé
        if (!Files.exists(path)) {
            return ResponseEntity.notFound().build(); // 404
        }

        String contentType = "image/jpeg";
        String photo = student.getPhoto().toLowerCase();

        if (photo.endsWith(".png")) {
            contentType = "image/png";
        } else if (photo.endsWith(".webp")) {
            contentType = "image/webp";
        }

        return ResponseEntity.ok()
                .header("Content-Type", contentType)
                .body(studentService.readStudentPhoto(path));
    }
    @DeleteMapping("/students/{id}")
    public void deleteStudent(@PathVariable Long id) {
        studentRepository.deleteById(id);
    }

    // ======================= PAYMENTS ENDPOINTS =======================

    /**
     * Récupérer tous les paiements
     * URL : GET /payments
     */
    @GetMapping("/payments")
    public List<Payment> allPayments(){
        return paymentRepository.findAll();
    }

    /**
     * Récupérer un paiement par son ID
     * URL : GET /payments/{id}
     */
    @GetMapping("/payments/{id}")
    public Payment getPaymentById(@PathVariable Long id){
        return paymentRepository.findById(id).get();
    }

    /**
     * Récupérer les paiements d'un étudiant via son code
     * URL : GET /students/{code}/payments
     */
    @GetMapping("/students/{code}/payments")
    public List<Payment> paymentsByStudentCode(@PathVariable String code){
        return paymentRepository.findByStudent_Code(code);
    }

    /**
     * Récupérer les paiements selon leur statut
     * URL : GET /paymentsByStatus?status=CREATED
     */
    @GetMapping("/paymentsByStatus")
    public List<Payment> paymentsByStaus(@RequestParam PaymentStatus status){
        return paymentRepository.findByStatus(status);
    }

    /**
     * Mettre à jour le statut d'un paiement
     * URL : PUT /payments/{paymentId}/updateStatus?status=VALIDATED
     */
    @PutMapping("/payments/{paymentId}/updateStatus")
    public Payment updatePaymentStatus(@RequestParam PaymentStatus status,
                                       @PathVariable Long paymentId){
        return paymentService.updatePaymentStatus(status,paymentId);
    }

    /**
     * Ajouter un nouveau paiement avec un fichier (PDF reçu)
     * URL : POST /payments
     * Content-Type : multipart/form-data
     */
    @PostMapping(path="/payments", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Payment savePayment(@RequestParam MultipartFile file,
                               @RequestParam double amount,
                               @RequestParam PaymentType type,
                               @RequestParam LocalDate date,
                               @RequestParam String studentCode) throws IOException {

        return paymentService.savePayment(file,amount,type,date,studentCode);
    }

    /**
     * Télécharger le fichier PDF associé à un paiement
     * URL : GET /payments/{id}/file
     */
    @GetMapping(path = "payments/{id}/file")
    public ResponseEntity<byte[]> getPaymentFile(@PathVariable Long id) throws IOException {

        byte[] pdfBytes = paymentService.getPaymentFile(id);

        return ResponseEntity.ok()
                .header("Content-Disposition", "inline; filename=payment.pdf")
                .header("Content-Type", "application/pdf")
                .body(pdfBytes);
    }
}