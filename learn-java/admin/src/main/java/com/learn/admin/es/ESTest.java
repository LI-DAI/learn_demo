package com.learn.admin.es;

import org.apache.http.HttpHost;
import org.elasticsearch.action.ActionListener;
import org.elasticsearch.action.DocWriteResponse;
import org.elasticsearch.action.bulk.BulkItemResponse;
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
import org.elasticsearch.action.support.replication.ReplicationResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.reindex.BulkByScrollResponse;
import org.elasticsearch.index.reindex.DeleteByQueryRequest;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author LD
 * @date 2021/5/20 17:11
 */
public class ESTest {

    public static void main(String[] args) throws IOException {

        RestHighLevelClient restHighLevelClient = new RestHighLevelClient(RestClient
                .builder(new HttpHost("172.28.200.85", 9200, "http"))
                .setRequestConfigCallback(requestConfigBuilder -> requestConfigBuilder.setSocketTimeout(3000)));

        //添加数据，如果没有索引会添加索引
        IndexResponse indexResponse = restHighLevelClient.index(createIndex2(), RequestOptions.DEFAULT);
        //添加数据2
        IndexResponse index = restHighLevelClient.index(createIndex3(), RequestOptions.DEFAULT);

        //异步添加数据
        restHighLevelClient.indexAsync(createIndex3(), RequestOptions.DEFAULT, ActionListener.wrap(
                (IndexResponse r) -> {
                    ReplicationResponse.ShardInfo shardInfo = r.getShardInfo();
                    if (shardInfo.getFailed() > 0) {
                        for (ReplicationResponse.ShardInfo.Failure failure :
                                shardInfo.getFailures()) {
                            String reason = failure.reason();
                            System.out.println(reason);
                        }
                    }
                },
                (Exception e) -> {
                    System.out.println(e.getMessage());
                }));

        //是否存在
        boolean exists = restHighLevelClient.exists(getRequest(), RequestOptions.DEFAULT);
        System.out.println(exists);

        //get
        GetResponse getResponse = restHighLevelClient.get(getRequest(), RequestOptions.DEFAULT);
        if (getResponse.isExists()) {
            System.out.println(getResponse.getSourceAsString());
        }

        //删除
        DeleteResponse delete = restHighLevelClient.delete(deleteRequest(), RequestOptions.DEFAULT);
        System.out.println(delete.status());

        //更新
        restHighLevelClient.update(updateRequest(), RequestOptions.DEFAULT);

        //批量操作
        BulkResponse bulk = restHighLevelClient.bulk(bulkRequest(), RequestOptions.DEFAULT);
        if (bulk.hasFailures()) {
            String msg = bulk.buildFailureMessage();
            System.out.println(msg);
        }
        //批量操作获取每个操作的返回结果
        for (BulkItemResponse bulkItemResponse : bulk) {
            if (bulkItemResponse.isFailed()) {
                BulkItemResponse.Failure failure = bulkItemResponse.getFailure();
                System.out.println(failure);
            }
            DocWriteResponse itemResponse = bulkItemResponse.getResponse();
            switch (bulkItemResponse.getOpType()) {
                case INDEX:
                case CREATE:
                    IndexResponse indexResponse1 = (IndexResponse) itemResponse;
                    System.out.println(indexResponse1.toString());
                    break;
                case UPDATE:
                    UpdateResponse updateResponse = (UpdateResponse) itemResponse;
                    System.out.println(updateResponse.toString());
                    break;
                case DELETE:
                    DeleteResponse deleteResponse = (DeleteResponse) itemResponse;
                    System.out.println(deleteResponse.toString());
                    break;
                default:
                    break;
            }
        }

        //搜索
        SearchResponse search = restHighLevelClient.search(searchRequest(), RequestOptions.DEFAULT);
        SearchHits hits = search.getHits();
        long total = hits.getTotalHits().value;
        System.out.println(total);
        for (SearchHit hit : hits) {
            System.out.println(hit.getSourceAsString());
        }

        //查询删除
        BulkByScrollResponse bulkByScrollResponse = restHighLevelClient.deleteByQuery(deleteByQueryRequest(), RequestOptions.DEFAULT);
        System.out.println(bulkByScrollResponse.toString());
    }

