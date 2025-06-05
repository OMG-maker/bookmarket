package com.example.bookmarket.purchase.entity;

import com.example.bookmarket.purchaseBook.entity.PurchaseBook;
import com.example.bookmarket.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class Purchase {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime purchasedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    private BigDecimal totalPrice;

    @Enumerated(EnumType.STRING)
//    private Purchase.Status status;
    private Status status;

    public enum Status {
        ACTIVE, CANCELLED, COMPLETED
    }

    @OneToMany(mappedBy = "purchase", cascade = CascadeType.ALL)
    private List<PurchaseBook> purchaseBooks;

}