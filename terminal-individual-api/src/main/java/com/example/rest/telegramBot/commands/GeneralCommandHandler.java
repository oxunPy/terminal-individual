package com.example.rest.telegramBot.commands;

import com.example.rest.common.BotState;
import com.example.rest.common.Currency;
import com.example.rest.constant.Status;
import com.example.rest.model.BotUserModel;
import com.example.rest.service.RestTelegramService;
import com.example.rest.telegramBot.calendar.CalendarMarkup;
import com.example.rest.telegramBot.commands.constants.CommandMessages;
import com.example.rest.telegramBot.commands.constants.TextCodeMessages;
import com.example.rest.telegramBot.commands.constants.core.Commands;
import com.example.rest.telegramBot.commands.constants.core.TextCode;
import com.example.rest.telegramBot.commands.operation.Operations;
import com.example.rest.telegramBot.service.TelegramService;
import com.example.rest.telegramBot.utils.CalendarMarkupUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import javax.annotation.Resource;

@Service
@RequiredArgsConstructor
public class GeneralCommandHandler implements CommandHandler<Message> {

    private final TelegramService telegramService;
    private final RestTelegramService restTelegramService;
    private final Operations operations;

    @Resource
    private CommandMessages commandMessages;
    @Resource
    private TextCodeMessages textCodeMessages;

