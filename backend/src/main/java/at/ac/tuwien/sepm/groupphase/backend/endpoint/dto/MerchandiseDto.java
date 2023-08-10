package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto;

import java.math.BigDecimal;

public class MerchandiseDto {
    private Integer id;
    private BigDecimal price;

    private Integer pointsPrice;

    private Integer pointsReward;

    private String title;

    private String description;

    private String imagePath;

    // private Set<MerchandiseOrdered> merchandiseMerchandiseOrdereds;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getPointsPrice() {
        return pointsPrice;
    }

    public void setPointsPrice(Integer pointsPrice) {
        this.pointsPrice = pointsPrice;
    }

    public Integer getPointsReward() {
        return pointsReward;
    }

    public void setPointsReward(Integer pointsReward) {
        this.pointsReward = pointsReward;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    /*
    public Set<MerchandiseOrdered> getMerchandiseMerchandiseOrdereds() {
        return merchandiseMerchandiseOrdereds;
    }

    public void setMerchandiseMerchandiseOrdereds(Set<MerchandiseOrdered> merchandiseMerchandiseOrdereds) {
        this.merchandiseMerchandiseOrdereds = merchandiseMerchandiseOrdereds;
    }

     */

    @Override
    public String toString() {
        return "MerchandiseDto{"
            + "id=" + id
            + ", price='" + price + '\''
            + ", pointsPrice='" + pointsPrice + '\''
            + ", pointsReward=" + pointsReward
            + ", title=" + title
            + ", description=" + description
            + ", imagePath=" + imagePath
            // NOT NEEDED: + ", merchandiseMerchandiseOrdereds= " + merchandiseMerchandiseOrdereds
            + '}';
    }

}
