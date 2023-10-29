package feedcontroller;

import feedmodel.Article;
import feedmodel.Feed;
import feedview.UserInterface;
import org.dom4j.*;
import org.dom4j.io.SAXReader;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FeedController {

    private final Logger logger = Logger.getLogger("Controller");
    private final List<Feed> feeds = new ArrayList<>();
    private static final int ARTICLES_PER_FEED = 5;

    private final UserInterface ui;

    public FeedController() {
        ui = new UserInterface(this);
    }

    public void start() {
        ui.show();
    }

    public void addFeed(String inputURLString) {
        try {
            URL url = new URL(inputURLString);
            Document feedDoc = getFeed(url);
            Feed feed = new Feed(feedDoc.valueOf(".//channel/title"), url.toString());
            List<Node> articles = feedDoc.selectNodes(".//channel/item");
            if (!articles.isEmpty()) {
                for (Node s : articles) {
                    Article.ArticleBuilder builder = new Article.ArticleBuilder(
                            feed.getName(),
                            s.selectSingleNode("title").getText(),
                            s.selectSingleNode("link").getText());
                    Node contentNode = s.selectSingleNode("description");
                    if (contentNode != null) {
                        String content = contentNode.getText();
                        Pattern pattern = Pattern.compile("<img src=\"(.*?)\">(.*)");
                        Matcher matcher = pattern.matcher(content);
                        if (matcher.find()) {
                            builder.setImageURL(new URL(matcher.group(1)));
                            builder.setSummary(matcher.group(2));
                        } else {
                            builder.setSummary(content);
                        }
                    }
                    feed.addArticle(builder.build());
                }
            }
            feeds.add(feed);
            ui.updateArticleList(getCurrentArticleList());

        } catch(IOException | DocumentException e) {
            ui.showError("Malformed URL or invalid feed. Please double-check input");
            throw new RuntimeException(e);
        }
    }
    private Document getFeed(URL url) throws IOException, DocumentException {
        return new SAXReader().read(url);
    }

    public void requestRemoveArticle(Article article) {
        for (Feed f : feeds) {
            f.remove(article);
        }
        logger.log(Level.INFO, String.format("Removed %s\n", article.getTitle()));
        ui.updateArticleList(getCurrentArticleList());
    }

    public void openInBrowser(String url) {
        try {
            URI uri = new URI(url);
            if (Desktop.isDesktopSupported()) {
                Desktop desktop = Desktop.getDesktop();
                if (desktop.isSupported(Desktop.Action.BROWSE)) {
                    desktop.browse(uri);
                }
            }
        } catch (URISyntaxException | IOException e) {
            ui.showError("Unable to open browser or action not supported.");
            e.printStackTrace();
        }
    }

    private List<Article> getCurrentArticleList()  {
        List<Article> articles = new ArrayList<>();
        for (Feed f : feeds) {
            for (int i = 0; i < ARTICLES_PER_FEED; i++) {
                if (f.getArticles().size() <= i) {
                    break;
                }
                articles.add(f.getArticles().get(i));
            }
        }
        return articles;
    }


}
