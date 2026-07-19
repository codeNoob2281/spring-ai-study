package org.example.alibaba;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.example.alibaba.service.DocService;
import org.springframework.ai.document.Document;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author floyd
 */
@Slf4j
@Component
public class SimpleRunner implements CommandLineRunner {

    @Resource
    DocService docService;

    @Resource
    private VectorStore vectorStore;

    @Override
    public void run(String... args) throws Exception {
        //doIndex("E:\\Work\\docs\\个人知识库\\03_Knowledge\\编程\\Spring AI");
        //doIndex("E:\\Work\\docs\\个人知识库\\03_Knowledge\\编程\\Python\\实践：数据处理");
        searchSimilarDocuments("如何学习SpringAI");
    }

    public void searchSimilarDocuments(String query) {
        // 从向量数据库检索相似文档
        log.info("获取到用户问题：{}", query);
        log.info("开始检索....");
        List<Document> documents = vectorStore.similaritySearch(query);
        for (Document document : documents) {
            log.info("检索到文档：\n{}", document.getText());
        }
    }

    private void doIndex(String docPath) {
        List<Document> documents = docService.loadDocument(docPath);
        log.info("从知识库加载文档：{}，共加载{}篇文档", docPath, documents.size());
        TokenTextSplitter splitter = TokenTextSplitter.builder().build();
        List<Document> chunks = splitter.transform(documents);
        for (Document chunk : chunks) {
            log.info("id：{}", chunk.getId());
            log.info("text：{}", chunk.getText());
        }

        vectorStore.accept(chunks);
    }
}
