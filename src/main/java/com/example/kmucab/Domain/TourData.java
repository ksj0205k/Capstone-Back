package com.example.kmucab.Domain;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@Data
@Entity
public class TourData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private int areaCode;
    private int singunguCode;
    private String mapx;
    private String mapy;
    private String address;
    private int contentid;
    private int contenttypeid;
    private String tel;
    private Date modifiedtime;
    private Date eventstartdate;
    private Date eventenddate;
    private String summary;

    // getters and setters
}
