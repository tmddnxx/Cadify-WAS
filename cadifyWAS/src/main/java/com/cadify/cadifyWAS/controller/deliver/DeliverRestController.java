package com.cadify.cadifyWAS.controller.deliver;

import com.cadify.cadifyWAS.model.entity.TestEntity;
import com.cadify.cadifyWAS.repository.TestRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Log4j2
@RequestMapping("/deliver")
public class DeliverRestController {


    private final TestRepository testRepository;

    @PostMapping("/post")
    public ResponseEntity<TestEntity> postDeliver(@RequestBody TestEntity body){
        log.info("Inserting TestEntity: {}", body);

        TestEntity savedEntity = testRepository.save(body);  // 엔티티 저장

        log.info("Inserted entity with tno: {}", savedEntity.getTno());  // 삽입된 엔티티의 tno 값 출력

        return ResponseEntity.ok(savedEntity);  // 삽입된 엔티티 반환
    }


}
