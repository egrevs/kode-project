package com.egrevs.project.domain.entity.restaurant;

import com.egrevs.project.domain.enums.VariantSize;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import java.math.BigDecimal;

@Entity
@Table(name = "menu_items_variants")
@Getter
@Setter
public class MenuItemsVariant {

    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    @Column(name = "id")
    private String id;

    @Column(name = "size")
    private VariantSize size;

    @Column(name = "price")
    private BigDecimal price;

    @Column(name = "preparation_time")
    private Integer preparationTime;

    @ManyToOne
    @JoinColumn(name = "menuItems_id")
    private MenuItems menuItems;
}
