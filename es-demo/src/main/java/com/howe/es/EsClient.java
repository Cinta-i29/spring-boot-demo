package com.howe.es;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;

/**
 * 使用单例模式创建es client
 * @author howe
 */
public class EsClient {
    private static EsClient instance;
    private ElasticsearchClient esClient;



    // 在构造函数中初始化 esClient
    private EsClient() {
        // 采用账号密码的连接方式
        final CredentialsProvider credentialsProvider =
                new BasicCredentialsProvider();
        credentialsProvider.setCredentials(AuthScope.ANY,
                new UsernamePasswordCredentials("elastic", "elasticnode1"));

        RestClientBuilder restClient = RestClient.builder(
                        new HttpHost("DouyinDBA.raqtpie.xyz", 9200))
                .setHttpClientConfigCallback(i -> i
                        .setDefaultCredentialsProvider(credentialsProvider));

        ElasticsearchTransport transport = new RestClientTransport(
                restClient.build(), new JacksonJsonpMapper()
        );
        esClient = new ElasticsearchClient(transport);
        System.out.println("Es客户端初始化成功");
    }

    public static EsClient getInstance() {
        if (instance == null) {
            instance = new EsClient();
        }
        return instance;
    }

    public ElasticsearchClient getEsClient() {
        return esClient;
    }
}
