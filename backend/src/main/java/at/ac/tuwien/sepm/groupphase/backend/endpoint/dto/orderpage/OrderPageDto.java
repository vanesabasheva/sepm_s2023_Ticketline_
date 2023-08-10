package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.orderpage;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.LocationDto;

import java.time.LocalDateTime;
import java.util.List;

public class OrderPageDto {
    private LocalDateTime orderTs;
    private LocationDto location;
    private boolean cancelled;
    private OrderPagePaymentDetailDto paymentDetail;
    private List<OrderPageTicketDto> tickets;
    private List<OrderPageMerchandiseDto> merchandise;
    private List<OrderPageTransactionDto> transactions;

    public LocalDateTime getOrderTs() {
        return orderTs;
    }

    public void setOrderTs(LocalDateTime orderTs) {
        this.orderTs = orderTs;
    }

    public LocationDto getLocation() {
        return location;
    }

    public boolean isCancelled() {
        return cancelled;
    }

    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    public void setLocation(LocationDto location) {
        this.location = location;
    }

    public OrderPagePaymentDetailDto getPaymentDetail() {
        return paymentDetail;
    }

    public void setPaymentDetail(OrderPagePaymentDetailDto paymentDetail) {
        this.paymentDetail = paymentDetail;
    }

    public List<OrderPageTicketDto> getTickets() {
        return tickets;
    }

    public void setTickets(List<OrderPageTicketDto> tickets) {
        this.tickets = tickets;
    }

    public List<OrderPageMerchandiseDto> getMerchandise() {
        return merchandise;
    }

    public void setMerchandise(List<OrderPageMerchandiseDto> merchandise) {
        this.merchandise = merchandise;
    }

    public List<OrderPageTransactionDto> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<OrderPageTransactionDto> transactions) {
        this.transactions = transactions;
    }
}