    @Override
    public void executeCommand(Message message, String command) throws TelegramApiException {
        BotUserModel userModel = restTelegramService.getBotUser(message.getChatId());
        Currency currency = userModel != null ? userModel.getCurrency() : Currency.USD;
        String commandMethod = userModel != null ? userModel.getCommand() : null;

//        if (command.equals("Поиск по продукту❓")) {
//            telegramService.executeMessage(SendMessage.builder()
//                    .chatId(message.getChatId().toString())
//                    .text("product name like ?")
//                    .build());
//
//            userModel.setBotState(BotState.SEARCH_BY_PRODUCT);
//            userService.saveUser(userModel);
//            return;
//        }
//
//        else if(userModel.getBotState() == BotState.SEARCH_BY_PRODUCT && !message.getText().isEmpty()) {
//
//            telegramService.executeMessage(SendMessage.builder()
//                            .chatId(message.getChatId().toString())
//                            .text(operations.getSearchProductPrettify(userService.getSearchProducts(message.getText())))
//                    .build());
//        }

        if (command.equals(Commands.START)) {
            if (!restTelegramService.userExists(message.getChatId())) { // creating user
                telegramService.executeMessage(SendMessage.builder()
                        .text(commandMessages.getMessage(Commands.START))
                        .chatId(message.getChatId().toString())
                        .replyMarkup(CommandMarkups.getShareContactKeyBoard())
                        .build());
            } else if (userModel != null && userModel.getStatus() == Status.ACTIVE) {
                telegramService.executeMessage(SendMessage.builder()
                        .text(Commands.WHOLE_OPERATION)
                        .chatId(message.getChatId().toString())
                        .replyMarkup(CommandMarkups.getWholeOperationKeyboard())
                        .build());
            }
        } else if (command.equals(Commands.UPDATE)) {
            // make bot user status active
            if (restTelegramService.activateUser(message.getChatId())) {
                telegramService.executeMessage(SendMessage.builder()
                        .text(commandMessages.getMessage(Commands.UPDATE))
                        .chatId(message.getChatId().toString())
                        .build());
                telegramService.executeMessage(SendMessage.builder()
                        .text(Commands.WHOLE_OPERATION)
                        .chatId(message.getChatId().toString())
                        .replyMarkup(CommandMarkups.getWholeOperationKeyboard())
                        .build());
            } else {
                telegramService.executeMessage(SendMessage.builder()
                        .text(textCodeMessages.getMessage(TextCode.SEND_TO_ADMIN_REQUEST))
                        .chatId(message.getChatId().toString())
                        .replyMarkup(CommandMarkups.getUpdateMarkup())
                        .build());
            }
        } else if (userModel != null && userModel.getStatus() == Status.ACTIVE) {
            switch (command) {
                case Commands.WHOLE_OPERATION:
                    userModel.setCommand(Commands.WHOLE_OPERATION);
                    userModel.setBotState(BotState.CURRENCY_STATE);
                    restTelegramService.saveOrEditBotUser(userModel);
                    telegramService.executeMessage(SendMessage.builder()
                            .text(commandMessages.getMessage(Commands.WHOLE_OPERATION))
                            .chatId(String.valueOf(message.getChatId()))
                            .replyMarkup(CommandMarkups.getCurrencyKeyboardMarkup())
                            .build());
                    break;

                case Commands.UZBEK_CURRENCY:
                    userModel.setBotState(BotState.DATE_STATE);
                    userModel.setCurrency(Currency.UZS);
                    restTelegramService.saveOrEditBotUser(userModel);
                    telegramService.executeMessage(SendMessage.builder()
                            .text(textCodeMessages.getMessage(TextCode.UZS))
                            .chatId(String.valueOf(message.getChatId()))
                            .replyMarkup(CommandMarkups.getDateMarkup())
                            .build());
                    break;

                case Commands.AMERICAN_CURRENCY:
                    userModel.setBotState(BotState.DATE_STATE);
                    userModel.setCurrency(Currency.USD);
                    restTelegramService.saveOrEditBotUser(userModel);
                    telegramService.executeMessage(SendMessage.builder()
                            .text(textCodeMessages.getMessage(TextCode.USD))
                            .chatId(String.valueOf(message.getChatId()))
                            .replyMarkup(CommandMarkups.getDateMarkup())
                            .build());
                    break;

                case Commands.TODAY_OPERATION:
                    telegramService.executeMessage(SendMessage.builder()
                            .text(operations.getTodayOperations(currency, commandMethod))
                            .chatId(String.valueOf(message.getChatId()))
                            .replyMarkup(CommandMarkups.getDateMarkup())
                            .build());
                    break;

                case Commands.THIS_WEEKEND:
                    telegramService.executeMessage(SendMessage.builder()
                            .text(operations.getThisWeekendOperations(currency, commandMethod))
                            .chatId(String.valueOf(message.getChatId()))
                            .replyMarkup(CommandMarkups.getDateMarkup())
                            .build());
                    break;

                case Commands.THIS_MONTH:
                    telegramService.executeMessage(SendMessage.builder()
                            .text(operations.getThisMonthOperations(currency, commandMethod))
                            .chatId(String.valueOf(message.getChatId()))
                            .replyMarkup(CommandMarkups.getDateMarkup())
                            .build());
                    break;

                case Commands.OTHER_DATE:

                    InlineKeyboardMarkup dateToDateCalendar = new CalendarMarkupUtils().getFromDateToDateCalendar();
                    InlineKeyboardMarkup toDateCalendar = new CalendarMarkupUtils().convertToToDateCalendarMarkup(new CalendarMarkup().getCalendarInstance());

                    telegramService.executeMessage(SendMessage.builder()
                            .text(textCodeMessages.getMessage(TextCode.SELECT_OTHER_DATE))
                            .chatId(message.getChatId().toString())
                            .replyMarkup(CommandMarkups.getCalendarMarkup())
                            .build());
                    telegramService.executeMessage(SendMessage.builder()
                            .text(textCodeMessages.getMessage(TextCode.OTHER_DATE))
                            .chatId(String.valueOf(message.getChatId()))
                            .replyMarkup(commandMethod != null && commandMethod.equals(Commands.GET_TOTAL_BALANCE_CLIENT) ? toDateCalendar : dateToDateCalendar)
                            .build());

                    break;

                case Commands.GET_PAYMENT_CASH:
                    userModel.setCommand(Commands.GET_PAYMENT_CASH);
                    userModel.setBotState(BotState.CURRENCY_STATE);
                    restTelegramService.saveOrEditBotUser(userModel);

                    telegramService.executeMessage(SendMessage.builder()
                            .text(commandMessages.getMessage(Commands.GET_PAYMENT_CASH))
                            .chatId(String.valueOf(message.getChatId()))
                            .replyMarkup(CommandMarkups.getCurrencyKeyboardMarkup())
                            .build());
                    break;

                case Commands.GET_PAYMENT_BANK:
                    userModel.setCommand(Commands.GET_PAYMENT_CASH);
                    userModel.setBotState(BotState.CURRENCY_STATE);
                    restTelegramService.saveOrEditBotUser(userModel);

                    telegramService.executeMessage(SendMessage.builder()
                            .text(commandMessages.getMessage(Commands.GET_PAYMENT_BANK))
                            .chatId(String.valueOf(message.getChatId()))
                            .replyMarkup(CommandMarkups.getCurrencyKeyboardMarkup())
                            .build());
                    break;

                case Commands.GET_RECEIPT_BANK:
                    userModel.setCommand(Commands.GET_PAYMENT_CASH);
                    userModel.setBotState(BotState.CURRENCY_STATE);
                    restTelegramService.saveOrEditBotUser(userModel);

                    telegramService.executeMessage(SendMessage.builder()
                            .text(commandMessages.getMessage(Commands.GET_RECEIPT_BANK))
                            .chatId(String.valueOf(message.getChatId()))
                            .replyMarkup(CommandMarkups.getCurrencyKeyboardMarkup())
                            .build());
                    break;

                case Commands.GET_RECEIPT_CASH:
                    userModel.setCommand(Commands.GET_PAYMENT_CASH);
                    userModel.setBotState(BotState.CURRENCY_STATE);
                    restTelegramService.saveOrEditBotUser(userModel);

                    telegramService.executeMessage(SendMessage.builder()
                            .text(commandMessages.getMessage(Commands.GET_RECEIPT_CASH))
                            .chatId(String.valueOf(message.getChatId()))
                            .replyMarkup(CommandMarkups.getCurrencyKeyboardMarkup())
                            .build());
                    break;

                case Commands.GET_TOTAL_BALANCE_CLIENT:
                    userModel.setCommand(Commands.GET_PAYMENT_CASH);
                    userModel.setBotState(BotState.CURRENCY_STATE);
                    restTelegramService.saveOrEditBotUser(userModel);

                    telegramService.executeMessage(SendMessage.builder()
                            .text(commandMessages.getMessage(Commands.GET_TOTAL_BALANCE_CLIENT))
                            .chatId(String.valueOf(message.getChatId()))
                            .replyMarkup(CommandMarkups.getCurrencyKeyboardMarkup())
                            .build());
                    break;

                case Commands.GET_TOTAL_RETURNED_AMOUNT_FROM_CLIENT:
                    userModel.setCommand(Commands.GET_PAYMENT_CASH);
                    userModel.setBotState(BotState.CURRENCY_STATE);
                    restTelegramService.saveOrEditBotUser(userModel);

                    telegramService.executeMessage(SendMessage.builder()
                            .text(commandMessages.getMessage(Commands.GET_TOTAL_RETURNED_AMOUNT_FROM_CLIENT))
                            .chatId(String.valueOf(message.getChatId()))
                            .replyMarkup(CommandMarkups.getCurrencyKeyboardMarkup())
                            .build());
                    break;


                case Commands.BACK:
                    userModel.setBotState(userModel.getBotState().getParentState() != null ? userModel.getBotState().getParentState() : BotState.START_STATE);
                    restTelegramService.saveOrEditBotUser(userModel);

                    telegramService.executeMessage(SendMessage.builder()
                            .text(Commands.BACK)
                            .chatId(message.getChatId().toString())
                            .replyMarkup(CommandMarkups.getMenuMarkupBy(userModel.getBotState()))
                            .build());
            }
        }
    }
}
