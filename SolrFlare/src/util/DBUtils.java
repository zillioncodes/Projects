package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.log4j.Logger;

import DBUtils.servlets.User;
import DBUtils.servlets.UserDoc;

public class DBUtils {
	public static Map<String, String> facebookCategoryMappingMap = null;
	public static Map<String, String> nyTimesCategoryMappingMap = null;
	public static List<String> categoryList = null;
	static final int numberOfCategories = 8;
	static {
		facebookCategoryMappingMap = new HashMap<String, String>();
		facebookCategoryMappingMap.put("Actor/Director", "Entertainment");
		facebookCategoryMappingMap.put("Movie", "Entertainment");
		facebookCategoryMappingMap.put("Producer", "Entertainment");
		facebookCategoryMappingMap.put("Writer", "Entertainment");
		facebookCategoryMappingMap.put("Studio", "Entertainment");
		facebookCategoryMappingMap.put("Movie Theater", "Entertainment");
		facebookCategoryMappingMap.put("TV/Movie Award", "Entertainment");
		facebookCategoryMappingMap.put("Fictional Character", "Entertainment");
		facebookCategoryMappingMap.put("Movie Character", "Entertainment");
		facebookCategoryMappingMap.put("Album", "Entertainment");
		facebookCategoryMappingMap.put("Song", "Entertainment");
		facebookCategoryMappingMap.put("Musician/Band", "Entertainment");
		facebookCategoryMappingMap.put("Music Video", "Entertainment");
		facebookCategoryMappingMap.put("Concert Tour", "Entertainment");
		facebookCategoryMappingMap.put("Concert Venue", "Entertainment");
		facebookCategoryMappingMap.put("Radio Station", "Entertainment");
		facebookCategoryMappingMap.put("Record Label", "Entertainment");
		facebookCategoryMappingMap.put("Music Award", "Entertainment");
		facebookCategoryMappingMap.put("Music Chart", "Entertainment");
		facebookCategoryMappingMap.put("Book", "Lifestyle");
		facebookCategoryMappingMap.put("Author", "Lifestyle");
		facebookCategoryMappingMap.put("Book Store", "Lifestyle");
		facebookCategoryMappingMap.put("Library", "Lifestyle");
		facebookCategoryMappingMap.put("Magazine", "Lifestyle");
		facebookCategoryMappingMap.put("Book Series", "Lifestyle");
		facebookCategoryMappingMap.put("TV Show", "Lifestyle");
		facebookCategoryMappingMap.put("TV Network", "Lifestyle");
		facebookCategoryMappingMap.put("TV Channel", "Lifestyle");
		facebookCategoryMappingMap.put("Athlete", "Miscellaneous");
		facebookCategoryMappingMap.put("Artist", "Miscellaneous");
		facebookCategoryMappingMap.put("Public Figure", "Miscellaneous");
		facebookCategoryMappingMap.put("Journalist", "Miscellaneous");
		facebookCategoryMappingMap.put("News Personality", "Miscellaneous");
		facebookCategoryMappingMap.put("Chef", "Miscellaneous");
		facebookCategoryMappingMap.put("Lawyer", "Miscellaneous");
		facebookCategoryMappingMap.put("Doctor", "Miscellaneous");
		facebookCategoryMappingMap.put("Business Person", "Miscellaneous");
		facebookCategoryMappingMap.put("Comedian", "Miscellaneous");
		facebookCategoryMappingMap.put("Entertainer", "Entertainment");
		facebookCategoryMappingMap.put("Teacher", "Miscellaneous");
		facebookCategoryMappingMap.put("Dancer", "Entertainment");
		facebookCategoryMappingMap.put("Designer", "Miscellaneous");
		facebookCategoryMappingMap.put("Photographer", "Miscellaneous");
		facebookCategoryMappingMap.put("Entrepreneur", "Business");
		facebookCategoryMappingMap.put("Politician", "Business");
		facebookCategoryMappingMap.put("Government Official", "Business");
		facebookCategoryMappingMap.put("Sports League", "Miscellaneous");
		facebookCategoryMappingMap.put("Professional Sports Team", "Miscellaneous");
		facebookCategoryMappingMap.put("Coach", "Miscellaneous");
		facebookCategoryMappingMap.put("Amateur Sports Team", "Miscellaneous");
		facebookCategoryMappingMap.put("School Sports Team", "Miscellaneous");
		facebookCategoryMappingMap.put("Restaurant/Cafe", "Miscellaneous");
		facebookCategoryMappingMap.put("Bar", "Lifestyle");
		facebookCategoryMappingMap.put("Club", "Lifestyle");
		facebookCategoryMappingMap.put("Company", "Lifestyle");
		facebookCategoryMappingMap.put("Product/Service", "Miscellaneous");
		facebookCategoryMappingMap.put("Website", "Miscellaneous");
		facebookCategoryMappingMap.put("Cars", "Automobiles");
		facebookCategoryMappingMap.put("Bags/Luggage", "Lifestyle");
		facebookCategoryMappingMap.put("Camera/Photo", "Lifestyle");
		facebookCategoryMappingMap.put("Clothing", "Lifestyle");
		facebookCategoryMappingMap.put("Computers", "Science and Technology");
		facebookCategoryMappingMap.put("Software", "Science and Technology");
		facebookCategoryMappingMap.put("Office Supplies", "Miscellaneous");
		facebookCategoryMappingMap.put("Electronics", "Science and Technology");
		facebookCategoryMappingMap.put("Health/Beauty", "Lifestyle");
		facebookCategoryMappingMap.put("Appliances", "Lifestyle");
		facebookCategoryMappingMap.put("Building Materials", "");
		facebookCategoryMappingMap.put("Commercial Equipment", "Lifestyle");
		facebookCategoryMappingMap.put("Home Decor", "Lifestyle");
		facebookCategoryMappingMap.put("Furniture", "Lifestyle");
		facebookCategoryMappingMap.put("Household Supplies", "Lifestyle");
		facebookCategoryMappingMap.put("Kitchen/Cooking", "Lifestyle");
		facebookCategoryMappingMap.put("Patio/Garden", "Lifestyle");
		facebookCategoryMappingMap.put("Tools/Equipment", "Lifestyle");
		facebookCategoryMappingMap.put("Wine/Spirits", "Lifestyle");
		facebookCategoryMappingMap.put("Jewelry/Watches", "Lifestyle");
		facebookCategoryMappingMap.put("Pet Supplies", "Lifestyle");
		facebookCategoryMappingMap.put("Outdoor Gear/Sporting Goods", "Lifestyle");
		facebookCategoryMappingMap.put("Baby Goods/Kids Goods", "Lifestyle");
		facebookCategoryMappingMap.put("Media/News/Publishing", "Lifestyle");
		facebookCategoryMappingMap.put("Bank/Financial Institution", "Business");
		facebookCategoryMappingMap.put("Non-Governmental Organization (NGO)", "Business");
		facebookCategoryMappingMap.put("Insurance Company", "Business");
		facebookCategoryMappingMap.put("Small Business", "Business");
		facebookCategoryMappingMap.put("Energy/Utility", "Business");
		facebookCategoryMappingMap.put("Retail and Consumer Merchandise", "Business");
		facebookCategoryMappingMap.put("Automobiles and Parts", "Automobiles");
		facebookCategoryMappingMap.put("Industrials", "Business");
		facebookCategoryMappingMap.put("Transport/Freight", "Business");
		facebookCategoryMappingMap.put("Health/Medical/Pharmaceuticals", "Science and Technology");
		facebookCategoryMappingMap.put("Aerospace/Defense", "World news");
		facebookCategoryMappingMap.put("Mining/Materials", "Science and Technology");
		facebookCategoryMappingMap.put("Farming/Agriculture", "Science and Technology");
		facebookCategoryMappingMap.put("Chemicals", "Science and Technology");
		facebookCategoryMappingMap.put("Consulting/Business Services", "Business");
		facebookCategoryMappingMap.put("Legal/Law", "Business");
		facebookCategoryMappingMap.put("Education", "Business");
		facebookCategoryMappingMap.put("Engineering/Construction", "Science and Technology");
		facebookCategoryMappingMap.put("Food/Beverages", "Lifestyle");
		facebookCategoryMappingMap.put("Telecommunication", "Science and Technology");
		facebookCategoryMappingMap.put("Biotechnology", "Science and Technology");
		facebookCategoryMappingMap.put("Computers/Technology", "Science and Technology");
		facebookCategoryMappingMap.put("Internet/Software", "Science and Technology");
		facebookCategoryMappingMap.put("Travel/Leisure", "Travel");
		facebookCategoryMappingMap.put("Community Organization", "Miscellaneous");
		facebookCategoryMappingMap.put("Political Organization", "Miscellaneous");
		facebookCategoryMappingMap.put("Vitamins/Supplements", "Miscellaneous");
		facebookCategoryMappingMap.put("Drugs", "Miscellaneous");
		facebookCategoryMappingMap.put("Church/Religious Organization", "Miscellaneous");
		facebookCategoryMappingMap.put("Phone/Tablet", "Science and Technology");
		facebookCategoryMappingMap.put("Games/Toys", "Science and Technology");
		facebookCategoryMappingMap.put("App Page", "Science and Technology");
		facebookCategoryMappingMap.put("Video Game", "Science and Technology");
		facebookCategoryMappingMap.put("Board Game", "Science and Technology");
		facebookCategoryMappingMap.put("Local Business", "Business");
		facebookCategoryMappingMap.put("Hotel", "Business");
		facebookCategoryMappingMap.put("Landmark", "Business");
		facebookCategoryMappingMap.put("Airport", "Business");
		facebookCategoryMappingMap.put("Sports Venue", "Miscellaneous");
		facebookCategoryMappingMap.put("Arts/Entertainment/Nightlife", "Entertainment");
		facebookCategoryMappingMap.put("Automotive", "Automobiles");
		facebookCategoryMappingMap.put("Spas/Beauty/Personal Care", "Lifestyle");
		facebookCategoryMappingMap.put("Event Planning/Event Services", "Lifestyle");
		facebookCategoryMappingMap.put("Bank/Financial Services", "Business");
		facebookCategoryMappingMap.put("Food/Grocery", "Lifestyle");
		facebookCategoryMappingMap.put("Health/Medical/Pharmacy", "Lifestyle");
		facebookCategoryMappingMap.put("Home Improvement", "Lifestyle");
		facebookCategoryMappingMap.put("Pet Services", "Lifestyle");
		facebookCategoryMappingMap.put("Professional Services", "Lifestyle");
		facebookCategoryMappingMap.put("Business Services", "Business");
		facebookCategoryMappingMap.put("Community/Government", "Business");
		facebookCategoryMappingMap.put("Real Estate", "Business");
		facebookCategoryMappingMap.put("Shopping/Retail", "Business");
		facebookCategoryMappingMap.put("Public Places", "Business");
		facebookCategoryMappingMap.put("Attractions/Things to Do", "Travel");
		facebookCategoryMappingMap.put("Sports/Recreation/Activities", "Travel");
		facebookCategoryMappingMap.put("Tours/Sightseeing", "Travel");
		facebookCategoryMappingMap.put("Transportation", "Travel");
		facebookCategoryMappingMap.put("Hospital/Clinic", "Miscellaneous");
		facebookCategoryMappingMap.put("Museum/Art Gallery", "Miscellaneous");
		facebookCategoryMappingMap.put("Organization", "Miscellaneous");
		facebookCategoryMappingMap.put("School", "Miscellaneous");
		facebookCategoryMappingMap.put("University", "Miscellaneous");
		facebookCategoryMappingMap.put("Non-Profit Organization", "Miscellaneous");
		facebookCategoryMappingMap.put("Government Organization", "Miscellaneous");
		facebookCategoryMappingMap.put("Cause", "Miscellaneous");
		facebookCategoryMappingMap.put("Political Party", "Miscellaneous");
		facebookCategoryMappingMap.put("Pet", "Miscellaneous");
		facebookCategoryMappingMap.put("Middle School", "Miscellaneous");

		nyTimesCategoryMappingMap = new HashMap<String, String>();
		nyTimesCategoryMappingMap.put("and", "Miscellaneous");
		nyTimesCategoryMappingMap.put("new", "Miscellaneous");
		nyTimesCategoryMappingMap.put("region", "Travel");
		nyTimesCategoryMappingMap.put("york", "Miscellaneous");
		nyTimesCategoryMappingMap.put("arts", "Miscellaneous");
		nyTimesCategoryMappingMap.put("death", "Miscellaneous");
		nyTimesCategoryMappingMap.put("notices", "Miscellaneous");
		nyTimesCategoryMappingMap.put("paid", "Miscellaneous");
		nyTimesCategoryMappingMap.put("business", "Business");
		nyTimesCategoryMappingMap.put("opinion", "Business");
		nyTimesCategoryMappingMap.put("sports", "");
		nyTimesCategoryMappingMap.put("world", "World news");
		nyTimesCategoryMappingMap.put("u.s", "u.s");
		nyTimesCategoryMappingMap.put("washington", "u.s");
		nyTimesCategoryMappingMap.put("corrections", "Miscellaneous");
		nyTimesCategoryMappingMap.put("technology", "Science and Technology");
		nyTimesCategoryMappingMap.put("style", "Lifestyle");
		nyTimesCategoryMappingMap.put("health", "Lifestyle");
		nyTimesCategoryMappingMap.put("books", "Entertainment");
		nyTimesCategoryMappingMap.put("movies", "Entertainment");
		nyTimesCategoryMappingMap.put("front", "Miscellaneous");
		nyTimesCategoryMappingMap.put("page", "Miscellaneous");
		nyTimesCategoryMappingMap.put("education", "Miscellaneous");
		nyTimesCategoryMappingMap.put("magazine", "Entertainment");
		nyTimesCategoryMappingMap.put("theater", "Entertainment");
		nyTimesCategoryMappingMap.put("travel", "Travel");
		nyTimesCategoryMappingMap.put("estate", "Business");
		nyTimesCategoryMappingMap.put("real", "Business");
		nyTimesCategoryMappingMap.put("obituaries", "Miscellaneous");
		nyTimesCategoryMappingMap.put("science", "Science and Technology");
		nyTimesCategoryMappingMap.put("dining", "Lifestyle");
		nyTimesCategoryMappingMap.put("wine", "Lifestyle");
		nyTimesCategoryMappingMap.put("in", "Lifestyle");
		nyTimesCategoryMappingMap.put("review", "Lifestyle");
		nyTimesCategoryMappingMap.put("week", "Lifestyle");
		nyTimesCategoryMappingMap.put("garden", "Lifestyle");
		nyTimesCategoryMappingMap.put("home", "Lifestyle");
		nyTimesCategoryMappingMap.put("automobiles", "Automobiles");
		nyTimesCategoryMappingMap.put("job", "Miscellaneous");
		nyTimesCategoryMappingMap.put("market", "Miscellaneous");
		nyTimesCategoryMappingMap.put("editors", "Miscellaneous");
		nyTimesCategoryMappingMap.put("notes", "Miscellaneous");

		categoryList = new ArrayList<String>();
		categoryList.add("Science and Technology");
		categoryList.add("Lifestyle");
		categoryList.add("Entertainment");
		categoryList.add("Travel");
		categoryList.add("Business");
		categoryList.add("Automobiles");
		categoryList.add("World news");
		categoryList.add("Miscellaneous");
	}

