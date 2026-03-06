package tn.nadia.backend.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tn.nadia.backend.entities.Student;

import java.util.List;
@Repository
public interface StudentRepository extends JpaRepository<Student, String> {

    Student findByCode(String code);
    List<Student> findByProgramId(String programId);

}
