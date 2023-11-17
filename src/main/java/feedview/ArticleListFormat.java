package feedview;

import feedcontroller.AppController;
import feedmodel.Article;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public abstract class ArticleListFormat {

    AppController controller;

    public ArticleListFormat(AppController controller) {
        this.controller = controller;
    }

    abstract List<JPanel> create(Map<String, List<Article>> articles);
}

class AggregateArticlePanel extends ArticleListFormat {


    public AggregateArticlePanel(AppController controller) {
        super(controller);
    }

    /**
     * This one is a simple list. Flatten the list of lists (since this is a map of feed names to article lists)
     * and then create a simple article display panel for each article
     * @param articles map of feed names and articles
     * @return simple list of JPanels
     */
    @Override
    public List<JPanel> create(Map<String, List<Article>> articles) {
        List<Article> articleFlatList = articles.values().stream().flatMap(List::stream).toList();
        return articleFlatList.stream().map(article -> new ArticleDisplayPanel(controller, article).getPanel()).toList();
    }
}

class ArticleByFeedPanel extends ArticleListFormat {

    public ArticleByFeedPanel(AppController controller) {
        super(controller);
    }

    @Override
    public List<JPanel> create(Map<String, List<Article>> articles) {
        return null;
    }
}
