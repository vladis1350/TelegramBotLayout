package com.example.aducarBot.botapi;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Defines message handlers for each state.
 */
@Component
public class BotStateContext {
    private Map<BotState, InputMessageHandler> messageHandlers = new HashMap<>();

    public BotStateContext(List<InputMessageHandler> messageHandlers) {
        messageHandlers.forEach(handler -> this.messageHandlers.put(handler.getHandlerName(), handler));
    }

    public void processInputMessage(BotState currentState, Message message) {
        InputMessageHandler currentMessageHandler = findMessageHandler(currentState);
        currentMessageHandler.handle(message);
    }

    private InputMessageHandler findMessageHandler(BotState currentState) {
        if (isFillingProfileState(currentState)) {
            return messageHandlers.get(BotState.ASK_PERSONAL_INFO);
        } else if (isSupplementProfileState(currentState)) {
            return messageHandlers.get(BotState.ASK_SUPPLEMENT_PERSONAL_INFO);
        } else if (isAuthorizationState(currentState)) {
            return messageHandlers.get(BotState.USER_AUTHORIZATION);

        } else if (isFinishFillingBodyParamState(currentState)) {
            return messageHandlers.get(BotState.ASK_FINISH_BODY_PARAM);

        } else if (isFillingReport(currentState)) {
            return messageHandlers.get(BotState.ASK_REPORT);
        } else if (isFillingGoals(currentState)) {
            return messageHandlers.get(BotState.ASK_ADMIN_GOALS);
        } else if (isFillingMarathonState(currentState)) {
            return messageHandlers.get(BotState.START_NEW_MARATHON);
        } else if (isSendMessageAllState(currentState)) {
            return messageHandlers.get(BotState.ASK_MESSAGE_FOR_USER);
        } else if (isOpenCustomerInfoState(currentState)) {
            return messageHandlers.get(BotState.OPEN_CUSTOMER_INFO);
        } else if (isFillingMealPlanState(currentState)) {
            return messageHandlers.get(BotState.ASK_ADMIN_MEAL_PLAN);
        }
        else if (isFilingPasswordBot(currentState)) {
            return messageHandlers.get(BotState.PASSWORD_ADMIN);
        }
        return messageHandlers.get(currentState);
    }

    private boolean isFilingPasswordBot(BotState currentState) {
        switch (currentState) {
            case NEW_PASSWORD:
            case PASSWORD_ADMIN:
            case EDIT_PASSWORD:
            case REPEAT_NEW_PASSWORD:
            case PASSWORD_SET:
                return true;
            default:
                return false;
        }
    }

    private boolean isAuthorizationState(BotState currentState) {
        switch (currentState) {
            case USER_AUTHORIZATION:
            case ASK_PASSWORD:
                return true;
            default:
                return false;

        }
    }

    private boolean isFillingMealPlanState(BotState currentState) {
        switch (currentState) {
            case ASK_ADMIN_LOAD_MEAL_PLAN1:
            case ASK_ADMIN_LOAD_MEAL_PLAN2:
            case ASK_ADMIN_LOAD_MEAL_PLAN3:
            case ASK_ADMIN_NUMBER_FOR_PLAN3:
            case ASK_ADMIN_NUMBER_FOR_PLAN2:
            case ASK_ADMIN_NUMBER_FOR_PLAN1:
            case ASK_ADMIN_LOAD_MEAL_PLAN_BASKET:
            case ASK_ADMIN_DAYS_FOR_FOOD_BASKET:
            case ASK_ADMIN_PLAN:
            case ASK_ADMIN_ADD_MEAL_PLAN:
            case MEAL_PLAN_FILLED:
                return true;
            default:
                return false;
        }
    }

    private boolean isFillingMarathonState(BotState currentState) {
        switch (currentState) {
            case START_NEW_MARATHON:
            case ASK_DATE_START_MARATHON:
            case ASK_DATE_FINISH_MARATHON:
                return true;
            default:
                return false;
        }
    }

