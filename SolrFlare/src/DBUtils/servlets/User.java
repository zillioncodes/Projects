package DBUtils.servlets;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class User implements Serializable {

	private static final long serialVersionUID = -2577394084544341212L;
	private String userId;
	private String userName;
	private String email;
	private String password;
	private String lastLogin;
	private String active;
	private boolean isFacebookUser;
	
	public User(String userId, String userName, String email, String password, String lastLogin, String active, boolean isFacebookUser, Map<String, String> userCatMap, List<String> categoryList, Map<String, String> prefCatMap) {
		super();
		this.userId = userId;
		this.userName = userName;
		this.email = email;
		this.password = password;
		this.lastLogin = lastLogin;
		this.active = active;
		this.isFacebookUser = isFacebookUser;
		this.userCatMap = userCatMap;
		this.categoryList = categoryList;
		this.prefCatMap = prefCatMap;
	}
	
	private Map<String,String> userCatMap;
	private List<String> categoryList;
	private Map<String,String> prefCatMap;
	private Map<String,Integer> userDocWeightMap;
	private List<String> prefernceList;
	
	public User()
	{
		userCatMap= new HashMap<String, String>();
		//For storing preference as a key and category as a value for a particular user.
		prefCatMap= new HashMap<String, String>();
		//For storing the category list for a particular user.
		categoryList=new ArrayList<String>();
		//For storing the preference list for a particular user.
		prefernceList=new ArrayList<String>();
		//For storing the ArticlId and weight associated for a particular user.
		userDocWeightMap= new HashMap<String, Integer>();
	}
	public User(String userId){
		this.userId=userId;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getLastLogin() {
		return lastLogin;
	}
	public void setLastLogin(String lastLogin) {
		this.lastLogin = lastLogin;
	}
	public String getActive() {
		return active;
	}
	public void setActive(String active) {
		this.active = active;
	}

	public Map<String, String> getUserCatMap() {
		return userCatMap;
	}

	public void setUserCatMap(Map<String, String> userCatMap) {
		this.userCatMap = userCatMap;
	}

	public List<String> getCategoryList() {
		return categoryList;
	}

	public void setCategoryList(List<String> categoryList) {
		this.categoryList = categoryList;
	}

	public Map<String, String> getCatPrefMap() {
		return prefCatMap;
	}

	public void setCatPrefMap(Map<String, String> catPrefMap) {
		prefCatMap = catPrefMap;
	}
	
	public void printString()
	{
		System.out.println("UserDetails-->"+"\n"+this.userId+"\n"+this.userName+"\n"+this.email+"\n"+this.password+"\n"+this.lastLogin+"\n"+this.active);
		for(int i=0;i<this.categoryList.size();i++)
		{
			System.out.println("\nCategories-->"+categoryList.get(i));
		}
		for(String pref:this.prefCatMap.keySet())
		{
			System.out.println("\npreference-->"+pref+" "+"\nCatgories---->"+prefCatMap.get(pref));
		}
	}
	public boolean isFacebookUser() {
		return isFacebookUser;
	}
	public void setFacebookUser(boolean isFacebookUser) {
		this.isFacebookUser = isFacebookUser;
	}
	public List<String> getPrefernceList() {
		return prefernceList;
	}
	public void setPrefernceList(List<String> prefernceList) {
		this.prefernceList = prefernceList;
	}
	public Map<String, Integer> getUserDocWeightMap() {
		return userDocWeightMap;
	}
	public void setUserDocWeightMap(Map<String, Integer> userDocWeightMap) {
		this.userDocWeightMap = userDocWeightMap;
	}
}
