import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.aggregations.HistogramBucket;
import co.elastic.clients.elasticsearch._types.query_dsl.MatchQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch._types.query_dsl.RangeQuery;
import co.elastic.clients.elasticsearch.core.*;
import co.elastic.clients.elasticsearch.core.search.Hit;
import co.elastic.clients.json.JsonData;
import com.howe.es.EsClient;
import com.howe.pojo.Hotel;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author howe
 */
@Slf4j
public class EsCrudTest {
    // 获取客户端
    ElasticsearchClient es = EsClient.getInstance().getEsClient();

    // 索引单个文档
    @Test
    public void indexingSingleDocuments() throws IOException {
        Long id = 8L;
        Hotel hotel = new Hotel(id, "度假村H", "广东省广州市",
                8000, 8, "度假村", "三亚",
                "", "", "11", "22", "33");
        es.index(i -> i
                .index("hotel")
                .id(id.toString())
                .document(hotel)
        );
//        es.create()
//        es.delete()
//        es.update()
//        es.search()
        SearchResponse<Hotel> searchResponse = es.search(b1 -> b1
                        .index("hotel")
                        .query(b2 -> b2
                                .match(t -> t
                                        .field("id")
                                        .query(id)
                                ))
                , Hotel.class
        );
        System.out.println(searchResponse.hits().hits().get(0));
    }

    // 批量索引多个文档
    @Test
    public void indexingMultipleDocuments() throws IOException {
        // 文档操作(多个)
        List<Hotel> hotels = new ArrayList<>();
        BulkRequest.Builder br = new BulkRequest.Builder();
        for (Hotel h : hotels) {
            br.operations(op -> op
                    // 这里可以是 index create update delete
                    .index(idx -> idx
                            .index("hotel")
                            .id(String.valueOf(h.getId()))
                            .document(h)
                    )
            );
            br.operations(op -> op
                    .create(idx -> idx
                            .index("hotel")
                            .id("8")
                    )
            );
        }
        BulkResponse result = es.bulk(br.build());
    }

    // 通过ID读取文档
    @Test
    public void readingDocumentsById() throws IOException {
        GetResponse<Hotel> response = es.get(g -> g
                        .index("hotel")
                        .id("4"),
                // 使用原始json ObjectNode.class
                Hotel.class
        );
        if (response.found()) {
            Hotel hotel = response.source();
            System.out.println("得到的对象=>" + hotel);
        } else {
            System.out.println("没用找到对象");
        }
    }

    // 搜索文档
    // 简单搜索查询
    @Test
    public void searchingForDocuments1() throws IOException {
        String searchText = "酒店";
        SearchResponse<Hotel> response = es.search(s -> s
                        .index("products")
                        .query(q -> q
                                .match(t -> t
                                        .field("name")
                                        .query(searchText)
                                )
                        ),
                Hotel.class
        );
        List<Hit<Hotel>> hits = response.hits().hits();
        for (Hit<Hotel> hit : hits) {
            Hotel hotel = hit.source();
            System.out.println(hotel);
        }

    }

    // 嵌套搜索查询
    @Test
    public void searchingForDocuments2() throws IOException {
        String searchText = "酒店";
        double maxPrice = 200.0;
        // 指定搜索条件
        Query byName = MatchQuery.of(m -> m
                .field("name")
                .query(searchText)
        )._toQuery();//查询变体 联合类型
        Query byMaxPrice = RangeQuery.of(r -> r
                .field("price")
                .gte(JsonData.of(maxPrice))
        )._toQuery();
        // 执行
        SearchResponse<Hotel> response = es.search(s -> s
                        .index("products")
                        .query(q -> q
                                .bool(b -> b
                                        .must(byName)
                                        .must(byMaxPrice)
                                )
                        ),
                Hotel.class
        );

        // 得到搜索结果
        List<Hit<Hotel>> hits = response.hits().hits();
        for (Hit<Hotel> hit : hits) {
            Hotel hotel = hit.source();
            System.out.println(hotel);
        }
    }

    // 模板搜索
    @Test
    public void searchingForDocuments3() throws IOException {
        // 创建模板 值不是固定的
        es.putScript(r -> r
                .id("query-script")
                .script(s -> s
                        .lang("mustache")
                        .source("{\"query\":{\"match\":{\"{{field}}\":\"{{value}}\"}}}")
                ));

        SearchTemplateResponse<Hotel> response = es.searchTemplate(r -> r
                        .index("hotel")
                        .id("query-script") // 要使用的模板的标识符
                        // 模板参数值
                        .params("field", JsonData.of("some-field")) // 参数名称
                        .params("value", JsonData.of("some-data")), // 匹配的值
                Hotel.class
        );

        List<Hit<Hotel>> hits = response.hits().hits();
        for (Hit<Hotel> hit : hits) {
            Hotel hotel = hit.source();
            System.out.println(hotel);
        }
    }

    // 聚合查询
    @Test
    public void aggregations() throws IOException {
        String searchText = "酒店";

        Query query = MatchQuery.of(m -> m
                .field("name")
                .query(searchText)
        )._toQuery();

        SearchResponse<Void> response = es.search(b -> b
                        .index("hotel")
                        //将匹配文档的数量设置为零，使结果中不包含文档，只包含聚合结果
                        .size(0)
                        //设置填充过滤要运行聚合的产品的查询
                        .query(query)
                        //创建一个名为“price-histogram”的聚合。您可以根据需要添加任意数量的命名聚合。
                        .aggregations("price-histogram", a -> a
                                .histogram(h -> h // 选择histogram聚合变体
                                        .field("price")
                                        .interval(50.0)
                                )
                        ),
                //我们不关心匹配（size设置为零），使用Void将忽略响应中的任何文档。
                Void.class
        );

        List<HistogramBucket> buckets = response.aggregations()
                //获取“价格直方图”聚合的结果。
                .get("price-histogram")
                //获取“价格直方图”聚合的结果。这必须与聚合一致
                .histogram()
                //桶可以表示为数组或映射。这会向下转换为数组变体（默认）。
                .buckets().array();

        for (HistogramBucket bucket : buckets) {
            System.out.println("There are " + bucket.docCount() + " hotel under " + bucket.key());
        }
    }
}
