package esfe.handlers;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.model.Response;
import com.amazon.ask.request.Predicates;
import esfe.handlers.utils.Utils;

import java.util.Optional;

public class SelectIntentHandler  implements RequestHandler {
    @Override
    public boolean canHandle(HandlerInput input) {
        return input.matches(Predicates.intentName("SelectIntentHandler"));
    }

    @Override
    public Optional<Response> handle(HandlerInput input) {
        return null;
    }
}
