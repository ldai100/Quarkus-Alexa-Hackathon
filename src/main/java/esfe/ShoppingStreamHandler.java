package esfe;

import com.amazon.ask.Skill;
import com.amazon.ask.Skills;
import com.amazon.ask.SkillStreamHandler;

import esfe.handlers.CancelAndStopIntentHandler;
import esfe.handlers.StartShoppingIntentHandler;
import esfe.handlers.HelpIntentHandler;
import esfe.handlers.SessionEndedRequestHandler;
import esfe.handlers.FallbackIntentHandler;
import esfe.handlers.LaunchRequestHandler;

import org.eclipse.microprofile.faulttolerance.Retry;

import javax.inject.Named;

@Named("alexa")
public class ShoppingStreamHandler extends SkillStreamHandler {

    @Retry(maxRetries = 3)
    private static Skill getSkill() {
        return Skills.standard()
                .addRequestHandlers(
                        new CancelAndStopIntentHandler(),
                        new StartShoppingIntentHandler(),
                        new FallbackIntentHandler(),
                        new HelpIntentHandler(),
                        new LaunchRequestHandler(),
                        new SessionEndedRequestHandler())
                .build();
    }

    public ShoppingStreamHandler() {
        super(getSkill());
    }

}