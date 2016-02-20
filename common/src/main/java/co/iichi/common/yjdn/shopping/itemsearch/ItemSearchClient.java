package co.iichi.common.yjdn.shopping.itemsearch;

import co.iichi.common.yjdn.shopping.itemsearch.response.ResultSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

@Component
public class ItemSearchClient {

    public static final Integer LIMIT = 48;

    public static final Integer IMAGE_SIZE = 600;

    Logger logger = LoggerFactory.getLogger(ItemSearchClient.class);

    @Value("${yjdn.application_id}")
    protected String applicationId;

    @Value("${yjdn.shopping.itemsearch_uri}")
    protected String uri;

    protected RestTemplate restTemplate = new RestTemplate();

    public Optional<ResultSet> searchByQuery(String query, Integer offset) {
        String url = new StringBuilder()
                .append(uri)
                .append("?appid=").append(applicationId)
                .append("&query=").append(query)
                .append("&offset=").append(offset)
                .append("&image_size=").append(IMAGE_SIZE)
                .append("&hits=").append(LIMIT)
                .append("&sorted=score")
                .toString();

        logger.info("request url is {}", url);

        try {
            ResultSet resultSet = restTemplate.getForObject(url, ResultSet.class);
            logger.info("response is {}", resultSet);

            return Optional.of(resultSet);
        } catch (HttpClientErrorException e) {
            logger.info("Got HttpClientErrorException. response body is {}", e.getResponseBodyAsString());
            return Optional.empty();
        }
    }

    public Optional<ResultSet> searchByQuery(String query) {
        return searchByQuery(query, 0);
    }
}
