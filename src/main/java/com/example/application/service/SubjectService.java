package com.example.application.service;

import com.example.application.repository.SubjectRepository;
import org.springframework.stereotype.Service;

@Service
public class SubjectService {

    public  SubjectRepository subjectRepository;

    public SubjectService(SubjectRepository subjectRepository){
        this.subjectRepository = subjectRepository;
    }
}
