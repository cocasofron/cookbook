package ro.cookbook.domain;

import lombok.*;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.io.Serializable;

@MappedSuperclass
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
class BaseEntity<ID extends Serializable> implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private ID id;
}