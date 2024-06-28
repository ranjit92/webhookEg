package com.myself.studentdata.repository;

import com.myself.studentdata.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentRepo extends JpaRepository<Student, Integer> {
}