    public static IndexRequest createIndex1() {
        IndexRequest request = new IndexRequest("posts");
        request.id("3");
        String jsonString = "{" +
                "\"user\":\"牛牛牛\"," +
                "\"postDate\":\"2013-01-30\"," +
                "\"message\":\"trying out Elasticsearch\"" +
                "}";
        request.source(jsonString, XContentType.JSON);
        return request;
    }


    public static IndexRequest createIndex2() {
        Map<String, Object> jsonMap = new HashMap<>();
        jsonMap.put("user", "小明");
        jsonMap.put("postDate", new Date());
        jsonMap.put("message", "trying out Elasticsearch");
        IndexRequest request = new IndexRequest("posts").id("1").source(jsonMap);
        return request;
    }

    public static IndexRequest createIndex3() throws IOException {
        XContentBuilder builder = XContentFactory.jsonBuilder();
        builder.startObject();
        {
            builder.field("user", "kimchy");
            builder.timeField("postDate", new Date());
            builder.field("message", "trying out Elasticsearch");
        }
        builder.endObject();
        IndexRequest request = new IndexRequest("posts").id("2").source(builder);

        //设置超时
//        request.timeout(TimeValue.timeValueSeconds(1));
//        request.timeout("1s");

        //设置延时刷新
//        reqst.setRefreshPolicy(WriteRequest.RefreshPolicy.WAIT_UNTIL);
//        request.setRefreshPolicy("wait_for");

        return request;
    }

    public static GetRequest getRequest() {
        return new GetRequest("posts", "1");
    }

    public static DeleteRequest deleteRequest() {
        return new DeleteRequest("posts", "2");
    }

    public static UpdateRequest updateRequest() {
        UpdateRequest request = new UpdateRequest("posts", "1");
        Map<String, Object> jsonMap = new HashMap<>();
        jsonMap.put("user", "小红");
        jsonMap.put("postDate", new Date());
        request.doc(jsonMap);
        return request;
    }

    public static BulkRequest bulkRequest() {
        BulkRequest request = new BulkRequest();
//        request.add(new IndexRequest("posts").id("1").source(XContentType.JSON, "field", "foo"));
//        request.add(new IndexRequest("posts").id("2").source(XContentType.JSON, "field", "bar"));
//        request.add(new IndexRequest("posts").id("3").source(XContentType.JSON, "field", "baz"));

        request.add(new DeleteRequest("posts", "3"));
        request.add(new UpdateRequest("posts", "2").doc(XContentType.JSON, "other", "test"));
        request.add(new IndexRequest("posts").id("4").source(XContentType.JSON, "field", "baz"));
        return request;
    }

    public static SearchRequest searchRequest() {
        SearchRequest searchRequest = new SearchRequest("posts");
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

        searchSourceBuilder.query(QueryBuilders.matchAllQuery());
        searchSourceBuilder.from(0).size(10);
//        searchSourceBuilder.sort("age", SortOrder.ASC);

//        String[] includeFields = new String[]{"title", "innerObject.*"};
//        String[] excludeFields = new String[]{"user"};
//        searchSourceBuilder.fetchSource(includeFields, excludeFields);

//        HighlightBuilder highlightBuilder = new HighlightBuilder();
//        HighlightBuilder.Field highlightTitle = new HighlightBuilder.Field("title");
//        highlightTitle.highlighterType("unified");
//        highlightBuilder.field(highlightTitle);
//        HighlightBuilder.Field highlightUser = new HighlightBuilder.Field("user");
//        highlightBuilder.field(highlightUser);
//        searchSourceBuilder.highlighter(highlightBuilder);

        searchSourceBuilder.timeout(new TimeValue(60, TimeUnit.SECONDS));
        searchRequest.source(searchSourceBuilder);
        return searchRequest;
    }

    public static DeleteByQueryRequest deleteByQueryRequest() {
        return new DeleteByQueryRequest("posts").setQuery(QueryBuilders.matchAllQuery());
    }
}
