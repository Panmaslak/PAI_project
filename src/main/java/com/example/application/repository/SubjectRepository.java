package com.example.application.repository;

import com.example.application.model.Subject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Collection;
import java.util.List;

@Repository
public interface SubjectRepository extends JpaRepository<Subject, Long> {

    List<Subject> findAllByNameIn(Collection<String> names);
    Subject findByName(String subjectName);


}



