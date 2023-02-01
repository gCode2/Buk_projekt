package buk_web;

import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import com.buk.dao.EventDAO;
import com.buk.entities.Event;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

@Named("eventBB")
@ViewScoped
public class EventBB implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@EJB
	EventDAO eventDAO;
	
	private String homeTeamName;
	private String awayTeamName;
	private int homeScore;
	private int awayScore;
	private float homeOdds;
	private float awayOdds;
	private float drawOdds;
	private LocalDateTime matchDate;
	private int matchID;
	private List<Event> matchList = new ArrayList<>();
	private String status;
	private String result;

    // gettery i settery
    public String getHomeTeamName() {
		return homeTeamName;
	}
	public void setHomeTeamName(String homeTeamName) {
		this.homeTeamName = homeTeamName;
	}
	public String getAwayTeamName() {
		return awayTeamName;
	}
	public void setAwayTeamName(String awayTeamName) {
		this.awayTeamName = awayTeamName;
	}
	public int getHomeScore() {
		return homeScore;
	}
	public void setHomeScore(int homeScore) {
		this.homeScore = homeScore;
	}
	public int getAwayScore() {
		return awayScore;
	}
	public void setAwayScore(int awayScore) {
		this.awayScore = awayScore;
	}
	public float getHomeOdds() {
		return homeOdds;
	}
	public void setHomeOdds(float homeOdds) {
		this.homeOdds = homeOdds;
	}
	public float getAwayOdds() {
		return awayOdds;
	}
	public void setAwayOdds(float awayOdds) {
		this.awayOdds = awayOdds;
	}
	public float getDrawOdds() {
		return drawOdds;
	}
	public void setDrawOdds(float drawOdds) {
		this.drawOdds = drawOdds;
	}
	public LocalDateTime getMatchDate() {
		return matchDate;
	}
	public void setMatchDate(LocalDateTime matchDate) {
		this.matchDate = matchDate;
	}
	public int getMatchID() {
		return matchID;
	}
	public void setMatchID(int matchID) {
		this.matchID = matchID;
	}
	public List<Event> getMatchList() {
	    return matchList;
	}
	
	public void fetchData() throws IOException, InterruptedException {
		
		matchList = new ArrayList<>();
		
		//HTTP REQUEST DO API
		
		HttpClient apiClient = HttpClient.newHttpClient();
		HttpRequest apiRequest = HttpRequest.newBuilder()
		                                 .uri(URI.create("https://api.football-data.org/v4/matches"))
		                                 .header("X-Auth-Token", "a94156b1b8504e61b996d586018eeab8")
		                                 .build();
		HttpResponse<String> apiResponse = apiClient.send(apiRequest, BodyHandlers.ofString());
		
		String json = apiResponse.body().toString();
		System.out.println(json);
	
		Gson gson = new Gson();
		
		JsonObject jsonObject = gson.fromJson(json, JsonObject.class);
		
		JsonArray matches = jsonObject.getAsJsonArray("matches");
			
		for (int i = 0; i < matches.size(); i++) {
			
			//W CELU LATWIEJSZEGO OPEROWANIA NA API
		    JsonObject match = matches.get(i).getAsJsonObject();
		    
		    this.homeTeamName = match.get("homeTeam").getAsJsonObject().get("name").getAsString();
		    this.awayTeamName = match.get("awayTeam").getAsJsonObject().get("name").getAsString();
		    this.homeScore = 0;
		    this.awayScore = 0;
		    this.homeOdds = generateRandomDouble();
		    this.awayOdds = generateRandomDouble();
		    this.drawOdds = generateRandomDouble();
		    this.matchDate = LocalDateTime.parse(match.get("utcDate").getAsString(), DateTimeFormatter.ISO_DATE_TIME);
		    this.matchID = match.get("id").getAsInt();
		    this.status = match.get("status").getAsString();
		    this.result = null;
		    
		    //SPRAWDZANIE CZY SCORE JEST NULLEM - PRZED MECZEM DRUZYNY MAJA WYNIK NULL : NULL (A NIE 0:0)
		    JsonElement score = match.get("score").getAsJsonObject().get("fullTime").getAsJsonObject().get("home");
		    if (!score.isJsonNull()) {
		        this.homeScore = score.getAsInt();
		    } else {
		        this.homeScore = 0;
		    }
		    score = match.get("score").getAsJsonObject().get("fullTime").getAsJsonObject().get("away");
		    if (!score.isJsonNull()) {
		        this.awayScore = score.getAsInt();
		    } else {
		        this.awayScore = 0;
		    }
		    
		    //TAK SAMO W PRZYPADKU REZULTATU - PRZED MECZEM JEST NULL, A NIE "DRAW"
		    JsonElement resultHelper = match.get("score").getAsJsonObject().get("winner");
		    if(!resultHelper.isJsonNull()) {
		    	this.result = resultHelper.getAsString();
		    }
		    else {
		    	this.result = "DRAW";
		    }
		    
		    /*if(this.status == "FINISHED" || this.status == "IN_PLAY") {
		    	
		    	continue;
		    }
		    */
		    
		    Event event = new Event();
		    event.setTeama(this.homeTeamName);
		    event.setTeamb(this.awayTeamName);
		    event.setScorea(this.homeScore);
		    event.setScoreb(this.awayScore);
		    event.setRatea(this.homeOdds);
		    event.setRateb(this.awayOdds);
		    event.setRate0(this.drawOdds);
		    event.setMatchID(this.matchID);
		    event.setMatchDate(this.matchDate);
		    event.setResult(this.result);

		    //event.setStatus("FINISHED"); <-- wczesniej sluzylo do testowania
		    event.setStatus(this.status);
		    matchList.add(event);
		}
		reloadEvents();
	}
	
	//GENEROWANIE KURSOW LOSOWO (JESLI EVENT NIE JEST JUZ ZAPISANY W BAZIE DANYCH)
	public static float generateRandomDouble() {
	    double min = 1.01;
	    double max = 4.50;
	    return new BigDecimal(Math.random() * (max - min) + min)
	        .setScale(2, RoundingMode.HALF_UP)
	        .floatValue();
	}
	
	public void reloadEvents() {
		//AKTUALIZOWANIE WYNIKOW ORAZ STATUSOW MECZU (API)
		for (int i = 0; i < matchList.size(); i++) {
		    Event event = matchList.get(i);
		    Event eventInDb = eventDAO.checkEvent(event.getMatchID());
		    if (eventInDb != null) {
		    	System.out.println("Mecz o ID " + eventInDb.getMatchID() + " już istnieje w bazie. Aktualizuję dane.");
		    	eventInDb.setScorea(event.getScorea());
		    	eventInDb.setScoreb(event.getScoreb());
		    	//eventInDb.setStatus("FINISHED"); <-- C.D. TESTOW
		    	eventInDb.setStatus(event.getStatus());
		    	eventInDb.setResult(event.getResult());
		    	
		    	//ZOSTAW KURSY ZAPISANE W BAZIE DLA TEGO EVENTU
		    	event.setRatea(eventInDb.getRatea());
		    	event.setRateb(eventInDb.getRateb());
		    	event.setRate0(eventInDb.getRate0());
		    	eventDAO.update(eventInDb);
		    } else {
		       System.out.println("Dodawanie meczu o ID " + event.getMatchID() + " do bazy danych");
		       eventDAO.insert(event);
		    }
		}
		//WCZYTYWANIE WSZYSTKICH MECZÓW Z DZISIAJ
		List<Event> allMatches = eventDAO.getAllTodaysMatches();
		matchList.clear();
		matchList.addAll(allMatches);
	}
	
//	public boolean isMatchFinished(String st) {
//		if (st == "FINISHED" || st == "IN_PLAY") {
//			return true;
//		}
//		else {
//			return false;
//		}
//	}
}
