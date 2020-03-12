package com.ipiecoles.java.java350.repository;

import com.ipiecoles.java.java350.model.Employe;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;

@SpringBootTest
public class EmployeRepositoryTest {

    @Autowired
    EmployeRepository employeRepository;

    @BeforeEach
    public void setUp(){
        employeRepository.deleteAll();
    }

    @Test
    public void testFindLastMatriculeNoEmployes(){
        //Given

        //When
        String lastMatricule = employeRepository.findLastMatricule();
        //Then
        Assertions.assertThat(lastMatricule).isNull();
    }

    @Test
    public void testFindLastMatricule2Employes(){
        //Given
        Employe jean = employeRepository.save(new Employe("jean", "jean", "M12345", LocalDate.now(), 1200.0, 2, 1.0));
        Employe francis = employeRepository.save(new Employe("francis", "francis", "T05421", LocalDate.now(), 1200.0, 2, 1.0));
        //When
        String lastMatricule = employeRepository.findLastMatricule();
        //Then
        Assertions.assertThat(lastMatricule).isEqualTo("12345");
    }

    @Test
    public void testAvgPerformanceWhereMatriculeStartsWith(){
        //Given
        Employe commercial = employeRepository.save(new Employe("jean", "jean", "C12345", LocalDate.now(), 2000.0, 5, 1.0));
        Employe commercial2 = employeRepository.save(new Employe("paul", "paul", "C12346", LocalDate.now(), 2000.0, 3, 1.0));
        Employe technicien = employeRepository.save(new Employe("francis", "francis", "T12345", LocalDate.now(), 1500.0, 2, 1.0));
        //When
        Double moyennePerf = employeRepository.avgPerformanceWhereMatriculeStartsWith("C");
        //Then
        Assertions.assertThat(moyennePerf).isEqualTo(4.0);
    }
}
