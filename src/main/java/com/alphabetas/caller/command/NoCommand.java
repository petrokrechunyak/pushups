package com.alphabetas.caller.command;

import com.alphabetas.caller.comparator.StringLengthComparator;
import com.alphabetas.caller.model.CallerChat;
import com.alphabetas.caller.model.CallerName;
import com.alphabetas.caller.model.CallerUser;
import com.alphabetas.caller.model.enums.UserStates;
import com.alphabetas.caller.service.CallerChatService;
import com.alphabetas.caller.service.CallerNameService;
import com.alphabetas.caller.service.CallerUserService;
import com.alphabetas.caller.service.MessageService;
import com.alphabetas.caller.utils.AddNameUtils;
import com.alphabetas.caller.utils.CommandUtils;
import com.alphabetas.caller.utils.DeleteNameUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static com.alphabetas.caller.utils.CommandUtils.makeLink;

@Slf4j
public class NoCommand implements Command {
    private CallerUserService userService;
    private CallerChatService chatService;
    private CallerNameService nameService;
    private MessageService messageService;

    public static final Map<String, String> rpCommands = new HashMap<>(){{
        put("запросити на чай", "%s запросив(ла) на чай %s \uD83E\uDED6\uD83C\uDF6A");
        put("обійняти", "%s обійняв(ла) %s \uD83E\uDEC2");
        put("кохатися", "%s покохався(лася) з %s ❤\u200D\uD83D\uDD25");
        put("поцілувати", "%s поцілував(ла) \uD83D\uDE18 %s");
        put("посадити на гіляку", "%s посадив(ла) на гіляку %s \uD83D\uDE08\uD83E\uDEB5");
        put("вдарити", "%s вдарив(ла) \uD83E\uDD1C\uD83E\uDD15 %s");
        put("вкусити", "%s вкусив(ла) %s \uD83E\uDEE6");
        put("погодувати", "%s погодував(ла) %s \uD83C\uDF7D");
        put("потиснути руку", "%s потиснув(ла) руку \uD83E\uDD1D\uD83E\uDD1D %s");
        put("тягнути за вухо", "%s потягнув(ла) за вухо \uD83D\uDC42\uD83E\uDD0F %s");
        put("дати п'ять", "%s дав(ла) п'ять \uD83D\uDC4B %s");
        put("похвалити", "%s похвалив(ла) %s \uD83E\uDD17");
        put("погладити", "%s погладив(ла) %s ☺️");
        put("випити кров", "%s випив(ла) кров у %s \uD83E\uDE78");
        put("прочитати думки", "%s прочитав думки у %s \uD83E\uDEAC");
        put("чихнути", "%s чихнув(ла) на %s \uD83E\uDDA0");
        put("зненавидіти", "%s зненавидіти(ла) %s \uD83D\uDEA9");
        put("привітати", "%s привітав(ла) %s \uD83C\uDF82");
        put("записати в дез ноут", "%s записав(ла) в дез ноут %s \uD83D\uDCD3");
        put("звабити", "%s звабив(ла) %s \uD83D\uDE0F");
        put("пожаліти", "%s пожалів(ла) %s \uD83D\uDE22");
        put("знешкодити", "%s знешкодив(ла) %s \uD83D\uDC4E");
        put("вбити", "%s вбив(ла) %s ☠️");
        put("налякати", "%s налякав(ла) %s \uD83D\uDE40");
        put("обдурити", "%s обдурив(ла) %s \uD83E\uDD21");
        put("дмухнути", "%s дмухнув(ла) на %s \uD83D\uDE2E\u200D\uD83D\uDCA8");
        put("показати язик", "%s показав(ла) язик %s \uD83D\uDE1B");
        put("скуштувати", "%s скуштував(ла) %s \uD83E\uDD5F");
        put("допомогти", "%s допоміг(ла) %s \uD83E\uDD32");
        put("закінчити в", "%s закінчив(ла) у %s \uD83D\uDCA6");
        put("зґвалтувати", "%s зґвалтував(ла) %s \uD83D\uDC49\uD83D\uDC4C");
        put("віддатися", "%s віддався(лась) %s \uD83E\uDD2D");
        put("споїти", "%s споїв(ла) %s \uD83E\uDD74");
        put("напитись з", "%s напився(лась) з %s \uD83E\uDD74");
        put("заїбатися", "%s заїбався(лась) через %s \uD83E\uDD74");
        put("лизнути", "%s лизнув(ла) %s \uD83D\uDC45");

    }};

