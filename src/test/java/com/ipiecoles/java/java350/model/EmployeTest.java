package com.ipiecoles.java.java350.model;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.time.LocalDate;

public class EmployeTest {
    //Employe dateEmbauche avec date 2ans avant aujourd'hui=>
    //2 années d'ancienneté

    @Test
    public void testAncienneteDateEmbaucheNMoins2(){
        //Given création d'un employé avec 2 ans d'ancienneté
        Employe employe = new Employe();
        employe.setDateEmbauche(LocalDate.now().minusYears(2));
        //When
        Integer nbAnnees = employe.getNombreAnneeAnciennete();
        //Then
        Assertions.assertThat(nbAnnees).isEqualTo(2);
    }

    //Embauche dans le futur
    @Test
    public void testAncienneteDateEmbaucheNPlus2(){
        //Given création d'un employé avec embauche dans le futur
        Employe employe = new Employe();
        employe.setDateEmbauche(LocalDate.now().plusYears(2));
        //When
        Integer nbAnnees = employe.getNombreAnneeAnciennete();
        //Then
        Assertions.assertThat(nbAnnees).isEqualTo(0);
    }

    //Embauche aujourd'hui=>0
    @Test
    public void testAncienneteDateEmbaucheAuj(){
        //Given
        Employe employe = new Employe();
        employe.setDateEmbauche(LocalDate.now());
        //When
        Integer nbAnnees = employe.getNombreAnneeAnciennete();
        //Then
        Assertions.assertThat(nbAnnees).isEqualTo(0);
    }

    //Embauche indéfinie =>0
    @Test
    public void testAncienneteDateNulle(){
        //Given
        Employe employe = new Employe();
        employe.setDateEmbauche(null);
        //When
        Integer nbAnnees = employe.getNombreAnneeAnciennete();
        //Then
        Assertions.assertThat(nbAnnees).isEqualTo(0);
    }

    //Test prime annuelle
    //Performance, matricule, anciennete, tps partiel, prime
    @ParameterizedTest()
    @CsvSource({
            "1, 'T12345', 0, 1.0, 1000.0",
            "1, 'T12345', 0, 0.5, 500.0",
            "1, 'M12345', 0, 1.0, 1700.0",
            "2, 'T12345', 0, 1.0, 2300.0"
    })
    public void testGetPrimeAnnuelle(Integer performance, String matricule, Integer nbAnneesAnciennete, Double tempsPartiel, Double prime){
        //Given
        Employe employe = new Employe();
        employe.setPerformance(performance);
        employe.setMatricule(matricule);
        employe.setDateEmbauche(LocalDate.now().minusYears(nbAnneesAnciennete));
        employe.setTempsPartiel(tempsPartiel);

        //When
        Double primeCalculee = employe.getPrimeAnnuelle();

        //Then
        Assertions.assertThat(primeCalculee).isEqualTo(prime);
    }

    @Test
    public void testPourcentageAugmentation10(){
        //Given
        Employe employe = new Employe();
        employe.setSalaire(1000.0);

        //When
        //Augmentation de 10% = 10.0
        double augmentation = employe.augmenterSalaire(10.0);

        //Then
        Assertions.assertThat(employe.getSalaire()).isEqualTo(1100.0);
    }
    @Test
    public void testPourcentageAugmentationNegatif(){
        //Given
        Employe employe = new Employe();
        employe.setSalaire(1000.0);

        //When
        //Diminution salaire -> pourcentage = -10.0
        double coeff = employe.augmenterSalaire(-10.0);

        //Then
        Assertions.assertThat(employe.getSalaire()).isNegative();
    }



}
