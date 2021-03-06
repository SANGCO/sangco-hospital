package dev.sangco.hospital;

import com.querydsl.jpa.impl.JPAQueryFactory;
import dev.sangco.hospital.domain.*;
import dev.sangco.hospital.repository.HospitalRepository;
import dev.sangco.hospital.repository.PatientRepository;
import dev.sangco.hospital.repository.VisitRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.stream.IntStream;

@SpringBootApplication
@EnableJpaAuditing
public class SangcoHospitalApplication {

	public static void main(String[] args) {
		SpringApplication.run(SangcoHospitalApplication.class, args);
	}

	@Bean
	JPAQueryFactory jpaQueryFactory(EntityManager em) {
		return new JPAQueryFactory(em);
	}

	@Bean
	public CommandLineRunner runner(HospitalRepository hospitalRepository,
									PatientRepository patientRepository,
									VisitRepository visitRepository) {
		return (args -> {
			Hospital hospital = Hospital.builder()
					.name("테스트 병원")
					.number("0000000000")
					.director("김아무개").build();
			Hospital savedHospital = hospitalRepository.save(hospital);

			IntStream.rangeClosed(1, 10).forEach(i -> {
				Patient patient = Patient.builder()
						.hospital(savedHospital)
						.name("테스트환자" + i)
						.gender(Gender.MALE)
						.birthdate("1988-01-01")
						.phoneNumber("010-0000-0000").build();
				patientRepository.save(patient);

				IntStream.rangeClosed(1, 10).forEach(j -> {
					Visit visit = Visit.builder()
							.hospital(hospital)
							.patient(patient)
							.schedule(LocalDateTime.now())
							.state(State.ONGOING).build();
					visitRepository.save(visit);
				});

				IntStream.rangeClosed(1, 10).forEach(k -> {
					Visit visit = Visit.builder()
							.hospital(hospital)
							.patient(patient)
							.schedule(LocalDateTime.now())
							.state(State.END).build();
					visitRepository.save(visit);
				});

				IntStream.rangeClosed(1, 10).forEach(l -> {
					Visit visit = Visit.builder()
							.hospital(hospital)
							.patient(patient)
							.schedule(LocalDateTime.now())
							.state(State.CANCEL).build();
					visitRepository.save(visit);
				});
			});
		});
	}

}