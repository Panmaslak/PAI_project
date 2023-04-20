package com.example.application.repository;

import com.example.application.model.Subject;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface SubjectRepository extends JpaRepository<Subject, Long> {


}



