package com.myself.studentdata.repository;

import com.myself.studentdata.model.SchoolData;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SchoolDataRepo extends JpaRepository<SchoolData, Integer> {

}