	static Logger logger = Logger.getLogger(DBUtils.class);
	private Connection connection;

	public DBUtils(String dbURL, String user, String pwd) throws ClassNotFoundException, SQLException {
		Class.forName("com.mysql.jdbc.Driver");
		this.connection = DriverManager.getConnection(dbURL, user, pwd);
	}

	public DBUtils(Connection con) throws ClassNotFoundException, SQLException {
		Class.forName("com.mysql.jdbc.Driver");
		this.connection = con;
	}

	public Connection getConnection() {
		return this.connection;
	}

	public Boolean authenticateUser(String userId, String password) throws SQLException {
		boolean successFlag = false;
		if (userId == null || userId.equals("") || password == null || password.equals("")) {
			successFlag = false;
		} else {
			Connection con = getConnection();
			PreparedStatement ps = null;
			ResultSet rs = null;
			try {
				ps = con.prepareStatement("select count(*) from user where idUser=? and password=?");
				ps.setString(1, userId.trim());
				ps.setString(2, password.trim());
				rs = ps.executeQuery();
				if (rs != null && rs.next()) {
					int count = rs.getInt(1);
					if (count > 0) {
						successFlag = true;
						logger.info("User found with details=" + userId);
					} else {
						successFlag = false;
						logger.info("User not found");
					}
				}
			} catch (SQLException e) {
				e.printStackTrace();
				logger.error("Database connection problem");
			} finally {
				try {
					if (rs != null)
						rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
				try {
					if (ps != null)
						ps.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		return successFlag;
	}

	public boolean isUserExists(String userId) throws SQLException {
		boolean successFlag = false;
		if (userId == null || userId.equals("")) {
			successFlag = false;
		} else {
			Connection con = getConnection();
			PreparedStatement ps = null;
			ResultSet rs = null;
			try {
				ps = con.prepareStatement("select count(*) from user where idUser=?");
				ps.setString(1, userId);
				rs = ps.executeQuery();
				if (rs != null && rs.next()) {
					int count = rs.getInt(1);
					if (count > 0) {
						successFlag = true;
						logger.info("User found with details=" + userId);
					} else {
						successFlag = false;
						logger.info("User not found");
					}
				}
			} catch (SQLException e) {
				e.printStackTrace();
				logger.error("Database connection problem");
			} finally {
				try {
					if (rs != null)
						rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
				try {
					if (ps != null)
						ps.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		return successFlag;
	}

	public int getArticleWeight(String articleId) throws SQLException {
		int weight = 0;
		if (articleId == null || articleId.equals("")) {
			return -1;
		} else {
			Connection con = getConnection();
			PreparedStatement ps = null;
			ResultSet rs = null;

			try {
				ps = con.prepareStatement("select weight from user where articleId=?");
				ps.setString(1, articleId);
				rs = ps.executeQuery();
				if (rs != null && rs.next()) {
					weight = rs.getInt(1);
				} else {
					weight = 0;
				}
			} catch (SQLException e) {
				e.printStackTrace();
				logger.error("Database connection problem");
			} finally {
				try {
					if (rs != null)
						rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
				try {
					if (ps != null)
						ps.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		return weight;
	}

	public User populateUserData(String userId) throws DBUtilException {
		User user = new User();
		List<String> categoryList = user.getCategoryList();
		List<String> prefernceList = user.getPrefernceList();
		Map<String, String> prefCatMap = user.getCatPrefMap();
		Map<String, Integer> userDocWeightMap = user.getUserDocWeightMap();
		if (userId == null || userId.equals("")) {
			throw new DBUtilException("Exception occured in DBUtils");
		} else {
			Connection con = getConnection();
			PreparedStatement ps = null, ps1 = null, ps2 = null, ps3 = null, ps4 = null;
			ResultSet rs = null, rs1 = null, rs2 = null, rs3 = null, rs4 = null;
			try {
				// For setting user related data.
				ps = con.prepareStatement("select idUser,nameUser,email,password,lastLogin,active from user where idUser=?");
				ps.setString(1, userId);
				rs = ps.executeQuery();
				if (rs != null && rs.next()) {
					user.setUserId(rs.getString(1));
					user.setUserName(rs.getString(2));
					user.setEmail(rs.getString(3));
					user.setPassword(rs.getString(4));
					user.setLastLogin(rs.getString(5));
					user.setActive(rs.getString(6));
				}
				// For setting List of categories for a particular user.
				ps1 = con.prepareStatement("select nameCategory from category where idUser=?");
				ps1.setString(1, userId);
				rs1 = ps1.executeQuery();
				while (rs1 != null && rs1.next()) {
					categoryList.add(rs1.getString(1));
				}
				user.setCategoryList(categoryList);

				// For setting List of preferences for a particular user.
				ps3 = con.prepareStatement("select preferenceName from preference where idUser=?");
				ps3.setString(1, userId);
				rs3 = ps3.executeQuery();
				while (rs3 != null && rs3.next()) {
					prefernceList.add(rs3.getString(1));
				}
				user.setPrefernceList(prefernceList);

				// For setting preference-->category map in user object
				ps2 = con.prepareStatement("select p.preferenceName,c.nameCategory from category c,preference p where p.idUser=c.idUser and p.idCategory=c.idCategory and c.idUser=?");
				ps2.setString(1, userId);
				rs2 = ps2.executeQuery();
				while (rs2 != null && rs2.next()) {
					prefCatMap.put(rs2.getString(1), rs2.getString(2));
				}
				user.setCatPrefMap(prefCatMap);

				// For setting ArticleId-->weight map in user object
				ps4 = con.prepareStatement("select articleId,weight from article where idUser=?");
				ps4.setString(1, userId);
				rs4 = ps4.executeQuery();
				while (rs4 != null && rs4.next()) {
					userDocWeightMap.put(rs4.getString(1), rs4.getInt(2));
				}
				user.setUserDocWeightMap(userDocWeightMap);

			} catch (Exception e) {
				e.printStackTrace();
				logger.error("Database connection problem");
			} finally {
				try {
					if (rs != null)
						rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
				try {
					if (rs1 != null)
						rs1.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
				try {
					if (ps != null)
						ps.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
				try {
					if (ps1 != null)
						ps1.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}

				try {
					if (ps2 != null)
						ps2.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
				try {
					if (ps3 != null)
						ps3.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
				try {
					if (ps4 != null)
						ps4.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
				try {
					if (rs2 != null)
						rs2.close();
				} catch (SQLException e2) {
					e2.printStackTrace();
				}
				try {
					if (rs3 != null)
						rs3.close();
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
				try {
					if (rs4 != null)
						rs4.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}

			}
		}
		return user;
	}

	public Boolean insertPreCatMapIntoDB(String userId, Map<String, List<String>> CatPreMap) throws DBUtilException, SQLException {
		boolean successFlag = false;
		if (userId == null || userId.equals("")) {
			throw new DBUtilException("Exception occured in DBUtils");
		} else {
			Connection con = getConnection();
			PreparedStatement ps = null, ps1 = null, ps2 = null, ps3 = null, ps4 = null, ps5 = null, ps6 = null;
			ResultSet rs = null, rs2 = null, rs5 = null;
			String categoryIdValue = null;
			boolean preferencePresent = false;
			boolean categoryPresent = false;
			try {
				for (Entry<String, List<String>> entry : CatPreMap.entrySet()) {
					String category = entry.getKey();
					List<String> preference = entry.getValue();
					// if category is already in the table fetch the
					// frequency and update it
					ps2 = con.prepareStatement("select idCategory,frequency from category where idUser=? and nameCategory=?");
					ps2.setString(1, userId);
					ps2.setString(2, category);
					rs2 = ps2.executeQuery();
					if (rs2 != null && rs2.next()) {
						categoryPresent = true;
						categoryIdValue = rs2.getString(1);
						int CatFrequency = rs2.getInt(2);
						CatFrequency++;
						ps3 = con.prepareStatement("update category set frequency=? where nameCategory=?");
						ps3.setInt(1, CatFrequency);
						ps3.setString(2, category);
						int r = ps3.executeUpdate();
						if (r < 0) {
							throw new DBUtilException("Exception in Updating Category Table");
						}
					}
					// if category is not present in the table insert a new
					// record.
					if (!categoryPresent) {
						ps4 = con.prepareStatement("insert into category(nameCategory,frequency,idUser) values(?,?,?)");
						ps4.setString(1, category);
						ps4.setInt(2, 1);
						ps4.setString(3, userId);
						int r = ps4.executeUpdate();
						if (r < 0) {
							throw new DBUtilException("Exception in Inserting in Category Table");
						}
					}
					for (String pref : preference) {
						ps = con.prepareStatement("select preferenceName,preferenceFrequency from preference where idUser=? and preferenceName=?");
						ps.setString(1, userId);
						ps.setString(2, pref);
						rs = ps.executeQuery();
						// if prefernce is already in the table fetch the
						// frequency and update it
						if (rs != null && rs.next()) {
							preferencePresent = true;
							int prefFrequency = rs.getInt(2);
							prefFrequency++;
							ps1 = con.prepareStatement("update preference set preferenceFrequency=? where preferenceName=?");
							ps1.setInt(1, prefFrequency);
							ps1.setString(2, pref);
							int r = ps1.executeUpdate();
							if (r < 0) {
								throw new DBUtilException("Exception in Updating Preference Table");
							}
						}
						// if preference is not present in the table
						// insert a new record.
						if (!preferencePresent) {
							ps5 = con.prepareStatement("select idCategory from category where idUser=? and nameCategory=?");
							ps5.setString(1, userId);
							ps5.setString(2, category);
							rs5 = ps5.executeQuery();
							if (rs5 != null && rs5.next()) {
								categoryIdValue = rs5.getString(1);
							}
							ps6 = con.prepareStatement("insert into preference (idCategory,idUser,preferenceName,preferenceFrequency) values(?,?,?,?)");
							ps6.setString(1, categoryIdValue);
							ps6.setString(2, userId);
							ps6.setString(3, pref);
							ps6.setInt(4, 1);
							int r = ps6.executeUpdate();
							if (r < 0) {
								throw new DBUtilException("Exception in Inserting in Preference Table");
							}
						}

					}
				}
				successFlag = true;
			} catch (SQLException e) {
				successFlag = false;
				e.printStackTrace();
				con.setAutoCommit(false);
				con.rollback();
			} finally {
				if (ps != null)
					ps.close();
				if (ps1 != null)
					ps1.close();
				if (ps2 != null)
					ps2.close();
				if (ps3 != null)
					ps3.close();
				if (ps4 != null)
					ps4.close();
				if (ps5 != null)
					ps5.close();
				if (ps6 != null)
					ps6.close();
				if (rs5 != null)
					rs5.close();
				if (rs != null)
					rs.close();
				if (rs2 != null)
					rs2.close();
			}
		}
		return successFlag;

	}

	public Boolean insertUserDocMapInDB(Map<String, UserDoc> userDocMap, String userId) throws DBUtilException, SQLException {
		boolean successFlag = false;
		Connection con = getConnection();
		PreparedStatement ps = null, ps1 = null, ps2 = null;
		ResultSet rs = null;
		try {
			for (Entry<String, UserDoc> entry : userDocMap.entrySet()) {
				String articleId = entry.getKey();
				int weight = entry.getValue().getWeight();
				// First check whether the article is already there or not
				// in the table if yes update the weight of article
				ps = con.prepareStatement("select articleId from article where idUser=?");
				ps.setString(1, userId);
				rs = ps.executeQuery();
				if (rs != null && rs.next()) {
					ps1 = con.prepareStatement("update article set weight=? where idUser=?");
					ps1.setInt(1, weight);
					ps1.setString(2, userId);
					int r = ps1.executeUpdate();
					if (r < 0) {
						throw new DBUtilException("Exception in Updating article Table");
					}
				}
				// Enter new row in the table if not present
				else {
					ps2 = con.prepareStatement("insert into article (idUser,articleId,weight) values(?,?,?)");
					ps2.setString(1, userId);
					ps2.setString(2, articleId);
					ps2.setInt(3, weight);
					int r = ps1.executeUpdate();
					if (r < 0) {
						throw new DBUtilException("Exception Occured in InsertUserDocMapInDB");
					}
				}
			}
			successFlag = true;
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return successFlag;

	}

	// Function to get facebook categories mapped
	public String getFbCategMapping(String category) {
		if (facebookCategoryMappingMap.containsKey(category)) {
			String categoryMapped = facebookCategoryMappingMap.get(category);
			return categoryMapped;
		} else {
			return "";
		}

	}

	// Function to get NyTimes categories mapped
	public String getNyTimesCategMapping(String category) {
		if (nyTimesCategoryMappingMap.containsKey(category)) {
			String categoryMapped = nyTimesCategoryMappingMap.get(category);
			return categoryMapped;
		} else {
			return "";
		}

	}

	public Map<String, Map<Integer, Integer>> PopulateUserDataForWekaFile() throws DBUtilException, SQLException {
		Map<String, Map<Integer, Integer>> wekaDataMap = new LinkedHashMap<String, Map<Integer, Integer>>();
		Map<Integer, Integer> catfrequency = new HashMap<Integer, Integer>();
		List<String> UserIdList = new LinkedList<String>();
		Connection con = getConnection();
		PreparedStatement ps = null, ps1 = null;
		ResultSet rs = null, rs1 = null;
		try {
			int count = 0;
			ps1 = con.prepareStatement("select u.idUser from user u,category c where u.idUser=c.idUser order by u.idUser");
			rs1 = ps1.executeQuery();
			while (rs1 != null && rs1.next()) {
				UserIdList.add(rs1.getString(1));
			}
			if (ps1 != null)
				ps1.close();
			if (rs1 != null)
				rs1.close();
			ps = con.prepareStatement("select idUser,frequency,nameCategory from category c where idUser in (select idUser from user group by idUser) order by c.idUser");
			rs = ps.executeQuery();
			rs.next();
			while (!isMyResultSetEmpty(rs)) {
				// Check for all the categories for a particular userId if
				// its value is there set it in the list else set it 0
				if (UserIdList.contains(rs.getString(1)) && count != numberOfCategories) {
					if (categoryList.contains(rs.getString(3))) {
						catfrequency.put(categoryList.indexOf(rs.getString(3)), rs.getInt(2));
						String id = rs.getString(1);
						UserIdList.remove(rs.getString(1));
						rs.next();
						if (!UserIdList.contains(id))
							rs.previous();
						count++;
					} else {
						System.err.println("WHYYYYYYY????");
//						catfrequency.add(0);
//						String id = rs.getString(1);
//						UserIdList.remove(rs.getString(1));
//						rs.next();
//						if (!UserIdList.contains(id))
//							rs.previous();
//						count++;
					}
				} else {
					if (count != numberOfCategories) {
//						for (int i = count + 1; i <= numberOfCategories; i++) {
//							catfrequency.add(i, 0);
//						}
						
						for (int i = 0; i < categoryList.size(); i++) {
							if (!catfrequency.containsKey(i)) {
								catfrequency.put(i, 0);	
							}
						}
						
						wekaDataMap.put(rs.getString(1), catfrequency);
						catfrequency = new HashMap<Integer, Integer>();
						count = 0;
						rs.next();
					} else {
						wekaDataMap.put(rs.getString(1), catfrequency);
						catfrequency = new HashMap<Integer, Integer>();
						count = 0;
						rs.next();
					}
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (ps != null) {
				ps.close();
			}
			if (rs != null) {
				rs.close();
			}
		}
		return wekaDataMap;
	}
	
	/*public Map<String, List<Integer>> PopulateUserDataForWekaFile() throws DBUtilException, SQLException {
		Map<String, List<Integer>> wekaDataMap = new LinkedHashMap<String, List<Integer>>();
		List<Integer> catfrequency = new ArrayList<Integer>();
		List<String> UserIdList = new LinkedList<String>();
		Connection con = getConnection();
		PreparedStatement ps = null, ps1 = null;
		ResultSet rs = null, rs1 = null;
		try {
			int count = 0;
			ps1 = con.prepareStatement("select u.idUser from user u,category c where u.idUser=c.idUser order by u.idUser");
			rs1 = ps1.executeQuery();
			while (rs1 != null && rs1.next()) {
				UserIdList.add(rs1.getString(1));
			}
			if (ps1 != null)
				ps1.close();
			if (rs1 != null)
				rs1.close();
			ps = con.prepareStatement("select idUser,frequency,nameCategory from category c where idUser in (select idUser from user group by idUser) order by c.idUser");
			rs = ps.executeQuery();
			rs.next();
			while (!isMyResultSetEmpty(rs)) {
				// Check for all the categories for a particular userId if
				// its value is there set it in the list else set it 0
				if (UserIdList.contains(rs.getString(1)) && count != numberOfCategories) {
					if (categoryList.contains(rs.getString(3))) {
						catfrequency.add(rs.getInt(2));
						String id = rs.getString(1);
						UserIdList.remove(rs.getString(1));
						rs.next();
						if (!UserIdList.contains(id))
							rs.previous();
						count++;
					} else {
						catfrequency.add(0);
						String id = rs.getString(1);
						UserIdList.remove(rs.getString(1));
						rs.next();
						if (!UserIdList.contains(id))
							rs.previous();
						count++;
					}
				} else {
					if (count != numberOfCategories) {
						for (int i = count + 1; i <= numberOfCategories; i++) {
							catfrequency.add(0);
						}
						wekaDataMap.put(rs.getString(1), catfrequency);
						catfrequency = new ArrayList<Integer>();
						count = 0;
						rs.next();
					} else {
						wekaDataMap.put(rs.getString(1), catfrequency);
						catfrequency = new ArrayList<Integer>();
						count = 0;
						rs.next();
					}
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (ps != null) {
				ps.close();
			}
			if (rs != null) {
				rs.close();
			}
		}
		return wekaDataMap;
	}*/

	public boolean isMyResultSetEmpty(ResultSet rs) throws SQLException {
		return (!rs.isBeforeFirst() && rs.getRow() == 0);
	}

	public List<String> getDocIdsFromSimilarUsers(String userId, List<String> similarUsers, String category) {
		List<String> docIdList = new ArrayList<String>();

		Connection con = getConnection();
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {

			String similarUserString = "";
			boolean isFirst = true;
			for (String curUser: similarUsers) {
				if (isFirst) {
					similarUserString = "idUser='" + curUser + "'";
				} else {
					similarUserString += " or idUser='" + curUser + "'";
				}
				isFirst = false;
			}

			String query = "select articleId, weight from magnumdb.article where (" + similarUserString + ") "
					+ "and idCategory in (select idCategory from category where nameCategory=" + category + " and (" + similarUserString + ")) and articleId not in (select articleId from magnumdb.article where idUser='" + userId.trim() + "') order by weight DESC;";
			Statement st = con.createStatement();
			rs = st.executeQuery(query);
			while (rs != null && rs.next()) {
				String docId = rs.getString(1);
				docIdList.add(docId);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			logger.error("Database connection problem");
		} finally {
			try {
				if (rs != null)
					rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			try {
				if (ps != null)
					ps.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return docIdList;
	}
}