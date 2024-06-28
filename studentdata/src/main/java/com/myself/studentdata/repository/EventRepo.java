package com.myself.studentdata.repository;

import com.myself.studentdata.model.WebhookDetails;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventRepo extends JpaRepository<WebhookDetails,Integer> {
}
