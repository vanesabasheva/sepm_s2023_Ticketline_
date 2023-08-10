package at.ac.tuwien.sepm.groupphase.backend.service;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.OrderDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.OrderHistoryDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.bookings.BookingDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.orderpage.OrderPageDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.Order;
import at.ac.tuwien.sepm.groupphase.backend.exception.ConflictException;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.exception.UnauthorizedException;
import at.ac.tuwien.sepm.groupphase.backend.exception.ValidationException;
import jakarta.servlet.http.HttpServletResponse;

import java.util.List;

/**
 * The interface Order service.
 */
public interface OrderService {

    /**
     * Find all orders by user id.
     *
     * @param id the user id
     * @return the list of orders by the user
     * @throws NotFoundException if the user does not exist
     * @throws ValidationException if the user id is not valid
     */
    List<OrderHistoryDto> getOrderHistory(Integer id) throws NotFoundException, ValidationException;

    /**
     * Buy tickets returns order dto.
     *
     * @param userId the user id
     * @return the order dto
     */
    OrderDto buyTickets(Integer userId, BookingDto bookingDto) throws ConflictException, ValidationException;


    /**
     * Cancel items from order.
     *
     * @param userId the user id
     * @param orderId the order id
     * @param ticketIds the ticket ids
     * @param merchandiseIds the merchandise ids
     * @throws ValidationException if the order dto is not valid
     * @throws ConflictException if the request conflicts with the current state of the system
     * @throws UnauthorizedException if the user is not authorized to perform this action
     */
    void cancelItems(Integer userId, Integer orderId, Integer[] ticketIds, Integer[] merchandiseIds) throws ValidationException, UnauthorizedException, ConflictException;

    /**
     * Cancel order.
     *
     * @param userId the user id
     * @param orderId the order id
     * @throws ConflictException if the request conflicts with the current state of the system
     * @throws UnauthorizedException if the user is not authorized to perform this action
     */
    void cancelOrder(Integer userId, Integer orderId) throws UnauthorizedException, ConflictException;

    /**
     * Get order.
     *
     * @param userId the user id
     * @param orderId the order id
     * @return the order page dto
     * @throws UnauthorizedException if the user is not authorized to perform this action
     */
    Order getOrder(Integer userId, Integer orderId) throws UnauthorizedException;

    /**
     * Get a transaction.
     *
     * @param userId the user id
     * @param transactionId the transaction id
     * @param response the http response
     */
    void getTransaction(Integer userId, Integer transactionId, HttpServletResponse response) throws UnauthorizedException;
}
