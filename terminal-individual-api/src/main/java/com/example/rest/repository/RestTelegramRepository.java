package com.example.rest.repository;

import com.example.rest.data_interface.BalanceInterface;
import com.example.rest.data_interface.UserInterface;
import com.example.rest.entity.RootEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;
import java.util.Optional;

@Repository
public interface RestTelegramRepository extends JpaRepository<RootEntity, Long> {

    @Query(nativeQuery = true, value = "select * from get_receipt_cash(:currency_id, :from_date, :to_date)")
    Optional<BigDecimal> getReceiptCash(
            @Param("currency_id") Integer currencyId, @Param("from_date") LocalDate fromDate, @Param("to_date") LocalDate toDate);


    @Query(nativeQuery = true, value = "select * from get_payment_cash(:currency_id, :from_date, :to_date)")
    Optional<BigDecimal> getPaymentCash(
            @Param("currency_id") Integer currencyId, @Param("from_date") LocalDate fromDate, @Param("to_date") LocalDate toDate);

    @Query(nativeQuery = true, value = "select * from get_receipt_bank(:currency_id, :from_date, :to_date)")
    Optional<BigDecimal> getReceiptBank(
            @Param("currency_id") Integer currencyId, @Param("from_date") LocalDate fromDate, @Param("to_date") LocalDate toDate);

    @Query(nativeQuery = true, value = "select * from get_payment_bank(:currency_id, :from_date, :to_date)")
    Optional<BigDecimal> getPaymentBank(
            @Param("currency_id") Integer currencyId, @Param("from_date") LocalDate fromDate, @Param("to_date") LocalDate toDate);

    @Query(nativeQuery = true, value = "select * from get_total_returned_amount_from_client(:currency_id, :from_date, :to_date)")
    Optional<BigDecimal> getTotalReturnedAmountFromClient(
            @Param("currency_id") Integer currencyId, @Param("from_date") LocalDate fromDate, @Param("to_date") LocalDate toDate);

    @Query(nativeQuery = true, value = "select * from get_total_balance_client(:currency_id, :to_date)")
    Optional<BalanceInterface> getTotalBalanceClient(@Param("currency_id") Integer currencyId, @Param("to_date") LocalDate toDate);
    @Query(nativeQuery = true, value =  "SELECT\n" +
                                        "         save_bot_user(:first_name ,:last_name, :command, :chat_id, :bot_state, :contact, :currency)")
    Integer save(@Param("first_name") String firstName, @Param("last_name") String lastName,
                 @Param("command") String command, @Param("chat_id") Long chatId, @Param("bot_state") String botState, @Param("contact") String contact, @Param("currency") String currency);

    @Query(nativeQuery = true, value = "select  " +
                                            "id as id, status as status, bot_state as botState, chat_id as chatId, \n" +
                                            "first_name as firstName, last_name as lastName, user_name as userName, \n" +
                                            "command as command, contact as contact, currency as currency \n" +
                                            "from bot_users \n" +
                                            "where chat_id = :chat_id")
    Optional<UserInterface> getBotUserByChatId(@Param("chat_id") Long chatId);


    @Query(value = "SELECT EXISTS(SELECT 1 FROM bot_users WHERE chat_id = :chat_id)",nativeQuery = true)
    boolean existsUserByChatId(@Param("chat_id") Long chatId);

    @Modifying
    @Query(nativeQuery = true, value = "UPDATE bot_users SET STATUS = 2 WHERE chat_id = :chat_id\n")
    int activateUser(@Param("chat_id") Long chatId);
}
