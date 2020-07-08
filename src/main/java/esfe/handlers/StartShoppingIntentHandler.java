package esfe.handlers;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.model.*;
import com.amazon.ask.request.Predicates;
import esfe.handlers.utils.Product;
import esfe.handlers.utils.Utils;

import java.util.*;

public class StartShoppingIntentHandler implements RequestHandler {

    @Override
    public boolean canHandle(HandlerInput input) {
        return input.matches(Predicates.intentName("StartShoppingIntentHandler"));
    }

    @Override
    public Optional<Response> handle(HandlerInput input) {
        Request request = input.getRequestEnvelope().getRequest();
        IntentRequest intentRequest = (IntentRequest) request;
        Intent intent = intentRequest.getIntent();
        Map<String, Slot> slots = intent.getSlots();

        Slot buyItemSlot = slots.get(Utils.ITEM_SLOT);
        String speechText, repromptText;

        if (buyItemSlot != null && buyItemSlot.getResolutions() != null && buyItemSlot.getResolutions().toString().contains("ER_SUCCESS_MATCH")){
            String buyItem = buyItemSlot.getValue();
            List<Product> productList = new ArrayList<>();
            StringBuilder sb = new StringBuilder();
            try{
                productList = Utils.parse(buyItem);
                for (int i = 0; i < Math.min(2, productList.size()); ++i){
                    Product product = productList.get(i);
                    sb.append(i+1+" ").append(product.getDescription()).append(" price is ").append(product.getPrice()).append(",");
                }
                if (productList.size() >= 3){
                    sb.append(3+" ").append(productList.get(2).getDescription()).append(" price is ").append(productList.get(2).getPrice()).append(".");
                }
            } catch (Exception e){
                e.printStackTrace();
            }
            if (sb.length() == 0){
                speechText = String.format("please tell me a valid item");
                repromptText = String.format("you can try again by say get me milk");
            } else {
                input.getAttributesManager().setSessionAttributes(Collections.singletonMap(Utils.ITEM_KEY, sb.toString()));
                speechText = String.format("here is all %s list", buyItem);
                repromptText = String.format("you can start to select the %s by saying i want to select", buyItem);
            }
        } else {
            speechText = String.format("please tell me a valid item");
            repromptText = String.format("you can try again by say get me milk");
        }


        return input.getResponseBuilder()
                .withSimpleCard("HelloWorld", speechText)
                .withSpeech(speechText)
                .withReprompt(repromptText)
                .withShouldEndSession(false)
                .build();
    }

}