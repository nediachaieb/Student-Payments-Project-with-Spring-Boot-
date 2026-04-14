package tn.nadia.backend.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tn.nadia.backend.entities.Student;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {

    Student findByCode(String code);
    List<Student> findByProgramId(String programId);

    boolean existsByCode(String code);


}