    private boolean isFillingProfileState(BotState currentState) {
        switch (currentState) {
            case ASK_NAME:
            case ASK_AGE:
            case ASK_HEIGHT:
            case ASK_WEIGHT:
            case ASK_PERSONAL_INFO:
            case PROFILE_FILLED:
            case SHOW_MAIN_MENU:
                return true;
            default:
                return false;
        }
    }

    private boolean isFillingReport(BotState currentState) {

        switch (currentState) {
            case ASK_REPORT:
            case ASK_GOALS:
            case ASK_PHOTO:
            case ASK_PHOTO_WEIGHER:
            case ASK_TASK_ONE:
            case ASK_TASK_TWO:
            case ASK_TASK_THREE:
            case ASK_TASK_FOUR:
            case ASK_TASK_FIVE:
            case ASK_TASK_SIX:
            case FILLING_REPORT:
                return true;
            default:
                return false;
        }
    }

    private boolean isSupplementProfileState(BotState currentState) {
        switch (currentState) {
            case ASK_BELLY:
            case ASK_CHEST:
            case ASK_HIPS:
            case ASK_HIP:
            case ASK_ARM:
            case ASK_NECK:
            case ASK_SHIN:
            case ASK_DATE:
            case ASK_WAIST:
            case ASK_START_PHOTO:
            case ASK_START_PHOTO_WEIGHER:
            case ASK_FINISH_PHOTO_WEIGHER:
            case ASK_FINISH_PHOTO_BODY:
            case PERSONAL_INFO_FILLED:
            case ASK_SUPPLEMENT_PERSONAL_INFO:
                return true;
            default:
                return false;
        }
    }

    private boolean isFinishFillingBodyParamState(BotState currentState) {
        switch (currentState) {
            case ASK_FINISH_WEIGHT:
            case ASK_FINISH_BELLY:
            case ASK_FINISH_CHEST:
            case ASK_FINISH_HIPS:
            case ASK_FINISH_HIP:
            case ASK_FINISH_ARM:
            case ASK_FINISH_NECK:
            case ASK_FINISH_SHIN:
            case ASK_FINISH_WAIST:
            case FINISH_BODY_PARAM_FILLED:
            case ASK_FINISH_BODY_PARAM:
                return true;
            default:
                return false;
        }
    }

    private boolean isFillingGoals(BotState currentState) {
        switch (currentState) {
            case FILLING_GOALS:
            case ASK_ADMIN_TASK_FIVE:
            case ASK_ADMIN_TASK_ONE:
            case ASK_ADMIN_TASK_FOUR:
            case ASK_ADMIN_TASK_THREE:
            case ASK_ADMIN_TASK_TWO:
            case ASK_ADMIN_TASK_SIX:
            case ASK_ADMIN_EDIT_TASK_SIX:
            case ASK_ADMIN_EDIT_TASK_FIVE:
            case ASK_ADMIN_EDIT_TASK_FOUR:
            case ASK_ADMIN_EDIT_TASK_THREE:
            case ASK_ADMIN_EDIT_TASK_TWO:
            case ASK_ADMIN_EDIT_TASK_ONE:
            case ASK_ADMIN_EDIT_TIMESTAMP:
            case ASK_ADMIN_NUMBER_GOAL:
            case ASK_ADMIN_NUMBER_DEL_GOAL:
            case GOALS_FILLED:
                return true;
            default:
                return false;
        }
    }

    private boolean isSendMessageAllState(BotState currentState) {
        switch (currentState) {
            case INPUT_MESSAGE_TO_ALL:
            case ADMIN_SEND_PRIVATE_MESSAGE:
            case MESSAGE_SENT:
                return true;
            default:
                return false;
        }
    }

    private boolean isOpenCustomerInfoState(BotState currentState) {
        switch (currentState) {
            case ASK_NUMBER_CLIENT:
            case VIEW_PROFILE:
                return true;
            default:
                return false;
        }
    }
}