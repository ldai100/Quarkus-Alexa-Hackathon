package esfe.handlers;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.model.*;
import com.amazon.ask.request.Predicates;
import esfe.ShoppingStreamHandler;
import esfe.handlers.utils.Utils;
import org.apache.http.HttpHost;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.*;

import static esfe.handlers.utils.Utils.*;

public class ConfirmIntentHandler implements RequestHandler {

    RestHighLevelClient client = new RestHighLevelClient(
            RestClient.builder(
                    new HttpHost("3.22.175.238", 9200, "http")));

    @Override
    public boolean canHandle(HandlerInput input) {
        return input.matches(Predicates.intentName("ConfirmIntentHandler"));
    }

    private static final Logger log = LoggerFactory.getLogger(ShoppingStreamHandler.class);

    @Override
    public Optional<Response> handle(HandlerInput input) {
        log.error("get in ConfirmIntentHandler");

        Request request = input.getRequestEnvelope().getRequest();
        IntentRequest intentRequest = (IntentRequest) request;
        Intent intent = intentRequest.getIntent();
        Map<String, Slot> slots = intent.getSlots();
        Slot cartSlot = slots.get(Utils.CART_SLOT);
        String speechText = null;
//        String item =  (String) input.getAttributesManager().getSessionAttributes().get(ITEM_KEY);

        log.error("ConfirmIntentHandler  ==> check slot ");
        if (cartSlot != null && cartSlot.getResolutions() != null && cartSlot.getResolutions().toString().contains("ER_SUCCESS_MATCH")){
            String selectedItem = cartSlot.getValue();
            log.error("============slot ====> " + selectedItem+"==================");
            log.error("==================CHECK KEYS IN ConfirmIntentHandler=========================");
            log.error((String) input.getAttributesManager().getSessionAttributes().get(FIRST));

            String name = (String) input.getAttributesManager().getSessionAttributes().get(FIRST);
            String[] info = name.split("#");


            if (selectedItem.equals(FIRST)){
                speechText = String.format("Now I know you want %s with price %s, you can place order by saying place order", info[0], info[1]);
                input.getAttributesManager().setSessionAttributes(Collections.singletonMap(CART_NAME, info[0] + ", " + info[1]));

            } else if (selectedItem.equals(SECOND) && info.length >= 4){
                speechText = String.format("Now I know you want %s with price %s, you can place order by saying place order", info[2], info[3]);
                input.getAttributesManager().setSessionAttributes(Collections.singletonMap(CART_NAME, info[2] + ", " + info[3]));

            } else if (selectedItem.equals(THIRD) && info.length >= 6){
                speechText = String.format("Now I know you want %s with price %s, you can place order by saying place order", info[4], info[5]);
                input.getAttributesManager().setSessionAttributes(Collections.singletonMap(CART_NAME, info[4] + ", " + info[5]));

            } else {
                speechText = String.format("sorry, please say repeat it again by saying add first to my cart");
            }

        }
        log.error("----------------return to alexa from hello first--------------- ");

        return input.getResponseBuilder()
                .withSpeech(speechText)
                .withSimpleCard("HelloWorld", speechText)
                .withReprompt(speechText)
                .withShouldEndSession(false)
                .build();
    }
}