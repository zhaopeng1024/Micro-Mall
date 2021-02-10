package com.pingan.life.micromall.search;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pingan.life.micromall.search.config.ElasticSearchConfiguration;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.Avg;
import org.elasticsearch.search.aggregations.metrics.AvgAggregationBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.Map;

@SpringBootTest
class MicroMallSearchApplicationTests {

    @Autowired
    private RestHighLevelClient restHighLevelClient;

    @Test
    void testSearchData() throws IOException {
        // 创建检索请求
        SearchRequest searchRequest = new SearchRequest();
        // 指定索引
        searchRequest.indices("bank");
        // 构建检索条件 DSL
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        // 全局检索address中含“mill”中的数据
        searchSourceBuilder.query(QueryBuilders.matchQuery("address", "mill"));
        // 按年龄age进行聚合
        TermsAggregationBuilder termsAggregationBuilder = AggregationBuilders.terms("countAge").field("age").size(10);
        // 计算平均余额
        AvgAggregationBuilder avgAggregationBuilder = AggregationBuilders.avg("avgBalance").field("balance");

        searchSourceBuilder.aggregation(termsAggregationBuilder);
        searchSourceBuilder.aggregation(avgAggregationBuilder);

        searchRequest.source(searchSourceBuilder);

        // 执行检索，得到响应
        SearchResponse searchResponse = restHighLevelClient.search(searchRequest, ElasticSearchConfiguration.COMMON_OPTIONS);

        // 分析结果
        Map map = new ObjectMapper().readValue(searchResponse.toString(), Map.class);
        System.out.println(map);

        // 索引所有命中记录
        SearchHits hits = searchResponse.getHits();
        SearchHit[] data = hits.getHits();

        for (SearchHit hit : data) {
            System.out.println(hit.getSourceAsString());
        }

        // 获取聚合数据
        Aggregations aggregations = searchResponse.getAggregations();
        Terms agg = aggregations.get("countAge");
        for (Terms.Bucket bucket : agg.getBuckets()) {
            System.out.println("年龄: " + bucket.getKeyAsString() + "-->" + bucket.getDocCount() + "人");
        }

        Avg avg = aggregations.get("avgBalance");
        System.out.println("平均余额：" + avg.getValue());

    }

    /**
     * 测试ES存储数据
     */
    @Test
    void testIndexData() throws IOException {
        User user = new User("张三", 25, "男");
        String json = new ObjectMapper().writeValueAsString(user);

        IndexRequest indexRequest = new IndexRequest("users");
        indexRequest.source(json, XContentType.JSON);

        // 执行创建索引和保存数据
        IndexResponse response = restHighLevelClient.index(indexRequest, ElasticSearchConfiguration.COMMON_OPTIONS);
        System.out.println(response);
        System.out.println(response.status());
    }

    @Data
    @AllArgsConstructor
    class User {
        String username;
        int age;
        String gender;
    }

}
