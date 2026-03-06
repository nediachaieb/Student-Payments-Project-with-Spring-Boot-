package tn.nadia.backend.controller;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tn.nadia.backend.entities.Payment;
import tn.nadia.backend.entities.PaymentStatus;
import tn.nadia.backend.entities.PaymentType;
import tn.nadia.backend.entities.Student;
import tn.nadia.backend.repository.PaymentRepository;
import tn.nadia.backend.repository.StudentRepository;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

/**
 * REST Controller pour gérer les opérations liées aux étudiants et aux paiements.
 * Ce controller expose plusieurs endpoints REST pour consulter et gérer les données.
 */
@RestController
@CrossOrigin("*") // Autorise les requêtes provenant de n'importe quelle origine (utile pour Angular/React)
public class StudentRestController {

    // Repository pour accéder aux données des étudiants
    private StudentRepository studentRepository;

    // Repository pour accéder aux données des paiements
    private PaymentRepository paymentRepository;

    // Service contenant la logique métier des paiements
   // private PaymentService paymentService;

    /**
     * Injection des dépendances via le constructeur
     */
    public StudentRestController(StudentRepository studentRepository,
                                 PaymentRepository paymentRepository)
                                 {
        this.studentRepository = studentRepository;
        this.paymentRepository = paymentRepository;
        //this.paymentService = paymentService;
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
     * Récupérer un étudiant par son code
     * URL : GET /students/{code}
     */
    @GetMapping("/students/{code}")
    public Student getStudentByCode(@PathVariable String code){
        return studentRepository.findByCode(code);
    }

    /**
     * Récupérer les étudiants selon leur programme
     * URL : GET /studentsByProgram?programId=GLSID
     */
    @GetMapping(path = "/studentsByProgram")
    public List<Student> studentsByProgram(@RequestParam String programId){
        return studentRepository.findByProgramId(programId);
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
//
//    /**
//     * Mettre à jour le statut d'un paiement
//     * URL : PUT /payments/{paymentId}/updateStatus?status=VALIDATED
//     */
//    @PutMapping("/payments/{paymentId}/updateStatus")
//    public Payment updatePaymentStatus(@RequestParam PaymentStatus status,
//                                       @PathVariable Long paymentId){
//        return paymentService.updatePaymentStatus(status,paymentId);
//    }
//
//    /**
//     * Ajouter un nouveau paiement avec un fichier (PDF reçu)
//     * URL : POST /payments
//     * Content-Type : multipart/form-data
//     */
//    @PostMapping(path="/payments", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
//    public Payment savePayment(@RequestParam MultipartFile file,
//                               double amount,
//                               PaymentType type,
//                               LocalDate date,
//                               String studentCode) throws IOException {
//
//        return paymentService.savePayment(file,amount,type,date,studentCode);
//    }
//
//    /**
//     * Télécharger le fichier PDF associé à un paiement
//     * URL : GET /payments/{id}/file
//     */
//    @GetMapping(path="payments/{id}/file",produces = MediaType.APPLICATION_PDF_VALUE)
//    public byte[] getPaymentFile(@PathVariable Long id) throws IOException {
//        return paymentService.getPaymentFile(id);
//    }
}