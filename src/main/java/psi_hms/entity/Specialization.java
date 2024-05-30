package psi_hms.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "specialization")
@Data
public class Specialization {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "specialization_id")
        private Long specializationId;

        @Column(name = "specialization_name")
        private String specializationName;

}