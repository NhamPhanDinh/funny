package funfact.ninegag.model;



import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import funfact.ninegag.obj.ImageItem;

public class GetData {
	
	private String link_next;
	
	public List<ImageItem>parsing(String link) {

		List<ImageItem> listImageItem = new ArrayList<ImageItem>();

		String b = null;
		Document doc;
		try {
			doc = Jsoup.connect(link).userAgent("Chrome/30.0.1599.101").get();

			Element eLoad = doc.select("div.loading").get(0);
			link_next = "http://9gag.com/" + eLoad.select("a").get(0).attr("href");
	
			Elements eArticle = doc.select("article");

			for (int i = 0; i < eArticle.size(); i++) {
				ImageItem it = new ImageItem();

				it.setTitle(eArticle.get(i).select("header").get(0)
						.select("h2").get(0).text());//get Titile Image

				Elements eDiv = eArticle.get(i).select("div");
				int countDiv = eDiv.size();
				String urlImage = "";
				if (countDiv == 8) {
					urlImage = eDiv.get(2).select("a").get(0).select("img")
							.get(0).attr("src");
				} else {
					urlImage = eDiv.get(2).select("a").get(0).select("div")
							.get(0).attr("data-image");
				}

				it.setUrl(urlImage);
				listImageItem.add(it);

			}

		
		} catch (Exception e) {

			//System.out.println(e.toString());
		}
		return listImageItem;

	}


	public String getLink_next() {
		return link_next;
	}


	public void setLink_next(String link_next) {
		this.link_next = link_next;
	}
	
	
}
