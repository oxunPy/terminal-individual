package com.example.rest.telegramBot.commands.operation;

import com.example.rest.common.Currency;
import com.example.rest.dto.ProductDto;
import com.example.rest.model.ClientBalance;
import com.example.rest.service.RestTelegramService;
import com.example.rest.telegramBot.commands.constants.core.Commands;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Calendar;

@Service
@RequiredArgsConstructor
public class Operations {

    private final RestTelegramService restTelegramService;

    public String getTodayOperations(Currency currency, String command) {
        if (command.equals(Commands.WHOLE_OPERATION))
            return "Cегодня:   \n\n" + getWholePeriodOperations(currency, LocalDate.now().minusDays(1), LocalDate.now());
        return "Cегодня:   \n\n" + getPeriodOperation(currency, LocalDate.now().minusDays(1), LocalDate.now(), command);
    }

    public String getThisWeekendOperations(Currency currency, String command) {
        LocalDate now = LocalDate.now();
        Calendar calendar = Calendar.getInstance();
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        if (command.equals(Commands.WHOLE_OPERATION))
            return "На этой неделе:   \n\n" + getWholePeriodOperations(currency, now.minusDays(dayOfWeek - DayOfWeek.MONDAY.ordinal()), now);

        return "На этой неделе:   \n\n" + getPeriodOperation(currency, now.minusDays(dayOfWeek - DayOfWeek.MONDAY.ordinal()), now, command);
    }

    public String getThisMonthOperations(Currency currency, String command) {
        LocalDate now = LocalDate.now();
        if (command.equals(Commands.WHOLE_OPERATION))
            return "В этом месяце:   \n\n" + getWholePeriodOperations(currency, LocalDate.of(now.getYear(), now.getMonth(), 1), now);
        return "В этом месяце:   \n\n" + getPeriodOperation(currency, LocalDate.of(now.getYear(), now.getMonth(), 1), now, command);
    }

    public String getPeriodOperation(Currency currency, LocalDate fromDate, LocalDate toDate, String command) {
        switch (command) {
            case Commands.GET_PAYMENT_BANK:
                return capitalize(Commands.GET_PAYMENT_BANK) + ":   " + restTelegramService.getPaymentBank(currency, fromDate, toDate) + " " + currency;
            case Commands.GET_PAYMENT_CASH:
                return capitalize(Commands.GET_PAYMENT_CASH) + ":   " + restTelegramService.getPaymentCash(currency, fromDate, toDate) + " " + currency;
            case Commands.GET_RECEIPT_BANK:
                return capitalize(Commands.GET_RECEIPT_BANK) + ":   " + restTelegramService.getReceiptBank(currency, fromDate, toDate) + " " + currency;
            case Commands.GET_RECEIPT_CASH:
                return capitalize(Commands.GET_RECEIPT_CASH) + ":   " + restTelegramService.getReceiptCash(currency, fromDate, toDate) + " " + currency;
            case Commands.GET_TOTAL_BALANCE_CLIENT:
                ClientBalance balance = restTelegramService.getTotalBalanceClient(currency, toDate);

                return capitalize(Commands.GET_TOTAL_BALANCE_CLIENT) + ":\n" + "    кредит = " + balance.getCredit() + " " + currency + "\n"
                        + "    списание средств = " + balance.getDebit() + " " + currency;
            case Commands.GET_TOTAL_RETURNED_AMOUNT_FROM_CLIENT:
                return Commands.GET_TOTAL_RETURNED_AMOUNT_FROM_CLIENT + ":   " + restTelegramService.getTotalReturnedAmountFromClient(currency, fromDate, toDate) + " " + currency;
        }
        return "";
    }

    public String getWholePeriodOperations(Currency currency, LocalDate fromDate, LocalDate toDate) {
        String result =
                Commands.GET_PAYMENT_CASH + ":   " + restTelegramService.getPaymentCash(currency, fromDate, toDate) + " " + currency + "\n\n" +
                        Commands.GET_PAYMENT_BANK + ":   " + restTelegramService.getPaymentBank(currency, fromDate, toDate) + " " + currency + "\n\n" +
                        Commands.GET_RECEIPT_CASH + ":   " + restTelegramService.getReceiptCash(currency, fromDate, toDate) + " " + currency + "\n\n" +
                        Commands.GET_RECEIPT_BANK + ":   " + restTelegramService.getReceiptBank(currency, fromDate, toDate) + " " + currency + "\n\n" +
                        Commands.GET_TOTAL_RETURNED_AMOUNT_FROM_CLIENT + ":   " + restTelegramService.getTotalReturnedAmountFromClient(currency, fromDate, toDate) + " " + currency + "\n\n" +
                        Commands.GET_TOTAL_BALANCE_CLIENT + ":\n" + "    кредит =  " + restTelegramService.getTotalBalanceClient(currency, toDate).getCredit() + " " + currency + "\n"
                        + "    списание средств =  " + restTelegramService.getTotalBalanceClient(currency, toDate).getDebit() + " " + currency;
        return result;
    }

    public String getSearchProductPrettify(ProductDto... products) {

        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < 10; i++) {
            sb.append(i + 1).append(") ").append(products[i].getProductName()).append("\n");
        }

        return sb.toString();
    }

    private String capitalize(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }

        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }
}
