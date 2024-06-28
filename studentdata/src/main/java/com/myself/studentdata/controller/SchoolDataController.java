package com.myself.studentdata.controller;

import com.myself.studentdata.model.SchoolData;
import com.myself.studentdata.model.Student;
import com.myself.studentdata.model.WebhookDetails;
import com.myself.studentdata.repository.EventRepo;
import com.myself.studentdata.repository.SchoolDataRepo;
import com.myself.studentdata.repository.StudentRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/allSchool")
public class SchoolDataController {
    @Autowired
    private SchoolDataRepo schoolDataRepo;

    @Autowired
    private EventRepo eventRepo;

    @Autowired
    private StudentRepo studentRepo;

    @PostMapping("/addNewSchool")
    public @ResponseBody SchoolData addSchool(@RequestParam String schoolName) {

        SchoolData schoolData = new SchoolData();
        schoolData.setSchoolName(schoolName);
        schoolDataRepo.save(schoolData);
        return schoolData;
    }

    @GetMapping("/getAllSchool")
    public @ResponseBody List<SchoolData> getAllSchool() {
        return schoolDataRepo.findAll();
    }

    @PostMapping(path = "/addWebhookEvent/{schoolId}")
    public @ResponseBody WebhookDetails addWebhookEvent(@PathVariable Integer schoolId,
                                                @RequestBody WebhookDetails webhookDetails) {

        Optional<SchoolData> schoolDataOptional = schoolDataRepo.findById(schoolId);
        WebhookDetails details = null;
        if (schoolDataOptional.isPresent()) {
            SchoolData schoolData = schoolDataOptional.get();

            details = new WebhookDetails();
            details.setEventName(webhookDetails.getEventName());
            details.setEndpointURL(webhookDetails.getEndpointURL());
            details.setSchoolData(schoolData);
            eventRepo.save(details);
        }
        return details;
    }

    @PostMapping(path = "/addStudent/{schoolId}")
    public @ResponseBody Student addStudent(@PathVariable Integer schoolId,
                                                @RequestBody Student studentReq) {

        Optional<SchoolData> schoolDataOptional = schoolDataRepo.findById(schoolId);
        Student student = null;
        if (schoolDataOptional.isPresent()) {
            SchoolData schoolData = schoolDataOptional.get();

            student = new Student();
            student.setStudentName(studentReq.getStudentName());
            student.setAge(studentReq.getAge());
            student.setSchoolData(schoolData);
            studentRepo.save(student);

            //call webhook
            WebhookDetails webhookDetails = schoolData.getWebhookDetails()
                    .stream().filter(details_ -> details_.getEventName().equals("add"))
                    .findFirst()
                    .orElse(null);
            if(webhookDetails != null && webhookDetails.getEndpointURL() != null){
                String url = webhookDetails.getEndpointURL();
                url = url.replace("{name}", student.getStudentName());

                RestTemplate template = new RestTemplate();
                String response = template.getForObject(url, String.class);
                System.out.println("Webhook Response : " + response);

            }
        }
        return student;
    }

}
