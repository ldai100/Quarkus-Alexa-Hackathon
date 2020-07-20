package esfe.handlers;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.model.*;
import com.amazon.ask.request.Predicates;
import com.fasterxml.jackson.databind.ObjectMapper;
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

public class StartShoppingIntentHandler implements RequestHandler {

    private static final Logger log = LoggerFactory.getLogger(ShoppingStreamHandler.class);

    RestHighLevelClient client = new RestHighLevelClient(
            RestClient.builder(
                    new HttpHost("3.22.175.238", 9200, "http")));


    @Override
    public boolean canHandle(HandlerInput input) {
        return input.matches(Predicates.intentName("StartShoppingIntentHandler"));
    }

    @Override
    public Optional<Response> handle(HandlerInput input) throws NullPointerException {
        log.error("StartShoppingIntentHandler ====>  start");
        Request request = input.getRequestEnvelope().getRequest();
        IntentRequest intentRequest = (IntentRequest) request;
        Intent intent = intentRequest.getIntent();
        Map<String, Slot> slots = intent.getSlots();
        Slot itemSlot = slots.get(Utils.ITEM_SLOT);
        String speechText;
        List<Map<String, Object>> productList = null;

        log.error("StartShoppingIntentHandler ====>  start check slot if statement");

        if (itemSlot != null && itemSlot.getResolutions() != null && itemSlot.getResolutions().toString().contains("ER_SUCCESS_MATCH")){

            log.error("StartShoppingIntentHandler ====>  get into slot if statement");

            String buyItem = itemSlot.getValue();
            if (!WAREHOUSE.contains(buyItem)){
                speechText = String.format("please tell me a valid item, you can try again by saying get me milk");
            } else {
                input.getAttributesManager().setSessionAttributes(Collections.singletonMap(Utils.ITEM_KEY, buyItem));

                try{
                    productList = getData(buyItem);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                log.error("size of product list" + productList.size());
                if (productList == null || productList.size() == 0){
                    speechText = String.format("please tell me a valid item, you can try again by saying get me milk");
                } else if (productList.size() == 1){
                    input.getAttributesManager().setSessionAttributes(Collections.singletonMap(FIRST, convert(productList.get(0).get("description")) + "#" + productList.get(0).get("price")));
                    speechText = String.format("Here are list of %s, the first is %s with price %s, you can select by saying add first to my cart", buyItem, convert(productList.get(0).get("description")), productList.get(0).get("price"));
                }
                else if (productList.size() == 2){
                    input.getAttributesManager().setSessionAttributes(Collections.singletonMap(FIRST, convert(productList.get(0).get("description")) + "#" + productList.get(0).get("price") + "#" + convert(productList.get(1).get("description")) + "#" + productList.get(1).get("price")));
                    speechText = String.format("Here are list of %s, the first is %s with price %s, the second is %s with price %s, you can select by saying add first to my cart", buyItem, convert(productList.get(0).get("description")), productList.get(0).get("price"), convert(productList.get(1).get("description")), productList.get(1).get("price"));
                } else {
                    input.getAttributesManager().setSessionAttributes(Collections.singletonMap(FIRST, convert(productList.get(0).get("description")) + "#" + productList.get(0).get("price") + "#" + convert(productList.get(1).get("description")) + "#" + productList.get(1).get("price") + "#" + convert(productList.get(2).get("description")) + "#" + productList.get(2).get("price")));
                    speechText = String.format("Here are list of %s, the first is %s with price %s, the second is %s with price %s and the third is %s with price is %s, you can select by saying add first to my cart", buyItem, convert(productList.get(0).get("description")), productList.get(0).get("price"), convert(productList.get(1).get("description")), productList.get(1).get("price"), convert(productList.get(2).get("description")), productList.get(2).get("price"));
                }
            }
        } else {
            speechText = String.format("please tell me a valid item, you can try again by saying get me milk");
        }

        log.error("StartShoppingIntentHandler ===> check key slot ");
        log.error((String) input.getAttributesManager().getSessionAttributes().get(FIRST));

        log.error("StartShoppingIntentHandler ===> return to alexa ");

        return input.getResponseBuilder()
                .withSimpleCard("HelloWorld", speechText)
                .withSpeech(speechText)
                .withShouldEndSession(false)
                .build();
    }


    public List<Map<String, Object>> getData(String key) throws IOException {

        log.error("StartShoppingIntentHandler ===>  start get Data from els");

        List<Map<String, Object>> list = new ArrayList<>();
        int index = 0;
        while (true){
            Map<String, Object> var = getList(key, "" + (++index));
            if (var != null || list.size() == 3){
                list.add(var);
            } else {
                break;
            }
        }
        log.error("StartShoppingIntentHandler ===>  finished get Data from els");
        return list;
    }


    private Map<String, Object> getList(String index, String id) throws IOException {
        GetRequest getRequest = new GetRequest(index, id);
        GetResponse getResponse = client.get(getRequest, RequestOptions.DEFAULT);
        if (getResponse.isExists()){
            return getResponse.getSourceAsMap();
        }
        return null;
    }

    private String convert(Object s){
        String str = (String) s;
        String[] words = str.split(" ");
        StringBuilder sb = new StringBuilder();
        for (String word : words){
            StringBuilder temp = new StringBuilder();
            for (char c : word.toCharArray()){
                if (c == '.'){
                    continue;
                } else if (c == '-'){
                    temp.append('t').append('o');
                } else if (c == '&'){
                    temp.append('a').append('n').append('d');
                } else{
                    temp.append(c);
                }
            }
            sb.append(temp.toString()).append(" ");
        }
        return sb.deleteCharAt(sb.length() - 1).toString();
    }



}