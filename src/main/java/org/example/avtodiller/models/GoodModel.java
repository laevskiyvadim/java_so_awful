package org.example.avtodiller.models;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Table(name = "goods")
@AllArgsConstructor
@NoArgsConstructor
public class GoodModel {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;
    @Column(name = "name")
    private String name;
    @Column(name = "content")
    private String content;

    @ManyToOne
    @JoinColumn(name = "city_dept_id")
    private CityModel cityOfDeparture;

    @Column(name = "date_of_departure")
    private String dateOfDeparture;

    @ManyToOne
    @JoinColumn(name = "city_arr_id")
    private CityModel cityOfArrival;

    @Column(name = "date_of_arrival")
    private String dateOfArrival;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserModel user;
}