    public final static String[] specialArgs = new String[]{"/add",
            "кликун додай ім'я", "кликун додай",
            ".додати ім'я", ".додати"};

    public NoCommand(CallerUserService userService, CallerChatService chatService, CallerNameService nameService, MessageService messageService) {
        this.userService = userService;
        this.chatService = chatService;
        this.nameService = nameService;
        this.messageService = messageService;
    }

    @Override
    public void execute(Update update) {
        String msgText = update.getMessage().getText();
        log.info("Entered into NoCommand with text {} in chat {}", msgText,
                update.getMessage().getChat().getTitle());
        Long chatId = update.getMessage().getChatId();

        CallerChat chat = chatService.getById(chatId, update);
        CallerUser user = userService.getByUserIdAndCallerChat(chat, update);
        
        //check for rp commands
        checkForRp(update, user);

        // do action for state of user
        if (user.getUserState() != UserStates.OFF) {
            switch (user.getUserState()) {
                case ADD:
                    addState(chat, user, update);
                    break;
                case DELETE:
                    deleteState(chat, user, update);
                    break;
            }
            return;
        }

        // call user, if his name is in the chat
        callUser(chat, update);
    }

    private void checkForRp(Update update, CallerUser user) {
        String msgText = update.getMessage().getText().toLowerCase();
        Long chatId = user.getCallerChat().getId();
        String[] args = msgText.split("\n");
        String answer = rpCommands.get(args[0].trim());
        if(answer == null || update.getMessage().getReplyToMessage() == null) {
            return;
        }
        log.info("arg[0] is rp command, replyToMessage isn't null");
        Long replyToMessage = update.getMessage().getMessageId().longValue();

        StringBuilder builder = new StringBuilder(answer);
        User to = update.getMessage().getReplyToMessage().getFrom();
        if(args.length > 1 && !to.getIsBot()) {
            builder.append("\nЗ словами: \"<b>")
                    .append(StringUtils.replaceIgnoreCase(update.getMessage().getText(),
                            args[0] + "\n", ""))
                    .append("</b>\"");
        }
        log.info("temporary builder value: {}", builder);
        // if it did with caller
        if(to.getIsBot() && to.getUserName().equals("caller_ua_bot")) {
            switch (args[0].trim()) {
                case "вдарити":
                case "вкусити":
                case "посадити на гіляку":
                case "тягнути за вухо":
                case "випити кров":
                case "чихнути":
                case "зненавидіти":
                case "записати в дез ноут":
                case "знешкодити":
                case "вбити":
                case "налякати":
                case "показати язик":
                case "обдурити":
                case "прочитати думки":
                case "споїти":
                case "зґвалтувати":
                case "заїбатися":
                    builder.append("\nЗ словами: \"<b>Мене не обдуриш!</b>\"");
                    messageService.sendMessage(chatId,
                            String.format(builder.toString(), makeLink(to.getId(), "Кликун"),
                                    makeLink(user.getUserId(), user.getFirstname())), replyToMessage);
                    break;
                case "обійняти":
                case "поцілувати":
                case "кохатися":
                case "звабити":
                case "напитися з":
                    if(user.getUserId().equals(731921794L)) {
                        builder.append("\nЗ словами: \uD83E\uDD70");
                        messageService.sendMessage(chatId,
                                String.format(builder.toString(), makeLink(to.getId(), "Кликун"),
                                        makeLink(user.getUserId(), user.getFirstname())), replyToMessage);
                    } else {
                        messageService.sendMessage(chatId,
                                "Я не люблю це, вибач(", replyToMessage);
                    }
                    break;
                case "закінчити в":
                    builder.append("\nЗ словами: \"<b>Готово, перевіряй!</b>\"");
                    messageService.sendMessage(chatId,
                            String.format(builder.toString(), makeLink(to.getId(), "Кликун"),
                                    makeLink(user.getUserId(), user.getFirstname())), replyToMessage);
                    break;
                default:
                    messageService.sendMessage(chatId,
                            String.format(builder.toString(), makeLink(user.getUserId(), user.getFirstname()),
                                    makeLink(to.getId(), to.getFirstName())), replyToMessage);
            }
            return;
        }
        // if forever alone
        if(to.getId().equals(user.getUserId())) {
            messageService.sendMessage(chatId,
                    "Так не можна! ", replyToMessage);
            return;
        }
        // if user is bot
        if(to.getIsBot()) {
            messageService.sendMessage(chatId,
                    "РП-комадни не можна виконувати з ботами", replyToMessage);
            return;
        }
        log.info("all right, continue...");
        messageService.sendMessage(chatId,
                String.format(builder.toString(), makeLink(user.getUserId(), user.getFirstname()),
                        makeLink(to.getId(), to.getFirstName())), replyToMessage);
    }

