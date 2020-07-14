package esfe.handlers.utils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class Utils {

    public static final String ITEM_KEY = "ITEM";
    public static final String CART_KEY= "CART";
    public static final String ITEM_SLOT = "Item";
    public static final String CART_SLOT = "Cart";


    public static List<Product> parse(String keyWord) throws Exception {
        List<Product> list = new ArrayList<>();
        try {
            String url = "https://www.costco.com/CatalogSearch?dept=All&keyword=" + keyWord;
            Document document = Jsoup.parse(new URL(url), 250000);

            Element searchResult = document.getElementById("search-results");
            Elements productList = searchResult.getElementsByClass("product");

            for (Element element : productList) {
                String description = element.getElementsByClass("description").eq(0).text();
                String price = element.getElementsByClass("price").eq(0).text();
                if (description != null && description.length() > 0 && price != null && price.length() > 0) {
                    Product product = new Product();
                    product.setDescription(description);
                    product.setPrice(price);
                    list.add(product);
                }
            }
        }catch (Exception e){
            throw new Exception(e);
        }
        return list;
    }

}
