package esfe.handlers;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.model.Response;
import static com.amazon.ask.request.Predicates.intentName;

import java.util.Optional;

public class HelpIntentHandler implements RequestHandler {

    @Override
    public boolean canHandle(HandlerInput input) {
        return input.matches(intentName("AMAZON.HelpIntent"));
    }

    @Override
    public Optional<Response> handle(HandlerInput input) {
        String speechText = "Please tell me what you want to buy saying, get me milk";
        return input.getResponseBuilder()
                .withSimpleCard("HelloWorld", speechText)
                .withSpeech(speechText)
                .withShouldEndSession(false)
                .build();
    }
}