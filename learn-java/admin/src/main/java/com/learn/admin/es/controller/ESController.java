package com.learn.admin.es.controller;

import cn.hutool.core.map.MapUtil;
import com.alibaba.fastjson.JSON;
import com.learn.admin.es.model.Person;
import lombok.SneakyThrows;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.common.Strings;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.reindex.BulkByScrollResponse;
import org.elasticsearch.index.reindex.DeleteByQueryRequest;
import org.elasticsearch.rest.RestStatus;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.FetchSourceContext;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * @author LD
 * @date 2021/5/20 16:51
 */
@RestController
@RequestMapping("/es")
@ConditionalOnBean(RestHighLevelClient.class)
public class ESController {

    private final RestHighLevelClient restHighLevelClient;

    public ESController(RestHighLevelClient restHighLevelClient) {
        this.restHighLevelClient = restHighLevelClient;
    }

    @GetMapping("/create_index")
    public void test() throws Exception {
        CreateIndexRequest request = new CreateIndexRequest("test_index");
        CreateIndexResponse createIndexResponse = restHighLevelClient.indices().create(request, RequestOptions.DEFAULT);
        System.out.println(createIndexResponse.index());
    }

    @GetMapping("/index_exists")
    public void test2() throws Exception {
        GetIndexRequest getIndexRequest = new GetIndexRequest("test_index");
        boolean exists = restHighLevelClient.indices().exists(getIndexRequest, RequestOptions.DEFAULT);
        System.out.println(exists);
    }

    @GetMapping("/delete_index")
    public void test3() throws Exception {
        DeleteIndexRequest deleteIndexRequest = new DeleteIndexRequest("test_index");
        AcknowledgedResponse delete = restHighLevelClient.indices().delete(deleteIndexRequest, RequestOptions.DEFAULT);
        System.out.println(delete.isAcknowledged());
    }

    @PostMapping("/add")
    public void test4() throws Exception {
        Person ming = new Person("小明就是小明", 20);
        IndexRequest request = new IndexRequest("test_index").id("1").source(JSON.toJSONString(ming), XContentType.JSON);

        //设置超时
        request.timeout(TimeValue.timeValueSeconds(3));
        request.timeout("3s");

        IndexResponse response = restHighLevelClient.index(request, RequestOptions.DEFAULT);
        System.out.println(response.toString());
    }

    @GetMapping("/get")
    public void test5() throws Exception {
        GetRequest request = new GetRequest("test_index", "1");
        GetResponse response = restHighLevelClient.get(request, RequestOptions.DEFAULT);
        System.out.println(response.getSourceAsMap());
        System.out.println(response.getSourceAsString());
    }


    @GetMapping("/exists")
    public void test6() throws Exception {
        GetRequest request = new GetRequest("test_index", "1");
        //不获取 _source 中的字段信息
        request.fetchSourceContext(new FetchSourceContext(false));

        boolean exists = restHighLevelClient.exists(request, RequestOptions.DEFAULT);
        System.out.println(exists);
    }


    @PutMapping("/update")
    @SneakyThrows
    public void test7() {
        Person ming = new Person(999);
        //只更新年龄
        UpdateRequest request = new UpdateRequest("test_index", "1").doc(JSON.toJSONString(ming), XContentType.JSON);
        UpdateResponse response = restHighLevelClient.update(request, RequestOptions.DEFAULT);
        System.out.println(response.toString());
    }

    @DeleteMapping("/delete")
    @SneakyThrows
    public void test8() {
        DeleteRequest request = new DeleteRequest("test_index", "3");
        DeleteResponse response = restHighLevelClient.delete(request, RequestOptions.DEFAULT);

        if (response.status() == RestStatus.NOT_FOUND) {
            System.out.println("没有数据要删除");
        }
        System.out.println(response.toString());
    }

    @PostMapping("/bulk_add")
    @SneakyThrows
    public void test9() {
        List<Person> people = List.of(
                new Person("2", "德玛西亚之力", 33),
                new Person("3", "艾瑞利亚", 18),
                new Person("4", "诺克萨斯之手", 40));
        BulkRequest bulkRequest = new BulkRequest("test_index");
        bulkRequest.timeout(TimeValue.timeValueSeconds(10));
        for (Person person : people) {
            bulkRequest.add(new IndexRequest().id(person.getId()).source(JSON.toJSONString(person), XContentType.JSON));
        }
        BulkResponse bulkResponse = restHighLevelClient.bulk(bulkRequest, RequestOptions.DEFAULT);
        if (bulkResponse.hasFailures()) {
            System.out.println(bulkResponse.buildFailureMessage());
        }
        System.out.println(bulkResponse.toString());
    }


    @GetMapping("/search")
    @SneakyThrows
    public void test10() {
        SearchRequest searchRequest = new SearchRequest("test_index");
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

        //查询所有
        searchSourceBuilder.query(QueryBuilders.matchQuery("name", "小明"));
        searchSourceBuilder.from(0).size(10);
        searchSourceBuilder.sort("age", SortOrder.ASC);

        //只需要 name age 两个字段
        String[] includeFields = new String[]{"name", "age"};
        searchSourceBuilder.fetchSource(includeFields, Strings.EMPTY_ARRAY);

        searchSourceBuilder.timeout(TimeValue.timeValueSeconds(10));

        //高亮
        HighlightBuilder highlightBuilder = new HighlightBuilder();
        highlightBuilder.field("name");
        highlightBuilder.preTags("<span style='color:red'>");
        highlightBuilder.postTags("</span>");
        searchSourceBuilder.highlighter(highlightBuilder);

        searchRequest.source(searchSourceBuilder);

        SearchResponse response = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
        SearchHits hits = response.getHits();
        long total = hits.getTotalHits().value;
        System.out.println(total);
        for (SearchHit hit : hits) {
            Map<String, Object> sourceAsMap = hit.getSourceAsMap();

            Map<String, HighlightField> highlightFields = hit.getHighlightFields();
            HighlightField name = highlightFields.get("name");

            //替换高亮字段
            Optional.ofNullable(name).ifPresent(var -> {
                String n_name = Stream.of(var.fragments()).map(Text::string).reduce(String::concat)
                        .orElse(MapUtil.getStr(sourceAsMap, "name"));
                sourceAsMap.put("name", n_name);
            });

            System.out.println(sourceAsMap);
        }
    }

    @DeleteMapping("/delete_query")
    @SneakyThrows
    public void test11() {
        QueryBuilder queryBuilder = QueryBuilders.matchQuery("name", "小明");

        DeleteByQueryRequest request = new DeleteByQueryRequest("test_index").setQuery(queryBuilder);
        request.setTimeout(TimeValue.timeValueSeconds(5));

        BulkByScrollResponse response = restHighLevelClient.deleteByQuery(request, RequestOptions.DEFAULT);
        System.out.println(response.toString());
    }

}