    public void addState(CallerChat chat, CallerUser user, Update update) {
        String msgText = update.getMessage().getText();
        String returnMessage = AddNameUtils.saveNames(msgText, user, chat);

        user.setUserState(UserStates.OFF);
        userService.save(user);

        messageService.sendMessage(chat.getId(), returnMessage);
    }

    public void deleteState(CallerChat chat, CallerUser user, Update update) {
        String msgText = update.getMessage().getText();
        String returnMessage = DeleteNameUtils.deleteNames(msgText, user, chat);

        user.setUserState(UserStates.OFF);
        userService.save(user);

        messageService.sendMessage(chat.getId(), returnMessage);
    }

    public void callUser(CallerChat chat, Update update) {
        String msgText = " " + update.getMessage().getText() + " ";
        boolean send = false;
        chat.setCallerNames(chat.getCallerNames()
                .stream()
                .sorted(new StringLengthComparator())
                .collect(Collectors.toCollection(LinkedHashSet::new)));

        for (CallerName name : chat.getCallerNames()) {
            if (name.getCallerUser().getUserId().equals(update.getMessage().getFrom().getId())) {
                continue;
            }
            if (update.getMessage().getReplyToMessage() != null) {
                if (name.getCallerUser().getUserId().equals(update.getMessage().getReplyToMessage().getFrom().getId())) {
                    continue;
                }
            }
            String decrypted = CommandUtils.decryptSpace(name.getName());
            if (contains(msgText, decrypted)) {
                msgText = replace(msgText, decrypted, name);
                send = true;
            }
        }

        if (send) {
            msgText = CommandUtils.decryptSpace(msgText);
            SendMessage sendMessage = new SendMessage(chat.getId().toString(), msgText);
            sendMessage.enableHtml(true);
            sendMessage.setReplyToMessageId(update.getMessage().getMessageId());
            messageService.sendMessage(sendMessage);
        }
    }

    private static boolean contains(String text, String toSearch) {
        text = text.toLowerCase();
        toSearch = toSearch.toLowerCase();
        String exceptWords = "[^" + CommandUtils.TEMPLATE_REGEX + "]";
        String pattern = exceptWords + toSearch + exceptWords;
        return Pattern.compile(pattern, Pattern.CASE_INSENSITIVE).matcher(text).find();
    }

    private static String replace(String text, String toSearch, CallerName name) {
        toSearch = toSearch.toLowerCase();
        String exceptWords = "[^" + CommandUtils.TEMPLATE_REGEX + "]";
        Pattern pattern = Pattern.compile("(" + exceptWords + toSearch + exceptWords + ")", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(text.toLowerCase());
        while (matcher.find()) {
            String old = matcher.group(1).toLowerCase();
            text = StringUtils.replaceIgnoreCase(text, old,
                    old.charAt(0) +
                            makeLink(
                            name.getCallerUser().getUserId(),
                            name.getName()) +
                            old.charAt(old.length()-1
                    ));
        }
        return text;
    }
}
