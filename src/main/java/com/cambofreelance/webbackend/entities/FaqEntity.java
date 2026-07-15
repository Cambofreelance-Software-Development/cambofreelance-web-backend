package com.cambofreelance.webbackend.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.io.Serial;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.DynamicUpdate;

@Entity
@Table(name = "cms_faqs")
@Data
@DynamicUpdate
@EqualsAndHashCode(callSuper = false)
public class FaqEntity extends BaseEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id")
    private String id;

    @Column(name = "question", nullable = false, columnDefinition = "TEXT")
    private String question;

    @Column(name = "question_kh", columnDefinition = "TEXT")
    private String questionKh;

    @Column(name = "answer", nullable = false, columnDefinition = "TEXT")
    private String answer;

    @Column(name = "answer_kh", columnDefinition = "TEXT")
    private String answerKh;

    @Column(name = "sort_order")
    private Integer sortOrder = 0;
}
