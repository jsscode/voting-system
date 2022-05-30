package com.codejss.votingsystem.repositories;

import com.codejss.votingsystem.models.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentRepo extends JpaRepository<Student, Long> {

    Student findByDocument(String document);
}
