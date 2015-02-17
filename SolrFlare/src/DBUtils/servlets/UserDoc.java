package DBUtils.servlets;

public class UserDoc {

private String userId;
private String articleId;
private int  weight;
public String getUserId() {
	return userId;
}
public void setUserId(String userId) {
	this.userId = userId;
}
public String getArticleId() {
	return articleId;
}
public void setArticleId(String articleId) {
	this.articleId = articleId;
}
public int getWeight() {
	return weight;
}
public void setWeight(int weight) {
	this.weight = weight;
}
}
