package esfe.handlers;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.model.LaunchRequest;
import com.amazon.ask.model.Response;
import com.amazon.ask.request.Predicates;
import esfe.handlers.utils.Utils;

import java.util.Optional;

public class LaunchRequestHandler implements RequestHandler {

    @Override
    public boolean canHandle(HandlerInput input) {
        return input.matches(Predicates.requestType(LaunchRequest.class));
    }

    @Override
    public Optional<Response> handle(HandlerInput input) {
        FillWareHouse();
        String speechText = "Welcome to grocery shopping, you can start shopping by saying get me milk!";
        return input.getResponseBuilder()
                .withSpeech(speechText)
                .withSimpleCard("HelloWorld", speechText)
                .withReprompt(speechText)
                .build();
    }

    private void FillWareHouse(){
        Utils.WAREHOUSE.add("milk");
        Utils.WAREHOUSE.add("beef");
        Utils.WAREHOUSE.add("juice");
        Utils.WAREHOUSE.add("cookies");
    }

}