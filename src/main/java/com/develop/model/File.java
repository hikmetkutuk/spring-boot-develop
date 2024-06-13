package com.develop.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "t_file")
public class File extends BaseEntity {
    @Column(unique = true)
    private String fileName;
    private String fileType;

    @Column(length = 5000)
    private byte[] fileByte;

    @Column(length = 20000)
    private String fileBase64;
}
