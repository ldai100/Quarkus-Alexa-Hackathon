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
        Slot itemSlot = slots.get(Utils.ITEM_SLOT);
        String speechText;
        if (itemSlot != null && itemSlot.getResolutions() != null && itemSlot.getResolutions().toString().contains("ER_SUCCESS_MATCH")){
            String buyItem = itemSlot.getValue();
            input.getAttributesManager().setSessionAttributes(Collections.singletonMap(Utils.ITEM_KEY, buyItem));
//            speechText = String.format("Now i know the item you want to buy is %s.", buyItem);
            List<Product> productList = new ArrayList<>();
            StringBuilder sb = new StringBuilder();
            try {
                productList = Utils.parse(buyItem);
            } catch (Exception e){
                e.printStackTrace();
            }
//                for (int i = 0; i < Math.min(2, productList.size()); ++i){
//                    Product product = productList.get(i);
//                    sb.append(product.toString());
//                }
//                if (productList.size() >= 3){
//                    sb.append(3+" ").append(productList.get(2).getDescription()).append(" price is ").append(productList.get(2).getPrice()).append(".");
//                }
            if (sb.length() == 0){
                speechText = String.format("please tell me a valid item, you can try again by saying get me milk");
            } else {
                speechText = String.format("Now i know the item you want to buy is %s. we found it. %s", buyItem, productList.size());
            }
        } else {
            speechText = "Please provide a valid item, we have milk, beef";
        }

        return input.getResponseBuilder()
                .withSimpleCard("HelloWorld", speechText)
                .withSpeech(speechText)
                .build();
    }

}