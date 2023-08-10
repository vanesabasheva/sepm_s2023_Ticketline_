package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto;

import java.math.BigDecimal;

public class OrderMerchandiseDto {
    private Integer id;
    private String itemName;
    private Integer quantity;
    private BigDecimal price;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public static final class OrderMerchandiseDtoBuilder {
        private Integer id;
        private String itemName;
        private Integer quantity;
        private BigDecimal price;

        private OrderMerchandiseDtoBuilder() {
        }

        public static OrderMerchandiseDtoBuilder aOrderMerchandiseDto() {
            return new OrderMerchandiseDtoBuilder();
        }

        public OrderMerchandiseDtoBuilder withId(Integer id) {
            this.id = id;
            return this;
        }

        public OrderMerchandiseDtoBuilder withItemName(String itemName) {
            this.itemName = itemName;
            return this;
        }

        public OrderMerchandiseDtoBuilder withQuantity(Integer quantity) {
            this.quantity = quantity;
            return this;
        }

        public OrderMerchandiseDtoBuilder withPrice(BigDecimal price) {
            this.price = price;
            return this;
        }

        public OrderMerchandiseDto build() {
            OrderMerchandiseDto orderMerchandiseDto = new OrderMerchandiseDto();
            orderMerchandiseDto.setId(id);
            orderMerchandiseDto.setItemName(itemName);
            orderMerchandiseDto.setQuantity(quantity);
            orderMerchandiseDto.setPrice(price);
            return orderMerchandiseDto;
        }
    }
}
