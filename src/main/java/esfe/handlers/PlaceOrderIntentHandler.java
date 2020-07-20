package esfe.handlers;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.model.Response;
import com.amazon.ask.request.Predicates;
import esfe.ShoppingStreamHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

import static esfe.handlers.utils.Utils.*;

public class PlaceOrderIntentHandler implements RequestHandler {

    private static final Logger log = LoggerFactory.getLogger(ShoppingStreamHandler.class);

    @Override
    public boolean canHandle(HandlerInput input) {
        return input.matches(Predicates.intentName("PlaceOrderIntentHandler"));
    }

    @Override
    public Optional<Response> handle(HandlerInput input) {
        log.error("PlaceOrderIntentHandler ====> start");

        String name =  (String) input.getAttributesManager().getSessionAttributes().get(CART_NAME);
        String[] newName = name.split(",");
        log.error("PlaceOrderIntentHandler  ====> "+ newName[0] +" , "+ newName[1]);

        String speechText = String.format("the order %s with price %s has been placed, thank you, Good bye",newName[0], newName[1]);
        log.error("PlaceOrderIntentHandler ====> return to alexa");

        return input.getResponseBuilder()
                .withSpeech(speechText)
                .withSimpleCard("HelloWorld", speechText)
                .withReprompt(speechText)
                .withShouldEndSession(true)
                .build();
    }

}