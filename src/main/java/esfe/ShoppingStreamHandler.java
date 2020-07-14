package esfe;

import com.amazon.ask.Skill;
import com.amazon.ask.SkillStreamHandler;
import com.amazon.ask.Skills;
import esfe.handlers.*;
import org.eclipse.microprofile.faulttolerance.Retry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Named;



@Named("alexa")
public class ShoppingStreamHandler extends SkillStreamHandler {

    private static final Logger log = LoggerFactory.getLogger(ShoppingStreamHandler.class);

    @Retry(maxRetries = 3)
    private static Skill getSkill() {
        log.error("log test");

        return Skills.standard()
                .addRequestHandlers(
                        new HelloFirst(),
                        new HelloSecond(),
                        new HelloThird(),
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