package at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.orderpage.OrderPageTransactionDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.Transaction;
import org.mapstruct.Mapper;

import java.util.Comparator;
import java.util.List;
import java.util.Set;

@Mapper
public abstract class TransactionMapper {
    public List<OrderPageTransactionDto> transactionsToOrderPageTransactionDtos(Set<Transaction> transactions) {
        return transactions.stream()
            .sorted(Comparator.comparing(Transaction::getTransactionTs))
            .map(this::transactionToOrderPageTransactionDto)
            .toList();
    }

    public OrderPageTransactionDto transactionToOrderPageTransactionDto(Transaction transaction) {
        OrderPageTransactionDto dto = new OrderPageTransactionDto();
        dto.setId(transaction.getId());
        dto.setDeductedAmount(transaction.getDeductedAmount());
        dto.setDeductedPoints(transaction.getDeductedPoints());
        return dto;
    }
}
