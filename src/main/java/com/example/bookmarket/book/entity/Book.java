package com.example.bookmarket.book.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;


@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String author;

}

//
//    Table book {
//        id integer [primary key]
//        title varchar [not null]
//        author varchar [not null]
//        isbn varchar(20) [unique] // 고유번호
//        published_at timestamp
//        stock integer [not null]
//        price decimal(10, 2) [not null]
//        is_active boolean [default: true]
//        status enum('in_stock', 'out_of_stock', 'discontinued') [default: 'in_stock']
//        }